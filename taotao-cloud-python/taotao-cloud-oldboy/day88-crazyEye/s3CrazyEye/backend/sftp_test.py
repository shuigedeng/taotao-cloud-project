import os,sys
import paramiko

t = paramiko.Transport(('192.168.14.47',22))
t.connect(username='root',password='123456')

sftp = paramiko.SFTPClient.from_transport(t)

sftp.get('/root/test.py','d:/test.py')
sftp.put('d:/牛X的开机启动.jpg','/root/test.jpg')
t.close()

#
# import os,sys
# import paramiko
#
# t = paramiko.Transport(('182.92.219.86',22))
# t.connect(username='wupeiqi',password='123')
# sftp = paramiko.SFTPClient.from_transport(t)
# sftp.get('/tmp/test.py','/tmp/test2.py')
# t.close()