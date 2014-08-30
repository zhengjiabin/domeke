
DROP TABLE IF EXISTS goods;
CREATE TABLE IF NOT EXISTS goods (
goodsid		BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT ,
goods 		varchar(32) NOT NULL ,
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

DROP TABLE IF EXISTS works;
CREATE TABLE IF NOT EXISTS works (
worksid			BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT ,	
worksname 		varchar(255) NOT NULL ,		
workstype 		varchar(32) default 10 ,	
creativeprocess	varchar(32) default 10 ,
cover 			varchar(255) NOT NULL , 
describle		varchar(255) default NULL ,	
fileurl			varchar(255) NOT NULL ,	
pageviews		BIGINT(20) default 0 ,
collection		BIGINT(20) default 0 ,	
praise			BIGINT(20) default 0 ,
createtime		timestamp not null default current_timestamp ,
creater			BIGINT(20) NOT NULL ,
modifytime		timestamp not null default current_timestamp ,
modifier		BIGINT(20) NOT NULL					
);