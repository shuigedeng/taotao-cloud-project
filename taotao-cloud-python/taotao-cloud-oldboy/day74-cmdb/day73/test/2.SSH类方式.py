# 基于paramiko模块， pip3 install paramiko
import requests
import paramiko

# ################## 获取今日未采集主机名 ##################
#result = requests.get('http://www.127.0.0.1:8000/assets.html')
# result = ['c1.com','c2.com']


# ################## 通过paramiko连接远程服务器，执行命令 ##################
# 创建SSH对象
ssh = paramiko.SSHClient()
# 允许连接不在know_hosts文件中的主机
ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
# 连接服务器
ssh.connect(hostname='192.168.14.36', port=22, username='wupeiqi', password='123')

# 执行命令
# stdin, stdout, stderr = ssh.exec_command('df')

# 获取命令结果
# result = stdout.read()

# 关闭连接
# ssh.close()
# print(result)

# data_dict = {result}

# ##################  发送数据 ##################
# requests.post('http://www.127.0.0.1:8000/assets.html',data=data_dict)
