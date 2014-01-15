drop table gbook;
create table gbook (
	num int AUTO_INCREMENT not null,
	title varchar(50) not null,
	author varchar(50) not null,
	email varchar(50) not null,
	content varchar(1000),
	password varchar(20) not null,
	writeday datetime  not null,
	readnum int
);
