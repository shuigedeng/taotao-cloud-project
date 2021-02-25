SHOW DATABASES;
CREATE DATABASE mydb3;
#创建一个数据库
CREATE DATABASE companydb CHARACTER SET utf8;
#查询部分列
SELECT EMPLOYEE_ID,FIRST_NAME,email FROM t_employees;
#查询所有列
#1.使用*的方式
SELECT * FROM t_employees;
#2.使用列名的方式
SELECT EMPLOYEE_ID,FIRST_NAME,LAST_NAME,email,phone_number,hire_date,job_id,salary,commission_pct,manager_id,department_id
FROM t_employees;
#查询所有员工的年薪
SELECT EMPLOYEE_ID AS '编号',FIRST_NAME AS '姓氏',salary*12 AS '年薪' FROM t_employees;
SELECT * FROM t_employees;
#查询员工表中有多少个经理
SELECT DISTINCT manager_id FROM t_employees;
#按照员工工资进行升序排序
SELECT EMPLOYEE_ID,salary FROM t_employees ORDER BY salary ASC;
#按照员工工资进行降序排序
SELECT EMPLOYEE_ID,salary FROM t_employees ORDER BY salary DESC,EMPLOYEE_ID DESC;

#查询符合条件的数据
SELECT EMPLOYEE_ID,FIRST_NAME,salary
FROM t_employees
WHERE salary =11000;

SELECT EMPLOYEE_ID,FIRST_NAME,salary
FROM t_employees
WHERE salary = 24000;

#查询符合两个条件的数据
SELECT EMPLOYEE_ID,FIRST_NAME,salary 
FROM t_employees
WHERE salary = 11000 AND COMMISSION_PCT = 0.30 AND EMPLOYEE_ID='148';

SELECT EMPLOYEE_ID,FIRST_NAME,salary
FROM t_employees
WHERE salary = 11000 OR COMMISSION_PCT = 0.30;

SELECT EMPLOYEE_ID,FIRST_NAME,salary
FROM t_employees
WHERE NOT salary = 11000;

#!=  <>  两者
SELECT EMPLOYEE_ID,FIRST_NAME,salary
FROM t_employees
WHERE salary <>11000;
#查询区间
SELECT EMPLOYEE_ID,FIRST_NAME,salary
FROM t_employees
WHERE salary<=10000 AND salary >=6000;
#查询区间  between and
SELECT EMPLOYEE_ID,FIRST_NAME,salary
FROM t_employees
WHERE salary BETWEEN 10000 AND 6000;



SELECT * FROM t_employees;
#查询出经理编号为NULL的员工信息
SELECT EMPLOYEE_ID,FIRST_NAME,MANAGER_ID
FROM t_employees
WHERE MANAGER_ID IS NULL;

SELECT EMPLOYEE_ID,FIRST_NAME,MANAGER_ID
FROM t_employees
WHERE MANAGER_ID IS NOT NULL;


SELECT EMPLOYEE_ID,FIRST_NAME,salary,DEPARTMENT_ID 
FROM t_employees
WHERE DEPARTMENT_ID = 70 OR DEPARTMENT_ID = 80 OR DEPARTMENT_ID=90;

#枚举查询
SELECT EMPLOYEE_ID,FIRST_NAME,salary,DEPARTMENT_ID 
FROM t_employees
WHERE DEPARTMENT_ID IN(70,80,90);


#L开头的三个字的员工信息
SELECT EMPLOYEE_ID,FIRST_NAME,salary
FROM t_employees
WHERE first_name LIKE 'L__'
#查询所有以L开头的员工信息
SELECT EMPLOYEE_ID,FIRST_NAME,salary
FROM t_employees
WHERE FIRST_NAME LIKE 'L%'


#查询员工信息（编号，名字，薪资 , 薪资级别<对应条件表达式生成>）

SELECT EMPLOYEE_ID,FIRST_NAME,salary,
CASE
	WHEN salary>10000 THEN 'A'
	WHEN salary>=8000 AND salary<10000 THEN 'B'
	WHEN salary>=6000 AND salary<8000 THEN 'C'
	WHEN salary>=4000 AND salary<6000 THEN 'D'
	ELSE 'E'
END AS '薪资级别'
FROM t_employees;



#当前系统时间
SELECT SYSDATE();
#当前系统日期
SELECT CURDATE();
#当前系统时间
SELECT CURTIME();
#获取指定日期为一年中的第几周
SELECT WEEK(SYSDATE());
#获取指定日期中的年份
SELECT YEAR('2020-4-1');
#获取小时值
SELECT HOUR(CURTIME());
#获取分钟值
SELECT MINUTE(CURTIME());
#指定日期之间的相隔天数
SELECT DATEDIFF('2020-4-1','2019-4-1');
#计算Date日期加上N天后的日期
SELECT ADDDATE('2020-4-1',4);

#多个字符串做拼接
SELECT CONCAT('My','S','QL');
SELECT CONCAT(FIRST_NAME,LAST_NAME) AS '姓名' FROM t_employees;
#字符串替换
SELECT INSERT('这是一个数据库',3,2,'MySQL');
#字符串转小写
SELECT LOWER('MYSQL');
#字符串转大写
SELECT UPPER('mysql');
#指定内容截取
SELECT SUBSTRING('JavaMySQLOracle',5,5);



#聚合函数
#求单列所有数据的和
SELECT SUM(salary) FROM t_employees;
#求单列所有数据的平均值
SELECT AVG(salary) FROM t_employees;
#求单列最大值
SELECT MAX(salary) FROM t_employees;
#求单列最小值
SELECT MIN(salary) FROM t_employees;
#求总行数  员工总数
SELECT COUNT(EMPLOYEE_ID) FROM t_employees;
#统计有提成的人数 会自动忽略null值，不进行统计
SELECT COUNT(COMMISSION_PCT) FROM t_employees;

#查询各部门的总人数
SELECT department_id,COUNT(EMPLOYEE_ID)
FROM t_employees
GROUP BY DEPARTMENT_ID;
#查询各部门的平均工资
SELECT DEPARTMENT_ID,AVG(salary)
FROM t_employees
GROUP BY DEPARTMENT_ID;
#查询各个部门、各个岗位的总人数
SELECT DEPARTMENT_ID,JOB_ID,COUNT(EMPLOYEE_ID)
FROM t_employees
GROUP BY DEPARTMENT_ID,job_id;
#常见问题
SELECT DEPARTMENT_ID,COUNT(*),first_name
FROM t_employees
GROUP BY DEPARTMENT_ID;
#统计60、70、90部门工资最高的
SELECT DEPARTMENT_ID,MAX(salary)
FROM t_employees
GROUP BY DEPARTMENT_ID
HAVING DEPARTMENT_ID IN(60,70,90);

#查询前五条
SELECT * FROM t_employees LIMIT 0,5;
SELECT * FROM t_employees LIMIT 5,5;
SELECT * FROM t_employees LIMIT 10,5;
SELECT * FROM t_employees LIMIT 3,10;


#查询bruce工资
SELECT salary FROM t_employees WHERE FIRST_NAME = 'Bruce';
#查询工资大于6000的
SELECT * FROM t_employees WHERE salary > 6000;

#整合
SELECT * FROM t_employees WHERE salary > (SELECT salary FROM t_employees WHERE FIRST_NAME = 'Bruce');
#查询名为KIng的部门  有 80、90
SELECT * FROM t_employees WHERE last_name='King';

SELECT * FROM t_employees WHERE DEPARTMENT_ID IN (80,90);

SELECT * FROM t_employees WHERE DEPARTMENT_ID IN (SELECT DEPARTMENT_ID FROM t_employees WHERE last_name='King');


SELECT salary FROM t_employees WHERE DEPARTMENT_ID = 60;
#高于所有！
SELECT * FROM t_employees WHERE salary > ALL(SELECT salary FROM t_employees WHERE DEPARTMENT_ID = 60);
#高于部分
SELECT * FROM t_employees WHERE salary > ANY(SELECT salary FROM t_employees WHERE DEPARTMENT_ID = 60);





SELECT EMPLOYEE_ID,FIRST_NAME,salary FROM 
(SELECT EMPLOYEE_ID,FIRST_NAME,salary FROM t_employees ORDER BY salary DESC) AS temp
LIMIT 0,5;


SELECT * FROM t_departments UNION SELECT * FROM t_jobs;

SELECT * FROM t1 UNION  SELECT * FROM t2;

SELECT * FROM t_employees;
SELECT * FROM t_jobs;

#如果不指定连接条件，则会造成笛卡尔积的结果
#A{a,b} B{1,2,3}---> {a,1},{a,2},{a,3} {b,1}{b,2}{b,3}
SELECT * FROM t_employees 
INNER JOIN t_jobs
ON t_employees.`JOB_ID` = t_jobs.`JOB_ID`;
#MYSQL
SELECT * FROM t_employees,t_jobs WHERE t_employees.`JOB_ID` = t_jobs.`JOB_ID`;



#三表连接
SELECT EMPLOYEE_ID,t_employees.DEPARTMENT_ID,t_locations.LOCATION_ID FROM t_employees
INNER JOIN t_departments
ON t_employees.`DEPARTMENT_ID` = t_departments.`DEPARTMENT_ID`
INNER JOIN t_locations
ON t_locations.`LOCATION_ID` = t_departments.`LOCATION_ID`;




SELECT * FROM t_employees;
SELECT EMPLOYEE_ID,first_name,salary,DEPARTMENT_NAME FROM t_employees
LEFT JOIN t_departments
ON t_departments.`DEPARTMENT_ID` = t_employees.`DEPARTMENT_ID`;


SELECT EMPLOYEE_ID,first_name,salary,DEPARTMENT_NAME FROM t_employees
RIGHT JOIN t_departments
ON t_departments.`DEPARTMENT_ID` = t_employees.`DEPARTMENT_ID`;



#新增
INSERT INTO t_jobs(job_id,job_title,MIN_SALARY,MAX_SALARY)
VALUES('Java_Le','Java_Lecturer',2500,9000);

SELECT * FROM t_employees;
INSERT INTO t_employees
(EMPLOYEE_ID,FIRST_NAME,LAST_NAME,email,PHONE_NUMBER,HIRE_DATE,JOB_ID,salary,COMMISSION_PCT,MANAGER_ID,DEPARTMENT_ID)
VALUES('207','Steven','Gavin','Gavin','650.501.3876','1998-07-01','SH_CLERK',8000,NULL,124,50);

#修改
SELECT * FROM t_employees;
UPDATE t_employees SET salary = 25000 WHERE EMPLOYEE_ID = '100';
#修改多个列
UPDATE t_employees SET JOB_ID='ST_MAN',salary=3500 WHERE EMPLOYEE_ID='135';

#删除
DELETE FROM t_employees WHERE EMPLOYEE_ID = '135';
SELECT * FROM t_employees WHERE FIRST_NAME='Peter';
DELETE FROM t_employees WHERE FIRST_NAME='Peter' AND LAST_NAME = 'Hall';

SELECT * FROM t2;
#清空操作
TRUNCATE TABLE t1;#是对表做删除操作
DELETE FROM t2;#是对数据做删除操作



#创建Subject表
CREATE TABLE `subject`(
	subjectId INT ,
	subjectName VARCHAR(20),
	subjectHours INT
)CHARSET=utf8;#指定该表存储数据的字符集

SELECT * FROM `subject`;
INSERT INTO `subject`(subjectId,subjectName,subjectHours) VALUES(1,'Java',20);

#向现有表中添加列
ALTER TABLE `subject` ADD gradeId INT;
#修改表中的列
ALTER TABLE `subject` MODIFY subjectName VARCHAR(10);
#删除表中的列
ALTER TABLE `subject` DROP gradeId;
#修改表中的列名
ALTER TABLE `subject` CHANGE subjectHours classHours INT;
#修改表名
ALTER TABLE `subject` RENAME `sub`;
#删除表
DROP TABLE `sub`;


DROP TABLE `subject`;

#创建Subject表
CREATE TABLE `subject`(
	subjectId INT PRIMARY KEY AUTO_INCREMENT ,
	subjectName VARCHAR(20) UNIQUE NOT NULL,
	subjectHours INT DEFAULT 10
)CHARSET=utf8;#指定该表存储数据的字符集
SELECT * FROM `subject`;
INSERT INTO `subject`(subjectName,subjectHours) VALUES('HTML',20);
INSERT INTO `subject`(subjectName,subjectHours) VALUES('Java',20);
INSERT INTO `subject`(subjectName,subjectHours) VALUES('C++',DEFAULT);
#专业表
CREATE TABLE Speciality(
	id INT PRIMARY KEY AUTO_INCREMENT,
	SpecialName VARCHAR(20) UNIQUE NOT NULL
)CHARSET=utf8;

SELECT * FROM Speciality;
INSERT INTO Speciality(SpecialName) VALUES('Java');
INSERT INTO Speciality(SpecialName) VALUES('H5');
#课程表
CREATE TABLE `subject`(
	subjectId INT PRIMARY KEY AUTO_INCREMENT ,
	subjectName VARCHAR(20) UNIQUE NOT NULL,
	subjectHours INT DEFAULT 10,
	specialId INT NOT NULL,
	CONSTRAINT fk_subject_specialId FOREIGN KEY(specialId) REFERENCES Speciality(id)
)CHARSET=utf8;#指定该表存储数据的字符集
SELECT * FROM `subject`;
INSERT INTO `subject`(subjectName,subjectHours,specialId) VALUES('JavaSE',30,1);
INSERT INTO `subject`(subjectName,subjectHours,specialId) VALUES('CSS',20,2);

DROP TABLE Speciality;#主表 被引用表
DROP TABLE `subject`;#从表 引用表





CREATE TABLE Grade(
	gradeId INT PRIMARY KEY AUTO_INCREMENT,
	gradeName VARCHAR(20) UNIQUE NOT NULL
)CHARSET=utf8;
INSERT INTO Grade(gradeName) VALUES('一年一班');
INSERT INTO Grade(gradeName) VALUES('一年二班');

CREATE TABLE student(
	student_id VARCHAR(20) PRIMARY KEY,
	student_name VARCHAR(50) NOT NULL,
	sex CHAR(1) DEFAULT '男',
	borndate DATE NOT NULL,
	phone VARCHAR(11),
	gradeId INT NOT NULL,
	CONSTRAINT fk_student_gradeId FOREIGN KEY(gradeId) REFERENCES grade(gradeId)
)CHARSET=utf8;
INSERT INTO student(student_id,student_name,sex,borndate,phone,gradeId)
VALUES('S1001','Gavin',DEFAULT,'1999-09-09','15612341234',1);
SELECT * FROM student;



CREATE TABLE account(
	id INT,
	money INT
)CHARSET=utf8;
INSERT INTO account(id,money) VALUES(1,10000);
INSERT INTO account(id,money) VALUES(2,1000);
SELECT * FROM account;

#1号
#1.开启事务
START TRANSACTION;

#模拟转账， 账户1给账户2转钱1000
UPDATE account SET money = money - 1000 WHERE id = 1;

UPDATE account SET money = money + 1000 WHERE id = 2;
SELECT * FROM account;
#如果事务语句都成功
COMMIT;#提交
#如果事务语句有失败
ROLLBACK;#回滚

#创建用户
CREATE USER `zhangsan` IDENTIFIED BY '123';

#用户授权
GRANT ALL ON companydb.* TO `zhangsan`;
#撤销权限
REVOKE ALL ON companydb.* FROM `zhangsan`;
#删除用户
DROP USER `zhangsan`;

#创建视图
CREATE VIEW t_empinfo
AS
SELECT EMPLOYEE_ID,FIRST_NAME,LAST_NAME,email FROM t_employees;

#使用视图
SELECT * FROM t_empinfo;


#三表连接
CREATE VIEW t_departmentInfo
AS
SELECT EMPLOYEE_ID,t_employees.DEPARTMENT_ID,t_locations.LOCATION_ID FROM t_employees
INNER JOIN t_departments
ON t_employees.`DEPARTMENT_ID` = t_departments.`DEPARTMENT_ID`
INNER JOIN t_locations
ON t_locations.`LOCATION_ID` = t_departments.`LOCATION_ID`;

SELECT * FROM t_departmentInfo;


#视图的修改
#存在则更新，反之，创建指定新视图
CREATE OR REPLACE  VIEW t_empinfo
AS
SELECT EMPLOYEE_ID,FIRST_NAME,LAST_NAME,email,JOB_ID FROM t_employees;

SELECT * FROM t_empinfo;
#视图的修改  ALTER
ALTER VIEW t_empinfo
AS
SELECT EMPLOYEE_ID,FIRST_NAME,LAST_NAME,email FROM t_employees;
#删除视图
DROP VIEW t_empinfo;

CREATE TABLE USER(
	 userId INT PRIMARY KEY AUTO_INCREMENT,
  	 username VARCHAR(20) NOT NULL,
  	 PASSWORD VARCHAR(18) NOT NULL,
     address VARCHAR(100),
     phone VARCHAR(11)
);
INSERT INTO USER(username,PASSWORD,address,phone) VALUES('张三','123','北京昌平沙河','13812345678');
INSERT INTO USER(username,PASSWORD,address,phone) VALUES('王五','5678','北京海淀','13812345141');
INSERT INTO USER(username,PASSWORD,address,phone) VALUES('赵六','123','北京朝阳','13812340987');
INSERT INTO USER(username,PASSWORD,address,phone) VALUES('田七','123','北京大兴','13812345687');

CREATE TABLE category(
  cid VARCHAR(32) PRIMARY KEY ,
  cname VARCHAR(100) NOT NULL		#分类名称
);

INSERT INTO category VALUES('c001','电器');
INSERT INTO category VALUES('c002','服饰');
INSERT INTO category VALUES('c003','化妆品');
INSERT INTO category VALUES('c004','书籍');

# 商品表
CREATE TABLE `products` (
  `pid` VARCHAR(32) PRIMARY KEY,
  `name` VARCHAR(40) ,
  `price` DOUBLE(7,2),
   category_id VARCHAR(32),
   CONSTRAINT fk_products_category_id FOREIGN KEY(category_id) REFERENCES category(cid)
);

INSERT INTO products(pid,NAME,price,category_id) VALUES('p001','联想',5000,'c001');
INSERT INTO products(pid,NAME,price,category_id) VALUES('p002','海尔',3000,'c001');
INSERT INTO products(pid,NAME,price,category_id) VALUES('p003','雷神',5000,'c001');
INSERT INTO products(pid,NAME,price,category_id) VALUES('p004','JACK JONES',800,'c002');
INSERT INTO products(pid,NAME,price,category_id) VALUES('p005','真维斯',200,'c002');
INSERT INTO products(pid,NAME,price,category_id) VALUES('p006','花花公子',440,'c002');
INSERT INTO products(pid,NAME,price,category_id) VALUES('p007','劲霸',2000,'c002');
INSERT INTO products(pid,NAME,price,category_id) VALUES('p008','香奈儿',800,'c003');
INSERT INTO products(pid,NAME,price,category_id) VALUES('p009','相宜本草',200,'c003');
INSERT INTO products(pid,NAME,price,category_id) VALUES('p010','梅明子',200,NULL);

CREATE TABLE `orders`(
  `oid` VARCHAR(32) PRIMARY KEY ,
  `totalprice` DOUBLE(12,2), #总计
  `userId` INT,
   CONSTRAINT fk_orders_userId FOREIGN KEY(userId) REFERENCES USER(userId) #外键
);
INSERT INTO orders VALUES('o6100',18000.50,1);
INSERT INTO orders VALUES('o6101',7200.35,1);
INSERT INTO orders VALUES('o6102',600.00,2);
INSERT INTO orders VALUES('o6103',1300.26,4);

CREATE TABLE orderitem(
  oid VARCHAR(32),	#订单id
  pid VARCHAR(32),	#商品id
  num INT ,         #购买商品数量
  PRIMARY KEY(oid,pid), #主键
  CONSTRAINT fk_orderitem_oid FOREIGN KEY(oid) REFERENCES orders(oid),
  CONSTRAINT fk_orderitem_pid FOREIGN KEY(pid) REFERENCES products(pid)
);
INSERT INTO orderitem VALUES('o6100','p001',1),('o6100','p002',1),('o6101','p003',1);

#查询所有用户的订单
#用户   订单表
SELECT * FROM USER INNER JOIN orders ON orders.`userId` = user.`userId`;
#查询用户id为 1 的所有订单详情
#用户   订单   订单详情
SELECT * FROM USER 
INNER JOIN orders 
ON orders.`userId` = user.`userId`
INNER JOIN orderitem
ON orders.`oid` = orderitem.`oid`
WHERE user.`userId` = 1;

#查看用户为张三的订单
#子查询 单行单列
SELECT * FROM orders WHERE userid=(SELECT userid FROM USER WHERE username='张三');

#查询出订单的价格大于800的所有用户信息。
#子查询  多行单列  枚举查询
SELECT * FROM USER WHERE userid IN
(SELECT DISTINCT userid FROM orders WHERE totalprice > 800);


#查询第一页
SELECT * FROM orders LIMIT 0,5;
#查询第二页
SELECT * FROM orders LIMIT 5,5;

SELECT * FROM t_jobs;


CREATE TABLE `users`(
	id INT PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(20) UNIQUE NOT NULL,
	PASSWORD VARCHAR(20) NOT NULL,
	phone VARCHAR(11)
)CHARSET=utf8;

INSERT INTO users(username,PASSWORD,phone) VALUES('zhangsan','1234','12345678901');
INSERT INTO users(username,PASSWORD,phone) VALUES('lisi','1234','11123456789');
SELECT * FROM users;


SELECT * FROM users WHERE username='abc' OR 1=1;#' and password= '1234'

CREATE TABLE Person(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(20) NOT NULL,
	age INT NOT NULL,
	bornDate DATE,
	email VARCHAR(20),
	address VARCHAR(20)
)CHARSET=utf8;
SELECT * FROM person;

CREATE TABLE account(
	cardNo VARCHAR(20) PRIMARY KEY,
	PASSWORD VARCHAR(20) NOT NULL,
	NAME VARCHAR(20) NOT NULL,
	balance DOUBLE NOT NULL
)CHARSET=utf8;
INSERT INTO account(cardNo,PASSWORD,NAME,balance)VALUES('6002','1234','Gavin',10000);
INSERT INTO account(cardNo,PASSWORD,NAME,balance)VALUES('6003','1234','Aaron',1000);

SELECT * FROM account;


SELECT * FROM person;
SELECT * FROM person WHERE id=6
SELECT * FROM USER




CREATE TABLE admin(
	username VARCHAR(20) PRIMARY KEY NOT NULL,
	PASSWORD VARCHAR(20) NOT NULL,
	phone VARCHAR(11) NOT NULL,
	address VARCHAR(20) NOT NULL
)CHARSET=utf8;
SELECT * FROM admin;

INSERT INTO admin(username,PASSWORD,phone,address)
VALUES('gavin','123456','12345678901','北京市昌平区');
INSERT INTO admin(username,PASSWORD,phone,address)
VALUES('aaron','123456','12345678901','北京市昌平区');



CREATE TABLE manager(
	username VARCHAR(20) PRIMARY KEY,
	PASSWORD VARCHAR(20) NOT NULL
)CHARSET=utf8;

INSERT INTO manager(username,PASSWORD)VALUES('tom','123');



CREATE DATABASE emp;
USE emp;

CREATE TABLE emp(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(20) NOT NULL,
	salary DOUBLE NOT NULL,
	age INT NOT NULL
)CHARSET=utf8;
SELECT * FROM emp;
INSERT INTO emp(NAME,salary,age) VALUES('tom1',2000,16);
INSERT INTO emp(NAME,salary,age) VALUES('marry2',3000,26);
INSERT INTO emp(NAME,salary,age) VALUES('jack3',4000,36);
INSERT INTO emp(NAME,salary,age) VALUES('tom4',2000,16);
INSERT INTO emp(NAME,salary,age) VALUES('marry5',3000,26);
INSERT INTO emp(NAME,salary,age) VALUES('jack6',4000,36);

INSERT INTO emp(NAME,salary,age) VALUES('tom7',2000,16);
INSERT INTO emp(NAME,salary,age) VALUES('marry8',3000,26);
INSERT INTO emp(NAME,salary,age) VALUES('jack9',4000,36);
INSERT INTO emp(NAME,salary,age) VALUES('tom10',2000,16);
INSERT INTO emp(NAME,salary,age) VALUES('marry11',3000,26);
INSERT INTO emp(NAME,salary,age) VALUES('jack12',4000,36);

INSERT INTO emp(NAME,salary,age) VALUES('tom13',2000,16);
INSERT INTO emp(NAME,salary,age) VALUES('marry14',3000,26);
INSERT INTO emp(NAME,salary,age) VALUES('jack15',4000,36);
INSERT INTO emp(NAME,salary,age) VALUES('tom16',2000,16);
INSERT INTO emp(NAME,salary,age) VALUES('marry17',3000,26);
INSERT INTO emp(NAME,salary,age) VALUES('jack18',4000,36);
TRUNCATE emp;

CREATE TABLE empManager(
	username VARCHAR(20) NOT NULL,
	PASSWORD VARCHAR(20) NOT NULL
)CHARSET=utf8;

INSERT INTO empManager(username,PASSWORD)VALUES('gavin','123456');

#传页码 (页码 - 1)* 行数 ,行数

SELECT * FROM emp LIMIT 0,5;#第一页
SELECT * FROM emp LIMIT 5,5;#第二页
SELECT * FROM emp LIMIT 10,5;#第三页
SELECT * FROM emp LIMIT 15,5;#第四页



CREATE DATABASE ems;
USE ems;
CREATE TABLE emp(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(20) NOT NULL,
	salary DOUBLE NOT NULL,
	age INT NOT NULL
)CHARSET=utf8;

INSERT INTO emp(NAME,salary,age)VALUES('tom',2000,18);
INSERT INTO emp(NAME,salary,age)VALUES('jack',2000,18);
INSERT INTO emp(NAME,salary,age)VALUES('aaron',2000,18);

INSERT INTO emp(NAME,salary,age)VALUES('eric',2000,18);
INSERT INTO emp(NAME,salary,age)VALUES('tom',2000,18);
INSERT INTO emp(NAME,salary,age)VALUES('tom',2000,18);
INSERT INTO emp(NAME,salary,age)VALUES('tom',2000,18);
INSERT INTO emp(NAME,salary,age)VALUES('tom',2000,18);
INSERT INTO emp(NAME,salary,age)VALUES('tom',2000,18);
INSERT INTO emp(NAME,salary,age)VALUES('tom',2000,18);
INSERT INTO emp(NAME,salary,age)VALUES('tom',2000,18);
INSERT INTO emp(NAME,salary,age)VALUES('tom',2000,18);
#管理员表
CREATE TABLE empManager(
	username VARCHAR(20) NOT NULL,
	PASSWORD VARCHAR(20) NOT NULL
)CHARSET=utf8;














