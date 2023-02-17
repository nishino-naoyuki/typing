SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS ANS_TBL;
DROP TABLE IF EXISTS EVENT_QUESTION;
DROP TABLE IF EXISTS EVENT_USER;
DROP TABLE IF EXISTS EVENT_TBL;
DROP TABLE IF EXISTS QESTION_TBL;
DROP TABLE IF EXISTS USER_TBL;




/* Create Tables */

CREATE TABLE ANS_TBL
(
	ansid int NOT NULL AUTO_INCREMENT,
	uid int NOT NULL,
	eqid int NOT NULL,
	-- 解答
	answer varchar(16383) NOT NULL COMMENT '解答',
	-- 解答にかかった秒数
	time int NOT NULL COMMENT '解答にかかった秒数',
	-- 解答を出した日時
	ans_timestamp timestamp NOT NULL COMMENT '解答を出した日時',
	-- 正解フラグ
	correct_flg int NOT NULL COMMENT '正解フラグ',
	-- diffコマンドの実行結果
	difflog varchar(4000) NOT NULL COMMENT 'diffコマンドの実行結果',
	-- 得点
	score int NOT NULL COMMENT '得点',
	PRIMARY KEY (ansid)
);


CREATE TABLE EVENT_QUESTION
(
	eqid int NOT NULL AUTO_INCREMENT,
	-- イベント内での問題番号
	no int NOT NULL COMMENT 'イベント内での問題番号',
	eid int NOT NULL,
	qid int NOT NULL,
	PRIMARY KEY (eqid)
);


CREATE TABLE EVENT_TBL
(
	eid int NOT NULL AUTO_INCREMENT,
	-- イベント名
	name varchar(256) NOT NULL COMMENT 'イベント名',
	-- 公開日時
	public_date timestamp NOT NULL COMMENT '公開日時',
	-- 終了日時
	finish_date timestamp NOT NULL COMMENT '終了日時',
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


CREATE TABLE QESTION_TBL
(
	qid int NOT NULL AUTO_INCREMENT,
	-- タイトル
	title varchar(256) NOT NULL COMMENT 'タイトル',
	-- 問題の文章
	sentence varchar(16383) NOT NULL COMMENT '問題の文章',
	-- 0:本番用問題
	-- 1:練習用問題
	practiceflg int NOT NULL COMMENT '0:本番用問題
1:練習用問題',
	-- 難易度
	difficalty int NOT NULL COMMENT '難易度',
	-- 問題文の文字数
	count int NOT NULL COMMENT '問題文の文字数',
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

ALTER TABLE ANS_TBL
	ADD FOREIGN KEY (eqid)
	REFERENCES EVENT_QUESTION (eqid)
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


ALTER TABLE EVENT_QUESTION
	ADD FOREIGN KEY (qid)
	REFERENCES QESTION_TBL (qid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ANS_TBL
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



