## 変数・型・メソッド

> **練習課題01**

#### 実装
```
public class Day1Practice {

    public static void main(String[] args) {
        // 基本給と残業時間から総支給額を計算する
        int baseSalary = 250000;
        int overtimeHours = 12;
        int overtimeUnitPay = 2000;

        int totalPay = calculateTotalPay(baseSalary, overtimeHours, overtimeUnitPay);
        System.out.println("総支給額: " + totalPay + " 円");

        // 型変換（キャスト）の練習
        int a = 7;
        int b = 2;
        System.out.println("整数同士の割り算: " + (a / b));            // 3 （小数は切り捨て）
        System.out.println("小数にしたい場合: " + ((double) a / b));   // 3.5

        // 文字列と数値の連結
        String name = "田中";
        int age = 25;
        System.out.println(name + "さんは" + age + "歳です");

        // ヌルポを体験する（実行後コメントアウトすること）
        String nothing = null;
        System.out.println(nothing.length());
    }

    public static int calculateTotalPay(int base, int hours, int unitPay) {
        return base + hours * unitPay;
    }
}
```

> **正常ログ**

エラーが発生せずにすべての処理が期待通り実行された。
```
総支給額: 274000円
整数同士の割り算： 3
少数にしたい場合： 3.5
田中さんは25歳です

プロセスは終了コード 0 で終了しました
```

> **異常ログ**

nothing変数(Stringで定義したnull変数)にアクセスしようとしたため。<br>
※Stringは参照型のため「矢印がどこも指していないのにその先を見に行った」状態になった。
```
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "String.length()" because "nothing" is null
at src.main.java.Day1Practice.main(Day1Practice.java:54)
総支給額: 274000円
整数同士の割り算： 3
少数にしたい場合： 3.5
田中さんは25歳です

プロセスは終了コード 1 で終了しました
```
<br>

> **練習問題02**
#### 要件
```
1：mainの中で、社員名・基本給・今月の残業時間を変数に持つ
2：メソッド calculateOvertimePay(int hours, int unitPay) を作り、残業代を計算して返す
3：メソッド calculateTotalPay(int base, int overtimePay) を作り、総支給額を返す
4：メソッド printPaySlip(String name, int base, int overtimePay, int total) を作り、以下の形式で出力する
  ===== 給与明細 =====
   氏名     : 田中太郎
   基本給   : 250000 円
   残業代   :  24000 円
  --------------------
   総支給額 : 274000 円
5：メソッドは1つ1つが「1つのことだけ」をやるように作る（計算するメソッドの中で表示はしない）
　　<備考>
   　なぜ計算と表示を分けるのか？
　 　将来「画面ではなくCSVに出したい」となったとき、
　 　計算部分をそのまま再利用できるからです。この「役割を分ける」感覚が、Day 10 のレイヤードアーキテクチャに直結します。
```
#### 実装
```
package src.main.java;

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
```
> **結果**

期待通りに各処理が実行され、エラーも発生しないことを確認した。
```
===== 給与明細 =====
氏名     : 田中太郎
基本給    : 250000 円
残業代    :  24000 円
--------------------
総支給額   : 274000 円

プロセスは終了コード 0 で終了しました
```

> **備忘**

**金額に double を使わない。BigDecimal を使う。**<br>
　コンピュータは2進数で小数を扱うため、0.1 を正確に表現できないため。

**NullPointerException**<br>
　「矢印が何も指していないのに、その先を見に行った」という意味。

**メソッド**<br>
　処理のかたまりに名前を付けたもの（classのこと）。<br>
　`main`：特別なメソッド。JVMがプログラムを始めるとき必ずここから実行する。<br>
　`void`：「何も返さない」という型。main は何も返さないのでvoid型のメソッド。

**`%n` と `\n` の違い**<br>
　`%n` はOSに合わせた改行を出す。実務では `%n` が安全。<br>