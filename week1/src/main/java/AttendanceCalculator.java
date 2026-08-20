/********************************************************************************************************
 * 要件：
 * 1：1ヶ月分（20日分）の勤務時間を配列で持つ
 * 2：以下のメソッドを作る
 * 　- int totalHours(int[] hours) — 合計勤務時間
 * 　- double averageHours(int[] hours) — 平均勤務時間（小数第1位まで）
 * 　- int overtimeHours(int[] hours) — 1日8時間を超えた分の合計（残業時間）
 * 　- String judge(double average) — 平均が10以上なら "働きすぎ"、8以上なら "標準"、それ未満なら "余裕あり" を返す
 * 3：結果を整形して表示する
 * 4：hours が空配列（要素0個）だったらどうなるか考え、エラーにならないよう対処する
 ********************************************************************************************************/

package src.main.java;
import java.util.Arrays;
import java.util.OptionalDouble;

public class AttendanceCalculator {
    private static final double[] MONTHLY_WORK_HOURS = { 8, 10, 9, 8, 8.5, 0, 8, 8, 9, 8.5, 8, 12, 9, 3, 8.5, 8, 11, 9, 8, 8 };
    private static final int REGULAR_WORK_HOURS = 8;
    private static final int STANDARD_WORK_HOURS = 8;
    private static final int OVERWORK_LIMIT_HOURS = 10;
    private static final String JUDGMENT_OVERWORK = "働き過ぎです";
    private static final String JUDGMENT_STANDARD = "標準です";
    private static final String JUDGMENT_COMFORTABLE = "余裕があります";

    public static void main(String[] args) {
        // 空配列の場合、計算せずに安全に終了する
        if (MONTHLY_WORK_HOURS == null || MONTHLY_WORK_HOURS.length == 0) {
            System.out.println("勤務データが存在しません。");
            return;
        }
        double totalHours = calcTotalWorkHours(MONTHLY_WORK_HOURS);
        double overWorkHours = calcOverWorkHours(MONTHLY_WORK_HOURS);
        double averageHours = calcAverageWorkHours(MONTHLY_WORK_HOURS);
        String overWorkJudge = overWorkJudge(averageHours);
        printResult(totalHours, overWorkHours, averageHours, overWorkJudge);
    }

    // 合計勤務時間の計算処理
    public static double calcTotalWorkHours(double[] hours) {
        return Arrays.stream(hours).sum();
    }

    // 残業時間の計算処理
    public static double calcOverWorkHours(double[] hours) {
        return Arrays.stream(hours).map(h -> Math.max(0, h - REGULAR_WORK_HOURS)).sum();
    }

    // 稼働状況判定処理
    public static double calcAverageWorkHours(double[] hours) {
        // 1. 配列から平均（average）を計算すると、OptionalDouble型で返ってくる
        OptionalDouble average = Arrays.stream(hours).average();
        // 2. 「もし配列が空っぽだったら 0.0 を返す」という安全策をつけて double型にする
        double rawAverage = Arrays.stream(hours).average().orElse(0.0);
        // 少数第一位まで四捨五入して返却（出力処理の%5.1fでも四捨五入されるが、現段階の計算処理を正確に実施するため当処理においても実施）
        return (double) Math.round(rawAverage * OVERWORK_LIMIT_HOURS) / OVERWORK_LIMIT_HOURS;
    }

    // 稼働状況判定処理
    public static String overWorkJudge(double average) {
        if (average >= OVERWORK_LIMIT_HOURS) {
            return JUDGMENT_OVERWORK;
        } else if (average >= STANDARD_WORK_HOURS) {
            return JUDGMENT_STANDARD;
        } else {
            return JUDGMENT_COMFORTABLE;
        }
    }

    // 結果の整形・出力
    public static void printResult(double total, double overTime, double average, String judge) {
        System.out.println();
        System.out.printf("稼働時間(h)        :  %5.1f%n", total);
        System.out.printf("残業時間(h)        :  %5.1f%n", overTime);
        System.out.printf("一日の平均稼働時間(h):  %5.1f%n", average);
        System.out.println("---------------------------");
        System.out.printf("結果              :  %s%n", judge);
    }

}
