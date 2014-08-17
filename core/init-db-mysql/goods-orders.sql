
DROP TABLE IF EXISTS goods;
CREATE TABLE IF NOT EXISTS goods (
goodsid		BIGINT(32) PRIMARY KEY NOT NULL AUTO_INCREMENT ,
goods 		char(50) NOT NULL ,
goodsname	varchar(255) NOT NULL ,
price		float(7,2) unsigned NOT NULL ,
amount		int(10) unsigned NOT NULL ,
pic			varchar(255) NOT NULL ,
message		varchar(255) NOT NULL ,
tamllurl	varchar(255) NOT NULL ,
submitdate	timestamp NOT NULL default current_timestamp,
status		char(3) NOT NULL ,
create_time timestamp not null default current_timestamp,
creater		varchar(64) NOT NULL,
modify_time timestamp not null default current_timestamp,
modifier	varchar(64) NOT NULL 
);

DROP TABLE IF EXISTS orders;
CREATE TABLE IF NOT EXISTS orders (
orderid		BIGINT(32) PRIMARY KEY NOT NULL AUTO_INCREMENT,
goodsid		char(32) NOT NULL,
status		char(3) NOT NULL,
buyer		char(50) NOT NULL,
uid			mediumint(8) unsigned NOT NULL,
amount		int(10) unsigned NOT NULL,
price		float(7,2) unsigned NOT NULL,
submitdate	timestamp NOT NULL default current_timestamp,
confirmdate	timestamp NOT NULL default current_timestamp,
email		char(40) NOT NULL,
ip			char(15) NOT NULL,
create_time	timestamp not null default current_timestamp,
creater		varchar(64) NOT NULL,
modify_time	timestamp not null default current_timestamp,
modifier	varchar(64) NOT NULL
);