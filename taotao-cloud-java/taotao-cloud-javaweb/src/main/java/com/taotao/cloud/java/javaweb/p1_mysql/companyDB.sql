/*
 Navicat Premium Data Transfer

 Source Server         : MySqlConnection
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : companyDB

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 13/12/2019 18:20:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_countries
-- ----------------------------
DROP TABLE IF EXISTS `t_countries`;
CREATE TABLE `t_countries` (
  `COUNTRY_ID` varchar(255) NOT NULL,
  `COUNTRY_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`COUNTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of t_countries
-- ----------------------------
BEGIN;
INSERT INTO `t_countries` VALUES ('AR', 'Argentina');
INSERT INTO `t_countries` VALUES ('AU', 'Australia');
INSERT INTO `t_countries` VALUES ('BE', 'Belgium');
INSERT INTO `t_countries` VALUES ('BR', 'Brazil');
INSERT INTO `t_countries` VALUES ('CA', 'Canada');
INSERT INTO `t_countries` VALUES ('CH', 'Switzerland');
INSERT INTO `t_countries` VALUES ('CN', 'China');
INSERT INTO `t_countries` VALUES ('DE', 'Germany');
INSERT INTO `t_countries` VALUES ('DK', 'Denmark');
INSERT INTO `t_countries` VALUES ('EG', 'Egypt');
INSERT INTO `t_countries` VALUES ('FR', 'France');
INSERT INTO `t_countries` VALUES ('HK', 'HongKong');
INSERT INTO `t_countries` VALUES ('IL', 'Israel');
INSERT INTO `t_countries` VALUES ('IN', 'India');
INSERT INTO `t_countries` VALUES ('IT', 'Italy');
INSERT INTO `t_countries` VALUES ('JP', 'Japan');
INSERT INTO `t_countries` VALUES ('KW', 'Kuwait');
INSERT INTO `t_countries` VALUES ('MX', 'Mexico');
INSERT INTO `t_countries` VALUES ('NG', 'Nigeria');
INSERT INTO `t_countries` VALUES ('NL', 'Netherlands');
INSERT INTO `t_countries` VALUES ('SG', 'Singapore');
INSERT INTO `t_countries` VALUES ('UK', 'United Kingdom');
INSERT INTO `t_countries` VALUES ('US', 'United States of America');
INSERT INTO `t_countries` VALUES ('ZM', 'Zambia');
INSERT INTO `t_countries` VALUES ('ZW', 'Zimbabwe');
COMMIT;

-- ----------------------------
-- Table structure for t_departments
-- ----------------------------
DROP TABLE IF EXISTS `t_departments`;
CREATE TABLE `t_departments` (
  `DEPARTMENT_ID` varchar(255) NOT NULL,
  `DEPARTMENT_NAME` varchar(255) DEFAULT NULL,
  `MANAGER_ID` varchar(255) DEFAULT NULL,
  `LOCATION_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`DEPARTMENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of t_departments
-- ----------------------------
BEGIN;
INSERT INTO `t_departments` VALUES ('10', 'Administration', '200', '1700');
INSERT INTO `t_departments` VALUES ('100', 'Finance', '108', '1700');
INSERT INTO `t_departments` VALUES ('110', 'Accounting', '205', '1700');
INSERT INTO `t_departments` VALUES ('120', 'Treasury', NULL, '1700');
INSERT INTO `t_departments` VALUES ('130', 'Corporate Tax', NULL, '1700');
INSERT INTO `t_departments` VALUES ('140', 'Control And Credit', NULL, '1700');
INSERT INTO `t_departments` VALUES ('150', 'Shareholder Services', NULL, '1700');
INSERT INTO `t_departments` VALUES ('160', 'Benefits', NULL, '1700');
INSERT INTO `t_departments` VALUES ('170', 'Manufacturing', NULL, '1700');
INSERT INTO `t_departments` VALUES ('180', 'Construction', NULL, '1700');
INSERT INTO `t_departments` VALUES ('190', 'Contracting', NULL, '1700');
INSERT INTO `t_departments` VALUES ('20', 'Marketing', '201', '1800');
INSERT INTO `t_departments` VALUES ('200', 'Operations', NULL, '1700');
INSERT INTO `t_departments` VALUES ('210', 'IT Support', NULL, '1700');
INSERT INTO `t_departments` VALUES ('220', 'NOC', NULL, '1700');
INSERT INTO `t_departments` VALUES ('230', 'IT Helpdesk', NULL, '1700');
INSERT INTO `t_departments` VALUES ('240', 'Government Sales', NULL, '1700');
INSERT INTO `t_departments` VALUES ('250', 'Retail Sales', NULL, '1700');
INSERT INTO `t_departments` VALUES ('260', 'Recruiting', NULL, '1700');
INSERT INTO `t_departments` VALUES ('270', 'Payroll', NULL, '1700');
INSERT INTO `t_departments` VALUES ('30', 'Purchasing', '114', '1700');
INSERT INTO `t_departments` VALUES ('40', 'Human Resources', '203', '2400');
INSERT INTO `t_departments` VALUES ('50', 'Shipping', '121', '1500');
INSERT INTO `t_departments` VALUES ('60', 'IT', '103', '1400');
INSERT INTO `t_departments` VALUES ('70', 'Public Relations', '204', '2700');
INSERT INTO `t_departments` VALUES ('80', 'Sales', '145', '2500');
INSERT INTO `t_departments` VALUES ('90', 'Executive', '100', '1700');
COMMIT;

-- ----------------------------
-- Table structure for t_employees
-- ----------------------------
DROP TABLE IF EXISTS `t_employees`;
CREATE TABLE `t_employees` (
  `EMPLOYEE_ID` varchar(255) NOT NULL,
  `FIRST_NAME` varchar(255) DEFAULT NULL,
  `LAST_NAME` varchar(255) DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `PHONE_NUMBER` varchar(255) DEFAULT NULL,
  `HIRE_DATE` varchar(255) DEFAULT NULL,
  `JOB_ID` varchar(255) DEFAULT NULL,
  `SALARY` varchar(255) DEFAULT NULL,
  `COMMISSION_PCT` varchar(255) DEFAULT NULL,
  `MANAGER_ID` varchar(255) DEFAULT NULL,
  `DEPARTMENT_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`EMPLOYEE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of t_employees
-- ----------------------------
BEGIN;
INSERT INTO `t_employees` VALUES ('100', 'Steven', 'King', 'SKING', '515.123.4567', '1987-06-17', 'AD_PRES', '24000', NULL, NULL, '90');
INSERT INTO `t_employees` VALUES ('101', 'Neena', 'Kochhar', 'NKOCHHAR', '515.123.4568', '1989-09-21', 'AD_VP', '17000', NULL, '100', '90');
INSERT INTO `t_employees` VALUES ('102', 'Lex', 'De Haan', 'LDEHAAN', '515.123.4569', '1993-01-13', 'AD_VP', '17000', NULL, '100', '90');
INSERT INTO `t_employees` VALUES ('103', 'Alexander', 'Hunold', 'AHUNOLD', '590.423.4567', '1990-01-03', 'IT_PROG', '9000', NULL, '102', '60');
INSERT INTO `t_employees` VALUES ('104', 'Bruce', 'Ernst', 'BERNST', '590.423.4568', '1991-05-21', 'IT_PROG', '6000', NULL, '103', '60');
INSERT INTO `t_employees` VALUES ('105', 'David', 'Austin', 'DAUSTIN', '590.423.4569', '1997-06-25', 'IT_PROG', '4800', NULL, '103', '60');
INSERT INTO `t_employees` VALUES ('106', 'Valli', 'Pataballa', 'VPATABAL', '590.423.4560', '1998-02-05', 'IT_PROG', '4800', NULL, '103', '60');
INSERT INTO `t_employees` VALUES ('107', 'Diana', 'Lorentz', 'DLORENTZ', '590.423.5567', '1999-02-07', 'IT_PROG', '4200', NULL, '103', '60');
INSERT INTO `t_employees` VALUES ('108', 'Nancy', 'Greenberg', 'NGREENBE', '515.124.4569', '1994-08-17', 'FI_MGR', '12000', NULL, '101', '100');
INSERT INTO `t_employees` VALUES ('109', 'Daniel', 'Faviet', 'DFAVIET', '515.124.4169', '1994-08-16', 'FI_ACCOUNT', '9000', NULL, '108', '100');
INSERT INTO `t_employees` VALUES ('110', 'John', 'Chen', 'JCHEN', '515.124.4269', '1997-09-28', 'FI_ACCOUNT', '8200', NULL, '108', '100');
INSERT INTO `t_employees` VALUES ('111', 'Ismael', 'Sciarra', 'ISCIARRA', '515.124.4369', '1997-09-30', 'FI_ACCOUNT', '7700', NULL, '108', '100');
INSERT INTO `t_employees` VALUES ('112', 'Jose Manuel', 'Urman', 'JMURMAN', '515.124.4469', '1998-03-07', 'FI_ACCOUNT', '7800', NULL, '108', '100');
INSERT INTO `t_employees` VALUES ('113', 'Luis', 'Popp', 'LPOPP', '515.124.4567', '1999-12-07', 'FI_ACCOUNT', '6900', NULL, '108', '100');
INSERT INTO `t_employees` VALUES ('114', 'Den', 'Raphaely', 'DRAPHEAL', '515.127.4561', '1994-12-07', 'PU_MAN', '11000', NULL, '100', '30');
INSERT INTO `t_employees` VALUES ('115', 'Alexander', 'Khoo', 'AKHOO', '515.127.4562', '1995-05-18', 'PU_CLERK', '3100', NULL, '114', '30');
INSERT INTO `t_employees` VALUES ('116', 'Shelli', 'Baida', 'SBAIDA', '515.127.4563', '1997-12-24', 'PU_CLERK', '2900', NULL, '114', '30');
INSERT INTO `t_employees` VALUES ('117', 'Sigal', 'Tobias', 'STOBIAS', '515.127.4564', '1997-07-24', 'PU_CLERK', '2800', NULL, '114', '30');
INSERT INTO `t_employees` VALUES ('118', 'Guy', 'Himuro', 'GHIMURO', '515.127.4565', '1998-11-15', 'PU_CLERK', '2600', NULL, '114', '30');
INSERT INTO `t_employees` VALUES ('119', 'Karen', 'Colmenares', 'KCOLMENA', '515.127.4566', '1999-08-10', 'PU_CLERK', '2500', NULL, '114', '30');
INSERT INTO `t_employees` VALUES ('120', 'Matthew', 'Weiss', 'MWEISS', '650.123.1234', '1996-07-18', 'ST_MAN', '8000', NULL, '100', '50');
INSERT INTO `t_employees` VALUES ('121', 'Adam', 'Fripp', 'AFRIPP', '650.123.2234', '1997-04-10', 'ST_MAN', '8200', NULL, '100', '50');
INSERT INTO `t_employees` VALUES ('122', 'Payam', 'Kaufling', 'PKAUFLIN', '650.123.3234', '1995-05-01', 'ST_MAN', '7900', NULL, '100', '50');
INSERT INTO `t_employees` VALUES ('123', 'Shanta', 'Vollman', 'SVOLLMAN', '650.123.4234', '1997-10-10', 'ST_MAN', '6500', NULL, '100', '50');
INSERT INTO `t_employees` VALUES ('124', 'Kevin', 'Mourgos', 'KMOURGOS', '650.123.5234', '1999-11-16', 'ST_MAN', '5800', NULL, '100', '50');
INSERT INTO `t_employees` VALUES ('125', 'Julia', 'Nayer', 'JNAYER', '650.124.1214', '1997-07-16', 'ST_CLERK', '3200', NULL, '120', '50');
INSERT INTO `t_employees` VALUES ('126', 'Irene', 'Mikkilineni', 'IMIKKILI', '650.124.1224', '1998-09-28', 'ST_CLERK', '2700', NULL, '120', '50');
INSERT INTO `t_employees` VALUES ('127', 'James', 'Landry', 'JLANDRY', '650.124.1334', '1999-01-14', 'ST_CLERK', '2400', NULL, '120', '50');
INSERT INTO `t_employees` VALUES ('128', 'Steven', 'Markle', 'SMARKLE', '650.124.1434', '2000-03-08', 'ST_CLERK', '2200', NULL, '120', '50');
INSERT INTO `t_employees` VALUES ('129', 'Laura', 'Bissot', 'LBISSOT', '650.124.5234', '1997-08-20', 'ST_CLERK', '3300', NULL, '121', '50');
INSERT INTO `t_employees` VALUES ('130', 'Mozhe', 'Atkinson', 'MATKINSO', '650.124.6234', '1997-10-30', 'ST_CLERK', '2800', NULL, '121', '50');
INSERT INTO `t_employees` VALUES ('131', 'James', 'Marlow', 'JAMRLOW', '650.124.7234', '1997-02-16', 'ST_CLERK', '2500', NULL, '121', '50');
INSERT INTO `t_employees` VALUES ('132', 'TJ', 'Olson', 'TJOLSON', '650.124.8234', '1999-04-10', 'ST_CLERK', '2100', NULL, '121', '50');
INSERT INTO `t_employees` VALUES ('133', 'Jason', 'Mallin', 'JMALLIN', '650.127.1934', '1996-06-14', 'ST_CLERK', '3300', NULL, '122', '50');
INSERT INTO `t_employees` VALUES ('134', 'Michael', 'Rogers', 'MROGERS', '650.127.1834', '1998-08-26', 'ST_CLERK', '2900', NULL, '122', '50');
INSERT INTO `t_employees` VALUES ('135', 'Ki', 'Gee', 'KGEE', '650.127.1734', '1999-12-12', 'ST_CLERK', '2400', NULL, '122', '50');
INSERT INTO `t_employees` VALUES ('136', 'Hazel', 'Philtanker', 'HPHILTAN', '650.127.1634', '2000-02-06', 'ST_CLERK', '2200', NULL, '122', '50');
INSERT INTO `t_employees` VALUES ('137', 'Renske', 'Ladwig', 'RLADWIG', '650.121.1234', '1995-07-14', 'ST_CLERK', '3600', NULL, '123', '50');
INSERT INTO `t_employees` VALUES ('138', 'Stephen', 'Stiles', 'SSTILES', '650.121.2034', '1997-10-26', 'ST_CLERK', '3200', NULL, '123', '50');
INSERT INTO `t_employees` VALUES ('139', 'John', 'Seo', 'JSEO', '650.121.2019', '1998-02-12', 'ST_CLERK', '2700', NULL, '123', '50');
INSERT INTO `t_employees` VALUES ('140', 'Joshua', 'Patel', 'JPATEL', '650.121.1834', '1998-04-06', 'ST_CLERK', '2500', NULL, '123', '50');
INSERT INTO `t_employees` VALUES ('141', 'Trenna', 'Rajs', 'TRAJS', '650.121.8009', '1995-10-17', 'ST_CLERK', '3500', NULL, '124', '50');
INSERT INTO `t_employees` VALUES ('142', 'Curtis', 'Davies', 'CDAVIES', '650.121.2994', '1997-01-29', 'ST_CLERK', '3100', NULL, '124', '50');
INSERT INTO `t_employees` VALUES ('143', 'Randall', 'Matos', 'RMATOS', '650.121.2874', '1998-03-15', 'ST_CLERK', '2600', NULL, '124', '50');
INSERT INTO `t_employees` VALUES ('144', 'Peter', 'Vargas', 'PVARGAS', '650.121.2004', '1998-07-09', 'ST_CLERK', '2500', NULL, '124', '50');
INSERT INTO `t_employees` VALUES ('145', 'John', 'Russell', 'JRUSSEL', '011.44.1344.429268', '1996-10-01', 'SA_MAN', '14000', '0.4', '100', '80');
INSERT INTO `t_employees` VALUES ('146', 'Karen', 'Partners', 'KPARTNER', '011.44.1344.467268', '1997-01-05', 'SA_MAN', '13500', '0.3', '100', '80');
INSERT INTO `t_employees` VALUES ('147', 'Alberto', 'Errazuriz', 'AERRAZUR', '011.44.1344.429278', '1997-03-10', 'SA_MAN', '12000', '0.3', '100', '80');
INSERT INTO `t_employees` VALUES ('148', 'Gerald', 'Cambrault', 'GCAMBRAU', '011.44.1344.619268', '1999-10-15', 'SA_MAN', '11000', '0.3', '100', '80');
INSERT INTO `t_employees` VALUES ('149', 'Eleni', 'Zlotkey', 'EZLOTKEY', '011.44.1344.429018', '2000-01-29', 'SA_MAN', '10500', '0.2', '100', '80');
INSERT INTO `t_employees` VALUES ('150', 'Peter', 'Tucker', 'PTUCKER', '011.44.1344.129268', '1997-01-30', 'SA_REP', '10000', '0.3', '145', '80');
INSERT INTO `t_employees` VALUES ('151', 'David', 'Bernstein', 'DBERNSTE', '011.44.1344.345268', '1997-03-24', 'SA_REP', '9500', '0.25', '145', '80');
INSERT INTO `t_employees` VALUES ('152', 'Peter', 'Hall', 'PHALL', '011.44.1344.478968', '1997-08-20', 'SA_REP', '9000', '0.25', '145', '80');
INSERT INTO `t_employees` VALUES ('153', 'Christopher', 'Olsen', 'COLSEN', '011.44.1344.498718', '1998-03-30', 'SA_REP', '8000', '0.2', '145', '80');
INSERT INTO `t_employees` VALUES ('154', 'Nanette', 'Cambrault', 'NCAMBRAU', '011.44.1344.987668', '1998-12-09', 'SA_REP', '7500', '0.2', '145', '80');
INSERT INTO `t_employees` VALUES ('155', 'Oliver', 'Tuvault', 'OTUVAULT', '011.44.1344.486508', '1999-11-23', 'SA_REP', '7000', '0.15', '145', '80');
INSERT INTO `t_employees` VALUES ('156', 'Janette', 'King', 'JKING', '011.44.1345.429268', '1996-01-30', 'SA_REP', '10000', '0.35', '146', '80');
INSERT INTO `t_employees` VALUES ('157', 'Patrick', 'Sully', 'PSULLY', '011.44.1345.929268', '1996-03-04', 'SA_REP', '9500', '0.35', '146', '80');
INSERT INTO `t_employees` VALUES ('158', 'Allan', 'McEwen', 'AMCEWEN', '011.44.1345.829268', '1996-08-01', 'SA_REP', '9000', '0.35', '146', '80');
INSERT INTO `t_employees` VALUES ('159', 'Lindsey', 'Smith', 'LSMITH', '011.44.1345.729268', '1997-03-10', 'SA_REP', '8000', '0.3', '146', '80');
INSERT INTO `t_employees` VALUES ('160', 'Louise', 'Doran', 'LDORAN', '011.44.1345.629268', '1997-12-15', 'SA_REP', '7500', '0.3', '146', '80');
INSERT INTO `t_employees` VALUES ('161', 'Sarath', 'Sewall', 'SSEWALL', '011.44.1345.529268', '1998-11-03', 'SA_REP', '7000', '0.25', '146', '80');
INSERT INTO `t_employees` VALUES ('162', 'Clara', 'Vishney', 'CVISHNEY', '011.44.1346.129268', '1997-11-11', 'SA_REP', '10500', '0.25', '147', '80');
INSERT INTO `t_employees` VALUES ('163', 'Danielle', 'Greene', 'DGREENE', '011.44.1346.229268', '1999-03-19', 'SA_REP', '9500', '0.15', '147', '80');
INSERT INTO `t_employees` VALUES ('164', 'Mattea', 'Marvins', 'MMARVINS', '011.44.1346.329268', '2000-01-24', 'SA_REP', '7200', '0.1', '147', '80');
INSERT INTO `t_employees` VALUES ('165', 'David', 'Lee', 'DLEE', '011.44.1346.529268', '2000-02-23', 'SA_REP', '6800', '0.1', '147', '80');
INSERT INTO `t_employees` VALUES ('166', 'Sundar', 'Ande', 'SANDE', '011.44.1346.629268', '2000-03-24', 'SA_REP', '6400', '0.1', '147', '80');
INSERT INTO `t_employees` VALUES ('167', 'Amit', 'Banda', 'ABANDA', '011.44.1346.729268', '2000-04-21', 'SA_REP', '6200', '0.1', '147', '80');
INSERT INTO `t_employees` VALUES ('168', 'Lisa', 'Ozer', 'LOZER', '011.44.1343.929268', '1997-03-11', 'SA_REP', '11500', '0.25', '148', '80');
INSERT INTO `t_employees` VALUES ('169', 'Harrison', 'Bloom', 'HBLOOM', '011.44.1343.829268', '1998-03-23', 'SA_REP', '10000', '0.2', '148', '80');
INSERT INTO `t_employees` VALUES ('170', 'Tayler', 'Fox', 'TFOX', '011.44.1343.729268', '1998-01-24', 'SA_REP', '9600', '0.2', '148', '80');
INSERT INTO `t_employees` VALUES ('171', 'William', 'Smith', 'WSMITH', '011.44.1343.629268', '1999-02-23', 'SA_REP', '7400', '0.15', '148', '80');
INSERT INTO `t_employees` VALUES ('172', 'Elizabeth', 'Bates', 'EBATES', '011.44.1343.529268', '1999-03-24', 'SA_REP', '7300', '0.15', '148', '80');
INSERT INTO `t_employees` VALUES ('173', 'Sundita', 'Kumar', 'SKUMAR', '011.44.1343.329268', '2000-04-21', 'SA_REP', '6100', '0.1', '148', '80');
INSERT INTO `t_employees` VALUES ('174', 'Ellen', 'Abel', 'EABEL', '011.44.1644.429267', '1996-05-11', 'SA_REP', '11000', '0.3', '149', '80');
INSERT INTO `t_employees` VALUES ('175', 'Alyssa', 'Hutton', 'AHUTTON', '011.44.1644.429266', '1997-03-19', 'SA_REP', '8800', '0.25', '149', '80');
INSERT INTO `t_employees` VALUES ('176', 'Jonathon', 'Taylor', 'JTAYLOR', '011.44.1644.429265', '1998-03-24', 'SA_REP', '8600', '0.2', '149', '80');
INSERT INTO `t_employees` VALUES ('177', 'Jack', 'Livingston', 'JLIVINGS', '011.44.1644.429264', '1998-04-23', 'SA_REP', '8400', '0.2', '149', '80');
INSERT INTO `t_employees` VALUES ('178', 'Kimberely', 'Grant', 'KGRANT', '011.44.1644.429263', '1999-05-24', 'SA_REP', '7000', '0.15', '149', NULL);
INSERT INTO `t_employees` VALUES ('179', 'Charles', 'Johnson', 'CJOHNSON', '011.44.1644.429262', '2000-01-04', 'SA_REP', '6200', '0.1', '149', '80');
INSERT INTO `t_employees` VALUES ('180', 'Winston', 'Taylor', 'WTAYLOR', '650.507.9876', '1998-01-24', 'SH_CLERK', '3200', NULL, '120', '50');
INSERT INTO `t_employees` VALUES ('181', 'Jean', 'Fleaur', 'JFLEAUR', '650.507.9877', '1998-02-23', 'SH_CLERK', '3100', NULL, '120', '50');
INSERT INTO `t_employees` VALUES ('182', 'Martha', 'Sullivan', 'MSULLIVA', '650.507.9878', '1999-06-21', 'SH_CLERK', '2500', NULL, '120', '50');
INSERT INTO `t_employees` VALUES ('183', 'Girard', 'Geoni', 'GGEONI', '650.507.9879', '2000-02-03', 'SH_CLERK', '2800', NULL, '120', '50');
INSERT INTO `t_employees` VALUES ('184', 'Nandita', 'Sarchand', 'NSARCHAN', '650.509.1876', '1996-01-27', 'SH_CLERK', '4200', NULL, '121', '50');
INSERT INTO `t_employees` VALUES ('185', 'Alexis', 'Bull', 'ABULL', '650.509.2876', '1997-02-20', 'SH_CLERK', '4100', NULL, '121', '50');
INSERT INTO `t_employees` VALUES ('186', 'Julia', 'Dellinger', 'JDELLING', '650.509.3876', '1998-06-24', 'SH_CLERK', '3400', NULL, '121', '50');
INSERT INTO `t_employees` VALUES ('187', 'Anthony', 'Cabrio', 'ACABRIO', '650.509.4876', '1999-02-07', 'SH_CLERK', '3000', NULL, '121', '50');
INSERT INTO `t_employees` VALUES ('188', 'Kelly', 'Chung', 'KCHUNG', '650.505.1876', '1997-06-14', 'SH_CLERK', '3800', NULL, '122', '50');
INSERT INTO `t_employees` VALUES ('189', 'Jennifer', 'Dilly', 'JDILLY', '650.505.2876', '1997-08-13', 'SH_CLERK', '3600', NULL, '122', '50');
INSERT INTO `t_employees` VALUES ('190', 'Timothy', 'Gates', 'TGATES', '650.505.3876', '1998-07-11', 'SH_CLERK', '2900', NULL, '122', '50');
INSERT INTO `t_employees` VALUES ('191', 'Randall', 'Perkins', 'RPERKINS', '650.505.4876', '1999-12-19', 'SH_CLERK', '2500', NULL, '122', '50');
INSERT INTO `t_employees` VALUES ('192', 'Sarah', 'Bell', 'SBELL', '650.501.1876', '1996-02-04', 'SH_CLERK', '4000', NULL, '123', '50');
INSERT INTO `t_employees` VALUES ('193', 'Britney', 'Everett', 'BEVERETT', '650.501.2876', '1997-03-03', 'SH_CLERK', '3900', NULL, '123', '50');
INSERT INTO `t_employees` VALUES ('194', 'Samuel', 'McCain', 'SMCCAIN', '650.501.3876', '1998-07-01', 'SH_CLERK', '3200', NULL, '123', '50');
INSERT INTO `t_employees` VALUES ('195', 'Vance', 'Jones', 'VJONES', '650.501.4876', '1999-03-17', 'SH_CLERK', '2800', NULL, '123', '50');
INSERT INTO `t_employees` VALUES ('196', 'Alana', 'Walsh', 'AWALSH', '650.507.9811', '1998-04-24', 'SH_CLERK', '3100', NULL, '124', '50');
INSERT INTO `t_employees` VALUES ('197', 'Kevin', 'Feeney', 'KFEENEY', '650.507.9822', '1998-05-23', 'SH_CLERK', '3000', NULL, '124', '50');
INSERT INTO `t_employees` VALUES ('198', 'Donald', 'OConnell', 'DOCONNEL', '650.507.9833', '1999-06-21', 'SH_CLERK', '2600', NULL, '124', '50');
INSERT INTO `t_employees` VALUES ('199', 'Douglas', 'Grant', 'DGRANT', '650.507.9844', '2000-01-13', 'SH_CLERK', '2600', NULL, '124', '50');
INSERT INTO `t_employees` VALUES ('200', 'Jennifer', 'Whalen', 'JWHALEN', '515.123.4444', '1987-09-17', 'AD_ASST', '4400', NULL, '101', '10');
INSERT INTO `t_employees` VALUES ('201', 'Michael', 'Hartstein', 'MHARTSTE', '515.123.5555', '1996-02-17', 'MK_MAN', '13000', NULL, '100', '20');
INSERT INTO `t_employees` VALUES ('202', 'Pat', 'Fay', 'PFAY', '603.123.6666', '1997-08-17', 'MK_REP', '6000', NULL, '201', '20');
INSERT INTO `t_employees` VALUES ('203', 'Susan', 'Mavris', 'SMAVRIS', '515.123.7777', '1994-06-07', 'HR_REP', '6500', NULL, '101', '40');
INSERT INTO `t_employees` VALUES ('204', 'Hermann', 'Baer', 'HBAER', '515.123.8888', '1994-06-07', 'PR_REP', '10000', NULL, '101', '70');
INSERT INTO `t_employees` VALUES ('205', 'Shelley', 'Higgins', 'SHIGGINS', '515.123.8080', '1994-06-07', 'AC_MGR', '12000', NULL, '101', '110');
INSERT INTO `t_employees` VALUES ('206', 'William', 'Gietz', 'WGIETZ', '515.123.8181', '1994-06-07', 'AC_ACCOUNT', '8300', NULL, '205', '110');
COMMIT;

-- ----------------------------
-- Table structure for t_jobs
-- ----------------------------
DROP TABLE IF EXISTS `t_jobs`;
CREATE TABLE `t_jobs` (
  `JOB_ID` varchar(255) NOT NULL,
  `JOB_TITLE` varchar(255) DEFAULT NULL,
  `MIN_SALARY` varchar(255) DEFAULT NULL,
  `MAX_SALARY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`JOB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of t_jobs
-- ----------------------------
BEGIN;
INSERT INTO `t_jobs` VALUES ('AC_ACCOUNT', 'Public Accountant', '4200', '9000');
INSERT INTO `t_jobs` VALUES ('AC_MGR', 'Accounting Manager', '8200', '16000');
INSERT INTO `t_jobs` VALUES ('AD_ASST', 'Administration Assistant', '3000', '6000');
INSERT INTO `t_jobs` VALUES ('AD_PRES', 'President', '20000', '40000');
INSERT INTO `t_jobs` VALUES ('AD_VP', 'Administration Vice President', '15000', '30000');
INSERT INTO `t_jobs` VALUES ('FI_ACCOUNT', 'Accountant', '4200', '9000');
INSERT INTO `t_jobs` VALUES ('FI_MGR', 'Finance Manager', '8200', '16000');
INSERT INTO `t_jobs` VALUES ('HR_REP', 'Human Resources Representative', '4000', '9000');
INSERT INTO `t_jobs` VALUES ('IT_PROG', 'Programmer', '4000', '10000');
INSERT INTO `t_jobs` VALUES ('MK_MAN', 'Marketing Manager', '9000', '15000');
INSERT INTO `t_jobs` VALUES ('MK_REP', 'Marketing Representative', '4000', '9000');
INSERT INTO `t_jobs` VALUES ('PR_REP', 'Public Relations Representative', '4500', '10500');
INSERT INTO `t_jobs` VALUES ('PU_CLERK', 'Purchasing Clerk', '2500', '5500');
INSERT INTO `t_jobs` VALUES ('PU_MAN', 'Purchasing Manager', '8000', '15000');
INSERT INTO `t_jobs` VALUES ('SA_MAN', 'Sales Manager', '10000', '20000');
INSERT INTO `t_jobs` VALUES ('SA_REP', 'Sales Representative', '6000', '12000');
INSERT INTO `t_jobs` VALUES ('SH_CLERK', 'Shipping Clerk', '2500', '5500');
INSERT INTO `t_jobs` VALUES ('ST_CLERK', 'Stock Clerk', '2000', '5000');
INSERT INTO `t_jobs` VALUES ('ST_MAN', 'Stock Manager', '5500', '8500');
COMMIT;

-- ----------------------------
-- Table structure for t_locations
-- ----------------------------
DROP TABLE IF EXISTS `t_locations`;
CREATE TABLE `t_locations` (
  `LOCATION_ID` varchar(255) NOT NULL,
  `STREET_ADDRESS` varchar(255) DEFAULT NULL,
  `POSTAL_CODE` varchar(255) DEFAULT NULL,
  `CITY` varchar(255) DEFAULT NULL,
  `STATE_PROVINCE` varchar(255) DEFAULT NULL,
  `COUNTRY_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`LOCATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of t_locations
-- ----------------------------
BEGIN;
INSERT INTO `t_locations` VALUES ('1000', '1297 Via Cola di Rie', '00989', 'Roma', NULL, 'IT');
INSERT INTO `t_locations` VALUES ('1100', '93091 Calle della Testa', '10934', 'Venice', NULL, 'IT');
INSERT INTO `t_locations` VALUES ('1200', '2017 Shinjuku-ku', '1689', 'Tokyo', 'Tokyo Prefecture', 'JP');
INSERT INTO `t_locations` VALUES ('1300', '9450 Kamiya-cho', '6823', 'Hiroshima', NULL, 'JP');
INSERT INTO `t_locations` VALUES ('1400', '2014 Jabberwocky Rd', '26192', 'Southlake', 'Texas', 'US');
INSERT INTO `t_locations` VALUES ('1500', '2011 Interiors Blvd', '99236', 'South San Francisco', 'California', 'US');
INSERT INTO `t_locations` VALUES ('1600', '2007 Zagora St', '50090', 'South Brunswick', 'New Jersey', 'US');
INSERT INTO `t_locations` VALUES ('1700', '2004 Charade Rd', '98199', 'Seattle', 'Washington', 'US');
INSERT INTO `t_locations` VALUES ('1800', '147 Spadina Ave', 'M5V 2L7', 'Toronto', 'Ontario', 'CA');
INSERT INTO `t_locations` VALUES ('1900', '6092 Boxwood St', 'YSW 9T2', 'Whitehorse', 'Yukon', 'CA');
INSERT INTO `t_locations` VALUES ('2000', '40-5-12 Laogianggen', '190518', 'Beijing', NULL, 'CN');
INSERT INTO `t_locations` VALUES ('2100', '1298 Vileparle (E)', '490231', 'Bombay', 'Maharashtra', 'IN');
INSERT INTO `t_locations` VALUES ('2200', '12-98 Victoria Street', '2901', 'Sydney', 'New South Wales', 'AU');
INSERT INTO `t_locations` VALUES ('2300', '198 Clementi North', '540198', 'Singapore', NULL, 'SG');
INSERT INTO `t_locations` VALUES ('2400', '8204 Arthur St', NULL, 'London', NULL, 'UK');
INSERT INTO `t_locations` VALUES ('2500', 'Magdalen Centre, The Oxford Science Park', 'OX9 9ZB', 'Oxford', 'Oxford', 'UK');
INSERT INTO `t_locations` VALUES ('2600', '9702 Chester Road', '09629850293', 'Stretford', 'Manchester', 'UK');
INSERT INTO `t_locations` VALUES ('2700', 'Schwanthalerstr. 7031', '80925', 'Munich', 'Bavaria', 'DE');
INSERT INTO `t_locations` VALUES ('2800', 'Rua Frei Caneca 1360', '01307-002', 'Sao Paulo', 'Sao Paulo', 'BR');
INSERT INTO `t_locations` VALUES ('2900', '20 Rue des Corps-Saints', '1730', 'Geneva', 'Geneve', 'CH');
INSERT INTO `t_locations` VALUES ('3000', 'Murtenstrasse 921', '3095', 'Bern', 'BE', 'CH');
INSERT INTO `t_locations` VALUES ('3100', 'Pieter Breughelstraat 837', '3029SK', 'Utrecht', 'Utrecht', 'NL');
INSERT INTO `t_locations` VALUES ('3200', 'Mariano Escobedo 9991', '11932', 'Mexico City', 'Distrito Federal,', 'MX');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
