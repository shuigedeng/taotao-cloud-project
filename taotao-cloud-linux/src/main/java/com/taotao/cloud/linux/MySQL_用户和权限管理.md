## MySQL 用户和权限管理

    第 40 天 【用户、权限管理及查询缓存(01)】

- 权限类别：
    + 库级别
        * ALTER 
        * CREATE 
        * DROP
    + 表级别
        * ALTER 
        * CREATE
        * CREATE VIEW
        * DROP
        * INDEX
        * SHOW VIEW
        * GRANT OPTION：能够把自己获得的权限赠给其他用户一个副本；
    + 字段级别：
        * SELECT(col1, col2, ...)
        * UPDATE(col1, col2, ...)
        * INSERT(col1, col2, ...)
    + 管理类：
        * CREATE TEMPORARY TABLES
        * CREATE USER
        * FILE
        * SUPER
        * SHOW DATABASES
        * RELOAD
        * SHUTDOWN
        * REPLICATION SLAVE
        * REPLICATION CLIENT
        * LOCK TABLES
        * PROCESS
    + 程序类：
        * FUNCTION
        * PROCEDURE
        * TRIGGER
            - CREATE, ALTER, DROP, EXCUTE
    + 数据操作：
        * SELECT
        * INSERT
        * DELETE
        * UPDATE 
    + 所有：ALL PRIVILEGES, ALL

- 元数据数据库：mysql
    + 授权表：
        * db, host, user：限制可以登录的用户、主机和数据；
        * columns_priv, tables_priv, procs_priv, proxies_priv

- 用户账号：
```
    'USERNAME'@'HOST'
        @'HOST':
            主机名；
            IP 地址或 Network；
            统配符：
                %,_：172.16.%.%

```
    + 创建用户：CREATE USER
        ```
            CREATE USER 'USERNAME'@'HOST' [IDENTIFIED BY 'password'];

            CREATE USER 'jeffrey'@'localhost' IDENTIFIED BY 'mypass';
            CREATE USER 'jeffrey'@'localhost' IDENTIFIED BY PASSWORD '*90E462C37378CED12064BB3388827D2BA3A9B689';
        ```
    + 查看用户获得的授权：SHOW GRANT FOR
        ```
            SHOW GRANTS FOR 'USERNAME'@'HOST';

            MariaDB [mysql]> SHOW GRANTS FOR 'root'@'localhost';
            +----------------------------------------------------------------------------------------------------------------------------------------+
            | Grants for root@localhost                                                                                                              |
            +----------------------------------------------------------------------------------------------------------------------------------------+
            | GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' IDENTIFIED BY PASSWORD '*F95033EED5F4DC410F818F3921D01B0F353FDFD1' WITH GRANT OPTION |
            | GRANT PROXY ON ''@'' TO 'root'@'localhost' WITH GRANT OPTION                                                                           |
            +----------------------------------------------------------------------------------------------------------------------------------------+
            2 rows in set (0.00 sec)
        ```
    + 重命名用户：RENAME USER
        ```
            RENAME USER old_user_name TO new_user_name;

            RENAME USER 'jeffrey'@'localhost' TO 'jeff'@'127.0.0.1';
        ```
    + 删除用户：DROP USER 'USERNAME'@'HOST'
        ```
            DROP USER user [, user] ...

            DROP USER 'jeffrey'@'localhost';
        ```
    + 修改密码：
        * SET PASSWORD FOR
        ```
            SET PASSWORD [FOR user] =
            {
                PASSWORD('cleartext password')
              | OLD_PASSWORD('cleartext password')
              | 'encrypted password'
            }

            SET PASSWORD FOR 'bob'@'%.example.org' = PASSWORD('cleartext password');
        ```
        * UPDATE mysql.usr SET password=PASSWORD('your_password') WHERE clause;
            - 修改之后需要刷新下授权缓存：`flush privileges;``
        * mysqladmin password
        ```
            mysqladmin [OPTIONS] command command....

                -u：User for login if not current user.
                -h：Connect to host.
                -p：Password to use when connecting to server. If password is not given it's asked from the tty.

                password [new-password] Change old password to new-password in current format.
        ```

- 忘记管理员密码的解决办法：<br>
    + 启动 mysqld 进程时，为其使用：<br>
    ```
        --skip-grant-tables：跳过授权表，任何用户均可登录。
        --skip-networking：禁用网络登录功能，放置恶意攻击。
    ```
    + 使用 UPDATE 命令修改管理员密码；
    ```
        mysql> UPDATE mysql.user SET password=PASSWORD('NEW_PASSWORD') WHERE user='root';
    ```
    + 关闭 mysqld 进程，移除上述两个选项，重启 mysqld；

- 授权 GRANT
```
    GRANT priv_type[,...] ON [{table|function|procedure}] db.{table|routine} TO 'USERNAME'@'HOST' [IDENTIFIED BY 'password']
        [REQUIRE SSL] [WITH with_option]

        with_option:
            GRANT OPTION
          | MAX_QUERIES_PER_HOUR count      //每小时查询次数，默认为 0，不进行限制；
          | MAX_UPDATES_PER_HOUR counn      //每小时修改次数，默认为 0，不进行限制；
          | MAX_CONNECTIONS_PER_HOUR count  //每小时连接次数，默认为 0，不进行限制；
          | MAX_USER_CONNECTIONS count      //同一用户同一时间(simultaneous)可以连接的最大数量，默认为 0，不进行限制；

    CREATE USER 'jeffrey'@'localhost' IDENTIFIED BY 'mypass';
    GRANT ALL ON db1.* TO 'jeffrey'@'localhost';
    GRANT SELECT ON db2.invoice TO 'jeffrey'@'localhost';
    GRANT USAGE ON *.* TO 'jeffrey'@'localhost' WITH MAX_QUERIES_PER_HOUR 90;
```

- 取消授权 REVOKE
```
    REVOKE
        priv_type [(column_list)]
          [, priv_type [(column_list)]] ...
        ON [object_type] priv_level
        FROM user [, user] ...

    REVOKE INSERT ON *.* FROM 'jeffrey'@'localhost';
```

练习: <br>
授权 test 用户通过任意主机连接当前 mysqld， 但每秒最大查询次数不得超过5次；<br>
```
    用户最大查询次数限制是在授权时使用 with_option MAX_QUERIES_PER_HOUR count 实现，是以小时为单位的。

    GRANT ALL ON *.* TO 'test'@'%' MAX_QUERIES_PER_HOUR 5

    创建 test 用户：
        MariaDB [(none)]> CREATE USER 'test'@'%' IDENTIFIED BY 'testpass';

    限制其每小时查询次数为 5 次：
        MariaDB [testdb]> GRANT SELECT ON testdb.* TO 'test'@'%' WITH MAX_QUERIES_PER_HOUR 5;
        Query OK, 0 rows affected (0.00 sec)

    5 次查询类相关操作后，再执行查询类操作，其会出现相关查询错误提示：
        MariaDB [testdb]> status;
        ERROR 1226 (42000): User 'test' has exceeded the 'max_queries_per_hour' resource (current value: 5)
        MariaDB [testdb]> select * from students;
        ERROR 1226 (42000): User 'test' has exceeded the 'max_queries_per_hour' resource (current value: 5)
        MariaDB [testdb]> flush user_resources;
        ERROR 1226 (42000): User 'test' has exceeded the 'max_queries_per_hour' resource (current value: 5)
```

此账号的同时连接次数不得超过3次；
```
    使用 MAX_USER_CONNECTIONS count 限制用户同时连接服务器的最大次数：

    MariaDB [mysql]> UPDATE user SET max_user_connections=3 WHERE User='test';
    Query OK, 1 row affected (0.00 sec)
    Rows matched: 1  Changed: 1  Warnings: 0

    MariaDB [mysql]> SELECT user, max_user_connections FROM user WHERE User='test';
    +------+----------------------+
    | user | max_user_connections |
    +------+----------------------+
    | test |                    3 |
    +------+----------------------+
    1 row in set (0.00 sec)

    MariaDB [mysql]> FLUSH PRIVILEGES;
    Query OK, 0 rows affected (0.00 sec)

    当有 3 个用户同时的连接时，再使用此用户连接会提示错误：
        [root@node1 ~]# w
         20:45:26 up 45 days, 10:21,  6 users,  load average: 0.00, 0.00, 0.00
        USER     TTY      FROM              LOGIN@   IDLE   JCPU   PCPU WHAT
        root     tty1     -                08Apr18 38days  0.25s  0.25s -bash
        root     pts/1    192.168.122.1    20:41    3:06   0.13s  0.00s /usr/local/mysql/bin/mysql -utest -hlocalhost -p
        root     pts/2    192.168.122.1    20:42    1:39   0.14s  0.00s /usr/local/mysql/bin/mysql -utest -hlocalhost -p
        root     pts/3    192.168.122.1    20:44    1:01   0.14s  0.00s /usr/local/mysql/bin/mysql -utest -hlocalhost -p
        root     pts/4    192.168.122.1    20:44    0.00s  0.13s  0.00s w

        [root@node1 ~]# /usr/local/mysql/bin/mysql -utest -hlocalhost -p
        Enter password: 
        ERROR 1226 (42000): User 'test' has exceeded the 'max_user_connections' resource (current value: 3)
```

（完）
