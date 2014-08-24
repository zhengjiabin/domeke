
DROP TABLE IF EXISTS goods;
CREATE TABLE IF NOT EXISTS goods (
goodsid		BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT ,
goods 		varchar(50) NOT NULL ,
goodsname	varchar(255) NOT NULL ,
price		float(7,2) unsigned NOT NULL ,
amount		int(10) unsigned NOT NULL default 0 ,
pic			varchar(255) NOT NULL ,
message		varchar(255) NOT NULL ,
tamllurl	varchar(255) NOT NULL ,
submitdate	timestamp NOT NULL default current_timestamp ,
status		varchar(4) NOT NULL default '10' ,
createtime timestamp not null default current_timestamp ,
creater		bigint(20) NOT NULL ,
modifytime timestamp not null default current_timestamp ,
modifier	bigint(20) NOT NULL 
);

DROP TABLE IF EXISTS orders;
CREATE TABLE IF NOT EXISTS orders (
orderid		BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT ,
goodsid		BIGINT(20) NOT NULL ,
status		varchar(4) NOT NULL default '10',
buyer		varchar(50) NOT NULL ,
userid		BIGINT(20) unsigned NOT NULL ,
amount		int(10) unsigned NOT NULL default 0 ,
price		float(7,2) unsigned NOT NULL ,
submitdate	timestamp NOT NULL default current_timestamp ,
confirmdate	timestamp NOT NULL default current_timestamp ,
email		varchar(40) NOT NULL ,
ip			varchar(15) NOT NULL ,
createtime	timestamp not null default current_timestamp ,
creater		bigint(20) NOT NULL ,
modifytime	timestamp not null default current_timestamp ,
modifier	bigint(20) NOT NULL
);