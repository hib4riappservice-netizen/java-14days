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
