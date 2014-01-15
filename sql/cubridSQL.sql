-- cubrid db¿ë sql


INSERT INTO gbook VALUES(null,'1','1','1','1','1',SYS_DATETIME,0);

UPDATE gbook set readnum=readnum+1 WHERE num=1;

DELETE FROM gbook WHERE num=1 and password='1';

SELECT title,author,email,content,password, to_char(writeday, 'YYYY-MM-DD HH:MI AM'), readnum
from gbook WHERE num=1;

SELECT num,title,author,email,content,password, to_char(writeday, 'YYYY-MM-DD HH:MI AM'), readnum
from gbook order by num desc;


select * from gbook;

