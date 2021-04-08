# 1. 安装saltstack
#       rpm --import https://repo.saltstack.com/yum/redhat/6/x86_64/latest/SALTSTACK-GPG-KEY.pub
#
#
"""
        Master： yum install salt-master
       Master准备：
            a. 配置文件，监听本机IP
                vim /etc/salt/master
                interface: 本机IP地址
            b. 启动master
                /etc/init.d/salt-master start


        Slave：  yum install salt-minion
        Slave准备：
            a. 配置文件，连接那个master
                vim /etc/salt/minion
                master: 远程master地址
            b. 启动slave
                /etc/init.d/salt-minion start

2. 创建关系
    查看
    Master：salt-key -L
        Accepted Keys:
        Denied Keys:
        Unaccepted Keys:
            c1.com
            c2.com
            c3.com
        Rejected Keys:
    接受
    Master：salt-key -a c1.com
        Accepted Keys:
            c1.com
            c2.com
        Denied Keys:
        Unaccepted Keys:
            c3.com
        Rejected Keys:


3. 执行命令
    master：
        salt 'c1.com' cmd.run  'ifconfig'

    import salt.client
    local = salt.client.LocalClient()
    result = local.cmd('c2.salt.com', 'cmd.run', ['ifconfig'])

"""
# ################## 获取今日未采集主机名 ##################
#result = requests.get('http://www.127.0.0.1:8000/assets.html')
# result = ['c1.com','c2.com']


# ################## 远程服务器执行命令 ##################
# import subprocess
# result = subprocess.getoutput("salt 'c1.com' cmd.run  'ifconfig'")
#
# import salt.client
# local = salt.client.LocalClient()
# result = local.cmd('c2.salt.com', 'cmd.run', ['ifconfig'])


# ##################  发送数据 ##################
# requests.post('http://www.127.0.0.1:8000/assets.html',data=data_dict)