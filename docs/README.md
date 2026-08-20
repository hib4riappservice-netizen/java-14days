# Java 14日間 実務投入プログラム

新卒・未経験者を、14日間で「現場の戦力」まで引き上げるための学習教材一式です。

---

## ⚠ ファイル名について（先に読んでください）

**ファイル名は半角英数字にしてあります。** 日本語のファイル名は、環境によって文字化けするためです。
各ファイルの**1行目に日本語のタイトル**が入っているので、開けば内容はすぐ分かります。

これは実務でも通用する作法です。**開発プロジェクトのフォルダ名・ファイル名に日本語や空白を使うと、Java・Maven・Git・各種ツールで文字コード起因のトラブルが起きます。** 業務ファイル（提案書等）は日本語名で構いませんが、**ソースコードを置く場所は英数字**にしてください。

---

## 読む順番

1. **`00_start-here.md`** ← まずここ
2. `04_setup.md` ← Day 0 で環境構築
3. `02_curriculum-week1.md` → `03_curriculum-week2.md` ← 毎日進める

---

## ファイル一覧

| # | ファイル名 | 日本語タイトル | いつ読むか |
|---|---|---|---|
| 00 | `00_start-here.md` | はじめに・学習の進め方 | **最初に全部** |
| 01 | `01_glossary.md` | 用語辞典（248項目） | **分からない語が出るたび** |
| 02 | `02_curriculum-week1.md` | カリキュラム 第1週（Day 0〜7） | 該当日に |
| 03 | `03_curriculum-week2.md` | カリキュラム 第2週（Day 8〜14） | 該当日に |
| 04 | `04_setup.md` | 環境構築手順（Git の push 手順・Maven プロジェクト作成・DB準備を含む） | Day 0（Day 3・6・8 でも参照） |
| 05 | `05_project-spec.md` | 総合課題・勤怠管理API仕様 | Day 1 に一読、Day 9 以降は毎日 |
| 06 | `06_roadmap-6months.md` | 15日目以降・6ヶ月ロードマップ | Day 14 の夜 |
| 07 | `07_troubleshooting.md` | つまずいたとき集 | エラーが出たとき |
| 08 | `08_answers.md` | 解答例（Day 1〜5・12・14）＋自己検証チェック（Day 6〜11・13） | **30分自力で考えてから** |
| 09 | `09_assessment.md` | 到達度チェックテスト | **Day 7 の夜、Day 14 の夜（必須）** |
| 10 | `10_review-log.md` | 教材レビュー記録 | 品質検証の記録 |
| 11 | `11_conversation-log.md` | 会話記録 | 制作の経緯 |
| 12 | `12_design-decisions.md` | 作業記録・設計判断 | 設計判断と検証ログ |

---

## 作業フォルダ構成

リポジトリ `java-14days` の直下に、**4つのファイルと6つのフォルダ**を置きます。

| 名前 | 中身 | 使う日 |
|---|---|---|
| `README.md` | リポジトリの説明（Day 14 で完成させる） | Day 14 |
| `.gitignore` | Gitに載せないものの一覧 | Day 0 |
| `.gitattributes` | 改行コードを LF に固定 | Day 0 |
| `compose.yaml` | ローカル用 PostgreSQL の定義 | Day 8〜 |
| `docs/` | **教材**（00〜12。このフォルダ。自分では書き換えない） | 毎日 |
| `log/` | 学習ログ `day00.md` 〜 `day14.md` | 毎日 |
| `week1/` | Java の練習コード（Maven プロジェクト） | Day 0〜7 |
| `week2/` | SQL ファイル（`schema.sql` / `sample-data.sql` / `queries.sql`） | Day 8 |
| `attendance-api/` | **成果物**の Spring Boot プロジェクト | Day 9〜14 |
| `.github/workflows/` | CI の定義（`ci.yml`） | Day 13 |

自分で書く設計書は、成果物と一緒に `attendance-api/docs/` へ入れます。

- **`db-design.md`**（Day 8）… テーブル設計とその理由
- **`api-log.md`**（Day 9）… curl のリクエスト／レスポンス記録
- **`api-errors.md`**（Day 11）… エラーコード一覧
- **`coding-standards.md`**（Day 12）… 自分のコーディング規約
- **`design-ATT-30.md`** / **`decisions.md`**（Day 14）… 設計書と決めごとの記録

> **⚠ 教材の `docs/`（このフォルダ）と、自分が書く `attendance-api/docs/` は別物です。**
> 各日の課題に出てくる「`attendance-api/docs/xxx.md` に書く」は、必ず後者を指します。

---

## 前提環境

- JDK 21 (LTS)
- IntelliJ IDEA（2025.3以降の統合版。無料の範囲で完結します）
- Git / GitHub アカウント
- Maven（IntelliJ 同梱のもので可）
- **Docker Desktop**（Day 8 以降。PostgreSQL の起動と、Day 13 の Testcontainers に使用）
- PostgreSQL 16（Docker Compose で起動。H2 などの簡易DBは使いません）
- **Spring Boot 4.1系**（Day 9 以降。Spring Initializr の既定バージョン）

> **⚠ Spring Boot 3.x 向けの記事はそのまま使えません。** Boot 4 では、テスト用アノテーションの置き場所（`@WebMvcTest` `@DataJpaTest`）や、自動設定モジュールの分け方（Flyway など）が変わっています。
> **本教材は Boot 4 前提で書かれており、変更点はその都度「旧はこうだった」と併記しています。**

### 本教材が採用している「実務標準」

学習用に簡略化せず、**現場でそのまま使われている構成**で進めます。

| 項目 | 採用しているもの | 扱う日 |
|---|---|---|
| DB | PostgreSQL（開発・テスト・本番で同一製品） | Day 8〜 |
| ローカル環境 | Docker Compose | Day 8 |
| スキーマ管理 | Flyway（マイグレーションファイルをGit管理） | Day 10 |
| 定型コード削減 | Lombok（**手で書けるようになってから** Day 10 で導入） | Day 10 |
| テスト用DB | Testcontainers（テスト時に本物のDBを使い捨て起動） | Day 13 |
| CI | GitHub Actions ＋ `main` のブランチ保護 | Day 7・13 |

セットアップ手順は `04_setup.md` にあります。**Git の add / commit / push の手順、Maven プロジェクトの作り方、Day 8 用の PostgreSQL の準備も同ファイルに載っています。**

---

## Markdownの読み方

`.md` はMarkdownという書式のテキストファイルです。メモ帳でも読めますが、記号がそのまま見えて読みにくいので、以下のいずれかを推奨します。

- **Visual Studio Code**（無料）で開き、`Ctrl + Shift + V` でプレビュー表示
- **IntelliJ IDEA** で開く（右上のプレビュータブで整形表示）
- ブラウザ拡張やMarkdownビューアアプリ

**IntelliJ は Day 0 でどのみち入れるので、そこで開くのが一番手間がかかりません。**

---

## 品質検証について

本教材は3回の検証パスを通しています（詳細は `10_review-log.md`）。

- 未定義用語：**0語**（辞典248項目）
- 掲載コードの実機検証：**17項目 PASS / 0 FAIL**（JDK 21）
- 現場事故に直結する必須警告：**欠落0件 / 20項目**
- 自己チェック項目：**163項目**（Day 0〜14 の⑥自己チェック＋週次の到達確認）

> **追加の整合性修正（2026-08-18）**：Day 0 の Git 手順欠落、Maven プロジェクト作成手順の欠落、Day 8 の DB 準備手順、仕様書の割増率・境界値の不整合、非推奨API（`@MockBean`）などを修正しました。
> 上記の検証数値は初版レビュー時のもので、辞典・自己チェックの項目数は実カウントに更新しています。
