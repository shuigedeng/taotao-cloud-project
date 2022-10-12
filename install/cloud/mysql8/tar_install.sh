https://dev.mysql.com/downloads/mysql/

tar -zvxf mysql-5.7.22-linux-glibc2.12-x86_64.tar.gz

mv mysql-5.7.22-linux-glibc2.12-x86_64 mysql

groupadd mysql
useradd -r -g mysql mysql （-r表示建立系统账号，-g表示指定所属群组）

cd /
mkdir -p data （-p表示如果没有才创建）
cd data/
mkdir -p mysql

chown mysql:mysql -R /data/mysql

vi /etc/my.cnf
[mysqld]
bind-address=0.0.0.0
port=3306
user=mysql
basedir=/usr/local/mysql
datadir=/data/mysql
socket=/tmp/mysql.sock
log-error=/data/mysql/mysql.err
pid-file=/data/mysql/mysql.pid
#character config
character_set_server=utf8mb4
symbolic-links=0

sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
basedir = /opt/module/mysql
datadir = /opt/module/mysql/data
port = 3306
socket = /tmp/mysql.sock
character-set-server=utf8
log-error = /opt/module/mysql/data/mysqld.log
pid-file = /opt/module/mysql/data/mysqld.pid


cd /usr/local/mysql/bin/
./mysqld --defaults-file=/etc/my.cnf --basedir=/usr/local/mysql/ --datadir=/data/mysql/ --user=mysql --initialize
vi /data/mysql/mysql.err

service mysql start

mysql -uroot -p

SET PASSWORD = PASSWORD('root');
ALTER USER 'root'@'localhost' PASSWORD EXPIRE NEVER;（保证密码永不过期）
flush privileges;（MySQL用户数据和权限有修改后，希望在"不重启MySQL服务"的情况下直接生效，那么就需要执行这个命令。）


查看是否有mysql服务
ll /etc/init.d/ | grep mysql
如果上面没有继续执行
find / -name mysql.server
将找到的路径复制到 /etc/init.d/mysql路径中
cp /usr/local/mysql/support-files/mysql.server  /etc/init.d/mysql

增加软连接：ln -s /usr/local/mysql/bin/mysql /usr/bin
找到mysql/bin的位置，正常情况下是 /usr/local/mysql/bin在最后添加：export PATH=$PATH:/usr/local/mysql/bin 保存退出。
source /etc/profile
