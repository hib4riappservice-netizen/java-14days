# 環境構築手順（Day 0 で実施）

> ここでつまずくと2週間全部が止まります。焦らず、1つずつ確認しながら進めてください。
> **各ステップの「確認コマンド」が想定通りの表示になってから次へ進むこと。**

---

## 用語の前提

- **ターミナル**：コマンドを打つ黒い画面。**Windowsは「Git Bash」（§3 で Git と一緒に入ります）、Macは「ターミナル.app」を使ってください**
  - **なぜ Git Bash か**：本教材や世の中の技術記事のコマンド例は、ほぼすべて bash（Mac / Linux）の書き方です。PowerShell では `<`・`&&`・行末 `\` などが動かず、**「教材どおり打ったのに動かない」の原因の大半がこれ**です。Git Bash なら貼り付けたまま動きます
  - **もっと本質的な理由**：あなたが Day 13 で作る CI は Linux（`ubuntu-latest`）で動き、Javaの業務システムの本番サーバもほぼ Linux です。**手元を bash に寄せておくと、書いたコマンドがそのまま CI でもサーバでも通ります。** PowerShell で覚えた書き方は、Linuxサーバに入った瞬間に使えません
  - **PowerShell が不要という意味ではありません。** Windowsのサービス操作やAD・Azureの管理はPowerShellの領域です。**開発は bash、Windows運用は PowerShell** と住み分けてください
  - **Git Bash の弱点**：対話型コマンド（`psql` 等）で `winpty` が必要になる場合があります（`07_troubleshooting.md` §8）。より実務に近いのは **WSL2 上での開発**ですが、Docker との組み合わせで一段複雑になるため、**まず Git Bash → 慣れたら WSL2** の順を推奨します
  - IntelliJ の下部にある Terminal タブも、`Settings → Tools → Terminal → Shell path` に `C:\Program Files\Git\bin\bash.exe` を指定すれば Git Bash になります
- **パスを通す**：「このコマンドはここにあるよ」とOSに教える設定。インストーラが自動でやってくれることが多い
- **`$` や `>` の記号**：入力例の先頭にある記号は「ここから入力」の印。**この記号自体は打ちません**

---

## 1. JDK 21（LTS）のインストール

**LTS＝長期サポート版。実務ではLTSを使います。** 2026年8月現在、Java 21 と Java 25 が主要なLTSです。本教材は **Java 21** を前提とします（実務での採用が最も多いため）。

### Windows
1. https://adoptium.net/temurin/releases/ を開く
2. Version: **21 - LTS**、Operating System: **Windows**、Architecture: **x64**、Package Type: **JDK** を選ぶ
3. `.msi` をダウンロードして実行
4. インストール中、**「Set JAVA_HOME variable」「Add to PATH」に必ずチェック**を入れる

### Mac
```bash
# Homebrew（Macのソフト管理ツール）が無ければ先に入れる
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

brew install --cask temurin@21
```

### 確認
```bash
java -version
```
以下のように **21** で始まるバージョンが出れば成功です。
```
openjdk version "21.0.x" 2026-xx-xx
```

```bash
javac -version
```
`javac 21.0.x` が出ればコンパイラも入っています。**`javac` が出ない場合はJDKではなくJREを入れています。** 入れ直してください。

### ❌ `java: command not found` / `'java' は認識されていません` と出たら
- PATHが通っていません。**PCを再起動**してもう一度試してください（これで9割解決します）
- それでもダメなら、Windowsは「環境変数の編集」→ Path に `C:\Program Files\Eclipse Adoptium\jdk-21.x.x-hotspot\bin` を追加

---

## 2. IntelliJ IDEA Community Edition のインストール

**IDE＝統合開発環境。** エディタ・コンパイラ・デバッガが全部入った開発ソフトです。
Community Edition は**無料**です。Ultimate（有料）はSpring支援機能が強力ですが、Communityでも本教材は全て実施できます。

1. https://www.jetbrains.com/idea/download/ を開く
2. ページ下部の **Community Edition** をダウンロード（上部のUltimateではありません）
3. インストールして起動

### 最初にやる設定（重要）
- `File` → `Settings`（Macは `IntelliJ IDEA` → `Settings`）
- **Editor → General → Auto Import** → 「Add unambiguous imports on the fly」をON（import文を自動で書いてくれる）
- **Editor → Code Style → Java** → Tab size / Indent を **4** に（Javaの標準）
- **Editor → General → Appearance** → 「Show line numbers」をON（**行番号はエラー調査に必須**）

### 覚えるべきショートカット（初日に手に馴染ませる）
| 操作 | Windows | Mac |
|---|---|---|
| 実行 | Shift + F10 | Ctrl + R |
| デバッグ実行 | Shift + F9 | Ctrl + D |
| コード自動生成（getter等） | Alt + Insert | ⌘ + N |
| 自動フォーマット | Ctrl + Alt + L | ⌘ + Option + L |
| 名前の一括変更 | Shift + F6 | Shift + F6 |
| 定義へジャンプ | Ctrl + B | ⌘ + B |
| 使用箇所を検索 | Alt + F7 | Option + F7 |
| メソッド抽出（リファクタ） | Ctrl + Alt + M | ⌘ + Option + M |
| 何でも検索 | Shift 2回 | Shift 2回 |

> **「定義へジャンプ（Ctrl+B）」と「使用箇所検索（Alt+F7）」は、既存コードを読むときの主力武器です。** 現場で最初にやる仕事は「既存コードを読むこと」なので、この2つは初日から使ってください。

---

## 3. Git のインストール

### Windows
https://git-scm.com/download/win からダウンロードして実行。
インストール中の選択肢は**すべてデフォルトのままでOK**です。

### Mac
```bash
brew install git
```

### 確認と初期設定
```bash
git --version          # git version 2.x.x が出ればOK

git config --global user.name "<あなたのGitHubアカウント名>"
git config --global user.email "<GitHubに登録したメールアドレス>"
git config --global init.defaultBranch main
```
> **⚠ `<>` の部分は自分の値に置き換えてください。** メールアドレスは **GitHub の Settings → Emails に登録済みのもの**にしてください。別のアドレスだと、コミットがあなたのアカウントに紐付かず、**GitHub の草（contribution グラフ）が生えません**。成果物を面接で見せるときに効いてきます。

---

## 4. GitHub アカウント作成とリポジトリ準備

1. https://github.com でアカウント作成
2. 右上「+」→ `New repository`
3. Repository name: `java-14days`、**Public**、`Add a README file` にチェック → Create

### PCに持ってくる（クローン）
```bash
cd ~/           # 好きな作業フォルダへ移動（日本語や空白を含まないパスにすること）
git clone https://github.com/<あなたのアカウント>/java-14days.git
cd java-14days
```

### 認証について
`git push` すると認証を求められます。**GitHubのログインパスワードでは通りません**（2021年に廃止）。
- GitHub → Settings → Developer settings → Personal access tokens → **Tokens (classic)** → Generate new token
- スコープは `repo` にチェック
- 生成されたトークンを**パスワード欄に貼り付ける**
- **⚠ トークンは画面を閉じると二度と表示されません。安全な場所に保管してください**

---

## 4-2. 【Day 0 で必ず実施】最初のコミットと push

> Day 0 の課題「GitHub に自分のコードが上がっている」は、ここの手順でクリアします。
> **ブランチ運用・プルリクエストは Day 7 で扱います。** ここでは「手元の変更を GitHub に載せる」最小の手順だけ覚えてください。

### ステップ0：`.gitignore` を作る（コミットの前に必ず）
リポジトリの一番上（`java-14days` フォルダ直下）に `.gitignore` というファイルを作り、以下を貼り付けます。

```
# ビルド生成物
target/
out/
build/
*.class
*.jar

# IDE
.idea/
*.iml
.vscode/

# OS
.DS_Store
Thumbs.db

# 秘密情報・環境依存設定（絶対にコミットしない）
application-local.yml
.env

# ログ（毎回変わるうえ、個人情報が入りうる）
*.log
logs/
```

**なぜ最初にやるのか**：`.class` や `.idea/` は「あなたのPCでビルドした結果」であり、他人には無意味なうえ差分が毎回汚れます。**一度コミットしてしまうと履歴から消すのが面倒**なので、最初に書きます。

### ステップ0-2：`.gitattributes` を作る（改行コードを固定する）

`.gitignore` と同じ場所に `.gitattributes` を作り、以下を貼り付けてください。

```
# リポジトリ内部・作業ファイルとも LF に統一する
* text=auto eol=lf

# Windows専用のバッチファイルだけは CRLF でないと動かない
*.bat text eol=crlf
*.cmd text eol=crlf

# シェルスクリプトは絶対に LF（CRLF だと Linux で bad interpreter エラーになる）
*.sh text eol=lf

# バイナリは変換しない
*.png binary
*.jpg binary
*.pdf binary
*.zip binary
*.jar binary
```

続けて、以下を実行します。
```bash
git config --global core.autocrlf false   # 以後はリポジトリ側の .gitattributes に従わせる
git add --renormalize .                   # 既存ファイルの改行を LF に揃え直す
```

> **【なぜ必要か】改行コードには2種類あります。** Windowsは `\r\n`（CRLF）、Mac/Linuxは `\n`（LF）です。
> `git add` したときに
> ```
> warning: in the working copy of '...', LF will be replaced by CRLF the next time Git touches it
> ```
> と出るのは、この変換が起きているという予告です（**警告であってエラーではありません**）。
>
> **放置すると起きること**
> 1. **CI（Linux）でだけ落ちる。** CRLF が混ざったシェルスクリプトは Linux で `bad interpreter: /bin/bash^M` になります。**原因が非常に分かりにくい定番の事故**です
> 2. **差分が汚染される。** 改行だけが違うと Git は「全行変更」と表示し、レビューで中身が読めなくなります
> 3. **人によって結果が変わる。** 各自の `core.autocrlf` 設定に依存するため、チームで揃いません
>
> **実務では、個人の設定に頼らず `.gitattributes` をリポジトリにコミットして全員に強制します**（`.gitattributes` は `core.autocrlf` より優先されます）。
> **コミットが増えてから直すと、全ファイルが変更扱いの巨大な差分になります。初日の今やるのが最も安上がりです。**

### ステップ1〜4：status → add → commit → push
```bash
# 1. 何が変わったかを見る（add の前に必ず見る習慣を付ける）
git status

# 2. コミットしたいファイルを選ぶ（ステージング）
git add .gitignore .gitattributes week1/day00/Hello.java log/day00.md
#   ⚠ git add . は「意図しないファイルまで載る」ので、慣れるまではファイル名を指定する

# 3. コミット（手元の履歴に1つ記録を刻む）
git commit -m "feat: Day 0 の Hello プログラムと学習ログを追加"

# 4. GitHub へ送る
git push origin main
```

**4段階の意味**（Day 7 で詳しくやります。今はこのイメージだけ）

| 段階 | コマンド | どこにある状態か |
|---|---|---|
| 作業中 | （編集しただけ） | 自分のPCのファイル |
| ステージング | `git add` | 「次のコミットに載せる」と印を付けた状態 |
| コミット | `git commit` | 自分のPCの**履歴**に記録された状態（まだGitHubには無い） |
| プッシュ | `git push` | **GitHub上**に反映された状態（ブラウザで見える） |

### 確認
ブラウザで `https://github.com/<自分のアカウント>/java-14days` を開き、**いま push したファイルが見えれば完了**です。
（Day 0 自己チェックの「GitHub に自分のコードが上がっている」はこれで✅になります）

> **⚠ 初回 push でエラーが出たら** → `07_troubleshooting.md` の「6. Git 関連」を見てください。
> `Author identity unknown` は上の `git config --global user.name / user.email` を実行していないのが原因です。

---

## 4-3. 【Day 1 の開始前に実施】Maven プロジェクトの作り方

> **Day 3 でパッケージ（`package com.example...`）、Day 6 で外部ライブラリ（SLF4J）、Day 7 で `mvn test` を使います。** これらは Maven プロジェクトの形になっていないと動きません。
> **後から移すのは手間なので、Day 1 を始める前にこの形にしておいてください。**（Day 0 を単一ファイルで済ませた人は、下の「既に Day 0 を終えている場合」を読んでください）

### IntelliJ で作る
1. `File` → `New` → `Project`
2. 左メニューから **New Project** を選択
3. 以下を設定
   - Name: **`week1`**
   - Location: **`C:\java-14days`** ← **⚠ ここが最大の注意点**
   - Build system: **Maven**
   - JDK: **21**
   - Advanced Settings → GroupId: `com.example`、ArtifactId: `week1`
4. `Create`

> **⚠ Location には「作りたいフォルダの1つ上」を指定します。** IntelliJ は **Location の下に Name のフォルダを作る**ためです。
> - ⭕ Location `C:\java-14days` ＋ Name `week1` → `C:\java-14days\week1`（正しい）
> - ❌ Location `C:\java-14days\week1` ＋ Name `week1` → `C:\java-14days\week1\week1`（**二重になる**）
>
> 作成直後に**タイトルバーのパスを必ず確認**してください。`week1\week1` になっていたら、IntelliJ を閉じてから1階層上げます。
> ```bash
> cd /c/java-14days/week1
> mv week1/pom.xml week1/src week1/.mvn .
> rm -rf week1 .idea      # .idea は再オープン時に自動生成される
> ```
> そのうえで `File` → `Open` で `C:\java-14days\week1` を開き直します。

生成される `week1/pom.xml` を、以下の形にしておきます（Day 6・Day 7 で使うものを最初から入れておく）。

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>week1</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- ログ（Day 6 で使用） -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.13</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.5.6</version>
        </dependency>

        <!-- テスト（Day 7 で使用） -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-core</artifactId>
            <version>3.25.3</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
            </plugin>
        </plugins>
    </build>
</project>
```

### できるフォルダ構成（これが Java の標準）

| パス | 何を置くか |
|---|---|
| `week1/pom.xml` | 依存ライブラリの定義 |
| `week1/src/main/java/` | **Day 0〜2** の練習（パッケージなしで置いてよい） |
| `week1/src/main/java/com/example/attendance/` | **Day 3〜6** のクラス。`package` 宣言とこの階層を必ず一致させる |
| `week1/src/test/java/com/example/attendance/` | **Day 7** のテストコード |

> **⚠ `src/main/java` の外に置いた `.java` はビルド対象になりません。** 練習コードは必ずこの下に置いてください。

> **【作成直後は `src/main/java` の中が空です。】** IntelliJ のプロジェクトビューでは、**空のフォルダには展開の三角（▶）が出ません**。「中が見られない＝失敗」ではないので安心してください。
> フォルダの色で、正しく認識されているか確認できます。
>
> | 表示 | 意味 |
> |---|---|
> | `java` が**青** | ソースルート（本体コードを置く場所）として認識されている |
> | `test/java` が**緑** | テストソースルートとして認識されている |
> | `resources` に専用アイコン | リソースルートとして認識されている |
>
> 色が付いていない場合は、フォルダを右クリック → `Mark Directory as` → `Sources Root` / `Test Sources Root` で指定できます。

### 既に Day 0 を終えている場合（`week1/day00/Hello.java` がある人）
1. 上の手順で `week1` を Maven プロジェクトにする
2. `week1/day00/Hello.java` を `week1/src/main/java/Hello.java` へ**移動**する（IntelliJ でドラッグ＆ドロップすればOK）
3. 空の `week1/day01`〜`day06` フォルダは削除してよい（Day 3 以降はパッケージで分けるため）
   - 同様に、空の `week2/day07`〜`day13` も削除して構いません。**Day 9 以降のコードは `attendance-api/` に、Day 8 の SQL は `week2/` 直下に置きます**（Day 7 は `week1` のテストコードです）
4. `mvn clean test` が BUILD SUCCESS になることを確認し、コミットする
```bash
git add week1
git commit -m "chore: week1 を Maven プロジェクト構成に変更"
git push origin main
```

### 確認
```bash
cd week1
mvn -v            # Apache Maven 3.x と Java 21 が表示される（IntelliJ 同梱のMavenでもOK）
mvn clean test    # BUILD SUCCESS が出れば準備完了（テストが0件でも成功します）
```

> **`mvn` コマンドが見つからない場合**：IntelliJ の右端「Maven」パネルからも同じ操作（clean / test）ができます。コマンドを入れたい場合は https://maven.apache.org/download.cgi から入れて PATH を通してください。**本教材はどちらでも進められます。**

---

## 5. Docker Desktop と PostgreSQL（Day 8 で使用）

> **本教材は PostgreSQL 一本、かつ「実務で実際に使われている形」で進めます。**
>
> | 項目 | 本教材のやり方 | なぜ（実務での理由） |
> |---|---|---|
> | DB製品 | **PostgreSQL のみ**（H2 は使わない） | 「開発は H2、本番は PostgreSQL」は方言差で**本番でだけ落ちる**事故の定番 |
> | ローカルDBの起動 | **Docker Compose** | 「私のPCでは動く」問題を消す。チーム全員が同じバージョン・同じ設定のDBを1コマンドで起動できる |
> | スキーマ管理 | **Flyway**（Day 10） | 本番のテーブル変更を手作業やフレームワーク任せにしない。**変更履歴がGitに残る**のが必須要件 |
> | テスト用DB | **Testcontainers**（Day 13） | テストのたびに本物のPostgreSQLを使い捨てで起動する。現在の実質標準 |
>
> **どれも「学習用の簡易版」ではなく、現場でそのまま使われているものです。** 初日は手間に感じますが、この4点を知らないまま現場に出ると、初週で確実に詰まります。

### 5-1. Docker Desktop のインストール

**Windows**
1. https://www.docker.com/products/docker-desktop/ からインストーラをダウンロードして実行
2. 「Use WSL 2 instead of Hyper-V」に**チェックが入ったまま**進める
3. インストール後に**PCを再起動**し、Docker Desktop を起動（クジラのアイコンが安定するまで待つ）
4. WSL 2 が未導入と言われたら、PowerShell を管理者で開いて `wsl --install` → 再起動

**Mac**
```bash
brew install --cask docker      # 起動は Launchpad から Docker.app
```

**確認**
```bash
docker --version          # Docker version 27.x など
docker compose version    # Docker Compose version v2.x など
docker run --rm hello-world   # "Hello from Docker!" が出れば成功
```

> **Docker とは**：アプリを「必要なものごと箱に詰めて」動かす仕組みです。PostgreSQL を自分のPCに直接インストールする代わりに、**設定済みのPostgreSQLが入った箱を起動する**と考えてください。捨てて作り直すのが一瞬なので、環境が壊れても復旧が数十秒で済みます。

### 5-2. PostgreSQL を Docker Compose で起動する

リポジトリ直下に **`compose.yaml`** を作ります（このファイル自体もコミットします。**チームで環境を共有するため**です）。

```yaml
services:
  db:
    image: postgres:16
    container_name: attendance-db
    environment:
      POSTGRES_DB: attendance
      POSTGRES_USER: appuser
      POSTGRES_PASSWORD: localdevonly    # ローカル専用。本番の値は絶対に書かない
      TZ: Asia/Tokyo
    ports:
      - "5432:5432"                      # 自分のPCの5432番を箱の中の5432番に繋ぐ
    volumes:
      - attendance-db-data:/var/lib/postgresql/data   # 停止してもデータが残る場所
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U appuser -d attendance"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  attendance-db-data:
```

**起動・停止**
```bash
docker compose up -d      # 起動（初回はイメージのダウンロードで数分）
docker compose ps         # 状態確認。STATUS が healthy になればOK
docker compose logs db    # 起動に失敗したときはログを見る
docker compose stop       # 停止（データは残る）
docker compose down       # 停止して箱を削除（データは volume に残る）
docker compose down -v    # ⚠ volume ごと削除＝データ全消去。作り直したいときだけ
```

> **`image: postgres:16` のようにバージョンを固定してください。** `postgres:latest` にすると、ある日勝手にメジャーバージョンが上がって動かなくなります。**「本番と同じバージョンを固定する」のが実務の鉄則**です。

### 5-3. SQL を打つ（Day 8 で使用）

```bash
docker compose exec db psql -U appuser -d attendance

# ⚠ Git Bash で画面が固まる / "the input device is not a TTY" と出たら、頭に winpty を付ける
winpty docker compose exec db psql -U appuser -d attendance
```
接続できたら psql の基本操作はこれだけ覚えれば十分です。

| コマンド | 意味 |
|---|---|
| `\dt` | テーブル一覧 |
| `\d employees` | テーブル定義を見る |
| `\l` | データベース一覧 |
| `\q` | 終了 |
| `\i /path/file.sql` | SQLファイルを流し込む |

ファイルに書いた SQL をまとめて流すときは、ホスト側から渡せます。
```bash
# Mac / Linux / Git Bash
docker compose exec -T db psql -U appuser -d attendance < week2/schema.sql
```
```powershell
# Windows PowerShell（`<` は使えないので Get-Content から渡す）
Get-Content week2/schema.sql | docker compose exec -T db psql -U appuser -d attendance
```

> **⚠ Windows の人へ：本教材のコマンド例は、断りがなければ Mac / Linux（bash）の書き方です。**
> PowerShell では以下が**そのままでは動きません**。詰まったら `07_troubleshooting.md`「9. Windows（PowerShell）でコマンド例が動かない」を見てください。
>
> | bash の書き方 | PowerShell での書き方 |
> |---|---|
> | `cmd < file` | `Get-Content file \| cmd` |
> | `cmd1 && cmd2` | `cmd1; cmd2`（Windows PowerShell 5.1 は `&&` 非対応） |
> | 行末の `\` で改行を続ける | 行末のバッククォート `` ` ``。**または1行で書く**（こちらが安全） |
> | `curl` | `curl.exe`（`curl` だけだと別コマンドの別名になります） |
>
> **確実なのは、Git のインストール時に一緒に入る「Git Bash」を使うこと**です。教材のコマンドをそのまま貼れます。**本教材では Git Bash を推奨します。**

（GUIが好みなら **pgAdmin** や **DBeaver** を使っても構いません。IntelliJ の Database ツールは **Ultimate 限定**で、Community Edition では使えません）

### 5-4. Java から繋ぐときの情報

Maven の依存（Day 8 の JDBC 課題で使用。Day 9 以降は Spring Initializr が入れてくれます）
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.3</version>
</dependency>
```

| 項目 | 値 |
|---|---|
| JDBC URL | `jdbc:postgresql://localhost:5432/attendance` |
| ユーザー | `appuser` |
| パスワード | `localdevonly`（**ローカル専用**） |
| テスト用DB | **不要**。Day 13 で Testcontainers が使い捨てのDBを自動起動します |

> **⚠ `localdevonly` は手元専用の値です。** 本番の接続情報は環境変数やシークレット管理から読みます（Day 11）。
> **「ローカル用の値だからコミットしてよい」と「本番の値は絶対にコミットしない」の線引き**を、ここで覚えてください。実務でも `compose.yaml` はコミットし、本番の値は別管理です。

### 5-5. Docker を使わずに進めたい場合（非推奨）

社内PCの制約などで Docker が使えない場合は、PostgreSQL を直接インストールしても進められます。
- Windows: https://www.postgresql.org/download/windows/ のインストーラ（ポート5432、既定のまま）
- Mac: `brew install postgresql@16 && brew services start postgresql@16`

その場合は以下を手で作ってください。
```sql
psql -U postgres
CREATE DATABASE attendance;
CREATE USER appuser WITH PASSWORD 'localdevonly';
GRANT ALL PRIVILEGES ON DATABASE attendance TO appuser;
\c attendance
GRANT ALL ON SCHEMA public TO appuser;    -- PostgreSQL 15以降はこれが必要
\q
```
> **ただし Day 13 の Testcontainers は Docker が必須です。** Docker が使えない環境の代替手順は Day 13 に記載しています。

---

## 6. API確認ツール（Day 9 で使用）

以下のどちらかを入れてください。
- **curl**：Windows 10以降とMacには標準搭載。`curl.exe --version` で確認
- **Postman**：https://www.postman.com/downloads/ GUIで操作でき、初心者には分かりやすい

---

## 7. 最終確認：全部揃ったかチェック

```bash
java -version            # 21.x.x
javac -version           # 21.x.x
git --version            # 2.x.x
curl.exe --version       # 7.x or 8.x
docker --version         # 27.x など（Day 8 以降で使用）
docker compose version   # v2.x など
```

> **Docker は Day 8 まで使いませんが、インストールと `docker run --rm hello-world` の確認だけは Day 0 に済ませてください。** 会社PCではポリシーで入らないことがあり、**Day 8 当日に発覚すると1日止まります**。「使う前に、使えることを確認しておく」のは実務でも同じです。

IntelliJ で新規プロジェクトを作り、以下を実行して `Hello, Java!` が出れば準備完了です。

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello, Java!");
    }
}
```

**最後に、以下の4つができていることを確認してください（Day 0 の完了条件です）。**
- [ ] `git push` したものが GitHub のブラウザ画面で見える（§4-2）
- [ ] `.gitignore` があり、`git status` に `out/` や `.idea/` が出てこない（§4-2）
- [ ] `week1` が Maven プロジェクトになっていて `mvn clean test` が BUILD SUCCESS（§4-3）
- [ ] `docker run --rm hello-world` が成功する（§5-1）

---

## 8. 学習用フォルダ構成

`java-14days` の直下に置くのは、**ファイル3つとフォルダ5つ**だけです。

| 名前 | 種類 | 中身 | 使う日 |
|---|---|---|---|
| `README.md` | ファイル | リポジトリの説明（Day 14 で完成させる） | Day 14 |
| `.gitignore` | ファイル | Gitに載せないものの一覧（§4-2） | Day 0 |
| `compose.yaml` | ファイル | ローカル用 PostgreSQL の定義（§5-2） | Day 8〜 |
| `docs/` | フォルダ | **教材**（00〜12）。自分では書き換えない | 毎日 |
| `log/` | フォルダ | 学習ログ `day00.md` 〜 `day14.md` | 毎日 |
| `week1/` | フォルダ | Java の練習コード（Maven プロジェクト。§4-3） | Day 0〜7 |
| `week2/` | フォルダ | SQL ファイル（`schema.sql` / `sample-data.sql` / `queries.sql`） | Day 8 |
| `attendance-api/` | フォルダ | **成果物**の Spring Boot プロジェクト | Day 9〜14 |

### `week1/` の中（§4-3 で作る Maven プロジェクト）

| パス | 何を置くか |
|---|---|
| `week1/pom.xml` | 依存ライブラリの定義 |
| `week1/src/main/java/` | Day 0〜2 の練習（パッケージなし） |
| `week1/src/main/java/com/example/attendance/` | Day 3〜6 のクラス（`domain/` `service/` などに分ける） |
| `week1/src/test/java/com/example/attendance/` | Day 7 のテストコード |

### `attendance-api/` の中（Day 9 に Spring Initializr で生成）

| パス | 何を置くか |
|---|---|
| `attendance-api/pom.xml` | 依存ライブラリの定義（自動生成） |
| `attendance-api/src/main/java/...` | 本体のコード（構成は `05_project-spec.md` §5） |
| `attendance-api/src/main/resources/` | `application.yml` と `db/migration/V1__*.sql`（Flyway） |
| `attendance-api/src/test/` | テストコード（DB接続設定は不要。Testcontainers が自動起動する） |
| `attendance-api/docs/` | **自分が書く設計書**（下表） |

### `attendance-api/docs/` に自分で書くもの

| ファイル | 内容 | 書く日 |
|---|---|---|
| `db-design.md` | テーブル設計とその理由 | Day 8 |
| `api-log.md` | curl のリクエスト／レスポンス記録 | Day 9 |
| `api-errors.md` | エラーコード一覧 | Day 11 |
| `coding-standards.md` | 自分のコーディング規約 | Day 12 |
| `design-ATT-30.md` | 月次集計APIの設計書 | Day 14 |
| `decisions.md` | 決めたことと、その根拠 | Day 14 |

> **⚠ 教材の `docs/` と、自分が書く `attendance-api/docs/` を混ぜないでください。**
> 各日の課題の「`attendance-api/docs/xxx.md` に書く」は、必ず後者を指します。
> Day 8 の時点ではまだ `attendance-api` が無いので、**フォルダだけ先に作って構いません**。

`.gitignore` の中身は §4-2 のステップ0を参照してください。

---

## 困ったときは `07_troubleshooting.md` を見てください
