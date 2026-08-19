# 解答例・模範回答集

> ## 使い方（守らないと効果が消えます）
> 1. **30分は自力で考えてから開いてください。** すぐ見ると力がつきません
> 2. **見たら閉じて、もう一度何も見ずに書き直してください。** これをやらないと定着しません
> 3. **解答例は「唯一の正解」ではありません。** 動いていて、読みやすくて、テストできるなら、あなたのコードも正解です
> 4. **自分のコードと解答例の「違いの理由」を考えてください。** そこにあなたの伸びしろがあります

## 収録範囲（どの日に何があるか）

| Day | このファイルにあるもの | 場所 |
|---|---|---|
| Day 0 | なし（環境構築が課題のため）。詰まったら `07_troubleshooting.md` | — |
| Day 1〜5 | **コード解答例＋採点ポイント** | 下記 各Day |
| Day 6〜11, 13 | **自己検証チェックリスト＋重要箇所のコード断片** | 末尾「自己検証チェック」 |
| Day 12 | **リファクタリング解答例（Before/After 全文）＋対応表** | 下記 Day 12 |
| Day 14 | **レビュー指摘への模範回答** | 下記 Day 14 |

> **なぜ Day 6 以降はコード全文が無いのか。**
> Day 6 以降の課題は「自分の設計判断を含む」ものになり、**正解が1つに定まりません**（クラスの切り方、例外の粒度、DTOの項目など）。
> 代わりに **「これが満たせていれば合格」という検証条件** を用意しました。現場のレビューも、これと同じ形（コードの正解を渡すのではなく、満たすべき条件を示す）で行われます。

---

# Day 1 課題：EmployeeInfo

```java
public class EmployeeInfo {

    // マジックナンバーを定数化する（Day 12 の先取り。初日から意識してよい）
    private static final int OVERTIME_UNIT_PAY = 2000;

    public static void main(String[] args) {
        String name = "田中太郎";
        int baseSalary = 250000;
        int overtimeHours = 12;

        int overtimePay = calculateOvertimePay(overtimeHours, OVERTIME_UNIT_PAY);
        int totalPay = calculateTotalPay(baseSalary, overtimePay);

        printPaySlip(name, baseSalary, overtimePay, totalPay);
    }

    /** 残業代を計算する（計算するだけ。表示はしない） */
    public static int calculateOvertimePay(int hours, int unitPay) {
        return hours * unitPay;
    }

    /** 総支給額を計算する */
    public static int calculateTotalPay(int base, int overtimePay) {
        return base + overtimePay;
    }

    /** 給与明細を表示する（表示するだけ。計算はしない） */
    public static void printPaySlip(String name, int base, int overtimePay, int total) {
        System.out.println("===== 給与明細 =====");
        System.out.printf("氏名     : %s%n", name);
        System.out.printf("基本給   : %6d 円%n", base);
        System.out.printf("残業代   : %6d 円%n", overtimePay);
        System.out.println("--------------------");
        System.out.printf("総支給額 : %6d 円%n", total);
    }
}
```

### 採点ポイント（自己採点してください）
| 観点 | できていたか |
|---|---|
| メソッドが「1つのことだけ」やっているか | 計算メソッドの中で `println` していないか |
| メソッド名が動詞で始まっているか | `calculate...` `print...` |
| 引数と戻り値の型が適切か | 金額は int（Day 6 で BigDecimal に直します） |
| `printf` の `%6d` で桁揃えできたか | できなくても減点なし。知っておくと便利 |

> **`%n` と `\n` の違い**：`%n` はOSに合わせた改行を出します。実務では `%n` が安全です。

---

# Day 2 課題：AttendanceCalculator

```java
public class AttendanceCalculator {

    private static final int STANDARD_WORK_HOURS = 8;

    public static void main(String[] args) {
        int[] monthlyHours = {8, 9, 7, 10, 8, 8, 12, 6, 8, 9,
                              8, 8, 11, 8, 7, 9, 8, 10, 8, 8};

        System.out.println("合計勤務時間 : " + totalHours(monthlyHours) + " 時間");
        System.out.printf("平均勤務時間 : %.1f 時間%n", averageHours(monthlyHours));
        System.out.println("残業時間     : " + overtimeHours(monthlyHours) + " 時間");
        System.out.println("判定         : " + judge(averageHours(monthlyHours)));

        // 異常系の確認：空配列
        int[] empty = {};
        System.out.println("--- 空データの場合 ---");
        System.out.println("合計: " + totalHours(empty));
        System.out.printf("平均: %.1f%n", averageHours(empty));
        System.out.println("判定: " + judge(averageHours(empty)));
    }

    public static int totalHours(int[] hours) {
        int total = 0;
        for (int h : hours) {
            total += h;
        }
        return total;          // 空配列なら 0 が返る（ループが1度も回らないだけ）
    }

    public static double averageHours(int[] hours) {
        // ★ここが今日の本題：0除算を防ぐ
        if (hours.length == 0) {
            return 0.0;
        }
        return (double) totalHours(hours) / hours.length;
        //     ↑ (double) を付けないと整数の割り算になり、小数が切り捨てられる
    }

    public static int overtimeHours(int[] hours) {
        int overtime = 0;
        for (int h : hours) {
            if (h > STANDARD_WORK_HOURS) {
                overtime += h - STANDARD_WORK_HOURS;
            }
        }
        return overtime;
    }

    public static String judge(double average) {
        if (average >= 10) return "働きすぎ";
        if (average >= 8)  return "標準";
        return "余裕あり";
    }
}
```

### 解説：なぜ空配列の対処が本題だったのか

`averageHours` で `hours.length` が 0 のとき、`total / 0` は **`ArithmeticException: / by zero`** で落ちます。

現場では、こういうデータは**普通に来ます**。
- 入社初日の社員（まだ勤怠が1件もない）
- 長期休職中の社員
- 月初1日目に集計を叩いたとき

**「あり得ない」と思った入力ほど、本番では来ます。** 正常系だけ動くコードは未完成品です。

### 別解：`null` も考慮するなら
```java
public static double averageHours(int[] hours) {
    if (hours == null || hours.length == 0) {
        return 0.0;
    }
    ...
}
```
> **ただし議論の余地があります。** 「0を返す」のと「例外を投げる」のと「Optionalを返す」のどれが正しいかは、**業務要件次第**です。
> - 画面に「0.0時間」と出したい → 0を返す
> - データが無いこと自体が異常 → 例外を投げる
> - 呼び出し側に判断させたい → `OptionalDouble` を返す
>
> **「仕様が決まっていないから自分で決めた」で終わらせず、「決めたことを記録する」のが実務です。** コメントに書いてください。

---

# Day 3 課題：Department / Employee

```java
// Department.java
package com.example.attendance.domain;

public class Department {
    private final String departmentId;
    private final String name;
    private final int capacity;

    public Department(String departmentId, String name, int capacity) {
        if (departmentId == null || departmentId.isBlank()) {
            throw new IllegalArgumentException("部署IDは必須です");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("部署名は必須です");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("上限人数は1以上で指定してください: " + capacity);
        }
        this.departmentId = departmentId;
        this.name = name;
        this.capacity = capacity;
    }

    public String getDepartmentId() { return departmentId; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }

    @Override
    public String toString() {
        return "Department{id=" + departmentId + ", name=" + name + "}";
    }
}
```

```java
// Employee.java
package com.example.attendance.domain;

public class Employee {

    private static final int OVERTIME_UNIT_PAY = 2000;

    private final String employeeId;
    private final String name;
    private Department department;        // 異動があるので final にしない
    private final int baseSalary;

    public Employee(String employeeId, String name, Department department, int baseSalary) {
        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("社員IDは必須です");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("氏名は必須です");
        }
        if (department == null) {
            throw new IllegalArgumentException("所属部署は必須です");
        }
        if (baseSalary < 0) {
            throw new IllegalArgumentException("基本給は0以上で指定してください: " + baseSalary);
        }
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.baseSalary = baseSalary;
    }

    /** 異動する（業務イベントを表す名前にする。setDepartment ではない） */
    public void transferTo(Department newDepartment) {
        if (newDepartment == null) {
            throw new IllegalArgumentException("異動先の部署は必須です");
        }
        this.department = newDepartment;
    }

    /** 総支給額を計算する */
    public int calculateSalary(int overtimeHours) {
        if (overtimeHours < 0) {
            throw new IllegalArgumentException("残業時間は0以上で指定してください: " + overtimeHours);
        }
        return baseSalary + overtimeHours * OVERTIME_UNIT_PAY;
    }

    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public Department getDepartment() { return department; }
    public int getBaseSalary() { return baseSalary; }

    @Override
    public String toString() {
        return "Employee{id=" + employeeId + ", name=" + name
             + ", dept=" + department.getName() + "}";
    }
}
```

### 採点ポイント
| 観点 | 解説 |
|---|---|
| **コンストラクタで全項目を検証したか** | 検証がないと「名前が空の社員」という不正なオブジェクトが作れてしまいます |
| **`final` を適切に付けたか** | 変わらないものは `final`。**「変わらない」を型で表現するとバグが減ります** |
| **セッターを機械的に作っていないか** | `setName` は作るべきではありません（社員IDと氏名は勝手に変わらない） |
| **`transferTo` という業務的な名前にできたか** | ここができていたら、設計センスが良いです |
| **エラーメッセージに実際の値を入れたか** | `"基本給が不正です"` より `"...: -500"` の方が調査が10倍速い |

> **よくある失敗**：`department.getName()` を `toString()` で呼んでいるので、`department` が null だとヌルポになります。
> コンストラクタで null チェックしているから安全 — これが**「コンストラクタで検証する」ことの真の価値**です。**オブジェクトが存在する＝常に正しい状態、を保証できます。**

---

# Day 4 課題：打刻種別と勤務時間ルール

```java
// AttendanceType.java
public enum AttendanceType {
    CLOCK_IN("出勤"),
    CLOCK_OUT("退勤"),
    BREAK_START("休憩開始"),
    BREAK_END("休憩終了");

    private final String label;

    AttendanceType(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }
}
```

```java
// WorkTimeRule.java
public interface WorkTimeRule {
    /**
     * 実労働時間（分）を計算する
     * @param clockIn  出勤時刻
     * @param clockOut 退勤時刻
     * @return 実労働時間（分）
     */
    int calculateWorkMinutes(LocalTime clockIn, LocalTime clockOut);
}
```

```java
// StandardWorkTimeRule.java
public class StandardWorkTimeRule implements WorkTimeRule {

    private static final int SIX_HOURS_IN_MINUTES = 360;
    private static final int EIGHT_HOURS_IN_MINUTES = 480;

    @Override
    public int calculateWorkMinutes(LocalTime clockIn, LocalTime clockOut) {
        if (clockIn == null || clockOut == null) {
            throw new IllegalArgumentException("出勤・退勤時刻はどちらも必須です");
        }
        if (!clockOut.isAfter(clockIn)) {
            throw new IllegalArgumentException(
                "退勤時刻は出勤時刻より後である必要があります: in=" + clockIn + ", out=" + clockOut);
        }
        int totalMinutes = (int) Duration.between(clockIn, clockOut).toMinutes();
        return totalMinutes - calculateBreakMinutes(totalMinutes);
    }

    /** 拘束時間から法定の休憩時間を求める（仕様書 3.2 の表に対応） */
    private int calculateBreakMinutes(int totalMinutes) {
        if (totalMinutes <= SIX_HOURS_IN_MINUTES)   return 0;    // 6時間ちょうどは休憩0
        if (totalMinutes <= EIGHT_HOURS_IN_MINUTES) return 45;   // 8時間ちょうどは45分
        return 60;
    }
}
```

```java
// FlexWorkTimeRule.java
public class FlexWorkTimeRule implements WorkTimeRule {

    private static final LocalTime CORE_START = LocalTime.of(10, 0);
    private static final LocalTime CORE_END   = LocalTime.of(15, 0);

    private final WorkTimeRule baseRule = new StandardWorkTimeRule();  // 委譲

    @Override
    public int calculateWorkMinutes(LocalTime clockIn, LocalTime clockOut) {
        if (clockIn.isAfter(CORE_START) || clockOut.isBefore(CORE_END)) {
            throw new IllegalArgumentException(
                "コアタイム(10:00-15:00)を満たしていません: in=" + clockIn + ", out=" + clockOut);
        }
        return baseRule.calculateWorkMinutes(clockIn, clockOut);   // 共通部分は使い回す
    }
}
```

```java
// AttendanceRecord.java
public record AttendanceRecord(
        String employeeId,
        LocalDate date,
        AttendanceType type,
        LocalTime time) {}
```

### 課題6の模範回答：「なぜ if 文で分岐せずインタフェースにしたのか」

> if 文で `if (ruleType.equals("STANDARD")) ... else if (ruleType.equals("FLEX")) ...` と書くと、勤務ルールが増えるたびに**この計算メソッドそのものを修正**しなければならない。
> 修正するということは、既存の STANDARD の動作を壊すリスクを毎回背負い、既存部分のテストも全部やり直すということ。
>
> インタフェースにすれば、新しいルール（裁量労働、シフト制）が増えても、**新しいクラスを1つ追加するだけ**で既存コードは1行も変わらない。既存部分のテストも不要。
> これが「拡張には開き、修正には閉じる」＝開放閉鎖の原則（OCP）。
>
> さらに、テストの観点でも利点がある。`StandardWorkTimeRule` だけを単体でテストでき、他のルールの影響を受けない。

> **これが書けていたら、Day 4 は合格です。** 「コードが動く」より「なぜその設計にしたか説明できる」ことの方が、現場では重要です。

### `FlexWorkTimeRule` で継承ではなく委譲を使った理由
`extends StandardWorkTimeRule` にすると、親の実装変更が子に直撃します（密結合）。
`private final WorkTimeRule baseRule` として**フィールドに持って呼ぶ**のが委譲です。**「継承より委譲」は業界の標準的な指針**です。

---

# Day 5 課題：AttendanceSummaryService

```java
public class AttendanceSummaryService {

    private static final int STANDARD_WORK_HOURS = 8;

    /** 社員別の合計勤務時間 */
    public Map<String, Integer> totalHoursByEmployee(List<Attendance> records) {
        return records.stream()
            .collect(Collectors.groupingBy(
                Attendance::getEmployeeId,
                Collectors.summingInt(Attendance::getWorkHours)));
    }

    /** 社員別の残業時間 */
    public Map<String, Integer> overtimeHoursByEmployee(List<Attendance> records) {
        return records.stream()
            .collect(Collectors.groupingBy(
                Attendance::getEmployeeId,
                Collectors.summingInt(Attendance::overtimeHours)));
    }

    /** 最も残業が多い社員ID（データが無ければ empty） */
    public Optional<String> findMostOvertimeEmployee(List<Attendance> records) {
        return overtimeHoursByEmployee(records).entrySet().stream()
            .max(Map.Entry.comparingByValue())     // 最大値のエントリを取る
            .map(Map.Entry::getKey);               // キー（社員ID）だけ取り出す
        // データが空なら max() が empty を返すので、そのまま empty が返る
    }

    /** 残業が閾値を超えた社員IDの一覧（多い順） */
    public List<String> findExcessiveOvertimeEmployees(List<Attendance> records, int threshold) {
        return overtimeHoursByEmployee(records).entrySet().stream()
            .filter(e -> e.getValue() > threshold)
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .toList();
    }
}
```

### 解説
- **`findMostOvertimeEmployee` が空リストで落ちない理由**：`stream().max()` は `Optional` を返す設計になっているため、要素が無ければ自動的に `empty` になります。**これが Optional の設計思想の実例です** — 「無いかもしれない」を型で表現しているので、null チェックを書き忘れることが構造的に起きません
- **`Map.Entry.<String, Integer>comparingByValue()`** の `<String, Integer>` は、Javaが型を推論できないときに明示する書き方です。**【今は暗記でOK】**
- `Collectors` は `import java.util.stream.Collectors;` が必要です

### 必要な import 一覧（写経時に忘れがち）
```java
import java.util.*;
import java.util.stream.Collectors;
```

---

# Day 12 ハンズオン：悪いコードのリファクタリング【最重要】

## Before（お題）
```java
public class Svc {
    public String proc(String id, int y, int m, List<int[]> d) {
        int t = 0; int o = 0;
        for (int i = 0; i < d.size(); i++) {
            if (d.get(i)[0] == m) {
                t = t + d.get(i)[1];
                if (d.get(i)[1] > 8) { o = o + d.get(i)[1] - 8; }
            }
        }
        int p = 250000 + o * 2000;
        String r = "";
        if (t > 200) { r = "over"; } else { if (t > 160) { r = "normal"; } else { r = "under"; } }
        return id + "," + y + "," + m + "," + t + "," + o + "," + p + "," + r;
    }
}
```

## After（解答例）

```java
// ① まず「int[]」という意味不明な型を、意味のあるクラスにする
public record DailyAttendance(int month, int workHours) {
    public DailyAttendance {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("月が不正です: " + month);
        }
        if (workHours < 0 || workHours > 24) {
            throw new IllegalArgumentException("勤務時間が不正です: " + workHours);
        }
    }

    /** 所定労働時間を超えた分 */
    public int overtimeHours() {
        return Math.max(0, workHours - WorkStandard.STANDARD_DAILY_HOURS);
    }
}
```

```java
// ② マジックナンバーを、意味のある名前の定数にまとめる
public final class WorkStandard {
    public static final int STANDARD_DAILY_HOURS = 8;          // 所定労働時間（就業規則第12条）
    public static final int OVER_THRESHOLD_HOURS = 200;        // 過重労働の判定閾値
    public static final int NORMAL_THRESHOLD_HOURS = 160;      // 標準勤務の下限
    public static final BigDecimal BASE_SALARY = new BigDecimal("250000");
    public static final BigDecimal OVERTIME_UNIT_PAY = new BigDecimal("2000");

    private WorkStandard() {}    // インスタンス化させない（定数置き場なので）
}
```

```java
// ③ 判定結果を enum にする（"over" という文字列はスペルミスしても気づけない）
public enum WorkLoadLevel {
    OVER("過重"), NORMAL("標準"), UNDER("余裕");

    private final String label;
    WorkLoadLevel(String label) { this.label = label; }
    public String getLabel() { return label; }

    public static WorkLoadLevel from(int totalHours) {
        if (totalHours > WorkStandard.OVER_THRESHOLD_HOURS)   return OVER;
        if (totalHours > WorkStandard.NORMAL_THRESHOLD_HOURS) return NORMAL;
        return UNDER;
    }
}
```

```java
// ④ 戻り値をCSV文字列ではなく、構造化されたオブジェクトにする
public record MonthlySummary(
        String employeeId,
        int year,
        int month,
        int totalWorkHours,
        int totalOvertimeHours,
        BigDecimal totalPay,
        WorkLoadLevel workLoadLevel) {}
```

```java
// ⑤ Service本体：責務を分割し、1メソッド1責務にする
public class MonthlyAttendanceService {

    /** 月次サマリを算出する */
    public MonthlySummary summarize(String employeeId, int year, int month,
                                    List<DailyAttendance> attendances) {
        Objects.requireNonNull(employeeId, "employeeId は必須です");
        List<DailyAttendance> target = filterByMonth(attendances, month);

        int totalHours    = sumWorkHours(target);
        int overtimeHours = sumOvertimeHours(target);
        BigDecimal pay    = calculateTotalPay(overtimeHours);

        return new MonthlySummary(employeeId, year, month,
                totalHours, overtimeHours, pay, WorkLoadLevel.from(totalHours));
    }

    private List<DailyAttendance> filterByMonth(List<DailyAttendance> attendances, int month) {
        if (attendances == null) return List.of();
        return attendances.stream()
                .filter(a -> a.month() == month)
                .toList();
    }

    private int sumWorkHours(List<DailyAttendance> attendances) {
        return attendances.stream().mapToInt(DailyAttendance::workHours).sum();
    }

    private int sumOvertimeHours(List<DailyAttendance> attendances) {
        return attendances.stream().mapToInt(DailyAttendance::overtimeHours).sum();
    }

    private BigDecimal calculateTotalPay(int overtimeHours) {
        return WorkStandard.BASE_SALARY.add(
                WorkStandard.OVERTIME_UNIT_PAY.multiply(BigDecimal.valueOf(overtimeHours)));
    }
}
```

```java
// ⑥ 文字列整形は、必要なら別クラスに分離する（表示は Service の責務ではない）
public class MonthlySummaryCsvFormatter {
    public String format(MonthlySummary s) {
        return String.join(",",
                s.employeeId(), String.valueOf(s.year()), String.valueOf(s.month()),
                String.valueOf(s.totalWorkHours()), String.valueOf(s.totalOvertimeHours()),
                s.totalPay().toPlainString(), s.workLoadLevel().name());
    }
}
```

## 改善点の対応表（自己採点用）

| # | Before の問題 | 適用した原則 | After |
|---|---|---|---|
| 1 | `Svc` `proc` `t` `o` `p` `r` `d` — 全て意味不明 | 命名 | 意味の分かる名前に |
| 2 | `List<int[]>` が何を表すか不明 | ドメインモデリング | `DailyAttendance` record 化 |
| 3 | 8, 250000, 2000, 200, 160 がマジックナンバー | マジックナンバー排除 | `WorkStandard` に定数化 |
| 4 | 集計・給与計算・判定・整形の4責務が1メソッド | **単一責任（SRP）** | メソッド／クラスに分割 |
| 5 | `if` の中に `if` でネスト | 早期リターン | `WorkLoadLevel.from()` で平坦化 |
| 6 | インデックス for 文 | 可読性 | Stream / 拡張for |
| 7 | 戻り値がCSV文字列 | インタフェース設計 | `MonthlySummary` record |
| 8 | 金額が `int` | 金額計算の鉄則 | `BigDecimal` |
| 9 | `"over"` という文字列判定 | 型安全 | `enum WorkLoadLevel` |
| 10 | null / 空リストの考慮なし | 異常系 | `List.of()` を返す |
| 11 | 引数チェックなし | 契約による設計 | `Objects.requireNonNull` / record の検証 |
| 12 | テストが書けない（1メソッドに全部） | テスタビリティ | 各メソッドを個別に検証可能に |

## リファクタリングを保証するテスト（課題1の答え）

**リファクタリング前に、まずこのテストを書いてください。** これが緑のままなら、動作は変わっていない保証になります。

```java
class MonthlyAttendanceServiceTest {

    private final MonthlyAttendanceService service = new MonthlyAttendanceService();

    @Test
    @DisplayName("対象月の勤務時間だけが集計される")
    void 対象月だけ集計する() {
        List<DailyAttendance> data = List.of(
                new DailyAttendance(8, 10),   // 対象月：残業2
                new DailyAttendance(8, 8),    // 対象月：残業0
                new DailyAttendance(9, 12));  // 対象外

        MonthlySummary result = service.summarize("E001", 2026, 8, data);

        assertThat(result.totalWorkHours()).isEqualTo(18);
        assertThat(result.totalOvertimeHours()).isEqualTo(2);
    }

    @ParameterizedTest
    @DisplayName("勤務時間による負荷判定の境界値")
    @CsvSource({
        "201, OVER",     // 200超 → OVER
        "200, NORMAL",   // 200ちょうどは OVER ではない ★境界値
        "161, NORMAL",
        "160, UNDER",    // 160ちょうどは NORMAL ではない ★境界値
        "159, UNDER"
    })
    void 負荷判定の境界値(int totalHours, WorkLoadLevel expected) {
        assertThat(WorkLoadLevel.from(totalHours)).isEqualTo(expected);
    }

    @Test
    @DisplayName("勤怠データが空でも例外にならず0で返る")
    void 空データ() {
        MonthlySummary result = service.summarize("E001", 2026, 8, List.of());

        assertThat(result.totalWorkHours()).isZero();
        assertThat(result.totalPay()).isEqualByComparingTo("250000");
        assertThat(result.workLoadLevel()).isEqualTo(WorkLoadLevel.UNDER);
    }

    @Test
    @DisplayName("nullが渡されても例外にならない")
    void nullデータ() {
        MonthlySummary result = service.summarize("E001", 2026, 8, null);
        assertThat(result.totalWorkHours()).isZero();
    }
}
```

> **`isEqualByComparingTo` を使う理由**：BigDecimal の `equals` は `250000` と `250000.00` を**別物と判定します**（スケールが違うため）。値としての比較には `compareTo`（AssertJでは `isEqualByComparingTo`）を使ってください。**これは実務で必ず一度は踏む落とし穴です。**

---

# Day 14：レビュー指摘への模範回答

> **これがこの教材の最終到達点です。** コードが書けることより、**設計判断を説明できること**が「即戦力」の本質です。

### 指摘1
```
[must] AttendanceService.calculateSummary が72行あります。責務ごとに分割してください。
```
**模範回答**
> ご指摘ありがとうございます。修正しました。
> 以下の4メソッドに分割しています。
> - `fetchAttendances()` — データ取得
> - `aggregateWorkTime()` — 労働時間の集計
> - `calculateOvertimePay()` — 残業代計算
> - `buildSummary()` — レスポンス組み立て
>
> 分割にあたり、残業代計算は `OvertimePolicy` インタフェースに切り出しました（現在の割増率は全雇用形態1.25で共通ですが、今後 深夜割増・休日割増を追加する際に、既存コードを変更せず実装クラスの追加で対応できます）。
> 各メソッドに個別の単体テストも追加しています（+6ケース）。

✅ **良い点**：何をどう直したかが具体的。さらに「今後の拡張」まで見据えた説明ができている。

### 指摘2
```
[must] SummaryResponse に hourlyRate（時給）が含まれています。この API の利用者に
       給与単価を公開してよいか確認済みでしょうか？仕様に無ければ削除してください。
```
**模範回答**
> **確認しておらず、認識が漏れていました。申し訳ありません。削除しました。**
> Entity をそのまま返さず DTO を分離していたにもかかわらず、DTO 側に不要な項目を入れてしまっており、分離の意味を活かせていませんでした。
>
> 併せて、他の Response DTO も全て見直し、以下の観点で点検しました。
> - 給与・単価に関する項目が含まれていないか → `EmployeeResponse` からも `hourlyRate` を削除
> - 個人情報（生年月日・住所等）が含まれていないか → 該当なし
>
> 今後、Response DTO を追加する際は「この項目を外部に出してよいか」をレビュー観点に入れたいと思います。`attendance-api/docs/coding-standards.md` に追記しました。

✅ **良い点**：①素直に非を認める ②同種の問題を横展開で点検 ③再発防止策まで示す。**この3点セットができる新人は、極めて高く評価されます。**

❌ **悪い回答例**：「すみません、直しました」だけ。→ 他にも同じ問題が残っているかレビュアーが確認し直すことになり、手間が増えます。

### 指摘3
```
[want] 45 という数字がコードに直接書かれています。労務基準の変更に備えて定数化を推奨します。
```
**模範回答**
> 修正しました。
> ```java
> /** 36協定における月間時間外労働の原則上限（労働基準法36条）。就業規則第18条により当社も同値 */
> private static final int MONTHLY_OVERTIME_ALERT_THRESHOLD_MINUTES = 45 * 60;
> ```
> 単位が「分」であることが名前から分かるようにし、根拠となる法令・社内規程をコメントに残しました。

✅ **良い点**：単に定数化するだけでなく、**「なぜ45なのか」という根拠**をコメントに残している。3年後にこの数字を変更してよいか判断する人が助かります。

### 指摘4
```
[want] findByEmployeeIdAndWorkDateBetween をループ内で呼んでいます。
       社員1000人だとN+1になり、性能要件の3秒を超える可能性があります。
```
**模範回答**
> ご指摘の通りで、修正しました。
> ループ内で1000回クエリが飛ぶ実装になっていました。社員IDのリストを渡して1回で取得する形に変更しています。
> ```java
> @Query("SELECT a FROM AttendanceEntity a WHERE a.employeeId IN :ids AND a.workDate BETWEEN :from AND :to")
> List<AttendanceEntity> findByEmployeeIdsAndPeriod(...);
> ```
> 取得後に `Collectors.groupingBy` で社員IDごとに分けています。
>
> **手元で社員1000人・31日分（31,000件）のデータを投入して計測しました。**
> - 修正前：14.2秒（クエリ1001回）
> - 修正後：0.6秒（クエリ1回）
>
> 性能要件の3秒以内を満たすことを確認しています。

✅ **良い点**：**実測値を出している。** 「直しました」ではなく「直して、要件を満たすことを測って確認しました」まで言えるのが実務です。

### 指摘5
```
[ask] 退勤打刻が無いレコードを黙ってスキップしていますが、
      これは意図的でしょうか？打刻漏れが検知できなくなりませんか？
```
**模範回答**
> 意図的ですが、**説明が不足していました。ご指摘の懸念はもっともです。**
>
> 設計意図は以下です。
> - 退勤打刻が無い日を労働時間0として集計に含めると、月次の労働時間が実態より少なく算出され、**36協定の超過を見落とすリスク**があると考えました
> - そのため労働時間の集計からは除外しています
>
> ただしご指摘の通り、黙って除外すると打刻漏れ自体が見えなくなります。そのため **`missingClockOutDays` フィールドでレスポンスに日付リストを返し**、呼び出し側が検知できるようにしています（`SummaryResponse` の該当項目）。
>
> この設計判断は `attendance-api/docs/decisions.md` に記録済みですが、**コード上のコメントが無かったため意図が伝わりませんでした。** 該当箇所にコメントを追加しました。
>
> なお、より望ましいのは「打刻漏れがある月は締め処理をブロックする」だと考えますが、締め機能自体が未実装のため今回はスコープ外としています。別チケット（ATT-31）として起票しました。

✅ **良い点**：①判断の理由を説明 ②指摘の妥当性を認める ③既に手当てしている箇所を示す ④伝わらなかった原因を特定して改善 ⑤スコープ外の扱いを明示して起票
**ここまで書けたら、新人としては満点です。**

### 指摘6
```
[nits] employeeIdList という変数名は、employeeIds の方が一般的です。
```
**模範回答**
> 修正しました。他の箇所も `xxxList` になっていないか確認し、3箇所を合わせて修正しています。

✅ **良い点**：`[nits]`（好みレベル）にも横展開している。ただし**深追いはしない**のも正解です。

---

## レビュー対応で絶対にやってはいけないこと

| ❌ | なぜダメか |
|---|---|
| 無言で直す | レビュアーが「対応されたか」を差分から探す羽目になる |
| 「すみません」だけ書く | 何をどう直したか分からない |
| 指摘を無視して別の変更を混ぜる | 差分が読めなくなり、レビューが破綻する |
| 感情的に反論する | 議論は理由と根拠で。「そう教わりました」は理由になりません |
| 納得できないのに黙って従う | 間違ったレビューもあります。**根拠を示して議論するのは正しい行為です** |

> **最後に一番大事なこと。**
> レビュー指摘は、**あなたの能力への評価ではなく、コードへの改善提案**です。
> 指摘が多いPRは「ダメなPR」ではありません。**指摘が多い＝レビュアーが真剣に読んでくれた**ということです。
> 一番危ないのは、指摘がゼロのPRです。それは読まれていない可能性があります。

---
---

# 自己検証チェック（Day 6〜11, 13）

> **使い方**：課題を終えたあと、この項目を1つずつ自分のコードで確認してください。
> **1つでも「いいえ」があれば、そこが今日の穴です。** 該当する日の座学に戻ってから次の日へ進んでください。

---

## Day 6：例外設計・日時・BigDecimal・ログ・ビルドツール

### 満たせていれば合格
- [ ] `catch` して何もしていない場所が**1箇所も無い**（ログを出す／投げ直す／明確に回復する のいずれか）
- [ ] すべての `log.error(...)` に**例外オブジェクト `e` を最後の引数で渡している**
- [ ] カスタム例外が `(String message, Throwable cause)` を受け取り `super(message, cause)` を呼んでいる
- [ ] ファイルを開く処理が **try-with-resources** になっている
- [ ] `java.util.Date` / `Calendar` / `SimpleDateFormat` を**一切使っていない**
- [ ] 金額の計算に `double` が**残っていない**（`BigDecimal` になっている）
- [ ] `BigDecimal` を `new BigDecimal("2000")` と**文字列で**生成している
- [ ] `divide` に**スケールと丸めモードを指定**している
- [ ] ログが `log.info("... id={}", id)` の**プレースホルダ形式**（`+` 連結が無い）
- [ ] `mvn clean test` が BUILD SUCCESS になる

### 詰まりやすい2箇所

**① 勤務時間を LocalTime で計算する（課題2）**
```java
public int workMinutes() {
    if (clockOut == null) {
        return 0;                    // 打刻漏れ。0扱いにするか例外にするかは「設計判断」。理由を log に書く
    }
    long total = Duration.between(clockIn, clockOut).toMinutes();
    return (int) (total - breakMinutes(total));   // 休憩控除は 05_project-spec.md のルールに従う
}

private long breakMinutes(long totalMinutes) {
    if (totalMinutes <= 6 * 60) return 0;
    if (totalMinutes <= 8 * 60) return 45;
    return 60;
}
```
> `Duration.between(LocalTime, LocalTime)` は**日をまたぐと負になります**。深夜勤務を扱うなら `LocalDateTime` が必要です。**この点に自分で気づけていたら、それだけで今日は成功です。**

**② 給与を BigDecimal で計算する（課題3）**
```java
private static final BigDecimal OVERTIME_RATE = new BigDecimal("1.25");

public BigDecimal overtimePay(int overtimeMinutes, int hourlyRate) {
    return new BigDecimal(overtimeMinutes)
        .divide(new BigDecimal("60"), 10, RoundingMode.HALF_UP)   // 時間に直す（途中は精度を残す）
        .multiply(new BigDecimal(hourlyRate))
        .multiply(OVERTIME_RATE)
        .setScale(0, RoundingMode.DOWN);      // 円未満切り捨て（05_project-spec.md 3.3）
}
```
> **途中で丸めず、最後に1回だけ丸める**のが金額計算の原則です。途中で丸めると誤差が積み上がります。

---

## Day 7：Git実務フロー と 単体テスト

### 満たせていれば合格
- [ ] テストが `src/test/java` の下にあり、`mvn test` で**20件以上**実行される
- [ ] 各クラスに **正常系・境界値・異常系** が揃っている（8時間ちょうど／0件／null／範囲外）
- [ ] 例外のテストを `assertThatThrownBy(...).isInstanceOf(...)` で書いている
- [ ] テストが**実行順に依存していない**（1つだけ実行しても通る）
- [ ] `feature/ATT-01-add-tests` のようなブランチを切ってPRを作った
- [ ] コミットが**意味のある単位で3つ以上**に分かれている（`test:` `fix:` `refactor:` など）
- [ ] PR説明文に「何を・なぜ・確認方法・見てほしい点」が書かれている
- [ ] 自分でコンフリクトを起こし、マーカーを消して解決し、**ビルドとテストを通してから** push した

### 境界値テストの作り方（迷ったらこの型）
| 仕様の言葉 | 書くべきテスト |
|---|---|
| 「8時間**を超えたら**残業」 | 7時間59分→0分 ／ 8時間ちょうど→0分 ／ 8時間1分→1分 |
| 「45時間**を超えたら**アラート」 | 2699分→false ／ 2700分→false ／ 2701分→true |
| 「0件でも動く」 | 空リスト → 例外を出さず 0 または `Optional.empty()` |

> **テストが通ったことよりも、「境界のどちら側か」を仕様と突き合わせたことの方が重要です。**

---

## Day 8：データベースとSQL

### 満たせていれば合格
- [ ] 全テーブルに**主キー**がある
- [ ] `attendances.employee_id` に**外部キー制約**がある
- [ ] 「1人1日1件」が **UNIQUE 制約**で表現されている（アプリのチェックだけに頼っていない）
- [ ] `NOT NULL` を付けるべき列に付いている
- [ ] `created_at` / `updated_at` がある
- [ ] 社員名など**同じ情報を2つのテーブルに持っていない**（正規化）
- [ ] `WHERE` / `GROUP BY` でよく使う列にインデックスを付け、**なぜ付けたか**を説明できる
- [ ] JDBC のコードが **`PreparedStatement` ＋ `?`** になっている（文字列連結がゼロ）
- [ ] `Connection` / `PreparedStatement` / `ResultSet` が **try-with-resources** で閉じられている
- [ ] `attendance-api/docs/db-design.md` に**設計理由**（なぜこの主キー／なぜこの制約）が書かれている
- [ ] `compose.yaml` がコミットされていて、**`docker compose up -d` だけでDBが起動する**
- [ ] `docker compose down -v` で消しても、`week2/schema.sql` と `sample-data.sql` から**同じ状態を再現できる**

### JDBC の最小形（課題2の骨格）
```java
private static final String SQL =
    "SELECT employee_id, name, department FROM employees WHERE employee_id = ?";

public Optional<Employee> findById(String employeeId) {
    try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/attendance", "appuser", "localdevonly");
         PreparedStatement ps = conn.prepareStatement(SQL)) {

        ps.setString(1, employeeId);      // ← ここで値を渡す。文字列連結は絶対にしない

        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                return Optional.empty();  // 「見つからない」は異常ではないので例外にしない
            }
            return Optional.of(new Employee(
                rs.getString("employee_id"), rs.getString("name"), rs.getString("department")));
        }
    } catch (SQLException e) {
        log.error("社員の取得に失敗しました. employeeId={}", employeeId, e);
        throw new DataAccessException("社員の取得に失敗しました", e);
    }
}
```

---

## Day 9：Web・HTTP・REST・Spring Boot入門

### 満たせていれば合格
- [ ] `GET /api/employees`、`GET /api/employees/{id}`、`POST /api/employees` が動く
- [ ] **Controller に業務ロジックが1行も無い**（Service を呼んで返すだけ）
- [ ] URL に**動詞が入っていない**（`/api/getEmployee` になっていない）
- [ ] 登録成功が **201 Created**、取得成功が 200、存在しないIDが **404**
- [ ] Service を**コンストラクタインジェクション**で受け取っている（`@Autowired` フィールドが無い）
- [ ] Request / Response が `record` で、**Entity をそのまま返していない**
- [ ] `attendance-api/docs/api-log.md` に curl と実際のレスポンスが記録されている

### 404 の返し方（この時点での最小形）
```java
// Service
public EmployeeResponse findById(String id) {
    return store.find(id)                       // データはまだ Map<String, Employee> でよい
        .map(e -> new EmployeeResponse(e.id(), e.name(), e.department()))
        .orElseThrow(() -> new EmployeeNotFoundException(id));
}

// Controller（Day 11 で @RestControllerAdvice に移す前の暫定形）
@ExceptionHandler(EmployeeNotFoundException.class)
public ResponseEntity<String> handleNotFound(EmployeeNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
}
```
> **Day 11 でこれを全Controller共通の `@RestControllerAdvice` に移します。** 今日は「404が返せる」ことが目的です。

---

## Day 10：レイヤードアーキテクチャとDB連携

### 満たせていれば合格
- [ ] Controller / Service / Repository / Entity / DTO の**5種類のファイルが分かれている**
- [ ] Controller が Repository を**直接呼んでいない**
- [ ] Repository が Service を**呼んでいない**
- [ ] Entity が Controller の戻り値に**出てきていない**（必ず DTO に変換）
- [ ] `@Transactional` が **Service にだけ**付いている（Controller / Repository に無い）
- [ ] 読み取り専用メソッドに `@Transactional(readOnly = true)` が付いている
- [ ] `logging.level.org.hibernate.SQL: debug` で**発行SQLを目で確認した**
- [ ] N+1 を**実際に起こしてログでSQLの本数を数え**、`JOIN FETCH` で減らしたことを確認した
- [ ] `@ManyToOne` に `fetch = FetchType.LAZY` が付いている
- [ ] Service の単体テストが Mockito で書かれていて、**DBに接続していない**
- [ ] テーブルが **Flyway（`db/migration/V1__*.sql`）で作られている**（`ddl-auto` は `validate`）
- [ ] `docker compose down -v` → `up -d` → アプリ起動で、**テーブルが自動で再構築される**
- [ ] Lombok を入れた場合、**Entity に `@Data` / `@Setter` を付けていない**（理由を説明できる）

### N+1 の確認手順（これをやらないと身につきません）
1. 勤怠一覧を取得するAPIを叩く
2. ログに出た `Hibernate: select ...` の**行数を数える**（社員10人なら11本出るはず）
3. `JOIN FETCH` に変えて叩き直す → **1本になる**
4. **Day 8 で投入した31,000件のデータに対し、修正前と修正後の応答時間を実測する**
   ```bash
   curl -w "\n所要時間: %{time_total}秒\n" -o /dev/null -s "http://localhost:8080/api/employees/E0001/attendances?year=2026&month=8"
   ```
5. 「SQLが何本から何本に減り、何秒が何秒になったか」を `log/day10.md` に書く

> **本数だけ数えて満足しないでください。** 現場で求められる報告は「N+1を直しました」ではなく、**「12秒が0.4秒になりました」**という数字です。**測っていない改善は、改善したと言えません。**

---

## Day 11：バリデーション・例外ハンドリング・設定管理

### 満たせていれば合格
- [ ] すべての Request DTO に `@NotBlank` / `@Size` / `@Min` 等が付いている
- [ ] Controller の引数に **`@Valid` が付いている**（付け忘れが最頻出の事故）
- [ ] `@RestControllerAdvice` が1つあり、**Controller 側に try-catch が無い**
- [ ] 400 / 404 / 409 / 500 が**それぞれ正しく返る**（curl で4種類とも確認した）
- [ ] 500 のレスポンスに**例外メッセージ・スタックトレース・テーブル名が含まれていない**
- [ ] エラーレスポンスが**全ケースで同じJSON形式**になっている
- [ ] traceId がレスポンスとログの**両方に**出ていて、突き合わせられる
- [ ] `MDC.clear()` を `finally` で呼んでいる
- [ ] 業務チェック（重複・退職済み・未来日）が **Service**、形式チェックが **Controller** にある
- [ ] `attendance-api/docs/api-errors.md` にエラーコード一覧がある

### エラーコード一覧表の書き方（課題3の形式例）
| コード | HTTPステータス | 発生条件 | 呼び出し側の対処 |
|---|---|---|---|
| `VALIDATION_ERROR` | 400 | 必須項目未入力、形式不正 | `errors[]` の field を見て入力を直す |
| `ATTENDANCE_FUTURE_DATE` | 400 | 未来日の打刻（BR-03） | 打刻日を今日以前にする |
| `EMPLOYEE_RETIRED` | 400 | 退職済み社員の打刻（BR-04） | 対象社員を確認する |
| `EMPLOYEE_NOT_FOUND` | 404 | 存在しない社員ID（BR-05） | 社員IDを確認する |
| `DUPLICATE_ATTENDANCE` | 409 | 同一社員・同一日の二重打刻（BR-01） | 修正APIを使う |
| `INTERNAL_ERROR` | 500 | 想定外の例外 | traceId を添えて問い合わせる |

> **この表は「API利用者に渡す資料」です。** 内部の例外クラス名は書かず、**利用者が次に何をすればよいか**を書くのがコツです。

---

## Day 13：テスト戦略・CI・セキュリティ最低限

### 満たせていれば合格
- [ ] テストが**合計40ケース以上**あり、`mvn clean verify` が緑
- [ ] Service（Mockito）／Controller（`@WebMvcTest`）／Repository（`@DataJpaTest`）／結合（`@SpringBootTest`）の**4種類すべて**がある
- [ ] **単体テストが最も多い**（テストピラミッドの形になっている）
- [ ] Repository・結合テストが **Testcontainers で本物の PostgreSQL** を使っている（組み込みDBに差し替えていない）
- [ ] `PostgreSQLContainer` が **`static`** で、テストクラスごとに1回だけ起動している
- [ ] **DBを手で起動しない状態で `mvn clean verify` が通る**（clone した人がすぐテストできる）
- [ ] `LocalDate.now()` / `LocalDateTime.now()` の直接呼び出しが**本体コードに残っていない**（`Clock` 注入）
- [ ] テストで `Clock.fixed(...)` を使い、**日付が変わっても壊れない**ようにした
- [ ] `.github/workflows/ci.yml` があり、**PRで自動実行されている**
- [ ] **わざとテストを落として、PRに赤いバツが出ることを目で確認した**
- [ ] パスワードが `BCryptPasswordEncoder` でハッシュ化され、**平文がDBにもログにも無い**
- [ ] `application.yml` に**パスワード・APIキーが直書きされていない**

### Clock 注入の型（課題2）
```java
// config/ClockConfig.java
@Configuration
public class ClockConfig {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}

// テスト側
Clock fixed = Clock.fixed(Instant.parse("2026-08-15T00:00:00Z"), ZoneId.of("Asia/Tokyo"));
AttendanceService service = new AttendanceService(repository, fixed);
```
> **「未来日の打刻は不可」のテストは、Clock を固定しないと書けません。** 実行した日によって「未来」が変わるからです。**これが「テストしにくい＝設計が悪い」の具体例です。**

### セキュリティの最終確認（1つでも該当したら直す）
- SQL を文字列連結している箇所がある
- 例外メッセージをそのまま HTTP レスポンスに入れている
- ログに氏名・メールアドレス・パスワードを出している
- `.gitignore` に入れ忘れた設定ファイルがコミットされている（`git log -p` で確認）
