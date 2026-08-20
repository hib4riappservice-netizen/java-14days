package src.main.java;

/*******************************
 * day01の練習課題クラス
 * 値を計算して画面に出すプログラム
 *******************************/
public class Day1Practice {

    public static void main(String[] args) {
        /*******************************
         * 基本給と残業時間から総支給額を計算
         *******************************/
        // 基本給
        int baseSalary = 250000;
        // 残業時間(h)
        int overtimeHours = 12;
        // 時給
        int overtimeUnitPay = 2000;

        // 総支給額計算処理の実行
        int totalPay = calculateTotalPay(baseSalary, overtimeHours, overtimeUnitPay);
        // 総支給額計算結果の出力
        System.out.println("総支給額: " + totalPay + "円");

        /*******************************
         * 型変換（キャスト）の練習
         *******************************/
        // 整数a
        int a = 7;
        // 整数b
        int b = 2;
        // 割り算結果の出力（整数）
        System.out.println("整数同士の割り算： " + (a / b));
        // 割り算結果の出力（少数）
        System.out.println("少数にしたい場合： " + ((double) a / b));

        /*******************************
         * 文字列と数値の連結
         *******************************/
        //
        String name = "田中";
        //
        int age = 25;
        //
        System.out.println(name + "さんは" + age + "歳です");

        /*******************************
         * NullPointerExceptionを体験する
         * （実行後にコメントアウトすること）
         *******************************/
//        // null変数
//        String nothing = null;
//        // null変数の文字数を数える
//        System.out.println(nothing.length());
    }

    public static int calculateTotalPay(int base, int hours, int unitPay) {
        /*******************************
         * 総支給額計算処理
         *******************************/
        //
        return base + hours * unitPay;
    }

}
