# つまずいたとき集 — よくあるエラーと対処

> **使い方**：エラーメッセージの一部を Ctrl+F で検索してください。
> **その前に必ず**：エラーメッセージを最後まで読んでください。答えが書いてあることが半分以上あります。

---

## 0. エラーが出たときの基本手順（毎回これをやる）

1. **エラーメッセージの1行目を読む** — 例外の種類とメッセージ
2. **`at` の行のうち、自分が書いたファイル名が出ている一番上の行を探す** — そこが原因箇所
3. **その行をIDEで開いて、何をしているか見る**
4. **デバッガでその行の直前にブレークポイントを置き、変数の中身を確認する**
5. それでも分からなければ、**エラーメッセージを英語のまま検索する**
6. 30分経ったら人に聞く（Day 14 の質問テンプレートを使う）

**やってはいけないこと：エラーを読まずにコードを適当に変えること。** 原因が分からないまま直ると、次に同じ問題が起きたとき何もできません。

---

## 1. コンパイルエラー（実行する前に出る）

### `';' expected`
セミコロンの付け忘れ。**エラーが出た行の1つ上の行**を見てください。

### `cannot find symbol`
```
cannot find symbol
  symbol:   variable employeeName
```
その名前が見つからない。原因は：
- **スペルミス**（大文字小文字も区別されます。`employeeName` と `employeename` は別物）
- **変数を宣言していない**
- **スコープ外**（`{ }` の外から中の変数を見ようとしている）
- **import を書いていない**（クラス名の場合。IntelliJなら `Alt+Enter` で自動追加）

### `incompatible types: String cannot be converted to int`
型が合っていません。`int x = "5";` のような代入をしています。
文字列を数値にしたいなら `Integer.parseInt("5")` を使ってください。

### `class X is public, should be declared in a file named X.java`
**public クラス名とファイル名は完全に一致させる必要があります。** ファイル名を直してください。

### `unreachable statement`
`return` の後にコードを書いています。絶対に実行されないので、コンパイラが弾いています。

### `variable x might not have been initialized`
変数を宣言しただけで値を入れずに使おうとしています。`int x;` → `int x = 0;`

### `missing return statement`
戻り値がある（`void` でない）メソッドで、`return` が無い経路があります。
`if` の中だけに `return` を書いていませんか？ `if` が偽のときの経路にも `return` が必要です。

---

## 2. 実行時エラー（動かしてから出る）

### `NullPointerException`（最頻出）
```
Exception in thread "main" java.lang.NullPointerException:
  Cannot invoke "String.length()" because "<local1>" is null
```
**Java 14以降、「何が null だったか」がメッセージに書いてあります。必ず読んでください。**

**原因**
- 変数に何も入れていない
- メソッドが `null` を返した（`map.get()` は存在しないキーで null を返します）
- 配列やリストの要素が null

**対処**
```java
// ① そもそも null を返さない設計にする（Optional を使う）
Optional<Employee> found = repository.findById(id);

// ② Map は getOrDefault を使う
int hours = map.getOrDefault("E999", 0);

// ③ どうしてもチェックが必要なら
if (name != null && !name.isBlank()) { ... }

// ④ Objects.requireNonNull で早期に検出する
this.name = Objects.requireNonNull(name, "name は必須です");
```

### `ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5`
配列やリストの範囲外にアクセスしています。
**インデックスは 0 から始まるので、要素5個なら 0〜4 です。**
`for (int i = 0; i <= array.length; i++)` の `<=` が `<` の間違い、が定番です。

### `NumberFormatException: For input string: "abc"`
数値に変換できない文字列を `Integer.parseInt()` に渡しています。
入力値の検証が必要です。空文字や全角数字も落ちます。

### `ArithmeticException: / by zero`
整数の 0 除算です。割る前に 0 かチェックしてください。
（`double` の場合は例外にならず `Infinity` になるので、こちらの方が厄介です）

### `ConcurrentModificationException`
**ループ中にリストの要素を追加・削除**しています。
```java
// ❌
for (String s : list) { if (s.isEmpty()) list.remove(s); }

// ⭕
list.removeIf(String::isEmpty);
```

### `StackOverflowError`
再帰呼び出しが止まらなくなっています。終了条件を確認してください。
`toString()` の中で自分自身を呼んでいるケースもよくあります。

### `ClassCastException`
キャストが不正です。`(String) obj` としたが obj が String ではなかった。
`instanceof` で確認してからキャストしてください。

---

## 3. Maven / ビルド関連

### `Could not resolve dependencies` / `Could not transfer artifact`
ライブラリをダウンロードできていません。
- **ネットワーク接続を確認**（社内プロキシ環境では設定が必要な場合があります）
- `mvn clean install -U` で強制的に再取得
- `~/.m2/repository` の該当フォルダを削除して再取得

### `Source option 8 is no longer supported`
pom.xml の Java バージョン指定が古いです。
```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>
```

### IntelliJ でコードは正しいのに赤線が消えない
- 右のMavenパネル → 更新ボタン（🔄）をクリック
- `File` → `Invalidate Caches` → 再起動
- **これで9割解決します。IntelliJのキャッシュ不整合は日常茶飯事です**

---

## 4. Spring Boot 関連

### `Field xxx required a bean of type 'yyy' that could not be found`
DI しようとした部品が Bean として登録されていません。
- 対象クラスに `@Service` / `@Repository` / `@Component` が付いているか
- そのクラスが**起動クラスと同じパッケージか、その配下**にあるか（Springは起動クラスの位置から下だけを探します）
- インタフェースを注入している場合、実装クラスにアノテーションが必要

### `Web server failed to start. Port 8080 was already in use.`
すでに別のアプリが8080番を使っています。
- 前回起動したアプリが残っている → IntelliJの停止ボタンで止める
- ポートを変える → `application.yml` に `server.port: 8081`

### `relation "employees" does not exist`（PostgreSQL）
- Flyway のマイグレーションが走っていない → ファイル名が `V1__xxx.sql`（**アンダースコア2つ**）か、`src/main/resources/db/migration/` に置いてあるか確認
- 起動ログに `Migrating schema "public" to version 1` が出ているか確認
- `ddl-auto: validate` は**テーブルを作りません**（検証するだけ）。テーブルは Flyway が作ります

### `Validate failed: Migration checksum mismatch for migration version 1`
**適用済みのマイグレーションファイルを書き換えました。** これは禁止事項です（Day 10 参照）。
- 正しい対処：**次の連番で新しいファイル（例 `V3__fix_xxx.sql`）を追加**して直す
- 学習中でデータを捨ててよい場合のみ：`docker compose down -v && docker compose up -d` でDBごと作り直す

### `column "employeeId" does not exist` のように列名だけ見つからない
**PostgreSQL は識別子を小文字に畳みます。** `employee_id` のようなスネークケースで定義し、Entity 側は `@Column(name = "employee_id")` で対応付けてください。
`"CamelCase"` と二重引用符付きで作ると、以後すべての参照で引用符が必須になります（**やらないこと**）。

### Testcontainers が `Could not find a valid Docker environment`
Docker Desktop が起動していません。クジラのアイコンが「running」になってから再実行してください。
- Windows で WSL2 が停止している場合は `wsl --shutdown` の後、Docker Desktop を再起動
- 初回はイメージ取得に数分かかります。**タイムアウトに見えても待ってください**

### `Failed to configure a DataSource: 'url' attribute is not specified`
**`spring-boot-starter-data-jpa` を依存に入れたのに、接続先を書いていません。**
- Day 9（データをメモリで持つ段階）なら、**JPA の依存自体を外す**のが正解
- Day 10 以降なら `application.yml` に `spring.datasource.url` を書く
- **「依存を足したら設定もセットで書く」** — Spring Boot の自動設定は、依存の有無だけを見て動き出します

### `No qualifying bean of type 'javax.sql.DataSource'`
DB接続設定が無いか間違っています。`application.yml` の `spring.datasource` を確認してください。

### `port is already allocated` / `bind: address already in use`（5432番）
**PCに直接インストールされた PostgreSQL が 5432 を使っています。** Docker のコンテナが同じポートを取れずに起動できません。

```powershell
# 誰が使っているか調べる（PowerShell）
Get-Service -Name "postgresql*"
Get-NetTCPConnection -LocalPort 5432 -State Listen
```
対処は `04_setup.md` §0-5 を参照（**既存サービスを止める**のが推奨。`compose.yaml` のポートをずらす方法もあります）。

### `Connection to localhost:5432 refused`（PostgreSQL）
DBが起動していません。
```bash
docker compose ps          # STATUS が healthy になっているか
docker compose up -d       # 起動していなければ起動する
docker compose logs db     # 起動に失敗しているならログを見る
```
- **PCを再起動した後は、Docker Desktop とコンテナが止まっています。** 毎朝 `docker compose up -d` から始めてください
- 5432番を他のPostgreSQLが使っていると `port is already allocated` になります。既存のPostgreSQLサービスを止めるか、`compose.yaml` の左側のポートを `"5433:5432"` に変えて `application.yml` も合わせます

### `FATAL: password authentication failed for user "appuser"`
ユーザー名かパスワードが違います。`compose.yaml` の `POSTGRES_USER` / `POSTGRES_PASSWORD` と `application.yml` を突き合わせてください。
- **`compose.yaml` の環境変数は、初回の `docker compose up` でDBが作られるときにしか効きません。** 後から書き換えても反映されないので、`docker compose down -v` で作り直してください
- 環境変数 `DB_USER` / `DB_PASSWORD` を設定している場合は、そちらが優先されます

### `@Transactional` が効かない
- **同じクラス内のメソッドを自分で呼んでいませんか？**（Springのプロキシを経由しないため効きません）
  → 別クラスに切り出すか、自己注入する
- メソッドが `public` でないと効きません
- 検査例外（`IOException` 等）ではデフォルトでロールバックしません
  → `@Transactional(rollbackFor = Exception.class)`

### `Failed to convert value of type 'String' to 'LocalDate'`
日付の形式が合っていません。`yyyy-MM-dd` で送るか、`@DateTimeFormat(iso = ISO.DATE)` を付けてください。

### バリデーションが動かない
- **`@Valid` を付け忘れていませんか？**（最頻出。リクエストボディの検証に必要）
- `spring-boot-starter-validation` の依存関係が pom.xml にありますか？

### `?month=13` が 400 ではなく 500 になる
**Controller クラスに `@Validated` を付けていませんか？ 付けていたら外してください。**
Spring Framework 6.1（Boot 3.2）以降、メソッド引数の検証は組み込みで動きます。クラスに `@Validated` があると古いAOP経由の検証が優先され、
飛ぶ例外が `HandlerMethodValidationException` ではなく **`ConstraintViolationException`** に変わるため、`GlobalExceptionHandler` で拾い漏らして500になります。
- 対処①：クラスの `@Validated` を外す（推奨）
- 対処②：`@ExceptionHandler` に `ConstraintViolationException.class` を追加する

### 存在しないURLを叩くと 404 ではなく 500 になる
`@ExceptionHandler(Exception.class)` が `NoResourceFoundException` まで拾っています。
`NoResourceFoundException` を個別に 404 で返すハンドラを追加してください（Day 11 参照）。

### 全APIが突然 401 を返すようになった
**`spring-boot-starter-security` を入れていませんか？** 入れた瞬間に全エンドポイントへ認証がかかります。
BCrypt（パスワードのハッシュ化）だけが目的なら、**`spring-security-crypto` のみ**を依存に入れてください（Day 13 参照）。

---

## 5. テスト関連

### テストが単体では通るのに、全部実行すると落ちる
**テスト同士が影響し合っています**（FIRST原則の Independent 違反）。
- static 変数や共有のDBデータを使っていませんか？
- `@Transactional` をテストに付けると、テスト後に自動ロールバックされます

### `LocalDate.now()` を使ったテストが、ある日突然落ちた
月末や年末に起きます。**時刻に依存するテストは必ず `Clock` を注入して固定してください**（Day 13 参照）。

### Mockito の `when()` が効かない
- モック対象が `final` メソッドではないか
- `@Mock` を付けたフィールドが `@InjectMocks` の対象に正しく注入されているか
- `@ExtendWith(MockitoExtension.class)` を付けているか

### `UnnecessaryStubbingException`
定義したモックの振る舞いが1度も使われていません。不要な `when()` を消してください。

---

## 6. Git 関連

### `fatal: not a git repository`
Gitリポジトリでないフォルダでコマンドを打っています。`cd` で正しい場所へ移動してください。

### `Author identity unknown` / `Please tell me who you are`
コミットする人の名前とメールが未設定です（**Day 0 の最頻出**）。
```bash
git config --global user.name "あなたのGitHubアカウント名"
git config --global user.email "あなたのメールアドレス"
```

### `Authentication failed` / パスワードを入れても弾かれる
**GitHubのログインパスワードでは push できません**（2021年に廃止）。
Personal Access Token（PAT）を発行し、**パスワード欄にトークンを貼り付けて**ください（手順は `04_setup.md` §4「認証について」）。
- 一度間違えて保存された認証情報が残っている場合：Windows は「資格情報マネージャー」→ Windows 資格情報 → `git:https://github.com` を削除してやり直す

### `src refspec main does not match any`
**まだ1つもコミットしていない**か、ブランチ名が違います。
```bash
git log --oneline     # 何も出なければコミットが0件
git branch            # 今いるブランチ名を確認（master になっていませんか）
git branch -M main    # main に変える場合
```

### `Updates were rejected because the remote contains work that you do not have`
GitHub 側に、自分の手元に無いコミットがあります（例：リポジトリ作成時の README）。
```bash
git pull origin main --rebase
git push origin main
```

### `nothing to commit, working tree clean` と出て push できない
変更が `git add` されていないのではなく、**そもそも変更が無い**状態です。`git status` で対象ファイルが本当に変わっているか確認してください。

### `warning: LF will be replaced by CRLF the next time Git touches it`
**エラーではなく警告です。`git add` は成功しています。** 改行コード（Windowsの CRLF ／ Linuxの LF）が変換されているという予告です。
放置すると **CI（Linux）でだけ落ちる**、**差分が全行変更に見える**といった問題が出ます。
→ **`.gitattributes` を作って改行を LF に固定してください**（手順は `04_setup.md` §4-2 ステップ0-2）。

### `bad interpreter: /bin/bash^M` / `$'\r': command not found`
シェルスクリプトが **CRLF 改行のまま Linux（CI やコンテナ）で実行**されています。上と同じ原因です。
```bash
file scripts/foo.sh          # "with CRLF line terminators" と出たらこれ
git add --renormalize .      # .gitattributes を置いたうえで実行して直す
```

### `.class` や `.idea` がコミット対象に出てくる
`.gitignore` が無いか、**すでにコミット済み**です。後者の場合は追跡を外します。
```bash
git rm -r --cached out .idea      # ファイル自体は消えず、Gitの管理からだけ外れる
git commit -m "chore: ビルド生成物とIDE設定を除外"
```

### `error: failed to push some refs`
リモートに、自分が持っていない変更があります。
```bash
git pull origin main --rebase   # 先に取り込む
# コンフリクトがあれば解決してから
git push origin main
```

### コンフリクトが起きた
```
<<<<<<< HEAD
自分の変更
=======
相手の変更
>>>>>>> main
```
1. **3つのマーカー行をすべて削除**
2. 正しい最終形にする（両方を活かすことも多い）
3. `git add ファイル名` → `git commit`
4. **必ずビルドとテストを流してから push**

### 間違えてコミットしてしまった
```bash
# 直前のコミットをやり直す（まだ push していない場合）
git commit --amend

# 直前のコミットを取り消す（変更内容は残す）
git reset --soft HEAD~1

# ⚠ push 済みのコミットを書き換えると、他人の作業を壊します。必ず相談してください
```

### 秘密情報をコミットしてしまった
**すぐに先輩・上長に報告してください。** 自分で消しても履歴に残ります。
そして**その認証情報を無効化・再発行**する必要があります。**これは隠してはいけない事案です。**

---

## 7. 「エラーは出ないが結果がおかしい」場合

これが一番厄介です。以下を順に確認してください。

1. **デバッガで1ステップずつ動かし、変数の中身を確認する**（最も確実）
2. **期待値と実際値をログに出して比較する**
3. **境界値を疑う**（`>` と `>=`、0件のとき、月末のとき）
4. **型を疑う**（`int / int` で小数が切り捨てられていないか、`double` の誤差ではないか）
5. **`==` で文字列やオブジェクトを比較していないか**
6. **SQLを疑う**（`logging.level.org.hibernate.SQL: debug` で実際に発行されたSQLを見る）
7. **キャッシュを疑う**（古いビルド結果が残っている → `mvn clean`）

---

## 8. Windows（PowerShell）でコマンド例が動かない

**本教材のコマンド例は bash（Mac / Linux / Git Bash）の書き方です。** PowerShell では以下が動きません。
**最も簡単な解決策は Git Bash を使うことです**（`04_setup.md`「用語の前提」参照）。どうしても PowerShell を使う場合は下の対応表で読み替えてください。

| 症状 | 原因 | PowerShell での書き方 |
|---|---|---|
| `The '<' operator is reserved for future use` | 入力リダイレクト `<` が使えない | `Get-Content file.sql \| docker compose exec -T db psql -U appuser -d attendance` |
| `トークン '&&' は、このバージョンでは有効なステートメント区切りではありません` | Windows PowerShell 5.1 は `&&` 非対応 | `docker compose down -v; docker compose up -d` |
| 行末に `\` を書いたら次の行が別コマンドとして実行される | 継続文字が違う | 行末を バッククォート `` ` `` にするか、**1行で書く**（推奨） |
| `curl` の挙動がおかしい／`-X` が使えない | `curl` が `Invoke-WebRequest` の別名 | **`curl.exe`** と明示的に書く |
| `curl.exe -d '{"name":"佐藤"}'` が 400 になる | シングルクォート内の `"` が剥がれてJSONが壊れる | JSONをファイルに保存して `-d "@body.json"` で渡す |

### Git Bash で `psql` や `docker exec -it` が固まる／`the input device is not a TTY`
**Git Bash は対話型プログラムの扱いが特殊**です。頭に `winpty` を付けてください（Git に同梱されています）。
```bash
winpty docker compose exec db psql -U appuser -d attendance
```
- **Day 8 で `psql` に入るときに踏みます。** 固まったら `Ctrl + C` で抜けて、`winpty` を付け直してください
- `docker compose exec -T ...`（`-T` 付き＝対話しない）の場合は `winpty` は不要です

### Git Bash でパスが勝手に書き換わる（Docker のボリューム指定など）
Git Bash は `/c/...` のようなパスを Windows 形式に自動変換します。意図しない変換で失敗する場合は無効化します。
```bash
MSYS_NO_PATHCONV=1 docker run -v /c/java-14days:/work ...
```

**Day 9 の POST 確認を PowerShell でやる場合**
```powershell
# body.json というファイルを作ってから
curl.exe -X POST http://localhost:8080/api/employees -H "Content-Type: application/json" -d "@body.json"
```

> **これは「Windowsが悪い」のではなく、実務でも普通に起きることです。** 現場のドキュメントも大半が bash 前提で書かれているため、**「この手順書はどのシェル向けか」を意識する**のは実務スキルの1つです。

---

## 9. どうしても解決しないとき

**30分ルール**：30分自力で調べて解決しなければ、必ず人に聞いてください。

聞く前に、Day 14 の質問テンプレートを埋めてください。
```
【やろうとしていること】
【起きていること】
【エラー内容（全文）】
【自分で調べたこと・試したこと】
【聞きたいこと】
【期限感】
```

**このテンプレートを埋めている途中で自己解決することが、実はよくあります。**
言語化する過程で、自分が確認していなかった前提に気づくからです。

そして — **聞くことは恥ではありません。悩んで止まっている時間の方が、チームにとって損失です。**
