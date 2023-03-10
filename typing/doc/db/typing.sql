set session foreign_key_checks=0;

/* drop tables */

drop table if exists ans_history_tbl;
drop table if exists ans_tbl;
drop table if exists event_question;
drop table if exists event_user;
drop table if exists event_tbl;
drop table if exists question_tbl;
drop table if exists user_tbl;




/* create tables */

create table ans_history_tbl
(
	anshid int not null auto_increment,
	ansid int not null,
	-- 何回目の提出か
	-- ans＿tblのsubmit_countと関係あり
	submit_no int not null comment '何回目の提出か
ans＿tblのsubmit_countと関係あり',
	-- 回答内容
	answer varchar(10000) not null comment '回答内容',
	-- 解答時間
	time int not null comment '解答時間',
	-- 提出時間
	ans_timestamp datetime not null comment '提出時間',
	-- 正解フラグ
	-- ０の場合は不正がった
	correct_flg int not null comment '正解フラグ
０の場合は不正がった',
	-- 採点結果のjsonデータ
	socre_json varchar(4000) not null comment '採点結果のjsonデータ',
	score int not null,
	primary key (anshid)
);


create table ans_tbl
(
	ansid int not null auto_increment,
	uid int not null,
	eqid int not null,
	-- 得点
	-- 最後の１回分のみ
	score int not null comment '得点
最後の１回分のみ',
	-- 提出回数
	submit_count int not null comment '提出回数',
	primary key (ansid),
	constraint uq_ueq unique (uid, eqid)
);


create table event_question
(
	eqid int not null auto_increment,
	-- イベント内での問題番号
	no int not null comment 'イベント内での問題番号',
	-- 練習問題の場合はnullで登録
	eid int comment '練習問題の場合はnullで登録',
	qid int not null,
	primary key (eqid),
	constraint eidqid unique (eid, qid)
);


create table event_tbl
(
	eid int not null auto_increment,
	-- イベント名
	name varchar(256) not null comment 'イベント名',
	-- 公開日時
	public_date datetime not null comment '公開日時',
	-- イベントの開始日
	start_date datetime not null comment 'イベントの開始日',
	-- 終了日時
	finish_date datetime not null comment '終了日時',
	-- 公開終了日時
	public_end_date datetime not null comment '公開終了日時',
	-- 主催者名
	ower_name varchar(256) not null comment '主催者名',
	-- ランキング表示フラグ
	-- 0:参加者に見せない
	-- 1:参加者にも見せる
	rankingdisplay int not null comment 'ランキング表示フラグ
0:参加者に見せない
1:参加者にも見せる',
	-- ランキングを隠すタイミング
	hiderankingtime int not null comment 'ランキングを隠すタイミング',
	-- 公開相手を限定したいときに使用する
	-- 公開したいメールアドレスのフィルターを正規表現で記載する
	filter varchar(256) comment '公開相手を限定したいときに使用する
公開したいメールアドレスのフィルターを正規表現で記載する',
	primary key (eid)
);


create table event_user
(
	euid int not null auto_increment,
	uid int not null,
	eid int not null,
	-- 削除フラグ
	del_flg int not null comment '削除フラグ',
	primary key (euid)
);


create table question_tbl
(
	qid int not null auto_increment,
	-- タイトル
	title varchar(256) not null comment 'タイトル',
	-- 問題の文章
	sentence varchar(10000) not null comment '問題の文章',
	-- 0:本番用問題
	-- 1:練習用問題
	practiceflg int not null comment '0:本番用問題
1:練習用問題',
	-- 難易度
	difficalty int not null comment '難易度',
	-- 問題文の文字数
	count int not null comment '問題文の文字数',
	-- 問題の作成日
	create_date datetime not null comment '問題の作成日',
	-- 更新日
	update_date datetime not null comment '更新日',
	primary key (qid)
);


create table user_tbl
(
	uid int not null auto_increment,
	-- メールアドレス
	-- ログインアカウント
	mail varchar(256) not null comment 'メールアドレス
ログインアカウント',
	password varchar(256) not null,
	-- 名前
	name varchar(256) not null comment '名前',
	-- 表示名
	disp_name varchar(256) not null comment '表示名',
	-- 所属
	-- 学校名やクラス名など
	affiliation varchar(256) comment '所属
学校名やクラス名など',
	-- 役割
	-- 0:学生
	-- 1:管理者
	role int not null comment '役割
0:学生
1:管理者',
	-- 削除フラグ
	del_flg int not null comment '削除フラグ',
	primary key (uid)
);



/* create foreign keys */

alter table ans_history_tbl
	add foreign key (ansid)
	references ans_tbl (ansid)
	on update restrict
	on delete restrict
;


alter table ans_tbl
	add foreign key (eqid)
	references event_question (eqid)
	on update restrict
	on delete restrict
;


alter table event_question
	add foreign key (eid)
	references event_tbl (eid)
	on update restrict
	on delete restrict
;


alter table event_user
	add foreign key (eid)
	references event_tbl (eid)
	on update restrict
	on delete restrict
;


alter table event_question
	add foreign key (qid)
	references question_tbl (qid)
	on update restrict
	on delete restrict
;


alter table ans_tbl
	add foreign key (uid)
	references user_tbl (uid)
	on update restrict
	on delete restrict
;


alter table event_user
	add foreign key (uid)
	references user_tbl (uid)
	on update restrict
	on delete restrict
;



