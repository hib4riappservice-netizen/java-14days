# 毎日使うコマンド早見表

> **`04_setup.md` は「最初の1回だけ」読むファイルです。こちらは逆に「毎日・何度も」開くためのファイルです。**
> Git・Maven・Docker Compose・psql のうち、**インストールが終わった後に繰り返し使うコマンド**だけをここに集めました。
> 「どのコマンドだったか忘れた」ときは、まずこのページを Ctrl+F してください。

---

## 1. Git：日々のコミット・push（Day 1 以降、毎日）

### 基本サイクル
```bash
cd /c/java-14days      # まずリポジトリのルートへ（どこにいるか分からなくなったら pwd）

git status             # ① 何が変わったかを見る（毎回必ず見る）
git add <ファイル名>     # ② 載せるファイルを選ぶ
git commit -m "feat: Day X の課題を実装"   # ③ 履歴に刻む
git push origin main   # ④ GitHub へ送る（Day 7 以降はブランチ経由。§3参照）
```

### `git status` の読み方（ここが読めれば迷いません）
- `Changes to be committed:` … `git add` 済み。**次の `commit` に載る**
- `Changes not staged for commit:` … 変更したが `add` していない。**このままでは載らない**
- `Untracked files:` … Gitがまだ知らない新規ファイル。**`add` しないと永久に載らない**

### 変更したファイルが多くて選ぶのが面倒なとき
```bash
git add -A             # 変更・追加・削除をまとめて載せる
git status             # ⚠ 載せた後に必ず確認する。意図しないファイルが混ざっていないか
```
> **`git add -A` は便利ですが、`.gitignore` の設定が甘いと不要なファイルまで載ります。** 必ず `git status` で中身を確認してから `commit` してください。
> **さらに注意：`git commit` は「その時点でインデックスにある全部」をコミットします。** IDEが裏で他のファイルを `add` 済みにしていることがあるので、**`commit` の直前にもう一度 `git status` で確認する**のが安全です。

### 1つのファイルに複数の意味の変更が混ざっているとき：`git add -p`
たとえば「今日書いたコードの変更」と「エディタが勝手に整形しただけの無関係な変更」が同じファイルに同居していることがあります。`git add <ファイル名>` では全部まとめて拾われてしまいます。

```bash
git add -p <ファイル名>
```
変更のかたまり（ハンク）ごとに聞かれるので、以下で答えます。
- `y` … このハンクを含める
- `n` … このハンクを除外する
- `q` … ここで終了（残りは全部除外）

> **使いどころ**：意味のあるコード変更と、エディタの自動整形やコピペミスなどの「本当は要らない変更」が同じファイルに混ざったとき。**「1コミット＝1つの意味のある変更」**を保つための道具です。

### コミットメッセージの型（Day 7 で詳しくやります。今はこの3つで十分）
- `feat:` … 機能を追加した（例：`feat: Day 2 の勤怠集計を追加`）
- `fix:` … 間違いを直した（例：`fix: 空配列で落ちる不具合を修正`）
- `docs:` … 文書だけ変えた（例：`docs: Day 3 の学習ログを追記`）

### 1日の終わりの定型（これを毎日繰り返す）
```bash
cd /c/java-14days
git status
git add -A
git status                                   # 中身を確認してから
git commit -m "feat: Day X の課題を実装"
git push origin main
```

> **なぜ毎日 push するのか。** PCが壊れても成果が残る、というだけではありません。
> **「毎日コミットが刻まれた履歴」そのものが、面接で見せられる証拠**になります。14日間の学習の軌跡が GitHub に残ります。

---

## 2. Git：巻き戻し・確認系（困ったとき）

```bash
git log --oneline -10          # 直近10件のコミット履歴
git diff <ファイル名>            # 未ステージの変更内容を見る
git diff --cached <ファイル名>   # ステージ済みの変更内容を見る
git restore <ファイル名>         # 未ステージの変更を破棄する（⚠ 元に戻せません）
git restore --staged <ファイル名> # ステージを取り消す（変更内容は残る）
```
> **`git restore` は取り消せません。** 消してよいか自信が無いときは、`git status` を見て何が消えるか確認してから実行してください。

---

## 3. Git：ブランチ・PR（Day 7 以降）

**Day 7 でブランチ保護を設定すると、`main` への直接 push はできなくなります。** 以降は毎回このサイクルです（詳しい解説は `02_curriculum-week1.md` の Day 7 を参照）。

```bash
git checkout main
git pull origin main                          # 最新の main を取り込む
git checkout -b feature/ATT-XX-何かの機能        # ブランチを切る

# ...コードを書く...

git status
git add <ファイル名>
git commit -m "feat: 何かの機能を追加"
git push origin feature/ATT-XX-何かの機能        # このブランチを初めて push するとき
```
push 後、GitHub の画面で PR（プルリクエスト）を作成し、CIが緑になったらマージします。

---

## 4. Maven：ビルド・テスト（`week1` と `attendance-api` の両方で使用）

```bash
mvn clean          # 生成物（target/）を消す
mvn compile         # コンパイルだけ
mvn test            # テストを実行
mvn clean test       # クリーンしてからテスト（迷ったらこれ）
mvn clean verify     # テスト＋もう少し厳密な検証（Day 13 の CI と同じ内容）
```
> **`mvn` コマンドが見つからない場合**：IntelliJ 右端の「Maven」パネルから同じ操作（`Lifecycle` → `clean` / `test`）ができます。コマンドを別途入れる場合は `04_setup.md` §1 を参照してください。

---

## 5. Docker Compose：PostgreSQLの起動・停止（Day 8 以降）

```bash
docker compose up -d      # 起動（初回はイメージのダウンロードで数分）
docker compose ps         # 状態確認。STATUS が healthy になればOK
docker compose logs db    # 起動に失敗したときはログを見る
docker compose stop       # 停止（データは残る）
docker compose down       # 停止して箱を削除（データは volume に残る）
docker compose down -v    # ⚠ volume ごと削除＝データ全消去。作り直したいときだけ
```
> **PCを再起動した後は、Docker Desktop とコンテナが止まっています。** 毎朝 `docker compose up -d` から始めてください（`04_setup.md` §5-2 で作った `compose.yaml` が必要です）。

---

## 6. psql：SQLを打つ（Day 8 以降）

```bash
docker compose exec db psql -U appuser -d attendance

# ⚠ Git Bash で画面が固まる / "the input device is not a TTY" と出たら、頭に winpty を付ける
winpty docker compose exec db psql -U appuser -d attendance
```

接続できたら、これだけ覚えれば十分です。
- `\dt` … テーブル一覧
- `\d employees` … テーブル定義を見る
- `\l` … データベース一覧
- `\q` … 終了
- `\i /path/file.sql` … SQLファイルを流し込む

ファイルに書いた SQL をまとめて流すとき（ホスト側から渡す場合）
```bash
# Mac / Linux / Git Bash
docker compose exec -T db psql -U appuser -d attendance < week2/schema.sql
```
```powershell
# Windows PowerShell（`<` は使えないので Get-Content から渡す）
Get-Content week2/schema.sql | docker compose exec -T db psql -U appuser -d attendance
```

---

## 7. bash ↔ PowerShell 変換表

**本教材のコマンド例は、断りがなければ Mac / Linux（bash）の書き方です。** Windows で **Git Bash** を使っていれば、ほぼそのまま貼り付けで動きます（`04_setup.md` 用語の前提を参照）。どうしても PowerShell を使う場合は、以下で読み替えてください。

- `cmd < file` → `Get-Content file | cmd`
- `cmd1 && cmd2` → `cmd1; cmd2`（Windows PowerShell 5.1 は `&&` に非対応）
- 行末の `\` で改行を続ける → バッククォートを使うか、**1行で書く**（こちらが安全）
- `curl` → **`curl.exe`**（`curl` だけだと別コマンドの別名になります）

詳しいエラー対処は `07_troubleshooting.md`「8. Windows（PowerShell）でコマンド例が動かない」を見てください。
