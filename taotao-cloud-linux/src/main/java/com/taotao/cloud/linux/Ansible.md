## Ansible
---

    第 33 天 【ansible入门（02）】

运维工作：系统安装（物理机、虚拟机）--> 程序包安装、配置、服务启动 --> 批量操作 --> 程序发布 --> 监控

- OS Provisioning：
    + 物理机： PXE、Cobbler
    + 虚拟机：Image Templates
- Configration：
    + puppet (ruby)
    + saltstack (python)
    + chef
    + cfengine
- Command and Control：
    + fabric

- 预发布验证：
    + 新版本的代码先发布到服务器（跟线上环境配置完全相同，只是未接入到调度器）；

- 程序发布：
    + 不能影响用户体验；
    + 系统不能停机；
    + 不能导致系统故障或造成系统完全不可用；

- 灰度发布：
    + 发布路径：
        * `/webapp/tuangou-1.1`
        * `/web/app/tuangou`
        * `/webapp/tuangou-1.2`
    + 在调度器上下线一批主机（maintanance）--> 关闭服务器 --> 部署新版本的应用程序 --> 启动服务 --> 在调度服务器上启用这一批服务器；
    + 自动化灰度发布：脚本、发布平台；

- 运维工具的分类：
    + agent：puppet, func
    + agentless：ansible, fabric
        * sshd

- ansible:
    + 模块化，调用特定的模块，完成特定的任务；
    + 基于Python语言实现，由Paramiko、PyYAML和Jinja2三个关键模块；
    + 部署简单，agentless；
    + 主从模式；
    + 支持自定义模块；
    + 支持Playbook
        * 幂等性：Playbook 可以执行多次，每次的结果相同；

- 安装：epel 源
```
    # yum install ansible 
```

- 配置文件：
```
    /etc/ansible/ansible.cfg. //主配置文件
    /etc/ansible/hosts  // 主机清单
```

- 设置基于密钥认证的方式联系各被管理的节点：
```
    创建主机密钥对儿：
        # ssh-keygen -t rsa -P ''
        Generating public/private rsa key pair.
        Enter file in which to save the key (/root/.ssh/id_rsa): 
        Created directory '/root/.ssh'.
        Your identification has been saved in /root/.ssh/id_rsa.
        Your public key has been saved in /root/.ssh/id_rsa.pub.
        The key fingerprint is:
        SHA256:hoEddFsqh4QgHbf1ECKiG5alM/EOdQoiEGLvehhlRns root@192.168.2.139
        The key's randomart image is:
        +---[RSA 2048]----+
        |X*+*o+B.. .      |
        |*o%o=B * +       |
        |oB OoE= =        |
        |.oX .  =         |
        |.. o  . S        |
        |  +    .         |
        | o .             |
        |  .              |
        |                 |
        +----[SHA256]-----+

    复制公钥至管理节点：
        # ssh-copy-id -i ~/.ssh/id_rsa.pub root@192.168.2.142
        /usr/bin/ssh-copy-id: INFO: Source of key(s) to be installed: "/root/.ssh/id_rsa.pub"
        The authenticity of host '192.168.2.142 (192.168.2.142)' can't be established.
        RSA key fingerprint is SHA256:3kp3bX544/NvWbTQ/EPGabPlBQ13QycubL0YNRu634c.
        RSA key fingerprint is MD5:fa:98:12:c2:b6:b9:8f:57:45:33:eb:ac:2b:eb:c5:19.
        Are you sure you want to continue connecting (yes/no)? yes
        /usr/bin/ssh-copy-id: INFO: attempting to log in with the new key(s), to filter out any that are already installed
        /usr/bin/ssh-copy-id: INFO: 1 key(s) remain to be installed -- if you are prompted now it is to install the new keys
        root@192.168.2.142's password: 

        Number of key(s) added: 1

        Now try logging into the machine, with:   "ssh 'root@192.168.2.142'"
        and check to make sure that only the key(s) you wanted were added.

    测试，执行 ifconfig 命令：
        # ssh 192.168.2.142 'ifconfig'     
        eth0      Link encap:Ethernet  HWaddr 00:0C:29:A7:9D:E5  
                  inet addr:192.168.2.142  Bcast:192.168.2.255  Mask:255.255.255.0
                  inet6 addr: fe80::20c:29ff:fea7:9de5/64 Scope:Link
                  UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
                  RX packets:82501 errors:0 dropped:0 overruns:0 frame:0
                  TX packets:39819 errors:0 dropped:0 overruns:0 carrier:0
                  collisions:0 txqueuelen:1000 
                  RX bytes:121895388 (116.2 MiB)  TX bytes:2427330 (2.3 MiB)

        lo        Link encap:Local Loopback  
                  inet addr:127.0.0.1  Mask:255.0.0.0
                  inet6 addr: ::1/128 Scope:Host
                  UP LOOPBACK RUNNING  MTU:65536  Metric:1
                  RX packets:8 errors:0 dropped:0 overruns:0 frame:0
                  TX packets:8 errors:0 dropped:0 overruns:0 carrier:0
                  collisions:0 txqueuelen:0 
                  RX bytes:480 (480.0 b)  TX bytes:480 (480.0 b)

    本机也需要基于密钥认证登录，所以也要复制给本机一份公钥：
        # ssh-copy-id -i ~/.ssh/id_rsa.pub root@192.168.2.139
        /usr/bin/ssh-copy-id: INFO: Source of key(s) to be installed: "/root/.ssh/id_rsa.pub"
        The authenticity of host '192.168.2.139 (192.168.2.139)' can't be established.
        ECDSA key fingerprint is SHA256:Gjt9vM5DW7RI1q915GjsadclbUfeRNgcq7PgJ52E3ec.
        ECDSA key fingerprint is MD5:48:4c:de:a6:cd:12:38:c3:54:93:4b:67:b7:74:4b:09.
        Are you sure you want to continue connecting (yes/no)? yes
        /usr/bin/ssh-copy-id: INFO: attempting to log in with the new key(s), to filter out any that are already installed
        /usr/bin/ssh-copy-id: INFO: 1 key(s) remain to be installed -- if you are prompted now it is to install the new keys
        root@192.168.2.139's password: 

        Number of key(s) added: 1

        Now try logging into the machine, with:   "ssh 'root@192.168.2.139'"
        and check to make sure that only the key(s) you wanted were added.
```

- 主机清单：
```
    将需要管理的主机地址添加到主机清单配置文件中去，/etc/ansible/hosts

    # vim /etc/ansible/hosts 
    # cp /etc/ansible/hosts /etc/ansible/hosts.original
    # vim /etc/ansible/hosts
    [webserver]
    192.168.2.142
```
- ansible 命令：
```
    ansible <host-pattern> [-f forks] [-m module_name] [-a args]
        -m module：默认为command

    例：
        # ansible 192.168.2.142 -m command -a 'ifconfig'
        192.168.2.142 | SUCCESS | rc=0 >>
        eth0      Link encap:Ethernet  HWaddr 00:0C:29:A7:9D:E5  
                  inet addr:192.168.2.142  Bcast:192.168.2.255  Mask:255.255.255.0
                  inet6 addr: fe80::20c:29ff:fea7:9de5/64 Scope:Link
                  UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
                  RX packets:82666 errors:0 dropped:0 overruns:0 frame:0
                  TX packets:39966 errors:0 dropped:0 overruns:0 carrier:0
                  collisions:0 txqueuelen:1000 
                  RX bytes:121961090 (116.3 MiB)  TX bytes:2442951 (2.3 MiB)

        lo        Link encap:Local Loopback  
                  inet addr:127.0.0.1  Mask:255.0.0.0
                  inet6 addr: ::1/128 Scope:Host
                  UP LOOPBACK RUNNING  MTU:65536  Metric:1
                  RX packets:8 errors:0 dropped:0 overruns:0 frame:0
                  TX packets:8 errors:0 dropped:0 overruns:0 carrier:0
                  collisions:0 txqueuelen:0 
                  RX bytes:480 (480.0 b)  TX bytes:480 (480.0 b)

    ansible-doc: Show Ansible module documentation
        -l, --list            List available modules
        -s, --snippet         Show playbook snippet for specified module(s)

    例如：
        # ansible-doc -s command
        - name: Executes a command on a remote node
          command:
              chdir:                 # Change into this directory before running the command.
              creates:               # A filename or (since 2.0) glob pattern, when it already exists, this step will *not* be
                                       run.
              free_form:             # (required) The command module takes a free form command to run.  There is no parameter
                                       actually named 'free form'. See the examples!
              removes:               # A filename or (since 2.0) glob pattern, when it does not exist, this step will *not* be
                                       run.
              stdin:                 # Set the stdin of the command directly to the specified value.
              warn:                  # If command_warnings are on in ansible.cfg, do not warn about this particular line if set
                                       to `no'.
```

>   第 33 天 【ansilbe常用模块详解(03)-2】

- ansible
```
    ansible <host-pattern> [-f forks] [-m module_name] [-a args]
        args:
            key=value

            注意：command模块要执行命令无须为key=value格式，而是直接给出要执行的命令即可；
    常用模块：
        command:
            -a 'COMMAND'

        user:
            -a 'name= state={present|absent} system= uid='

            # ansible webserver -m user -a 'name=hacluster state=present'

        group:
            -a 'name= gid= state={present|absent} system='

        cron:
            -a 'name= minute= hour= day= month= weekday= job= user= state='
            # ansible all -m cron -a 'name="sync time from ntpserver" minute="*/10" job="/sbin/ntpdate ntp1.aliyun.com &> /dev/null"'
            192.168.2.142 | SUCCESS => {
                "changed": true, 
                "envs": [], 
                "jobs": [
                    "sync time from ntpserver"
                ]
            }

        copy:Copies files to remote locations
            -a 'dest= src= mode= owner= group='
            # ansible webserver -m copy -a 'src=/etc/fstab dest=/tmp/fstab.tmp mode=600'
            192.168.122.61 | SUCCESS => {
                "changed": true, 
                "checksum": "cca0597bfff9a02cb72b2dec2ee6f07536c09c38", 
                "dest": "/tmp/fastab.tmp", 
                "gid": 0, 
                "group": "root", 
                "md5sum": "7a35538f0d090b41f2f7a67f976a0e79", 
                "mode": "0600", 
                "owner": "root", 
                "secontext": "unconfined_u:object_r:admin_home_t:s0", 
                "size": 465, 
                "src": "/root/.ansible/tmp/ansible-tmp-1523188846.3-117035343158238/source", 
                "state": "file", 
                "uid": 0
            }
            192.168.122.62 | SUCCESS => {
                "changed": true, 
                "checksum": "cca0597bfff9a02cb72b2dec2ee6f07536c09c38", 
                "dest": "/tmp/fastab.tmp", 
                "gid": 0, 
                "group": "root", 
                "md5sum": "7a35538f0d090b41f2f7a67f976a0e79", 
                "mode": "0600", 
                "owner": "root", 
                "secontext": "unconfined_u:object_r:admin_home_t:s0", 
                "size": 465, 
                "src": "/root/.ansible/tmp/ansible-tmp-1523188846.31-6552001981775/source", 
                "state": "file", 
                "uid": 0
            }

        file：Sets attributes of files
            -a 'path= mode= owner= group= state={directory|link|present|absent} src= '
            # ansible 192.168.122.61 -m file -a 'path=/tmp/testdir state=directory'     
            192.168.122.61 | SUCCESS => {
                "changed": true, 
                "gid": 0, 
                "group": "root", 
                "mode": "0755", 
                "owner": "root", 
                "path": "/tmp/testdir", 
                "secontext": "unconfined_u:object_r:user_tmp_t:s0", 
                "size": 4096, 
                "state": "directory", 
                "uid": 0
            }

            # ansible 192.168.122.61 -m file -a 'path=/tmp/fstab.symlink state=link src=/tmp/fstab.tmp'
            192.168.122.61 | SUCCESS => {
                "changed": true, 
                "dest": "/tmp/fstab.symlink", 
                "gid": 0, 
                "group": "root", 
                "mode": "0777", 
                "owner": "root", 
                "secontext": "unconfined_u:object_r:user_tmp_t:s0", 
                "size": 15, 
                "src": "/tmp/fastab.tmp", 
                "state": "link", 
                "uid": 0
            }

        ping:Try to connect to host, verify a usable python and return `pong' on success
            # ansible all -m ping
            192.168.122.62 | SUCCESS => {
                "changed": false, 
                "ping": "pong"
            }
            192.168.122.72 | SUCCESS => {
                "changed": false, 
                "ping": "pong"
            }
            192.168.122.61 | SUCCESS => {
                "changed": false, 
                "ping": "pong"
            }
            192.168.122.71 | SUCCESS => {
                "changed": false, 
                "ping": "pong"
            }

        yum：Manages packages with the `yum' package manager
            -a 'name= state={present|latest|absent}'
            # ansible webserver -m yum -a 'name=nginx state=latest'
            # ansible webserver -m yum -a 'name=nginx state=absent'

        service：Manage services.
            -a 'name= state={started|stopped|restarted} enabled='
            # ansible webserver -m service -a 'name=ngix state=started enabled=yes'
            # ansible webserver -m service -a 'name=ngix state=stopped enabled=no'

        shell：Execute commands in nodes.
            -a 'COMMAND'

            # ansible webserver -m shell -a 'echo centos | passwd --stdin centos'

        script：Runs a local script on a remote node after transferring it
            -a '/path/to/script'

            # vim /tmp/test.sh
            #!/bin/bash
            #
            # Date: 2018/04/08

            echo "$(hostname) ansible is good." > /tmp/ansible.txt

            # ansible webserver -m script -a '/tmp/test.sh'
            192.168.122.62 | SUCCESS => {
                "changed": true, 
                "rc": 0, 
                "stderr": "Shared connection to 192.168.122.62 closed.\r\n", 
                "stdout": "", 
                "stdout_lines": []
            }
            192.168.122.61 | SUCCESS => {
                "changed": true, 
                "rc": 0, 
                "stderr": "Shared connection to 192.168.122.61 closed.\r\n", 
                "stdout": "", 
                "stdout_lines": []
            }

        setup：Gathers facts about remote hosts
            # ansible webserver -m setup
```

- playbook的核心元素：
    + tasks：任务
    + variables：变量
        * facts：`ansible HOST -m steup`
        * 通过命令行传递变量：
        ```
            ansible-playbook test.yml --extra-vars "name=value name=value"
        ```
        * role定义：
        ```
                - hosts: webservers
                  roles:
                    - common
                    - { role: foo_app_instance, dir: '/web/htdocs/a.com',  port: 8080 }
        ```
        * Inventory中的变量：
            - 主机变量：`hostname name=value name=value`
            - 组变量：
            ```
                [groupname:vars]
                name=value
                name=value
            ```
    + templates：模版
    + handlers：处理器
    + roles：角色

- Inventory的高级用法：
```
    5.2.1 inventory文件格式
    inventory文件遵循INI文件风格，中括号中的字符为组名。可以将同一个主机同时归并到多个不同的组中；此外，当如若目标主机使用了非默认的SSH端口，还可以在主机名称之后使用冒号加端口号来标明。

        ntp.magedu.com

        [webservers]
        www1.magedu.com:2222
        www2.magedu.com

        [dbservers]
        db1.magedu.com
        db2.magedu.com
        db3.magedu.com

    如果主机名称遵循相似的命名模式，还可以使用列表的方式标识各主机，例如：

    [webservers]
    www[01:50].example.com

    [databases]
    db-[a:f].example.com

    5.2.2 主机变量

    可以在inventory中定义主机时为其添加主机变量以便于在playbook中使用。例如：

    [webservers]
    www1.magedu.com http_port=80 maxRequestsPerChild=808
    www2.magedu.com http_port=8080 maxRequestsPerChild=909

    5.2.3 组变量

    组变量是指赋予给指定组内所有主机上的在playboo中可用的变量。例如：

    [webservers]
    www1.magedu.com
    www2.magedu.com

    [webservers:vars]
    ntp_server=ntp.magedu.com
    nfs_server=nfs.magedu.com

    5.2.4 组嵌套

    inventory中，组还可以包含其它的组，并且也可以向组中的主机指定变量。不过，这些变量只能在ansible-playbook中使用，而ansible不支持。例如：

    [apache]
    httpd1.magedu.com
    httpd2.magedu.com

    [nginx]
    ngx1.magedu.com
    ngx2.magedu.com

    [webservers:children]
    apache
    nginx

    [webservers:vars]
    ntp_server=ntp.magedu.com

    5.2.5 inventory参数

        [websrvs]
        172.16.100.68 ansible_ssh_port=2222 ansible_ssp_user=centos ansible_ssh_pass=PASSWORD

    ansible基于ssh连接inventory中指定的远程主机时，还可以通过参数指定其交互方式；这些参数如下所示：

    ansible_ssh_host
      The name of the host to connect to, if different from the alias you wish to give to it.
    ansible_ssh_port
      The ssh port number, if not 22
    ansible_ssh_user
      The default ssh user name to use.
    ansible_ssh_pass
      The ssh password to use (this is insecure, we strongly recommend using --ask-pass or SSH keys)
    ansible_sudo_pass
      The sudo password to use (this is insecure, we strongly recommend using --ask-sudo-pass)
    ansible_connection
      Connection type of the host. Candidates are local, ssh or paramiko.  The default is paramiko before Ansible 1.2, and 'smart' afterwards which detects whether usage of 'ssh' would be feasible based on whether ControlPersist is supported.
    ansible_ssh_private_key_file
      Private key file used by ssh.  Useful if using multiple keys and you don't want to use SSH agent.
    ansible_shell_type
      The shell type of the target system. By default commands are formatted using 'sh'-style syntax by default. Setting this to 'csh' or 'fish' will cause commands executed on target systems to follow those shell's syntax instead.
    ansible_python_interpreter
      The target host python path. This is useful for systems with more
      than one Python or not located at "/usr/bin/python" such as \*BSD, or where /usr/bin/python
      is not a 2.X series Python.  We do not use the "/usr/bin/env" mechanism as that requires the remote user's
      path to be set right and also assumes the "python" executable is named python, where the executable might
      be named something like "python26".
    ansible\_\*\_interpreter
      Works for anything such as ruby or perl and works just like ansible_python_interpreter.
      This replaces shebang of modules which will run on that host.
```
