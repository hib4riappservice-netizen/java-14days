package src.main.java;

/************************************************************************************************************
 * 要件：
 *  ①mainの中で、社員名・基本給・今月の残業時間を変数に持つ
 *  ②メソッド calculateOvertimePay(int hours, int unitPay) を作り、残業代を計算して返す
 *  ③メソッド calculateTotalPay(int base, int overtimePay) を作り、総支給額を返す
 *  ④メソッド printPaySlip(String name, int base, int overtimePay, int total) を作り、以下の形式で出力する
 *    ===== 給与明細 =====
 *    氏名     : 田中太郎
 *    基本給   : 250000 円
 *    残業代   :  24000 円
 *    --------------------
 *    総支給額 : 274000 円
 *  ⑤メソッドは1つ1つが「1つのことだけ」をやるように作る（計算するメソッドの中で表示はしない）
 *  　なぜ計算と表示を分けるのか？
 *  　将来「画面ではなくCSVに出したい」となったとき、
 *  　計算部分をそのまま再利用できるからです。この「役割を分ける」感覚が、Day 10 のレイヤードアーキテクチャに直結します。
 ************************************************************************************************************/

public class EmployeeInfo {

    public static void main(String[] args) {
        // 社員名
        String employeeName = "田中太郎";
        // 基本給
        int baseSalary = 250000;
        // 残業時間(h)
        int overtimeHours = 12;
        // 時給
        int unitPay = 2000;

        // 残業代計算処理の実行
        int overtimePay = calculateOvertimePay(overtimeHours, unitPay);
        // 総支給額計算処理の実行
        int total = calculateTotalPay(baseSalary, overtimePay);
        // 明細出力処理の実行
        printPaySlip(employeeName, baseSalary, overtimePay, total);

    }

    // 残業代計算処理
    public static int calculateOvertimePay(int hours, int unitPay) {
        return hours * unitPay;
    }

    // 総支給額計算処理
    public static int calculateTotalPay(int base, int overtimePay) {
        return base + overtimePay;
    }

    // 明細出力処理
    public static void printPaySlip(String name, int base, int overtimePay, int total) {
        System.out.println("===== 給与明細 =====");

        // %-6s : 6文字分の幅を左詰めで確保（「氏名」などの文字幅を揃える）
        // %6d  : 6桁分の幅を右詰めで確保（金額を綺麗に並べる）
        System.out.printf("%-6s : %s%n", "氏名", name);
        System.out.printf("%-6s : %6d 円%n", "基本給", base);
        System.out.printf("%-6s : %6d 円%n", "残業代", overtimePay);

        System.out.println("--------------------");
        System.out.printf("%-6s : %6d 円%n", "総支給額", total);
    }

}
