## 制御構文・配列・文字列・エラーの読み方

> **練習課題01**
#### 実装
```
package src.main.java;
import java.util.Arrays;

public class Day2Practice {

    private static final int STANDARD_WORK_HOURS_PER_DAY = 8;

    public static void main(String[] args) {
        int[] weeklyHours = {8, 9, 7, 10, 8};
        String[] dayNames = {"月", "火", "水", "木", "金"};

        printHours(weeklyHours, dayNames);
        System.out.println("合計: " + sum(weeklyHours) + " 時間");
        System.out.println("残業した日数: " + countOvertimeDays(weeklyHours) + " 日");
        System.out.println("最も長かった日: " + maxHours(weeklyHours) + " 時間");
    }

    public static void printHours(int[] hours, String[] names) {
        for (int i = 0; i < hours.length; i++) {
            String mark = hours[i] > STANDARD_WORK_HOURS_PER_DAY ? "★残業" : "";
            System.out.println(names[i] + "曜： " + hours[i] + "時間 " + mark);
        }
    }

    public static int sum(int[] hours) {
        int total = 0;
        for (int h : hours) {
            total += h;
        }
        return total;
    }

    public static int countOvertimeDays(int[] hours) {
        int overtimeDays = 0;
        for (int h : hours) {
            if (STANDARD_WORK_HOURS_PER_DAY < h) {
                overtimeDays++;
            }
        }
        return overtimeDays;
    }

    public static int maxHours(int[] hours) {
//        int maxHour = hours[0];
//        for (int i = 1; i < hours.length; i++) {
//            if (maxHour < hours[i]) {
//                maxHour = hours[i];
//            }
//        }
        // Arraysクラスをimportし、StreamAPIを利用することで1行で書く方法。
        return Arrays.stream(hours).max().orElse(0);
    }

}
```

> **結果**
> 
期待通りに実行結果が表示され、エラーも発生しなかった。
```
月曜： 8時間
火曜： 9時間 ★残業
水曜： 7時間
木曜： 10時間 ★残業
金曜： 8時間
合計: 42 時間
残業した日数: 2 日
最も長かった日: 10 時間

プロセスは終了コード 0 で終了しました
```

#### 追加ハンズオン
　printHours の i < hours.length を i <= hours.length に変えて実行<br>
　→ 出たエラーを読み、行番号を特定して直す<br>
```
【スタックトレース】
月曜： 8時間 
火曜： 9時間 ★残業
水曜： 7時間 
木曜： 10時間 ★残業
金曜： 8時間 
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5
	at src.main.java.Day2Practice.printHours(Day2Practice.java:20)
	at src.main.java.Day2Practice.main(Day2Practice.java:12)

プロセスは終了コード 1 で終了しました
```
　要素数5の配列に対し6番目の要素（index5）を見ようとしてエラーが発生。<br>
　Day2Practice.javaの12行目から呼ばれた20行目で発生している。<br>
　手を加えた条件式の不等号を「<=」から「<」に戻して対応。

> **練習課題02**
#### 要件
```
1：1ヶ月分（20日分）の勤務時間を配列で持つ
2：以下のメソッドを作る
　- int totalHours(int[] hours) — 合計勤務時間
　- double averageHours(int[] hours) — 平均勤務時間（小数第1位まで）
　- int overtimeHours(int[] hours) — 1日8時間を超えた分の合計（残業時間）
　- String judge(double average) — 平均が10以上なら "働きすぎ"、8以上なら "標準"、それ未満なら "余裕あり" を返す
3：結果を整形して表示する
4：hours が空配列（要素0個）だったらどうなるか考え、エラーにならないよう対処する
```
#### 実装
※自ら要件を調整したという想定で内容を少し変えた。
```
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
```
> **結果**
>
期待通りに実行結果が表示され、エラーも発生しなかった。
```
稼働時間(h)        :  161.5
残業時間(h)        :   14.5
一日の平均稼働時間(h):    8.1
---------------------------
結果              :  標準です

プロセスは終了コード 0 で終了しました
```

<br>

> **備忘**

**スタックトレースの読み方**<br>
```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5
    at Day2Practice.printHours(Day2Practice.java:18)
    at Day2Practice.main(Day2Practice.java:8)
```
　見る順番：<br>
　　1：例外名(1行目)　　　　何が起きたかが分かる。<br>
　　2：メッセージ(1行目)　　詳細が分かる。<br>
　　3：atの1行目　　　　　　どこで起きたかが分かる。<br>
　　4：atの2行目　　　　　　どこから呼ばれたかが分かる。<br>

　ポイント：<br>
　　`at` の行は「自分が書いたファイル名が出ている一番上の行」だけ見ればOK。<br>
　　その下は「そこに至るまでの経路」で、ライブラリ内部の行は無視して構わない。<br>


**条件式の文字列比較**<br>
　文字列の比較に `==` を使ってはいけない。<br>
　`==` は「同じ場所を指しているか」、`equals` は「中身が同じか」を比べる。<br>
　文字列やオブジェクトの比較は必ず `equals`。

**条件分岐**<br>
　`if文`<br>
　　基本的な条件分岐文。<br>
　`Swich式`<br>
　　1つの値によってたくさんに分岐する」ときに使用。<br>
　　- **`->` を使う新しい書き方では `break` が不要**。<br>
　　- **`default` を書くか、enum の全ケースを書き切る**必要がある。<br>
　　- `enum` と組み合わせると真価を発揮する。<br>

**繰り返し**<br>
　`for文`<br>
　　回数指定。事前に回数が決まっている場合に使用。<br>
　　*例：「校庭を5周走ってください」*<br>
　　何周目かを数えながら走り、5周走ったら確実に終わる。<br>
　`while文`<br>
　　条件指定。事前に回数が決まっていない場合に使用。<br>
　　*例：「雨が 降ってくるまで 走ってください」*<br>
　　1周で終わるかもしれないし、100周走るかもしれない。雨が止めば終わり。<br>

**文字列の主要メソッド**<br>
```
String s = "  Taro Tanaka  ";<br>
```
　`文字数`<br>
　s.length()　　// 15<br>
　`前後の空白を除去（文字間の空白は残る）`<br>
　s.trim()　　// "Taro Tanaka"<br>
　`0文字か`<br>
　s.isEmpty()　　// false<br>
　`0文字あるいは空白のみか`<br>
　s.isBlank()　　// false<br>
　`大文字化`<br>
　s.toUpperCase()　　// "  TARO TANAKA  "<br>
　`指定した文字を含むか`<br>
　s.contains("Taro")　　// true<br>
　`半角スペースで区切って配列にする`<br>
　s.split(" ")　　// ["", "", "Taro", "Tanaka"]<br>
　※半角スペースで切った空白の左側に何かあればその文字列、何もなければ""が残り、末尾の""は自動で消える。<br>
　`指定インデックス間の文字(下記だと2～6)を切り出す`<br>
　s.substring(2, 6)　　// "Taro"<br>
　`整形`<br>
　String.format("%s は %d 歳", "田中", 25)　　// "田中 は 25 歳"<br>
　※%s に "田中" が、%d に 25 が埋め込まれて1つの文字列になる。


