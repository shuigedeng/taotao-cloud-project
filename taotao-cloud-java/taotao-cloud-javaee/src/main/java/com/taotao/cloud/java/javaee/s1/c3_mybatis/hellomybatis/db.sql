# create database mybatis_shine default charset =utf8;
# create table t_user(
#   id int primary key auto_increment,
#   username varchar(50),
#   password varchar(50),
#   gender tinyint,
#   regist_time datetime
# )default charset =utf8;

# create table t_student(
#   id varchar(32) primary key,
#   name varchar(50),
#   gender tinyint
# )default charset =utf8;

# create table t_student(
#                         id varchar(32) primary key,
#                         name varchar(50),
#                         gender tinyint
# )default charset =utf8;

create table t_departments(
    id int primary key auto_increment,
    name varchar(50),
    location varchar(100)
)default charset =utf8;

create table t_employees(
    id int primary key auto_increment,
    name varchar(50),
    salary double,
    dept_id int,
    foreign key (dept_id) references t_departments(id)
)default charset =utf8;

insert into t_departments values(1,"教学部","北京"),(2,"研发部","上海");
insert into t_employees values(1,"shine01",10000.5,1),(2,"shine02",20000.5,1),
                              (3,"张三",9000.5,2),(4,"李四",8000.5,2);

select t_departments.id ,t_departments.name,t_departments.location,
       t_employees.id emp_id,t_employees.name emp_name,t_employees.salary
from t_departments join t_employees
on t_departments.id = t_employees.dept_id
where t_departments.id=1;

select t_employees.id,t_employees.name,t_employees.salary,
       t_departments.id deptId ,t_departments.name detpName,t_departments.location

from t_employees join t_departments
on t_departments.id = t_employees.dept_id
where t_employees.id=3

# create table t_passengers(
#     id int primary key auto_increment,
#     name varchar(50),
#     sex varchar(1),
#     birthday date
# )default charset =utf8;
#
# create table t_passports(
#     id int primary key auto_increment,
#     nationality varchar(50),
#     expire date,
#     passenger_id int unique,
#     foreign key (passenger_id) references t_passengers(id)
# )default charset =utf8;
#
# insert into t_passengers values(null,'shine_01','f','2018-11-11');
# insert into t_passengers values(null,'shine_02','m','2019-12-12');
#
# insert into t_passports values(null,'China','2030-12-12',1);
# insert into t_passports values(null,'America','2035-12-12',2);
#
# select t_passengers.id,t_passengers.name,t_passengers.sex,t_passengers.birthday,
#        t_passports.id passId,t_passports.nationality,t_passports.expire
# from t_passengers join t_passports
# on t_passengers.id = t_passports.passenger_id
# where t_passengers.id=2;
#
# select t_passports.id,t_passports.nationality,t_passports.expire,
#        t_passengers.id passenger_id,t_passengers.name,t_passengers.sex,t_passengers.birthday
# from t_passports join t_passengers
# on t_passengers.id = t_passports.passenger_id
# where t_passports.id=1;

