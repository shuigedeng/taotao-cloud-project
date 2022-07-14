###########################################
sudo dnf install @mysql

#启动MySQL服务并开机自启
sudo systemctl enable --now mysqld
sudo systemctl disable mysqld

sudo systemctl status mysqld

mysql_secure_installation

# 1.输入y ，回车进入该配置
# 2.选择密码验证策略等级， 我这里选择0 （low），回车
# 3.输入新密码两次
# 4.确认是否继续使用提供的密码？输入y ，回车
# 5.移除匿名用户 输入n ，回车不允许root远程登陆
# 6.我这里需要远程登陆，所以输入n ，回车
# 7.移除test数据库 输入y（我这里选择了NO） ，回车
# 8.重新载入权限表 输入y ，回车
# 输入y ，回车进入该配置

# Securing the MySQL server deployment.
#
# Connecting to MySQL using a blank password.
#
# VALIDATE PASSWORD COMPONENT can be used to test passwords
# and improve security. It checks the strength of password
# and allows the users to set only those passwords which are
# secure enough. Would you like to setup VALIDATE PASSWORD component?
#
# Press y|Y for Yes, any other key for No: y  //配置验证密码组件
#
# There are three levels of password validation policy:
#
# LOW    Length >= 8
# MEDIUM Length >= 8, numeric, mixed case, and special characters
# STRONG Length >= 8, numeric, mixed case, special characters and dictionary                  file
#
# Please enter 0 = LOW, 1 = MEDIUM and 2 = STRONG: 0   //密码验证策略等级
# Please set the password for root here.
#
# New password:  // 输入新密码
#
# Re-enter new password:   //确认新密码
#
# Estimated strength of the password: 100
# Do you wish to continue with the password provided?(Press y|Y for Yes, any other key for No) : y  // 确认是否继续使用提供的密码？输入y
# By default, a MySQL installation has an anonymous user,
# allowing anyone to log into MySQL without having to have
# a user account created for them. This is intended only for
# testing, and to make the installation go a bit smoother.
# You should remove them before moving into a production
# environment.
#
# Remove anonymous users? (Press y|Y for Yes, any other key for No) : y    // 移除匿名用户
# Success.
#
#
# Normally, root should only be allowed to connect from
# 'localhost'. This ensures that someone cannot guess at
# the root password from the network.
#
# Disallow root login remotely? (Press y|Y for Yes, any other key for No) : n // 允许root远程登陆
#
#  ... skipping.
# By default, MySQL comes with a database named 'test' that
# anyone can access. This is also intended only for testing,
# and should be removed before moving into a production
# environment.
#
#
# Remove test database and access to it? (Press y|Y for Yes, any other key for No) : y // 移除test数据库
#  - Dropping test database...
# Success.
#
#  - Removing privileges on test database...
# Success.
#
# Reloading the privilege tables will ensure that all changes
# made so far will take effect immediately.
#
# Reload privilege tables now? (Press y|Y for Yes, any other key for No) : y // 重新载入权限表？ 输入y
# Success.
#
# All done!

mysql -uroot -p

先查看密码规则
-- SHOW VARIABLES LIKE 'validate_password%';

修改密码长度：
-- set global validate_password.length=6;

修改密码规则：
-- set global validate_password.policy=LOW;

-- use mysql;

-- update user set host='%' where user='root';

-- alter user 'root'@'%' identified by '123456' password expire never;

-- ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';

-- flush privileges; // 刷新权限

-- exit // 退出


GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';

##################### mysql.sh #############################
#!/bin/bash

function start_mysql() {
     systemctl start mysqld
     sleep 10
     echo "mysqld started"
}

function stop_mysql() {
     systemctl stop mysqld
     sleep 10
     echo "mysqld stoped"
}

case $1 in
"start")
    start_mysql
    ;;
"stop")
    stop_mysql
    ;;
"restart")
    stop_mysql
    sleep 10
    start_mysql
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
