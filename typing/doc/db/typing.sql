SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS ANS_DL_TBL;
DROP TABLE IF EXISTS ANS_HISTORY_TBL;
DROP TABLE IF EXISTS ANS_TBL;
DROP TABLE IF EXISTS ANS_TEMP_TBL;
DROP TABLE IF EXISTS EVENT_DOWNLOAD;
DROP TABLE IF EXISTS DOWNLOAD_TBL;
DROP TABLE IF EXISTS EVENT_QUESTION;
DROP TABLE IF EXISTS EVENT_USER;
DROP TABLE IF EXISTS EVENT_TBL;
DROP TABLE IF EXISTS QUESTION_TBL;
DROP TABLE IF EXISTS USER_TBL;




/* Create Tables */

CREATE TABLE ANS_DL_TBL
(
	ansid int NOT NULL,
	uid int NOT NULL,
	ed_id int NOT NULL,
	-- 提出時間
	submit_date datetime NOT NULL COMMENT '提出時間',
	PRIMARY KEY (ansid)
);


CREATE TABLE ANS_HISTORY_TBL
(
	anshid int NOT NULL AUTO_INCREMENT,
	ansid int NOT NULL,
	-- 何回目の提出か
	-- ANS＿TBLのsubmit_countと関係あり
	submit_no int NOT NULL COMMENT '何回目の提出か
ANS＿TBLのsubmit_countと関係あり',
	-- 回答内容
	answer varchar(10000) NOT NULL COMMENT '回答内容',
	-- 解答時間
	time int NOT NULL COMMENT '解答時間',
	-- 提出時間
	ans_timestamp datetime NOT NULL COMMENT '提出時間',
	-- 正解フラグ
	-- ０の場合は不正がった
	correct_flg int NOT NULL COMMENT '正解フラグ
０の場合は不正がった',
	-- 採点結果のJSONデータ
	socre_json varchar(4000) NOT NULL COMMENT '採点結果のJSONデータ',
	score int NOT NULL,
	PRIMARY KEY (anshid)
);


CREATE TABLE ANS_TBL
(
	ansid int NOT NULL AUTO_INCREMENT,
	uid int NOT NULL,
	eqid int NOT NULL,
	-- 得点
	-- 最後の１回分のみ
	score int NOT NULL COMMENT '得点
最後の１回分のみ',
	-- 提出回数
	submit_count int NOT NULL COMMENT '提出回数',
	PRIMARY KEY (ansid),
	CONSTRAINT UQ_UEQ UNIQUE (uid, eqid)
);


-- タイピングの画面で、開始ボタンをクリックしたときに開始時刻を保持するための一時テーブル
-- タイピング画面表示時にこのテー
CREATE TABLE ANS_TEMP_TBL
(
	-- トークン
	token varchar(64) NOT NULL COMMENT 'トークン',
	-- 開始時刻
	start_time datetime COMMENT '開始時刻',
	uid int NOT NULL,
	-- 練習問題として解いた時はNULLが入る
	-- ※先生アカウントでは本番用問題を練習問題として扱うことに注意
	eid int COMMENT '練習問題として解いた時はNULLが入る
※先生アカウントでは本番用問題を練習問題として扱うことに注意',
	qid int NOT NULL,
	PRIMARY KEY (token)
) COMMENT = 'タイピングの画面で、開始ボタンをクリックしたときに開始時刻を保持するための一時テーブル
タイピング画面表示時にこのテー';


CREATE TABLE DOWNLOAD_TBL
(
	download_id int NOT NULL,
	-- 表示順
	ordinal_num int NOT NULL COMMENT '表示順',
	-- ファイル名
	filename varchar(256) NOT NULL COMMENT 'ファイル名',
	PRIMARY KEY (download_id)
);


CREATE TABLE EVENT_DOWNLOAD
(
	ed_id int NOT NULL,
	eid int NOT NULL,
	download_id int NOT NULL,
	PRIMARY KEY (ed_id)
);


CREATE TABLE EVENT_QUESTION
(
	eqid int NOT NULL AUTO_INCREMENT,
	-- イベント内での問題番号
	no int NOT NULL COMMENT 'イベント内での問題番号',
	-- 練習問題の場合はNULLで登録
	eid int COMMENT '練習問題の場合はNULLで登録',
	qid int NOT NULL,
	PRIMARY KEY (eqid),
	CONSTRAINT eidqid UNIQUE (eid, qid)
);


CREATE TABLE EVENT_TBL
(
	eid int NOT NULL AUTO_INCREMENT,
	-- イベント名
	name varchar(256) NOT NULL COMMENT 'イベント名',
	-- 公開日時
	public_date datetime NOT NULL COMMENT '公開日時',
	-- イベントの開始日
	start_date datetime NOT NULL COMMENT 'イベントの開始日',
	-- 終了日時
	finish_date datetime NOT NULL COMMENT '終了日時',
	-- 公開終了日時
	public_end_date datetime NOT NULL COMMENT '公開終了日時',
	-- 主催者名
	ower_name varchar(256) NOT NULL COMMENT '主催者名',
	-- ランキング表示フラグ
	-- 0:参加者に見せない
	-- 1:参加者にも見せる
	rankingdisplay int NOT NULL COMMENT 'ランキング表示フラグ
0:参加者に見せない
1:参加者にも見せる',
	-- ランキングを隠すタイミング
	hiderankingtime int NOT NULL COMMENT 'ランキングを隠すタイミング',
	-- 公開相手を限定したいときに使用する
	-- 公開したいメールアドレスのフィルターを正規表現で記載する
	filter varchar(256) COMMENT '公開相手を限定したいときに使用する
公開したいメールアドレスのフィルターを正規表現で記載する',
	PRIMARY KEY (eid)
);


CREATE TABLE EVENT_USER
(
	euid int NOT NULL AUTO_INCREMENT,
	uid int NOT NULL,
	eid int NOT NULL,
	-- 削除フラグ
	del_flg int NOT NULL COMMENT '削除フラグ',
	PRIMARY KEY (euid)
);


CREATE TABLE QUESTION_TBL
(
	qid int NOT NULL AUTO_INCREMENT,
	-- タイトル
	title varchar(256) NOT NULL COMMENT 'タイトル',
	-- 問題の文章
	sentence varchar(10000) NOT NULL COMMENT '問題の文章',
	-- 0:本番用問題
	-- 1:練習用問題
	practiceflg int NOT NULL COMMENT '0:本番用問題
1:練習用問題',
	-- 難易度
	difficalty int NOT NULL COMMENT '難易度',
	-- 問題文の文字数
	count int NOT NULL COMMENT '問題文の文字数',
	-- 問題の作成日
	create_date datetime NOT NULL COMMENT '問題の作成日',
	-- 更新日
	update_date datetime NOT NULL COMMENT '更新日',
	PRIMARY KEY (qid)
);


CREATE TABLE USER_TBL
(
	uid int NOT NULL AUTO_INCREMENT,
	-- メールアドレス
	-- ログインアカウント
	mail varchar(256) NOT NULL COMMENT 'メールアドレス
ログインアカウント',
	password varchar(256) NOT NULL,
	-- 名前
	name varchar(256) NOT NULL COMMENT '名前',
	-- 表示名
	disp_name varchar(256) NOT NULL COMMENT '表示名',
	-- 所属
	-- 学校名やクラス名など
	affiliation varchar(256) COMMENT '所属
学校名やクラス名など',
	-- 役割
	-- 0:学生
	-- 1:管理者
	role int NOT NULL COMMENT '役割
0:学生
1:管理者',
	-- 削除フラグ
	del_flg int NOT NULL COMMENT '削除フラグ',
	PRIMARY KEY (uid)
);



/* Create Foreign Keys */

ALTER TABLE ANS_HISTORY_TBL
	ADD FOREIGN KEY (ansid)
	REFERENCES ANS_TBL (ansid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE EVENT_DOWNLOAD
	ADD FOREIGN KEY (download_id)
	REFERENCES DOWNLOAD_TBL (download_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ANS_DL_TBL
	ADD FOREIGN KEY (ed_id)
	REFERENCES EVENT_DOWNLOAD (ed_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ANS_TBL
	ADD FOREIGN KEY (eqid)
	REFERENCES EVENT_QUESTION (eqid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE EVENT_DOWNLOAD
	ADD FOREIGN KEY (eid)
	REFERENCES EVENT_TBL (eid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE EVENT_QUESTION
	ADD FOREIGN KEY (eid)
	REFERENCES EVENT_TBL (eid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE EVENT_USER
	ADD FOREIGN KEY (eid)
	REFERENCES EVENT_TBL (eid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ANS_TEMP_TBL
	ADD FOREIGN KEY (qid)
	REFERENCES QUESTION_TBL (qid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE EVENT_QUESTION
	ADD FOREIGN KEY (qid)
	REFERENCES QUESTION_TBL (qid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ANS_DL_TBL
	ADD FOREIGN KEY (uid)
	REFERENCES USER_TBL (uid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ANS_TBL
	ADD FOREIGN KEY (uid)
	REFERENCES USER_TBL (uid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ANS_TEMP_TBL
	ADD FOREIGN KEY (uid)
	REFERENCES USER_TBL (uid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE EVENT_USER
	ADD FOREIGN KEY (uid)
	REFERENCES USER_TBL (uid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



