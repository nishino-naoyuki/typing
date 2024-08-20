
DROP TABLE IF EXISTS ans_dl_tbl;
DROP TABLE IF EXISTS event_download;
DROP TABLE IF EXISTS download_tbl;

create table ans_dl_tbl
(
	ansid int not null auto_increment,
	uid int not null,
	ed_id int not null,
	-- 提出時間
	submit_date datetime not null comment '提出時間',
	ans_filepath varchar(512) not null,
	primary key (ansid)
);


create table download_tbl
(
	download_id int not null auto_increment,
	-- ファイル名
	filename varchar(256) not null comment 'ファイル名',
	primary key (download_id)
);


create table event_download
(
	ed_id int not null auto_increment,
	eid int not null,
	download_id int not null,
	no int not null,
	primary key (ed_id)
);




alter table ans_dl_tbl
	add foreign key (ed_id)
	references event_download (ed_id)
	on update restrict
	on delete restrict
;

alter table event_download
	add foreign key (eid)
	references event_tbl (eid)
	on update restrict
	on delete restrict
;

alter table ans_dl_tbl
	add foreign key (uid)
	references user_tbl (uid)
	on update restrict
	on delete restrict
;