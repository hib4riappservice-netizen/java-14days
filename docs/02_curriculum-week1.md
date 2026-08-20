# 第1週：Javaという言語を「使える」ようになる（Day 0 〜 Day 7）

> **毎朝の儀式（20分）**：前日の「自己チェックリスト」を何も見ずに口頭で答える。答えられない項目があれば、その項目だけ復習してから今日を始める。

---

# Day 0：地図を手に入れる（所要6〜8時間 ／ ダウンロード待ちを含む）

> **⚠ 時間について正直に書きます。** 手を動かす時間は3〜4時間ですが、**JDK・IntelliJ・Docker Desktop のダウンロードとインストール、PCの再起動で、実際には6〜8時間かかります**（回線が遅ければさらに）。
> **コツ：ダウンロードは並行して走らせてください。** Docker Desktop のインストーラを落としている間に JDK を入れ、その間に座学を読む、という進め方をすると大幅に短縮できます。
> **Day 0 で遅れても、Day 1 以降には影響しません。** 環境構築は「終わればよい」工程です。

## ① 今日のゴール
- 自分のPCでJavaプログラムが動く
- 「これから何を学ぶのか」の全体地図が頭に入っている
- Git と GitHub が使える状態になっている

## ② 新出用語（`01_glossary.md` で確認）
JDK / JVM / IDE / コンパイル / 実行 / リポジトリ / Git / GitHub / ビルドツール / Maven

## ③ 座学：「プログラムが動く」とはどういうことか（60分）

### コンピュータは日本語も英語も分からない
コンピュータが理解できるのは、電気が流れている／いない＝0と1だけです。ですが人間が0と1を書くのは無理です。そこで間に翻訳者を挟みます。

```
あなたが書く                翻訳                    実行
Main.java      →（コンパイル）→   Main.class    →（JVMが読む）→  結果
（人間が読める文字）           （中間コード）              （画面に文字が出る）
```

- **`.java` ファイル**：あなたが書く文章。**ソースコード**と呼びます
- **コンパイル**：`.java` を `.class` に翻訳する作業。翻訳できない書き方をしていると **コンパイルエラー** になります
- **`.class` ファイル**：**バイトコード**という中間形式。まだ機械語ではありません
- **JVM**：`.class` を読んで、そのPCの機械語に変えながら実行するエンジン

### なぜ「中間形式」なのか（Javaの最大の特徴）
C言語などは、Windows用にコンパイルしたらWindowsでしか動きません。Javaは中間形式で止めておき、**各OS用のJVMが最後の翻訳をやる**ので、同じ `.class` が Windows でも Mac でも Linux でも動きます。これを **Write Once, Run Anywhere（一度書けばどこでも動く）** と言います。業務システムでJavaが選ばれ続けている大きな理由です。

### Javaが業務システムで使われる理由（現場で聞かれます）
1. **静的型付け**：変数の型を必ず宣言するので、間違いをコンパイル時（実行前）に発見できる。大規模・長寿命システムで圧倒的に有利
2. **エコシステム**：Spring をはじめ業務に必要な部品が揃っている
3. **後方互換性**：昔書いたコードが今も動く。10年動く社内システムに向く
4. **人材の層が厚い**：長期保守で人を入れ替えられる

## ④ ハンズオン（180〜300分 ／ 大半はインストール待ち時間）
`04_setup.md` に従って以下を完了させてください。

> **⚠ まず `04_setup.md` の「0. すでにインストール済みのものがある場合」を実行してください。**
> 以前に入れた JDK・Git・PostgreSQL などが残っていると、**あとで原因の分かりにくいトラブル**になります（特に PostgreSQL は Day 8 で確実に衝突します）。**5分で終わる確認**です。
>
> **その次に Docker Desktop（項目10）のダウンロードを開始してから、1〜9 を進めてください。** 待ち時間を重ねられます。

1. **JDK 21（LTS）** をインストール → ターミナルで `java -version` が表示されればOK
2. **IntelliJ IDEA** をインストール（2025.3以降は無料版・有料版の区別なく1つのインストーラです）
3. **Git** をインストール → `git --version` が表示されればOK
   - **Windows の人は、以降のコマンドを「Git Bash」で実行してください**（`04_setup.md`「用語の前提」）
4. **GitHub アカウント**を作成
5. `java-14days` という名前でリポジトリを作り、自分のPCにクローン（`04_setup.md` §4）
6. 以下を書いて実行（**コピペ禁止・手打ち**）

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello, Java!");
    }
}
```

7. **わざと壊す練習**（これが本題です）
   - `System.out.println` の最後の `;` を消して実行 → エラーメッセージを読む
   - `println` を `printLn` に変えて実行 → エラーメッセージを読む
   - `"Hello, Java!"` の閉じ側の `"` を消して実行 → エラーメッセージを読む

   3種類のエラーメッセージを、**日本語で「何が起きたか」ノートに書いてください。** これが今日一番大事な課題です。

8. **`.gitignore` を作り、`git add` → `git commit` → `git push` する**
   - **手順は `04_setup.md` の「4-2. 最初のコミットと push」に全部書いてあります。** そちらを開いて、そのとおりに実行してください
   - 今日は `main` ブランチに直接 push して構いません。**ブランチとプルリクエストは Day 7 で扱います**

9. **`week1` を Maven プロジェクトの形にする**（`04_setup.md` の「4-3」）
   - Day 3 のパッケージ、Day 6 のライブラリ、Day 7 の `mvn test` で必要になります。**後から移すと手間なので今日やります**

10. **Docker Desktop をインストールし、`docker run --rm hello-world` が動くことを確認する**（`04_setup.md` §5-1）
    - 使うのは Day 8 からですが、**会社PCではポリシーで入らないことがあります**。当日に発覚すると1日止まるので、**今日のうちに「使える」ことだけ確認**します
    - WSL2 の導入とPCの再起動が必要な場合があります。**この項目だけは早めに着手してください**

## ⑤ 課題

> 💡 Day 0 の課題に解答例はありません（環境構築が「解答」です）。詰まったら `07_troubleshooting.md` を見てください。
- 上記10まで完了させ、**GitHub のブラウザ画面で自分のコードが見えること**を確認
- `05_project-spec.md` を**ざっと5分だけ**読む（分からなくてよい）
- `log/day00.md` に学習ログを書く（このファイルも忘れずにコミットする）

## ⑥ 自己チェック（全部✅で翌日へ）
- [x] JDK と JVM の違いを説明できる
- [x] コンパイルとは何をする作業か説明できる
- [x] `.java` と `.class` の違いを説明できる
- [x] ターミナルから `java -version` と `git --version` が実行できる
- [x] GitHub に自分のコードが上がっている（ブラウザで見える）
- [x] エラーメッセージを3種類見て、それぞれ何を意味するか自分の言葉で説明できる
- [x] `week1` で `mvn clean test` が BUILD SUCCESS になる
- [x] `docker run --rm hello-world` が動く（Day 8 で使います）

---

# Day 1：変数・型・メソッド（所要8時間）

## ① 今日のゴール
- 値を計算して画面に出すプログラムを、自力で書ける
- Javaの「型」がなぜ厳しいのか腑に落ちる
- メソッドに処理を切り出せる

## ② 新出用語
変数 / 型 / プリミティブ型 / 参照型 / メソッド / 引数 / 戻り値 / static / クラス / main / キャスト

## ③ 座学（90分）

### 変数と型
変数は「名前を付けた箱」です。Javaでは箱を作るとき **何を入れる箱か（型）を必ず宣言** します。

```java
int age = 25;              // 整数を入れる箱
double height = 170.5;     // 小数を入れる箱
boolean isActive = true;   // true か false を入れる箱
String name = "田中太郎";   // 文字列を入れる箱
```

`int age = "田中";` と書くとコンパイルエラーになります。**これがJavaの価値です。**
「実行してみたら数字のはずが文字だった」という事故を、**実行する前に**発見できます。100人が10年触るシステムでは、これが決定的な差になります。

### 覚えるべきプリミティブ型（8種類のうち実務で使うのは4つ）

| 型 | 入るもの | 実務での使いどころ |
|---|---|---|
| `int` | 整数（約±21億） | 個数、ID、回数。**まずこれを使う** |
| `long` | 大きい整数 | 金額の内部表現、ミリ秒時刻、DBのID |
| `double` | 小数 | 割合、平均。**金額には使わない**（後述） |
| `boolean` | true / false | フラグ、判定結果 |

（残り：`byte`, `short`, `float`, `char`。実務ではほぼ出ません。今は無視してOK）

> **【実務の落とし穴：金額に double を使うな】**
> `System.out.println(0.1 + 0.2);` を実行してみてください。`0.30000000000000004` と出ます。
> コンピュータは2進数で小数を扱うため、0.1 を正確に表現できません。**金額計算で1円ズレると事故です。**
> 金額には `BigDecimal` を使います（Day 6 で扱います）。**これは現場で最も有名な新人の罠です。**

### 参照型とnull
`String` や自作クラスは **参照型** です。箱の中に入っているのは「値そのもの」ではなく「値がある場所を指す矢印」です。

```java
String name = "田中";   // name は "田中" が置いてある場所を指している
String empty = null;    // empty は「どこも指していない」
System.out.println(empty.length());  // 実行時エラー！NullPointerException
```

**NullPointerException（通称ヌルポ）は、新人が最も多く遭遇するエラー**です。「矢印が何も指していないのに、その先を見に行った」という意味です。

### メソッド
処理のかたまりに名前を付けたものです。

```java
public class Calculator {

    // メソッド：2つの整数を足して返す
    //  public   → どこからでも呼べる
    //  static   → オブジェクトを作らなくても呼べる【今は暗記でOK】
    //  int      → 戻り値の型（このメソッドは整数を返す）
    //  add      → メソッド名
    //  (int a, int b) → 引数（受け取る値）
    public static int add(int a, int b) {
        return a + b;   // return で呼び出し元に値を返す
    }

    public static void main(String[] args) {
        int result = add(3, 5);
        System.out.println("答えは " + result);   // 答えは 8
    }
}
```

- `void`（ヴォイド）＝「何も返さない」という型。`main` は何も返さないので `void`
- `main` メソッドは特別で、**JVMがプログラムを始めるとき必ずここから実行する**と決まっています。だから書き方（`public static void main(String[] args)`）は変えられません。**【今は暗記でOK】**

### 良い名前の付け方（今日から意識する）
- クラス名：**大文字始まり**、名詞。`Employee`, `AttendanceService`
- メソッド名：**小文字始まり**、動詞から始める。`calculateTotal`, `findById`, `isValid`
- 変数名：**小文字始まり**、意味の分かる名詞。`employeeName`（❌ `a`, `tmp`, `data1`）
- 定数：**全部大文字＋アンダースコア**。`MAX_RETRY_COUNT`
- この「2語目から大文字」の書き方を **キャメルケース** と言います

> **命名は「あとで直せばいい」ものではありません。** レビューで最も指摘される項目であり、コードの読みやすさの8割を決めます。「この変数、3ヶ月後の自分が読んで分かるか？」を毎回自問してください。

## ④ 写経ハンズオン（120分）
以下を手打ちして実行し、**1行ずつコメントで説明を書き足してください**。

```java
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

**確認すること**
- `a / b` が 3.5 でなく 3 になる理由（整数同士の割り算は整数になる）
- `(double) a` の意味（キャスト＝型を強制的に変換する）
- ヌルポのスタックトレースを読み、「何行目で起きたか」を特定する

## ⑤ 課題（自力で書く・180分）

> 💡 **詰まったら30分考えてから** `08_answers.md` を参照してください。見たら必ず閉じて、もう一度自力で書き直すこと。
`EmployeeInfo.java` を作り、以下を満たすこと。

1. `main` の中で、社員名・基本給・今月の残業時間を変数に持つ
2. メソッド `calculateOvertimePay(int hours, int unitPay)` を作り、残業代を計算して返す
3. メソッド `calculateTotalPay(int base, int overtimePay)` を作り、総支給額を返す
4. メソッド `printPaySlip(String name, int base, int overtimePay, int total)` を作り、以下の形式で出力する

```
===== 給与明細 =====
氏名     : 田中太郎
基本給   : 250000 円
残業代   :  24000 円
--------------------
総支給額 : 274000 円
```

5. **メソッドは1つ1つが「1つのことだけ」をやるように作る**（計算するメソッドの中で表示はしない）

> **なぜ計算と表示を分けるのか？** 将来「画面ではなくCSVに出したい」となったとき、計算部分をそのまま再利用できるからです。この「役割を分ける」感覚が、Day 10 のレイヤードアーキテクチャに直結します。**今日から意識してください。**

## ⑥ 自己チェック
- [x] `int` と `double` と `String` と `boolean` の違いを説明できる
- [x] 金額計算に `double` を使ってはいけない理由を説明できる
- [x] `7 / 2` が 3 になる理由を説明できる
- [x] NullPointerException が起きる原因を説明できる
- [x] メソッドの「引数」と「戻り値」を説明できる
- [x] `void` の意味を説明できる
- [x] クラス名・メソッド名・変数名の命名ルールを説明できる
- [x] 計算処理と表示処理を分けるべき理由を説明できる

---

# Day 2：制御構文・配列・文字列・エラーの読み方（所要8時間）

## ① 今日のゴール
- 条件分岐と繰り返しを自在に書ける
- **スタックトレースを読んで、自力でバグを直せる**（今日の最重要項目）
- デバッガで変数の中身を覗ける

## ② 新出用語
if / switch式 / for / while / 拡張for文 / 配列 / インデックス / スタックトレース / デバッガ / ブレークポイント / ステップ実行

## ③ 座学（90分）

### 条件分岐
```java
int hours = 9;

if (hours > 8) {
    System.out.println("残業あり");
} else if (hours == 8) {
    System.out.println("定時");
} else {
    System.out.println("早退");
}
```

**要注意**：`=` は代入、`==` は比較です。`if (x = 5)` は間違いです。

### switch 式（Java 14以降の書き方）
「1つの値によって、たくさんに分岐する」ときは `if-else` を並べるより `switch` が読みやすくなります。

```java
String label = switch (dayType) {           // ← 値を返せる（これが「switch 式」）
    case "MON", "TUE", "WED", "THU", "FRI" -> "平日";
    case "SAT", "SUN"                      -> "休日";
    default -> throw new IllegalArgumentException("不正な曜日: " + dayType);
};
```

- **`->` を使う新しい書き方では `break` が不要**です（古い `case X:` の書き方は、`break` を忘れると次の case に流れ込む有名なバグ源でした）
- **`default` を書くか、enum の全ケースを書き切る**必要があります。書き漏らすとコンパイルエラーになります
- **Day 4 の `enum` と組み合わせると真価を発揮します**（enum に case を書き足し忘れると**コンパイルエラーで教えてくれる**）

> **古いコードでは `case X: ... break;` の形をよく見ます。** 読めれば十分ですが、**自分で新しく書くときは `->` の形**にしてください。

**さらに要注意**：**文字列の比較に `==` を使ってはいけません。**
```java
String a = new String("田中");
String b = new String("田中");
System.out.println(a == b);        // false！（矢印の指す先が違うから）
System.out.println(a.equals(b));   // true （中身を比べているから）
```
`==` は「同じ場所を指しているか」、`equals` は「中身が同じか」を比べます。**文字列やオブジェクトの比較は必ず `equals`。これは現場でも頻出のバグ原因です。**

### 繰り返し
```java
// for文：回数が決まっているとき
for (int i = 0; i < 5; i++) {
    System.out.println(i + "回目");
}
// i=0 から始めて、i<5 の間、毎回 i を1増やしながら繰り返す

// while文：条件が続く限り
int count = 0;
while (count < 5) {
    System.out.println(count);
    count++;
}
```

### 配列
同じ型の値を、決まった個数まとめて持つ入れ物です。

```java
int[] dailyHours = new int[7];      // 7個分の箱を作る（初期値は全部 0）
dailyHours[0] = 8;                  // 0番目に代入（**0から始まる**ことに注意）
dailyHours[1] = 9;

// まとめて初期化
int[] hours = {8, 9, 7, 8, 10};

// 拡張for文（実務ではこちらを多用する）
for (int h : hours) {
    System.out.println(h + "時間");
}
// 「hours の中身を1つずつ取り出して h に入れながら繰り返す」と読む
```

**要注意**：`hours[5]` にアクセスすると `ArrayIndexOutOfBoundsException`（配列の範囲外）が出ます。**インデックスは0から始まるので、要素5個なら 0〜4** です。

### 文字列の主要メソッド（実務で毎日使う）
```java
String s = "  Taro Tanaka  ";
s.length()              // 文字数
s.trim()                // 前後の空白を除去 → "Taro Tanaka"
s.isEmpty()             // 長さ0か
s.isBlank()             // 空白のみか（Java 11以降。実務ではこちらが便利）
s.toUpperCase()         // 大文字化
s.contains("Taro")      // 含むか
s.split(" ")            // 区切って配列にする
s.substring(2, 6)       // 一部を取り出す
String.format("%s は %d 歳", "田中", 25)   // 整形
```

### 【最重要】スタックトレースの読み方
以下のエラーが出たとします。
```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException:
        Index 5 out of bounds for length 5
    at Day2Practice.printHours(Day2Practice.java:18)
    at Day2Practice.main(Day2Practice.java:8)
```
（Day 3 以降はパッケージを付けるので、`at com.example.attendance.domain.Attendance.xxx(...)` のように**パッケージ名が前に付いた形**になります。読み方は同じです）

**読む順番はこうです。**

| 見る場所 | 何が分かるか | この例では |
|---|---|---|
| 1行目の例外名 | 何が起きたか | 配列の範囲外アクセス |
| 1行目のメッセージ | 詳細 | 長さ5の配列に、5番目でアクセスした |
| `at` の**一番上で、自分が書いたファイル名がある行** | どこで起きたか | Day2Practice.java の **18行目** |
| その下の `at` の行 | どこから呼ばれたか | main の8行目から呼ばれた |

**ポイント：`at` の行が20行あっても、あなたが見るのは「自分が書いたファイル名が出ている一番上の行」だけです。** その下は「そこに至るまでの経路」で、ライブラリ内部の行は無視して構いません。

**やってはいけないこと**：エラーを見た瞬間にコードを適当に変えること。**まずメッセージを最後まで読む。** これだけで新人の解決速度は3倍変わります。

### デバッガの使い方（IntelliJ）
1. 止めたい行の左端（行番号の右）をクリック → 赤い丸（**ブレークポイント**）が付く
2. 実行ボタンの隣の **虫のアイコン**（デバッグ実行）で起動
3. その行で止まるので、画面下の Variables に **今の変数の中身が全部見える**
4. **F8**（ステップオーバー）＝次の1行へ進む／**F7**（ステップイン）＝メソッドの中に入る／**F9** ＝次のブレークポイントまで進む

> **`System.out.println` を大量に入れてデバッグするのは初心者のやり方です。** デバッガを使えば、変数の中身を全部一望できます。今日から必ずデバッガを使ってください。**「デバッガが使える新人」は現場で明確に評価されます。**

## ④ 写経ハンズオン（120分）
```java
public class Day2Practice {

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
            String mark = hours[i] > 8 ? "★残業" : "";      // 三項演算子
            System.out.println(names[i] + "曜: " + hours[i] + "時間 " + mark);
        }
    }

    public static int sum(int[] hours) {
        int total = 0;
        for (int h : hours) {
            total += h;          // total = total + h と同じ
        }
        return total;
    }

    public static int countOvertimeDays(int[] hours) {
        int count = 0;
        for (int h : hours) {
            if (h > 8) {
                count++;
            }
        }
        return count;
    }

    public static int maxHours(int[] hours) {
        int max = hours[0];
        for (int h : hours) {
            if (h > max) {
                max = h;
            }
        }
        return max;
    }
}
```

**追加ハンズオン（必須）**
- `printHours` の `i < hours.length` を `i <= hours.length` に変えて実行 → 出たエラーを読み、**行番号を特定して直す**
- `maxHours` にブレークポイントを置き、デバッガで `max` が更新される様子を1ステップずつ観察する

## ⑤ 課題（180分）

> 💡 **詰まったら30分考えてから** `08_answers.md` を参照してください。見たら必ず閉じて、もう一度自力で書き直すこと。
`AttendanceCalculator.java` を作成。

1. 1ヶ月分（20日分）の勤務時間を配列で持つ
2. 以下のメソッドを作る
   - `int totalHours(int[] hours)` — 合計勤務時間
   - `double averageHours(int[] hours)` — 平均勤務時間（小数第1位まで）
   - `int overtimeHours(int[] hours)` — 1日8時間を超えた分の合計（残業時間）
   - `String judge(double average)` — 平均が10以上なら "働きすぎ"、8以上なら "標準"、それ未満なら "余裕あり" を返す
3. 結果を整形して表示する
4. **`hours` が空配列（要素0個）だったらどうなるか考え、エラーにならないよう対処する**

> 4番が今日の本当の課題です。**「異常な入力が来たらどうするか」を考えるのがプロの仕事です。** 正常系だけ動くコードは、現場では未完成品として扱われます。

## ⑥ 自己チェック
- [ ] `==` と `equals` の違いを説明できる
- [ ] 文字列比較に `==` を使ってはいけない理由を説明できる
- [ ] 配列のインデックスが0から始まることと、その落とし穴を説明できる
- [ ] 拡張for文と通常のfor文の使い分けを説明できる
- [ ] **スタックトレースを見て、自分のコードのどの行が原因か特定できる**
- [ ] ブレークポイントを置いてデバッガで変数の中身を確認できる
- [ ] F8（ステップオーバー）と F7（ステップイン）の違いを説明できる
- [ ] 空配列など「異常な入力」への備えが必要な理由を説明できる

---

# Day 3：クラスとオブジェクト（所要9時間）

> **今日から3日間が山場です。** 抽象的で、初日は必ず「分からない」と感じます。それが正常です。手を止めずに書き続けてください。

## ① 今日のゴール
- クラスを設計して、データと処理をまとめられる
- カプセル化がなぜ必要か腑に落ちる
- `static` の意味が分かる

## ② 新出用語
クラス / インスタンス / フィールド / コンストラクタ / this / カプセル化 / private / ゲッター / セッター / パッケージ / import / static / final / イミュータブル

## ③ 座学（120分）

### なぜクラスが必要か（ここが分からないと全部分からない）

Day 2 までのやり方で「社員100人の勤怠を管理する」プログラムを書くとこうなります。

```java
String[] names = new String[100];
int[] ages = new int[100];
int[] hours = new int[100];
String[] departments = new String[100];
```

**この設計の何が問題か。**
- 社員を1人削除したら、4つの配列を全部同じ位置で削除しないと**データがズレます**（`names[3]` は田中さんなのに `hours[3]` は佐藤さんの時間、という事故）
- 項目が増えるたび配列が増え、メソッドの引数が10個になる
- 「田中さんの残業代を計算する」処理をどこに置けばいいか分からない

**解決策：「関連するデータ」と「そのデータを使う処理」を1つの箱にまとめる。** これがクラスです。

```java
public class Employee {
    private String name;
    private int age;
    private String department;

    // このデータを使う処理も、同じ箱に入れる
    public boolean isVeteran() {
        return age >= 40;
    }
}
```

これで `Employee[] employees` の1本で済み、データがズレることもありません。**これがオブジェクト指向の出発点です。難しい理論ではなく、「散らかるから箱にまとめよう」という実務的な発想です。**

### クラスの基本構造

```java
package com.example.attendance.domain;   // このクラスが属するフォルダ

public class Employee {

    // ① フィールド（このクラスが持つデータ）
    private final String employeeId;   // final = 一度決めたら変更不可
    private final String name;
    private String department;         // 部署は異動でありうるので final にしない

    // ② コンストラクタ（オブジェクトが作られる瞬間に1回だけ動く初期化処理）
    public Employee(String employeeId, String name, String department) {
        // 引数チェック：不正なオブジェクトを作らせない（重要）
        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("社員IDは必須です");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("氏名は必須です");
        }
        this.employeeId = employeeId;   // this = 「この、今作られているオブジェクトの」
        this.name = name;
        this.department = department;
    }

    // ③ ゲッター（外から値を読むための入口）
    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public String getDepartment() { return department; }

    // ④ セッター（外から値を変えるための入口。必要なものだけ作る）
    public void changeDepartment(String newDepartment) {
        if (newDepartment == null || newDepartment.isBlank()) {
            throw new IllegalArgumentException("部署名は必須です");
        }
        this.department = newDepartment;
    }

    // ⑤ このクラスならではの振る舞い
    public String displayLabel() {
        return name + "（" + department + "）";
    }
}
```

使う側：
```java
Employee taro = new Employee("E001", "田中太郎", "営業部");
System.out.println(taro.displayLabel());   // 田中太郎（営業部）
taro.changeDepartment("開発部");
```

- `new Employee(...)` で **インスタンス（実体）** が作られます
- クラス＝たい焼きの型、インスタンス＝焼けたたい焼き。型は1つ、たい焼きは何個でも作れます

### カプセル化：なぜフィールドを private にするのか

もし `public String name;` にすると、外から `taro.name = "";` と空文字を入れられます。**そのオブジェクトは「名前のない社員」という、あってはならない状態になります。**

private にして、変更は必ずメソッド経由にすれば、**メソッドの中でチェックを書けます**。
つまりカプセル化とは、**「オブジェクトを常に正しい状態に保つ仕組み」** です。「隠すこと」自体が目的ではありません。

> **【実務の重要な指針】セッターを機械的に全フィールド分作るのはアンチパターンです。**
> IDEには「全フィールドのgetter/setterを自動生成」する機能がありますが、それをすると public フィールドと同じことになり、カプセル化の意味が消えます。
> **セッターは「業務上、本当に変わりうる項目」だけに、業務的な名前で作ってください。**
> ❌ `setDepartment(String d)` → ⭕ `changeDepartment(String newDepartment)` （異動という業務イベントを表現）
> これは Day 12 の設計原則に直結する、極めて重要な感覚です。

### static の意味
```java
public class Employee {
    private static int totalCount = 0;   // クラス全体で1個だけ存在する変数
    private String name;                 // インスタンスごとに1個ずつ存在する変数

    public Employee(String name) {
        this.name = name;
        totalCount++;                    // 社員が作られるたびに増える
    }

    public static int getTotalCount() {  // インスタンス無しで呼べる
        return totalCount;
    }
}

// 使い方
new Employee("田中");
new Employee("佐藤");
System.out.println(Employee.getTotalCount());  // 2 （クラス名から直接呼ぶ）
```

- **インスタンス変数**：オブジェクトごとに別々の値（田中さんの名前、佐藤さんの名前）
- **static変数**：クラスに1個だけ。全オブジェクトで共有

Day 1 で書いた `public static void main` の `static` は、「まだオブジェクトが1つも無い起動時点で呼ぶ必要があるから」です。

> **【実務の注意】static は便利ですが、多用は禁物です。** static変数は全体で共有されるため、複数の処理が同時に動く業務システムでは値が壊れる原因（スレッドセーフでない）になります。実務で static を使うのは、①定数（`static final`）②純粋な計算だけのユーティリティメソッド、のほぼ2つだけと考えてください。

### パッケージ
クラスが増えると整理が必要です。フォルダで分けるのがパッケージです。

```
week1/src/main/java/com/example/attendance/
├── domain/        ← 業務の概念（Employee, Attendance）
├── service/       ← 業務ロジック
├── repository/    ← DBアクセス
└── controller/    ← 外部からの受付
```
ファイルの1行目に `package com.example.attendance.domain;` と書きます。**フォルダ構成とパッケージ名は必ず一致させます。**

> **⚠ 今日から `week1` が Maven プロジェクトになっている必要があります。**（`04_setup.md` の「4-3」）
> `src/main/java` の下に上のフォルダを作り、そこに今日のクラスを置いてください。まだ Maven 化していない人は、**今すぐ先に済ませてください**（15分で終わります）。IntelliJ なら `src/main/java` を右クリック →`New` → `Package` で `com.example.attendance.domain` と入力すれば、フォルダとパッケージ宣言が揃った状態で作れます。

## ④ 写経ハンズオン（120分）
上記 `Employee` クラスを手打ちし、以下を追加。

```java
// Attendance.java（1日分の勤怠記録）
package com.example.attendance.domain;

public class Attendance {
    private final String employeeId;
    private final int day;          // 何日か（1〜31）
    private final int workHours;    // その日の勤務時間

    public Attendance(String employeeId, int day, int workHours) {
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("日付が不正です: " + day);
        }
        if (workHours < 0 || workHours > 24) {
            throw new IllegalArgumentException("勤務時間が不正です: " + workHours);
        }
        this.employeeId = employeeId;
        this.day = day;
        this.workHours = workHours;
    }

    public String getEmployeeId() { return employeeId; }
    public int getDay() { return day; }
    public int getWorkHours() { return workHours; }

    /** 8時間を超えた分を残業時間として返す */
    public int overtimeHours() {
        return Math.max(0, workHours - 8);
    }

    /** 残業した日かどうか */
    public boolean isOvertimeDay() {
        return overtimeHours() > 0;
    }
}
```

**確認すること**
- `new Attendance("E001", 32, 8)` を実行して、例外が出ることを確認する
- `overtimeHours()` のようなメソッドを **Attendance クラスの中に置いた理由** を説明してみる
  （答え：`workHours` というデータを持っているのが Attendance なので、それを使う計算も同じ場所にあるべき。これを **凝集度が高い** と言います）

## ⑤ 課題（240分）

> 💡 **詰まったら30分考えてから** `08_answers.md` を参照してください。見たら必ず閉じて、もう一度自力で書き直すこと。
`Department.java` と `EmployeeMain.java` を作成。

1. `Department` クラス：部署ID、部署名、上限人数を持つ
2. `Employee` に `Department` を持たせる（クラスがクラスを持つ＝**関連**）
3. `Employee` に以下を追加
   - `private final int baseSalary`（基本給）
   - `public int calculateSalary(int overtimeHours)` — 基本給 + 残業時間 × 2000 を返す
4. `EmployeeMain` で社員3人を作り、それぞれの給与を表示
5. **不正な値（負の基本給、null の名前、空文字の部署）を渡したとき、必ず例外が出ることを確認する**
6. `Employee` の `toString()` をオーバーライドして、`System.out.println(employee)` で読みやすく出るようにする

```java
@Override
public String toString() {
    return "Employee{id=" + employeeId + ", name=" + name + ", dept=" + department.getName() + "}";
}
```
（`toString()` は「オブジェクトを文字列にするとき呼ばれるメソッド」です。デバッグで極めて役立つので、**実務では自作クラスに必ず付けます**）

## ⑥ 自己チェック
- [ ] クラスとインスタンスの違いを説明できる
- [ ] なぜ配列を並べるよりクラスにまとめる方が良いのか説明できる
- [ ] コンストラクタは何をするものか説明できる
- [ ] `this` が何を指すか説明できる
- [ ] カプセル化の目的を「隠すため」以外の言葉で説明できる
- [ ] セッターを全フィールドに機械的に作ってはいけない理由を説明できる
- [ ] static変数とインスタンス変数の違いを説明できる
- [ ] 実務で static を多用すべきでない理由を説明できる
- [ ] コンストラクタで引数チェックをする理由を説明できる
- [ ] `toString()` をオーバーライドする理由を説明できる

---

# Day 4：継承・インタフェース・多態性（所要9時間）

> 今日が2週間で**最も抽象的な日**です。分からなくても進んでください。Day 10 で Spring Boot を触ると必ず繋がります。

## ① 今日のゴール
- インタフェースを使って「変更に強い構造」が作れる
- ポリモーフィズムが何の役に立つのか腑に落ちる
- enum と record を実務レベルで使える

## ② 新出用語
継承 / extends / スーパークラス / サブクラス / オーバーライド / 抽象クラス / abstract / インタフェース / implements / ポリモーフィズム / enum / record / @Override

## ③ 座学（150分）

### インタフェース：「約束」だけを決める

インタフェースは **「こういうメソッドを持っていること」という約束** だけを書いたものです。中身は書きません。

```java
// 約束：「残業代を計算できること」
public interface OvertimePayCalculator {
    int calculate(int overtimeHours);   // 中身は書かない。; で終わる
}
```

この約束を守るクラスを複数作れます。

```java
// 正社員：時給2000円 × 割増1.25
public class RegularEmployeeCalculator implements OvertimePayCalculator {
    @Override
    public int calculate(int overtimeHours) {
        return (int) (overtimeHours * 2000 * 1.25);
    }
}

// 契約社員：時給1800円 × 割増1.25
public class ContractEmployeeCalculator implements OvertimePayCalculator {
    @Override
    public int calculate(int overtimeHours) {
        return (int) (overtimeHours * 1800 * 1.25);
    }
}
```

> **【業務知識】割増率は雇用形態では変わりません。** 1日8時間を超える時間外労働の割増賃金（25%以上）は労働基準法37条で定められており、正社員・契約社員・アルバイトのいずれにも適用されます。
> **雇用形態で変わるのは「時給（単価）」の方**です。この教材でも `05_project-spec.md` の仕様に合わせ、**割増率は全形態 1.25、差は時給** で統一します。

> **⚠ 上のコードは金額計算に `double` と `int` を使っています。これは Day 1 で「やってはいけない」と学んだ書き方です。**
> **今日はポリモーフィズムの説明に集中するため、あえて単純な型で書いています。** 金額は **Day 6 で `BigDecimal` に直します**（そこで「なぜ直す必要があるのか」を実際の誤差で確認します）。
> **教材のコードでも「今は暫定」の箇所には必ずこの注記があります。** 現場でも同じで、暫定コードには理由と直す時期をコメントに書きます。何も書かれていない暫定コードは、永久に残ります。

### ポリモーフィズム：これが何の役に立つのか

**インタフェース無しで書くと、こうなります（悪い例）**
```java
public int calculatePay(String employeeType, int hours) {
    if (employeeType.equals("REGULAR")) {
        return (int) (hours * 2000 * 1.25);
    } else if (employeeType.equals("CONTRACT")) {
        return hours * 1800;
    } else if (employeeType.equals("PART_TIME")) {
        return hours * 1200;
    }
    // 雇用形態が増えるたび、このメソッドを修正する必要がある
    return 0;
}
```

**問題点**：新しい雇用形態（派遣社員）が増えるたびに、**このメソッドを開いて修正**しなければなりません。修正すれば、既存の動いている部分を壊すリスクが生じ、既存部分のテストも全部やり直しです。

**インタフェースを使うと、こうなります（良い例）**
```java
public int calculatePay(OvertimePayCalculator calculator, int hours) {
    return calculator.calculate(hours);   // 相手が誰かは知らない。約束だけ知っている
}
```

派遣社員が増えても、**このメソッドは一切変更不要**です。新しいクラスを1つ追加するだけです。

```java
public class TemporaryStaffCalculator implements OvertimePayCalculator {
    @Override
    public int calculate(int overtimeHours) {
        return (int) (overtimeHours * 1500 * 1.25);
    }
}
```

**これが「拡張には開き、修正には閉じる」＝開放閉鎖の原則（OCP）** です。Day 12 で詳しくやりますが、**今日体験しておくことが重要**です。

> **腑に落とすための一言**：ポリモーフィズムとは、**「呼ぶ側が、相手が誰なのかを知らなくてよくなる」** ということです。あなたはコンセントに何を挿すか知らなくても、コンセントの形さえ合えば動きます。インタフェースはこの「コンセントの形」です。

### 継承（extends）
親クラスの機能を引き継いで、子クラスを作ります。

```java
public class Employee {                      // 親（スーパークラス）
    protected String name;                   // protected = 子クラスからは触れる
    public String describe() { return "社員: " + name; }
}

public class Manager extends Employee {      // 子（サブクラス）
    private int teamSize;

    @Override                                // 親のメソッドを上書きする印
    public String describe() {
        return "管理職: " + name + "（" + teamSize + "名を管理）";
    }
}
```

> **【実務の重要指針】継承よりインタフェース（＋委譲）を優先してください。**
> 継承は親の実装まで丸ごと引き継ぐため、親を修正すると全ての子が壊れます（**密結合**）。また Java は親を1つしか持てません。
> 現場のルール：**「AはBの一種である」が100%成立するときだけ継承。それ以外はインタフェース。**
> ✅ `Manager is a Employee` → 継承OK
> ❌ `AttendanceService is a DatabaseConnection` → 継承NG（持つ＝委譲にする）
> **迷ったらインタフェース。これは業界の共通見解です。**

### 抽象クラス
「一部は共通実装したいが、一部は子に書かせたい」ときに使います。
```java
public abstract class AbstractReport {
    // 共通の流れは親が持つ
    public final String generate() {
        return header() + body() + footer();
    }
    private String header() { return "===== レポート =====\n"; }
    private String footer() { return "\n===================="; }

    protected abstract String body();   // 中身は子が書く（abstract = 未完成）
}

public class MonthlyReport extends AbstractReport {
    @Override
    protected String body() { return "今月の勤務時間は160時間です"; }
}
```
（これは **テンプレートメソッドパターン** という定石です。全体の流れを親が固定し、変わる部分だけ子に任せる）

### enum（列挙型）— 実務で必ず使う
「取りうる値が決まっているもの」は、String ではなく enum にします。

```java
public enum EmploymentType {
    REGULAR("正社員", 2000, 1.25),
    CONTRACT("契約社員", 1800, 1.25),
    PART_TIME("アルバイト", 1200, 1.25);   // 割増率は法定。雇用形態では変わらない

    private final String label;
    private final int hourlyRate;
    private final double overtimeRate;

    EmploymentType(String label, int hourlyRate, double overtimeRate) {
        this.label = label;
        this.hourlyRate = hourlyRate;
        this.overtimeRate = overtimeRate;
    }

    public String getLabel() { return label; }

    public int calculateOvertimePay(int hours) {
        return (int) (hours * hourlyRate * overtimeRate);
    }
}
```
使う側：
```java
EmploymentType type = EmploymentType.REGULAR;
System.out.println(type.getLabel());                     // 正社員
System.out.println(type.calculateOvertimePay(10));       // 25000
```

> **⚠ この `calculateOvertimePay` は「enum に振る舞いを持たせられる」ことを示すための例です。**
> **Day 10 以降、金額計算では使いません。** 実際の時給は社員ごとに違う（同じ正社員でも等級や昇給で変わる）ため、**計算にはDBの `employees.hourly_rate` を使います**（`05_project-spec.md` §3.3）。
> enum が持つ金額は、**社員登録時の初期値の目安**という位置づけです。**「区分から金額を導出する」設計は、例外が1件出た瞬間に破綻します。**

**enum を使う理由**：`String type = "REGULER";`（スペルミス）はコンパイルを通ってしまい、実行時にバグります。enum ならスペルミスは**コンパイルエラー**になります。**「間違いを実行前に見つける」というJavaの強みを最大限使う書き方です。**

### record（Java 16以降）— 定型コードを消す
「データを持つだけのクラス」を1行で書けます。
```java
public record MonthlySummary(String employeeId, int year, int month,
                             int totalHours, int overtimeHours) {}
```
これだけで、コンストラクタ・ゲッター・equals・hashCode・toString が自動生成されます。

```java
MonthlySummary s = new MonthlySummary("E001", 2026, 8, 168, 8);
System.out.println(s.totalHours());   // 168 （get は付かない）
System.out.println(s);                // MonthlySummary[employeeId=E001, ...]
```
**record は不変（イミュータブル）** です。作った後に値を変えられません。**DTO（層をまたぐデータの運び屋）には record が実務の定石**になっています。

## ④ 写経ハンズオン（120分）
上記の `OvertimePayCalculator` 一式と `EmploymentType` enum、`MonthlySummary` record を手打ちして動かす。

**確認すること**
- `OvertimePayCalculator` 型の変数に、3種類のクラスを順番に代入して同じメソッドを呼ぶ

（`List` は Day 5 で正式にやります。ここでは「複数のオブジェクトをまとめて順番に取り出す入れ物」とだけ理解すればOKです。`import java.util.List;` が必要です）
```java
List<OvertimePayCalculator> calculators = List.of(
    new RegularEmployeeCalculator(),
    new ContractEmployeeCalculator(),
    new TemporaryStaffCalculator()
);
for (OvertimePayCalculator c : calculators) {
    System.out.println(c.calculate(10));   // 呼び方は同じなのに結果が違う ← これが多態性
}
```

## ⑤ 課題（240分）

> 💡 **詰まったら30分考えてから** `08_answers.md` を参照してください。見たら必ず閉じて、もう一度自力で書き直すこと。
勤怠システムの「打刻種別」を設計する。

1. `enum AttendanceType` を作る：`CLOCK_IN`（出勤）, `CLOCK_OUT`（退勤）, `BREAK_START`（休憩開始）, `BREAK_END`（休憩終了）。それぞれに日本語ラベルを持たせる
2. `interface WorkTimeRule` を作る：`int calculateWorkMinutes(LocalTime clockIn, LocalTime clockOut)`
3. 実装を2つ作る
   - `StandardWorkTimeRule` — 退勤−出勤から、**`05_project-spec.md` §3.2 の休憩ルール**（6時間以下:0分／8時間以下:45分／それ超:60分）で自動控除する
   - `FlexWorkTimeRule` — コアタイム（10:00〜15:00）を含まない場合は例外を投げる
4. `record AttendanceRecord(String employeeId, LocalDate date, AttendanceType type, LocalTime time)` を作る
   （`LocalDate` / `LocalTime` は Day 6 で詳しくやります。今日は「日付と時刻の型」とだけ理解すればOK）
5. `WorkTimeRule` を差し替えて計算できることを確認する
6. **「なぜ if 文で分岐せずインタフェースにしたのか」を `log/day04.md` に自分の言葉で説明する**

## ⑥ 自己チェック
- [ ] インタフェースとは何か、1文で説明できる
- [ ] ポリモーフィズムが「何の問題を解決するのか」を説明できる
- [ ] if 分岐で書いた場合と比べて、インタフェースの利点を説明できる
- [ ] 継承よりインタフェースを優先すべき理由を説明できる
- [ ] 継承を使ってよい条件（is-a関係）を説明できる
- [ ] 抽象クラスとインタフェースの使い分けを説明できる
- [ ] String ではなく enum を使うべき理由を説明できる
- [ ] record が何を自動生成してくれるか説明できる
- [ ] `@Override` を書く意味を説明できる（＝書き間違いをコンパイルエラーにできる）

---

# Day 5：コレクション・Optional・Stream（所要9時間）

## ① 今日のゴール
- List / Map / Set を実務レベルで使い分けられる
- Stream API で集計処理が書ける
- Optional で NullPointerException を防げる

## ② 新出用語
コレクション / List / ArrayList / Map / HashMap / Set / HashSet / ジェネリクス / equals / hashCode / Optional / ラムダ式 / Stream / filter / map / collect / var（型推論） / テキストブロック

## ③ 座学（150分）

### List：順番のある入れ物
```java
List<String> names = new ArrayList<>();   // <String> = 中身は文字列だけ（ジェネリクス）
names.add("田中");
names.add("佐藤");
names.get(0);            // "田中"
names.size();            // 2
names.contains("佐藤");   // true
names.remove("田中");
for (String n : names) { System.out.println(n); }

// 変更不可なリストを作る（実務で多用）
List<String> fixed = List.of("月", "火", "水");
```

**なぜ配列でなく List を使うのか**：配列はサイズが固定です。List は自動で伸びます。**実務では99%が List** です。

> **`List<String> names = new ArrayList<>();` の左辺がなぜ `ArrayList` ではなく `List` なのか。**
> 「インタフェース型で受ける」と、後で `LinkedList` に差し替えても使う側のコードを変えずに済むからです。Day 4 の多態性の実践です。**現場ではこれが標準の書き方**なので、必ずこう書いてください。

### Map：キーと値のペア
```java
Map<String, Integer> monthlyHours = new HashMap<>();
monthlyHours.put("E001", 168);
monthlyHours.put("E002", 152);

monthlyHours.get("E001");                    // 168
monthlyHours.get("E999");                    // null ← 存在しないキーは null！
monthlyHours.getOrDefault("E999", 0);        // 0  ← 実務ではこちらを使う
monthlyHours.containsKey("E001");            // true

for (Map.Entry<String, Integer> entry : monthlyHours.entrySet()) {
    System.out.println(entry.getKey() + " → " + entry.getValue());
}
```

### Set：重複を許さない
```java
Set<String> departments = new HashSet<>();
departments.add("営業部");
departments.add("営業部");   // 2回目は無視される
System.out.println(departments.size());   // 1
```

### 【重要】equals と hashCode
自作クラスを List や Map で使うなら、この2つを必ずセットで実装します。

```java
Employee a = new Employee("E001", "田中");
Employee b = new Employee("E001", "田中");
System.out.println(a.equals(b));   // equals未実装なら false！（同じ内容なのに）
```
Java標準の `equals` は「同じ場所を指しているか」しか見ません。「社員IDが同じなら同じ社員」と判定させたいなら、自分で書く必要があります。

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Employee other = (Employee) o;
    return Objects.equals(employeeId, other.employeeId);
}

@Override
public int hashCode() {
    return Objects.hash(employeeId);
}
```
**【今は暗記でOK】この形は定型です。IntelliJ の `Alt+Insert` → `equals() and hashCode()` で自動生成できます。**

**なぜ hashCode もセットなのか**：`HashMap` や `HashSet` は、まず hashCode で「どの引き出しに入れるか」を決め、次に equals で中身を比べます。hashCode が違うと別の引き出しに入り、equals が呼ばれず「同じなのに別物」と判定されます。**「equals をオーバーライドしたら hashCode もオーバーライドする」は絶対ルール**です。

> **なお `record` を使えば equals/hashCode は自動生成されます。** データを持つだけのクラスなら record が第一選択です。

### Optional：null を型で表現する
```java
// 悪い書き方
public Employee findById(String id) {
    // 見つからないと null を返す → 呼び出し側が忘れるとヌルポ
}

// 良い書き方
public Optional<Employee> findById(String id) {
    // 「あるかもしれないし、無いかもしれない」ことが型で分かる
}

// 使う側
Optional<Employee> found = findById("E001");
if (found.isPresent()) {
    System.out.println(found.get().getName());
}
// より実務的な書き方
String name = found.map(Employee::getName).orElse("該当なし");
found.ifPresent(e -> System.out.println(e.getName()));
Employee e = found.orElseThrow(() -> new EmployeeNotFoundException("E001が見つかりません"));
```

**Optional の使いどころ（現場のルール）**
- ✅ **メソッドの戻り値**に使う（「無いかもしれない」を呼び出し側に強制的に意識させる）
- ❌ フィールドや引数には基本使わない（冗長になる）
- ❌ `optional.get()` をチェック無しで呼ばない（意味が無い）

### ラムダ式
「その場限りの短い処理」を渡す書き方です。
```java
// 従来の書き方
list.sort(new Comparator<String>() {
    @Override
    public int compare(String a, String b) { return a.compareTo(b); }
});

// ラムダ式
list.sort((a, b) -> a.compareTo(b));
```
`(引数) -> 処理` と読みます。

### 現代のJavaでよく見る2つの記法（読めるようにしておく）

**① `var`（型推論。Java 10以降）**
```java
var records = repository.findAll();          // 右辺を見れば型が分かるときだけ使ってよい
var totalByEmployee = new HashMap<String, Integer>();

// ❌ これは使ってはいけない例（何が返るのか読み手に分からない）
var result = service.process(data);
```
> **判断基準は「右辺を見て型が即分かるか」だけ**です。分からないなら型を書く。**タイプ数を減らすための機能ではありません。**
> 現場の既存コードには必ず出てくるので、**読めることが必須**です。

**② テキストブロック（`"""`。Java 15以降）**
```java
String json = """
    {"employeeId": "E001", "name": "田中太郎"}
    """;                                     // 改行と " をエスケープなしでそのまま書ける
```
> **テストでJSONやSQLを書くときに多用します**（Day 13 で実際に使います）。従来は `"{\"employeeId\": \"E001\"}"` のようにエスケープだらけでした。

### Stream API：集計処理の主役
```java
List<Attendance> records = List.of(
    new Attendance("E001", 1, 8),
    new Attendance("E001", 2, 10),
    new Attendance("E001", 3, 7),
    new Attendance("E002", 1, 9)
);

// ① 絞り込む（filter）
List<Attendance> overtimeDays = records.stream()
    .filter(a -> a.isOvertimeDay())
    .toList();

// ② 変換する（map）
List<Integer> hoursList = records.stream()
    .map(a -> a.getWorkHours())
    .toList();

// ③ 合計する
int total = records.stream()
    .mapToInt(a -> a.getWorkHours())
    .sum();

// ④ 社員IDでグルーピングして合計（実務で最頻出）
Map<String, Integer> totalByEmployee = records.stream()
    .collect(Collectors.groupingBy(
        Attendance::getEmployeeId,
        Collectors.summingInt(Attendance::getWorkHours)
    ));
// → {E001=25, E002=9}

// ⑤ 並べ替え
List<Attendance> sorted = records.stream()
    .sorted(Comparator.comparingInt(Attendance::getWorkHours).reversed())
    .toList();
```

`Attendance::getEmployeeId` は **メソッド参照** といい、`a -> a.getEmployeeId()` の短縮形です。

> **Streamの読み方**：`.stream()` で「流れ」を作り、`.filter()` `.map()` で加工し、`.toList()` `.collect()` `.sum()` で **最後に必ず取り出す**。取り出さないと何も実行されません（遅延評価）。
>
> **【実務の注意】Streamを何段もネストして書かないでください。** 3〜4段を超えたら、for文の方が読みやすいことが多いです。**「短く書く」ことより「読んで分かる」ことが優先**です。これはレビューでよく指摘されます。

## ④ 写経ハンズオン（120分）
上記コード全てを手打ちして動かす。特に groupingBy は結果を printf で表示して確認する。

## ⑤ 課題（240分）

> 💡 **詰まったら30分考えてから** `08_answers.md` を参照してください。見たら必ず閉じて、もう一度自力で書き直すこと。
`AttendanceSummaryService.java` を作成。

1. `List<Attendance>` を受け取り、以下を返すメソッドをそれぞれ作る
   - `Map<String, Integer> totalHoursByEmployee(List<Attendance> records)` — 社員別合計時間
   - `Map<String, Integer> overtimeHoursByEmployee(List<Attendance> records)` — 社員別残業時間
   - `Optional<String> findMostOvertimeEmployee(List<Attendance> records)` — 最も残業が多い社員ID（データが空なら empty）
   - `List<String> findExcessiveOvertimeEmployees(List<Attendance> records, int threshold)` — 残業が閾値を超えた社員IDの一覧
2. `Employee` クラスに `equals` / `hashCode` を実装し、`Set<Employee>` に同じIDの社員を2回入れて1件になることを確認
3. すべて Stream API で書く
4. **空リストを渡したときに例外が出ないことを確認する**

## ⑥ 自己チェック
- [ ] List / Map / Set の使い分けを説明できる
- [ ] `List<String> x = new ArrayList<>();` の左辺をインタフェース型にする理由を説明できる
- [ ] equals をオーバーライドしたら hashCode も必要な理由を説明できる
- [ ] Optional を使う目的と、使うべき場所（戻り値）を説明できる
- [ ] ラムダ式の読み方を説明できる
- [ ] filter / map / collect のそれぞれの役割を説明できる
- [ ] groupingBy を使って集計が書ける
- [ ] Stream を使いすぎない方がよい場合があることを説明できる

---

# Day 6：例外設計・日時・BigDecimal・ログ・ビルドツール（所要9時間）

## ① 今日のゴール
- 業務アプリらしい「堅牢な」コードが書ける
- 例外を「握りつぶさない」設計ができる
- Maven でライブラリを追加できる

## ② 新出用語
検査例外 / 非検査例外 / try-catch-finally / try-with-resources / カスタム例外 / BigDecimal / LocalDate / LocalDateTime / Duration / SLF4J / ログレベル / Maven / pom.xml / 依存関係

## ③ 座学（150分）

### 例外の種類
| 種類 | 例 | 対処 | 使いどころ |
|---|---|---|---|
| **検査例外**（Checked） | `IOException` | try-catch か throws が**強制される** | 呼び出し側が回復できる異常（ファイルが無い等） |
| **非検査例外**（Unchecked / RuntimeException） | `NullPointerException`, `IllegalArgumentException` | 強制されない | プログラムのバグ、引数不正 |

### 例外処理の書き方
```java
try {
    int result = riskyOperation();
} catch (IllegalArgumentException e) {
    log.warn("引数が不正です", e);
    throw new BusinessException("入力値を確認してください", e);   // 情報を足して投げ直す
} finally {
    // 例外が出ても出なくても必ず実行される（後片付け）
}
```

### 【最重要】やってはいけない例外処理
```java
// ❌❌❌ 絶対にやってはいけない：例外の握りつぶし
try {
    doSomething();
} catch (Exception e) {
    // 何もしない
}
```
これをやると、**障害が起きているのに誰も気づけません**。原因調査が不可能になり、現場では最も嫌われるコードです。

```java
// ❌ これもダメ：情報を捨てている
catch (Exception e) {
    log.error("エラーが発生しました");   // e を渡していない＝スタックトレースが消える
}

// ⭕ 正しい
catch (IOException e) {
    log.error("勤怠ファイルの読み込みに失敗しました. path={}", path, e);   // e を必ず渡す
    throw new AttendanceFileException("勤怠ファイルを読み込めませんでした", e);
}
```

**現場のルール**
1. `catch (Exception e)` と広く捕まえない。**捕まえるべき具体的な例外だけ**捕まえる
2. catch したら、**ログに出す** か **投げ直す** か **明確に回復する**。何もしないは禁止
3. **例外を渡す（`, e`）のを絶対に忘れない**。これが無いと原因が永久に分かりません
4. ログメッセージには **「何をしようとして失敗したか」＋「関係するID等の値」** を入れる

### カスタム例外
業務上の異常は、自分で例外クラスを作ります。
```java
public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String employeeId) {
        super("社員が見つかりません: " + employeeId);
    }
}
```
「見つからない」ことを `null` を返して表すのではなく、例外で表す方が呼び出し側が対処を強制されます。

### try-with-resources（ファイルやDB接続を扱うとき必須）
```java
// ⭕ 自動で close される
try (BufferedReader reader = Files.newBufferedReader(Path.of("attendance.csv"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
}
```
`try (...)` の括弧内で開いたものは、**例外が出ても必ず閉じられます**。ファイルやDB接続を閉じ忘れると、システムは数日で資源枯渇して停止します。**業務システムでは必須の書き方です。**

### 日時API（java.time）
```java
LocalDate today = LocalDate.now();                      // 2026-08-15（日付のみ）
LocalDate date = LocalDate.of(2026, 8, 15);
LocalTime time = LocalTime.of(9, 30);                   // 09:30（時刻のみ）
LocalDateTime dt = LocalDateTime.of(date, time);        // 日付＋時刻

date.plusDays(7);                                       // 7日後
date.getDayOfWeek();                                    // SATURDAY
date.withDayOfMonth(1);                                 // その月の1日

// 勤務時間の計算
LocalTime in = LocalTime.of(9, 0);
LocalTime out = LocalTime.of(18, 30);
Duration worked = Duration.between(in, out);            // 9時間30分
long minutes = worked.toMinutes();                      // 570

// 文字列との変換
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
String s = date.format(fmt);                            // "2026/08/15"
LocalDate parsed = LocalDate.parse("2026/08/15", fmt);
```

> **【重要】古い `java.util.Date` と `Calendar` は使わないでください。** バグの温床として有名で、Java 8 以降は `java.time` パッケージが正解です。既存コードで見かけたら「古い書き方だな」と認識してください。

### BigDecimal（金額計算）
Day 1 で触れた「double を金額に使うな」の答えです。
```java
BigDecimal hourlyRate = new BigDecimal("2000");      // ⚠ 必ず文字列で作る
BigDecimal hours = new BigDecimal("10.5");
BigDecimal pay = hourlyRate.multiply(hours);          // 21000

// 割り算は「小数何桁で、どう丸めるか」を必ず指定する（省略すると例外）
BigDecimal average = total.divide(count, 2, RoundingMode.HALF_UP);

// 比較は compareTo（equals は「0」と「0.00」を別物と判定するので注意）
if (pay.compareTo(BigDecimal.ZERO) > 0) { ... }
```
**`new BigDecimal(0.1)` と数値で書くと double の誤差がそのまま入ります。必ず `new BigDecimal("0.1")` と文字列で書いてください。**

### ログ
```java
private static final Logger log = LoggerFactory.getLogger(AttendanceService.class);

log.debug("処理開始 employeeId={}", employeeId);   // 開発時の詳細
log.info("月次集計完了 employeeId={} total={}", employeeId, total);  // 通常の記録
log.warn("残業時間が上限に近づいています employeeId={} hours={}", id, h);  // 注意
log.error("集計に失敗しました employeeId={}", employeeId, e);   // 異常（例外を最後に渡す）
```
- `{}` はプレースホルダで、後ろの引数が順に入ります。**`"..." + value` と文字列連結しないでください**（ログレベルで出力されない場合も連結処理が走り、性能を落とします）
- **ログに個人情報・パスワード・カード番号を出さないこと**。これは事故として扱われます

### Maven
`pom.xml` にライブラリを書くと、自動でダウンロードして使えるようにしてくれます。
```xml
<dependencies>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>2.0.18</version>
    </dependency>
</dependencies>
```
よく使うコマンド：
```
mvn clean       # 生成物を消す
mvn compile     # コンパイル
mvn test        # テスト実行
mvn package     # jar を作る
```

## ④ 写経ハンズオン（120分）

**先に、ログを使う準備をしてください。**（`04_setup.md` の「4-3」で作った `week1/pom.xml` に slf4j-api と logback-classic が入っていれば、そのままで構いません。入っていなければ今追加して、IntelliJ 右端の Maven パネルで🔄を押してください）
```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.18</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>       <!-- 実際にログを出力する実装。これが無いと警告だけ出て何も記録されない -->
    <artifactId>logback-classic</artifactId>
    <version>1.6.3</version>
</dependency>
```
> **SLF4J は「ログの共通インタフェース」で、実装（logback 等）とセットで初めて動きます。** これも Day 4 の「インタフェースと実装を分ける」の実例です。

**次に、この後で使うカスタム例外を作ってください**（先に定義しないとコンパイルが通りません）。
```java
// AttendanceFileException.java
public class AttendanceFileException extends RuntimeException {
    public AttendanceFileException(String message, Throwable cause) {
        super(message, cause);      // cause＝元の例外。これを渡すとスタックトレースが繋がる
    }
}
```
> `Throwable cause` を受け取って `super(message, cause)` に渡すのが**カスタム例外の必須作法**です。
> これを省くと「元々どこで何が起きたのか」の情報が消え、調査不能になります。

次に、CSVファイルから勤怠データを読み込む処理を書きます（`AttendanceCsvLoader` などのクラスを作り、その中に置いてください）。
```java
// クラスの先頭に置く。ログを出すクラスごとに1つ用意する
private static final Logger log = LoggerFactory.getLogger(AttendanceCsvLoader.class);

public List<Attendance> loadFromCsv(Path path) {
    List<Attendance> result = new ArrayList<>();
    try (BufferedReader reader = Files.newBufferedReader(path)) {
        String line;
        boolean isHeader = true;
        while ((line = reader.readLine()) != null) {
            if (isHeader) { isHeader = false; continue; }
            String[] cols = line.split(",");
            result.add(new Attendance(cols[0], Integer.parseInt(cols[1]), Integer.parseInt(cols[2])));
        }
    } catch (IOException e) {
        log.error("勤怠CSVの読み込みに失敗しました. path={}", path, e);
        throw new AttendanceFileException("勤怠CSVを読み込めませんでした: " + path, e);
    }
    return result;
}
```
**確認すること**
- 存在しないパスを渡して、例外メッセージとログを確認する
- 列が足りない不正なCSV行を作って、何が起きるか確認する（`ArrayIndexOutOfBoundsException`）→ **それを事前チェックで防ぐコードを追加する**

## ⑤ 課題（240分）

> 💡 **この日はコード全文の解答例がありません。** 課題を終えたら `08_answers.md` 末尾の「自己検証チェック（Day 6）」で、満たすべき条件を1つずつ確認してください。30分詰まったときは `07_troubleshooting.md` と該当日の座学に戻ること。
1. カスタム例外をあと2つ作る：`EmployeeNotFoundException`, `InvalidAttendanceException`
   （`AttendanceFileException` はハンズオンで作成済み）
2. `Attendance` の勤務時間を `int hours` から `LocalTime clockIn, LocalTime clockOut` に変え、`Duration` で勤務時間を計算するよう改修する
3. 給与計算を `BigDecimal` で書き直す
4. 全メソッドに適切なログを入れる（`debug` / `info` / `error` を使い分ける）
5. `week1/src/main/resources/logback.xml` を作り、**ログがコンソールだけでなくファイルにも出る**よう設定する
   - 出力先は **`week1/logs/app.log`** にしてください（リポジトリ直下の `log/` は**学習ログ専用**です。混ぜないこと）
   - `.gitignore` に `logs/` を追加する。**ログファイルはコミットしません**（毎回変わるうえ、個人情報が入りうるため）
   - 設定ファイルの雛形は logback 公式の "Chapter 3: Configuration" が最短です
6. **わざと例外を起こし、ログファイルにスタックトレースが記録されることを確認する**

## ⑥ 自己チェック
- [ ] 検査例外と非検査例外の違いを説明できる
- [ ] 例外を握りつぶしてはいけない理由を説明できる
- [ ] `log.error("失敗")` と `log.error("失敗", e)` の違いを説明できる
- [ ] try-with-resources が必要な理由を説明できる
- [ ] `java.util.Date` を使わない理由を説明できる
- [ ] `LocalDate` / `LocalTime` / `LocalDateTime` / `Duration` を使い分けられる
- [ ] `new BigDecimal("0.1")` と `new BigDecimal(0.1)` の違いを説明できる
- [ ] BigDecimal の割り算で丸めモードが必須な理由を説明できる
- [ ] ログレベル4種の使い分けを説明できる
- [ ] ログに `+` で文字列連結しない理由を説明できる
- [ ] `pom.xml` にライブラリを追加できる

---

# Day 7：【復習日】Git実務フロー と 単体テスト（所要10時間＋中間テスト90分）

## ① 今日のゴール
- Git のブランチ運用とプルリクエストが一人でできる
- JUnit で単体テストが書ける
- **Day 1〜6 の穴を埋める**

## ② 午前：総復習（180分）— 飛ばさないこと

1. Day 1〜6 の自己チェックリストを**全部**、何も見ずに口頭で答える
2. 答えられなかった項目を一覧化する
3. その項目だけ、該当日のページを読み直す
4. **答えられなかった概念について、小さなコードを新規に書いて動かす**（読み直すだけでは定着しません）

## ③ 午後座学①：Git 実務フロー（90分）

### 現場の標準的な流れ
```bash
# 1. 最新の main を取り込む
git checkout main
git pull origin main

# 2. 作業用ブランチを切る（ブランチ名は「種別/チケット番号-内容」が定番）
git checkout -b feature/ATT-12-monthly-summary

# 3. コードを書く

# 4. 変更を確認する（重要：add する前に必ず見る）
git status
git diff

# 5. ステージング（コミット対象に載せる）
git add src/main/java/com/example/attendance/service/SummaryService.java
# ⚠ git add . は避ける（意図しないファイルが混入する事故が多い）

# 6. コミット
git commit -m "feat: 月次勤怠集計機能を追加"

# 7. リモートに送る
git push origin feature/ATT-12-monthly-summary

# 8. GitHub でプルリクエストを作成 → レビュー依頼
```

### 【実務標準】main を直接触れないようにする（ブランチ保護）

**現場では、`main` に直接 push できないよう設定されているのが普通です。** 一人で学習していても、今日この設定を入れてください。**「そういうルールがある」ではなく「そもそも押せない」状態にするのが正しい運用**だと体で覚えるためです。

GitHub → リポジトリの `Settings` → `Rules` → `Rulesets` → `New branch ruleset`
- Target branches: `main`
- **Require a pull request before merging** をON（＝直接 push を禁止）
  - **Required approvals は `0` にしてください。** **一人で学習していると、自分のPRを自分で承認できません。** 1以上のままだと**永久にマージできなくなります**（チーム開発では通常1以上にします）
- **Require status checks to pass** をON（Day 13 で CI を作ったら、そのチェックを必須に指定）
- **Bypass list には何も追加しないでください。** 「そもそも押せない」状態を体験するのが今日の目的です
  - GitHub の Bypass は既定が **Always**（＝直接 push が通る）なので、**追加した時点でこの設定の意味が消えます**
  - どうしても詰まったときは、Ruleset の画面で一時的に `Disabled` にして作業し、**必ず `Active` に戻してください**（どうしても追加する場合は、モードを **For pull requests only** にすること）

**設定後は、マージを GitHub の PR 画面から行ってください。** ローカルで `git merge` して `git push origin main` はできなくなります（それが目的です）。

これで、以下の流れが**強制**されます。
```
ブランチを切る → 変更 → push → PR作成 → CIが緑 → マージ → main が更新される
```

> **なぜここまでするのか。** `main` は「いつでもリリースできる状態」でなければなりません。誰かが壊れたコードを直接 push すると、**その瞬間から全員の作業が壊れたコードの上に積み上がります**。
> **「レビューを通す」「テストが緑」を人の善意ではなく仕組みで担保する** — これが現場の当たり前です。
>
> **Day 0〜6 で `main` に直接 push していたのは、ブランチ保護を掛ける前の学習用の運用です。** 今日この設定を入れた瞬間から、自分のリポジトリでも**必ずブランチとPRを経由**することになります。

### コミットメッセージの書き方（実務標準：Conventional Commits）
```
feat: 月次勤怠集計機能を追加        ← 新機能
fix: 残業時間が負になるバグを修正    ← バグ修正
refactor: SummaryService の重複を除去 ← 動作を変えないリファクタ
test: 集計ロジックの単体テストを追加  ← テスト追加
docs: READMEにセットアップ手順を追記  ← ドキュメント
```
**悪い例**：`修正`, `いろいろ`, `wip`, `update`
**理由**：半年後に「このバグはいつ入った？」と履歴を追うとき、意味のないメッセージは何の助けにもなりません。

### コミットの粒度
- **1コミット = 1つの意味のある変更**
- 「機能追加とバグ修正とフォーマット整形」を1コミットに混ぜない
- **理由**：問題が起きたとき、そのコミットだけを取り消せるようにするため

### .gitignore（絶対に必要）
```
target/
*.class
.idea/
*.iml
application-local.yml
```
**⚠ パスワード・APIキー・接続情報を含むファイルは絶対にコミットしないでください。** GitHubに一度上がると、後で削除しても履歴に永久に残ります。**これは実際の重大インシデント事例が多数ある項目です。**

### コンフリクトの直し方
```
<<<<<<< HEAD
    自分の変更
=======
    相手の変更
>>>>>>> main
```
1. 3つのマーカー行（`<<<<<<<`, `=======`, `>>>>>>>`）を**すべて削除**する
2. 正しい最終形にコードを整える（両方採用することも多い）
3. `git add` して `git commit`
4. **必ずビルドとテストを流してから push する**

## ④ 午後座学②：単体テスト（JUnit 5）（90分）

### なぜテストを書くのか
新人が最初に思うのは「動作確認すれば十分では？」です。答えは：

1. **手動確認は3ヶ月後に再現できない。** 自動テストは何度でも同じ確認を1秒で繰り返せる
2. **リファクタリングができるようになる。** テストがあれば「壊していないこと」を確認しながら安心してコードを改善できる。テストが無いコードは、怖くて誰も触れなくなります（＝レガシー化）
3. **仕様書になる。** テストコードを読めば「このメソッドは何をするのか」が分かる

**「テストを書けない新人」と「テストを書ける新人」の間には、現場での信頼度に決定的な差があります。**

### 基本形

> **⚠ 以下の例は Day 3 の `Attendance(String employeeId, int day, int workHours)` を前提に書いています。**
> Day 6 の課題2で `LocalTime clockIn / clockOut` に改修した人は、**自分の今のコンストラクタに合わせて読み替えてください**（例：`new Attendance("E001", LocalDate.of(2026,8,1), LocalTime.of(9,0), LocalTime.of(19,0))`）。改修後のクラスでテストを書くのが正解です。

```java
// week1/src/test/java/com/example/attendance/domain/AttendanceTest.java
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.*;

class AttendanceTest {

    @Test
    @DisplayName("勤務時間が8時間を超えた分が残業時間になる")
    void 残業時間を計算できる() {
        // Arrange（準備）
        Attendance attendance = new Attendance("E001", 1, 10);

        // Act（実行）
        int overtime = attendance.overtimeHours();

        // Assert（検証）
        assertThat(overtime).isEqualTo(2);
    }

    @Test
    @DisplayName("勤務時間が8時間以下なら残業時間は0になる")
    void 残業なしの場合はゼロ() {
        Attendance attendance = new Attendance("E001", 1, 7);
        assertThat(attendance.overtimeHours()).isZero();
    }

    @Test
    @DisplayName("日付が32日の場合は例外が投げられる")
    void 不正な日付は例外() {
        assertThatThrownBy(() -> new Attendance("E001", 32, 8))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("日付が不正です");
    }
}
```

- **AAA パターン**（Arrange / Act / Assert）でコメントを書くと読みやすくなります。実務標準です
- テストメソッド名は**日本語でOK**です（実務でも増えています）。「何を検証しているか」が分かることが最優先
- `@DisplayName` でテスト結果の表示名を付けられます

### 複数パターンをまとめて検証（パラメータ化テスト）
```java
@ParameterizedTest
@CsvSource({
    "8, 0",
    "9, 1",
    "12, 4",
    "7, 0"
})
@DisplayName("勤務時間ごとの残業時間")
void 残業時間の境界値(int workHours, int expectedOvertime) {
    Attendance a = new Attendance("E001", 1, workHours);
    assertThat(a.overtimeHours()).isEqualTo(expectedOvertime);
}
```

### 何をテストすべきか（テストケースの作り方）

残業時間の計算を例にすると、必ずこの4観点を書きます。

- **正常系** … 10時間 → 残業2時間
- **境界値** … 8時間ちょうど → 0分、8時間1分 → 1分。**バグの大半は境界に潜みます**
- **異常系** … 負の時間、25時間、null
- **空・ゼロ件** … 勤怠データが0件のとき

> **境界値テストは、現場で最も価値が高いテストです。** 「8時間以上」なのか「8時間を超えたら」なのか、この1文字の違いが実際のバグになります。仕様書で「以上／超」「以下／未満」を見たら、必ず境界値テストを書いてください。

### 必要な pom.xml 設定
`04_setup.md`「4-3」の pom.xml をそのまま使っていれば、**すでに入っています**。入っていなければ `<dependencies>` に追加してください。
```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>6.1.3</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.27.7</version>
    <scope>test</scope>
</dependency>
```
テストコードは **`src/test/java` の下**に、本体と同じパッケージ構成で置きます（`src/main/java` に置くと `mvn test` の対象になりません）。

## ⑤ 課題（240分）

> 💡 **この日はコード全文の解答例がありません。** 課題を終えたら `08_answers.md` 末尾の「自己検証チェック（Day 7）」で、満たすべき条件を1つずつ確認してください。30分詰まったときは `07_troubleshooting.md` と該当日の座学に戻ること。
1. Day 1〜6 で作った全クラスに単体テストを書く（最低20テストケース）
   - 正常系・境界値・異常系を必ず含める
2. `mvn test` で全部通ることを確認
3. Git で以下を実践
   - `feature/ATT-01-add-tests` ブランチを切る
   - **意味のある単位で3コミット以上**に分ける
   - push してプルリクエストを作る
   - **PR の説明文を書く**（下記テンプレート使用）
4. わざとコンフリクトを起こして、自力で解決する
   - ブランチAで `README.md` の1行目を編集 → コミット → push → **PRを作ってマージ**（main が更新される）
   - ブランチB（Aを切る前の main から分岐したもの）で**同じ行**を編集 → コミット
   - ブランチBで `git pull origin main --rebase` → **コンフリクト発生** → 解決 → `mvn test` を通してから push
   > **`main` を直接編集する手順にしていないのは、上でブランチ保護をかけたからです。** 現場でも「main を直接触ってコンフリクトを作る」ことはできません。**保護された環境の中でコンフリクトを再現する**のが、実務に即した練習です。

### プルリクエスト説明文テンプレート（実務でそのまま使えます）
```markdown
## 何をしたか
月次勤怠集計ロジックの単体テストを追加しました。

## なぜ
リファクタリング前に既存動作を保証するため。

## 変更点
- AttendanceTest を追加（残業時間計算の正常系・境界値・異常系）
- SummaryServiceTest を追加（グルーピング集計）

## 確認方法
`mvn test` が全て成功することを確認済み

## レビューで特に見てほしい点
境界値（8時間ちょうど）のテストケースが十分か
```
**「レビューで特に見てほしい点」を書けるかどうかが、新人とそうでない人の差です。** レビュアーの時間を尊重する姿勢が伝わります。

## ⑥ 自己チェック
- [ ] ブランチを切ってPRを出すまでの流れを説明できる
- [ ] **`main` を保護し、直接 push を禁止する理由を説明できる**（設定も済ませた）
- [ ] `git add .` を避けるべき理由を説明できる
- [ ] コミットメッセージの良い書き方を説明できる
- [ ] 1コミットの粒度をどう決めるか説明できる
- [ ] `.gitignore` に何を入れるべきか説明できる
- [ ] 認証情報をコミットしてはいけない理由を説明できる
- [ ] コンフリクトを自力で解決できる
- [ ] 単体テストを書く理由を3つ説明できる
- [ ] AAAパターンを説明できる
- [ ] 境界値テストが重要な理由を説明できる
- [ ] 例外が投げられることをテストできる
- [ ] PR説明文を自力で書ける
- [ ] **Day 1〜6 の自己チェックが全て答えられる**

---

## ⑦ 【必須】中間到達度テストを受ける（90分）

**今夜、必ず `09_assessment.md` の「中間テスト」を実施してください。**
教材もネットもIDEも見ずに、紙かテキストエディタだけで解きます。

- **80点以上** → 順調。Day 8 へ進んでください
- **60〜79点** → 落とした分野を半日で補強してから Day 8 へ
- **40〜59点** → **第2週に進まないでください。** 1日使って Day 3〜5 をやり直します
- **40点未満** → 第1週を最初からやり直してください

（判定表は `09_assessment.md`「中間テストの合格ライン」と同じものです）

> **低い点が出たら、それは失敗ではなく成果です。** 穴が入社前に見つかったのですから。
> 一番まずいのは、穴に気づかないまま第2週に進み、Spring Boot を「呪文の写経」として消化してしまうことです。

---

## 第1週修了時点の到達確認

以下ができていれば、第2週に進んでよい状態です。

- [ ] Javaの文法で「読めない」ものがほぼ無い
- [ ] クラス設計をして、データと処理をまとめられる
- [ ] インタフェースで差し替え可能な構造を作れる
- [ ] Stream API で集計処理が書ける
- [ ] 例外を適切に設計・処理できる
- [ ] 単体テストが書ける
- [ ] Git で PR を出せる
- [ ] エラーメッセージを読んで自力で原因を特定できる

**1つでも欠けている場合、第2週に進む前に半日使って埋めてください。** 土台が欠けたまま Spring Boot に進むと、「動くけど何が起きているか分からない」状態になり、そこから抜け出せなくなります。
