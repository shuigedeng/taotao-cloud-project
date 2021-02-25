
使用的是kvm虚拟机，给虚拟机添加一块新的磁盘作为mariadb的数据库存放，下面使用 `vrish` 和 `qemu-img` 命令实现。

查看虚拟机列表，获取虚拟机Id和Name：
```
[root@Testserver ~]# virsh list
 Id    Name                           State
----------------------------------------------------
 3     node2_centos6                  running
 5     node1_centos6                  running
 12    node3_centos7                  running
 15    node4_centos7                  running
```

查看虚拟机当前使用的磁盘：
```
[root@Testserver ~]# virsh domblklist node1_centos6
Target     Source
------------------------------------------------
vda        /var/lib/libvirt/images/node1_centos6.img
hdc        -
```

创建一块新的磁盘：
```
[root@Testserver ~]# qemu-img create -f qcow2 -o size=20G,preallocation=metadata /var/lib/libvirt/images/node1_centos6_mydata.img  
Formatting '/var/lib/libvirt/images/node1_centos6_mydata.img', fmt=qcow2 size=21474836480 encryption=off cluster_size=65536 preallocation='metadata' 
[root@Testserver ~]# ll /var/lib/libvirt/images/node1_centos6_mydata.img
-rw-r--r-- 1 root root 21478375424 Apr 22 14:06 /var/lib/libvirt/images/node1_centos6_mydata.img
```

将磁盘添加至虚拟机：
```
[root@Testserver ~]# virsh attach-disk 5 /var/lib/libvirt/images/node1_centos6_mydata.img vdb 
Disk attached successfully
```

再次查看虚拟机当前使用的磁盘：
```
[root@Testserver ~]# virsh domblklist node1_centos6
Target     Source
------------------------------------------------
vda        /var/lib/libvirt/images/node1_centos6.img
vdb        /var/lib/libvirt/images/node1_centos6_mydata.img
hdc        -
```

创建lvm磁盘：
```
[root@node1 ~]# pvcreate -v /dev/vdb 
    Wiping cache of LVM-capable devices
    Set up physical volume for "/dev/vdb" with 41949952 available sectors
    Zeroing start of device /dev/vdb
    Writing physical volume data to disk "/dev/vdb"
  Physical volume "/dev/vdb" successfully created

root@node1 ~]# vgcreate -v myvg0 /dev/vdb 
    Wiping cache of LVM-capable devices
    Wiping cache of LVM-capable devices
    Adding physical volume '/dev/vdb' to volume group 'myvg0'
    Archiving volume group "myvg0" metadata (seqno 0).
    Writing physical volume data to disk "/dev/vdb"
    Physical volume "/dev/vdb" successfully written
    Creating volume group backup "/etc/lvm/backup/myvg0" (seqno 1).
  Volume group "myvg0" successfully created

[root@node1 ~]# lvcreate -L 20G -n mydata myvg0
  Logical volume "mydata" created.
```

格式化lvm磁盘：
```
[root@node1 ~]# mke2fs -t ext4 /dev/myvg0/mydata 
mke2fs 1.41.12 (17-May-2010)
Filesystem label=
OS type: Linux
Block size=4096 (log=2)
Fragment size=4096 (log=2)
Stride=0 blocks, Stripe width=0 blocks
1310720 inodes, 5242880 blocks
262144 blocks (5.00%) reserved for the super user
First data block=0
Maximum filesystem blocks=4294967296
160 block groups
32768 blocks per group, 32768 fragments per group
8192 inodes per group
Superblock backups stored on blocks: 
        32768, 98304, 163840, 229376, 294912, 819200, 884736, 1605632, 2654208, 
        4096000

Writing inode tables: done                            
Creating journal (32768 blocks): done
Writing superblocks and filesystem accounting information: done

This filesystem will be automatically checked every 37 mounts or
180 days, whichever comes first.  Use tune2fs -c or -i to override.
```

创建数据目录：
```
[root@node1 ~]# mkdir /mydata
```

使磁盘开机自动挂载：
```
[root@node1 ~]# vim /etc/fstab 
/dev/myvg0/mydata       /mydata                 ext4    defaults        0 0

[root@node1 ~]# mount -a
[root@node1 ~]# mount
/dev/mapper/vg_node1-lv_root on / type ext4 (rw)
proc on /proc type proc (rw)
sysfs on /sys type sysfs (rw)
devpts on /dev/pts type devpts (rw,gid=5,mode=620)
tmpfs on /dev/shm type tmpfs (rw,rootcontext="system_u:object_r:tmpfs_t:s0")
/dev/vda1 on /boot type ext4 (rw)
/dev/mapper/vg_node1-lv_home on /home type ext4 (rw)
none on /proc/sys/fs/binfmt_misc type binfmt_misc (rw)
/dev/mapper/myvg0-mydata on /mydata type ext4 (rw)
```

创建数据库存放目录并修改其属主和属组：
```
[root@node1 mydata]# mkdir data
[root@node1 mydata]# chown mysql:mysql data
[root@node1 mydata]# ll
total 20
drwxr-xr-x. 2 mysql mysql  4096 Apr 22 14:28 data
drwx------. 2 root  root  16384 Apr 22 14:24 lost+found
```

下载二进制格式的mariadb包：
```
[root@node1 ~]# wget http://192.168.122.1:8081/pub/mariadb/mariadb-5.5.59-linux-x86_64.tar   
--2018-04-22 14:51:56--  http://192.168.122.1:8081/pub/mariadb/mariadb-5.5.59-linux-x86_64.tar
Connecting to 192.168.122.1:8081... connected.
HTTP request sent, awaiting response... 200 OK
Length: 821566976 (784M) [application/x-tar]
Saving to: “mariadb-5.5.59-linux-x86_64.tar”

100%[============================================================================================================>] 821,566,976  126M/s   in 6.2s    

2018-04-22 14:52:02 (126 MB/s) - “mariadb-5.5.59-linux-x86_64.tar” saved [821566976/821566976]
```

解压至`/usr/local/`：
```
[root@node1 ~]# tar -xf mariadb-5.5.59-linux-x86_64.tar -C /usr/local/
[root@node1 ~]# cd /usr/local/
[root@node1 local]# ls
bin  etc  games  include  lib  lib64  libexec  mariadb-5.5.59-linux-x86_64  sbin  share  src
[root@node1 local]# ln -sv mariadb-5.5.59-linux-x86_64 mysql
`mysql' -> `mariadb-5.5.59-linux-x86_64'
```

初始化数据库：
```
[root@node1 local]# cd mysql/
[root@node1 mysql]# scripts/mysql_install_db --user=mysql --datadir=/mydata/data/
Installing MariaDB/MySQL system tables in '/mydata/data/' ...
180422 14:54:25 [Note] ./bin/mysqld (mysqld 5.5.59-MariaDB) starting as process 16527 ...
OK
Filling help tables...
180422 14:54:27 [Note] ./bin/mysqld (mysqld 5.5.59-MariaDB) starting as process 16536 ...
OK

To start mysqld at boot time you have to copy
support-files/mysql.server to the right place for your system

PLEASE REMEMBER TO SET A PASSWORD FOR THE MariaDB root USER !
To do so, start the server, then issue the following commands:

'./bin/mysqladmin' -u root password 'new-password'
'./bin/mysqladmin' -u root -h node1.quantacn.com password 'new-password'

Alternatively you can run:
'./bin/mysql_secure_installation'

which will also give you the option of removing the test
databases and anonymous user created by default.  This is
strongly recommended for production servers.

See the MariaDB Knowledgebase at http://mariadb.com/kb or the
MySQL manual for more instructions.

You can start the MariaDB daemon with:
cd '.' ; ./bin/mysqld_safe --datadir='/mydata/data/'

You can test the MariaDB daemon with mysql-test-run.pl
cd './mysql-test' ; perl mysql-test-run.pl

Please report any problems at http://mariadb.org/jira

The latest information about MariaDB is available at http://mariadb.org/.
You can find additional information about the MySQL part at:
http://dev.mysql.com
Consider joining MariaDB's strong and vibrant community:
https://mariadb.org/get-involved/

[root@node1 mysql]# ls /mydata/data/
aria_log.00000001  aria_log_control  mysql  performance_schema  test
```

复制服务启动脚本：
```
[root@node1 mysql]# cp support-files/mysql.server /etc/rc.d/init.d/mysqld
[root@node1 mysql]# chkconfig --list mysqld
service mysqld supports chkconfig, but is not referenced in any runlevel (run 'chkconfig --add mysqld')
[root@node1 mysql]# chkconfig --add mysqld
[root@node1 mysql]# chkconfig --list mysqld
mysqld          0:off   1:off   2:on    3:on    4:on    5:on    6:off
```

创建配置文件，并设置相关配置：
```
[root@node1 mysql]# mkdir /etc/mysql          
[root@node1 mysql]# cp support-files/my-large.cnf /etc/mysql/my.cnf
[root@node1 mysql]# vim /etc/mysql/my.cnf
datadir = /mydata/data
innodb_file_per_table = on
skip_name_resolve = on
```

启动服务：
```
[root@node1]# service mysqld start
```

执行安全安装脚本，根据提示操作：
```
[root@node1 ~]# /usr/local/mysql/bin/mysql_secure_installation
/usr/local/mysql/bin/mysql_secure_installation: line 393: find_mysql_client: command not found

NOTE: RUNNING ALL PARTS OF THIS SCRIPT IS RECOMMENDED FOR ALL MariaDB
      SERVERS IN PRODUCTION USE!  PLEASE READ EACH STEP CAREFULLY!

In order to log into MariaDB to secure it, we'll need the current
password for the root user.  If you've just installed MariaDB, and
you haven't set the root password yet, the password will be blank,
so you should just press enter here.

Enter current password for root (enter for none): 
OK, successfully used password, moving on...

Setting the root password ensures that nobody can log into the MariaDB
root user without the proper authorisation.

Set root password? [Y/n] y
New password: 
Re-enter new password: 
Password updated successfully!
Reloading privilege tables..
 ... Success!


By default, a MariaDB installation has an anonymous user, allowing anyone
to log into MariaDB without having to have a user account created for
them.  This is intended only for testing, and to make the installation
go a bit smoother.  You should remove them before moving into a
production environment.

Remove anonymous users? [Y/n] y
 ... Success!

Normally, root should only be allowed to connect from 'localhost'.  This
ensures that someone cannot guess at the root password from the network.

Disallow root login remotely? [Y/n] y
 ... Success!

By default, MariaDB comes with a database named 'test' that anyone can
access.  This is also intended only for testing, and should be removed
before moving into a production environment.

Remove test database and access to it? [Y/n] n
 ... skipping.

Reloading the privilege tables will ensure that all changes made so far
will take effect immediately.

Reload privilege tables now? [Y/n] y
 ... Success!

Cleaning up...

All done!  If you've completed all of the above steps, your MariaDB
installation should now be secure.

Thanks for using MariaDB!
```

登录：
```
[root@node1 ~]# mysql -uroot -hlocalhost -p
Enter password: 
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 11
Server version: 5.5.59-MariaDB MariaDB Server

Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.
```

查看`user`表中的相关用户信息：
```
mysql> use mysql;
Database changed
mysql> select user, host, password from user;
+------+-----------+-------------------------------------------+
| user | host      | password                                  |
+------+-----------+-------------------------------------------+
| root | localhost | *F95033EED5F4DC410F818F3921D01B0F353FDFD1 |
| root | 127.0.0.1 | *F95033EED5F4DC410F818F3921D01B0F353FDFD1 |
| root | ::1       | *F95033EED5F4DC410F818F3921D01B0F353FDFD1 |
+------+-----------+-------------------------------------------+
3 rows in set (0.00 sec)
```

查看版本：
```
MariaDB [testdb]> SELECT VERSION();
+----------------+
| VERSION()      |
+----------------+
| 5.5.59-MariaDB |
+----------------+
```

（完）