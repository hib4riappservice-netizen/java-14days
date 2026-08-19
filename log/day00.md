## Hello Java

> **ソースコード**

```
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello, Java!");
    }
}
```

> **正常ログ**

```
"C:\Program Files\Eclipse Adoptium\jdk-21.0.12.8-hotspot\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.1\lib\idea_rt.jar=59071" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath C:\java-14days\out\production\java-14days Hello
Hello, Java!

プロセスは終了コード 0 で終了しました
```

> **異常ログ**

- `System.out.println` の最後の `;` を消して実行

```
java: ';'がありません
```

実行ステップ末尾に「;」がなくて実行エラーが発生。

- `println` を `printLn` に変えて実行

```
java: シンボルを見つけられません
  シンボル:   メソッド printLn(java.lang.String)
  場所: タイプjava.io.PrintStreamの変数 out
```

- `"Hello, Java!"` の閉じ側の `"` を消して実行

```
java: 文字列リテラルが閉じられていません
```
