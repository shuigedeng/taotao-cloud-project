# from socket import *
# import subprocess
# ip_port=('127.0.0.1',8080)
# back_log=5
# buffer_size=1024
#
# tcp_server=socket(AF_INET,SOCK_STREAM)
# tcp_server.bind(ip_port)
# tcp_server.listen(back_log)
#
# while True:
#     conn,addr=tcp_server.accept()
#     print('新的客户端链接',addr)
#     while True:
#         #收
#         try:
#             cmd=conn.recv(buffer_size)
#             if not cmd:break
#             print('收到客户端的命令',cmd)
#
#             #执行命令，得到命令的运行结果cmd_res
#             res=subprocess.Popen(cmd.decode('utf-8'),shell=True,
#                                  stderr=subprocess.PIPE,
#                                  stdout=subprocess.PIPE,
#                                  stdin=subprocess.PIPE)
#             err=res.stderr.read()
#             if err:
#                 cmd_res=err
#             else:
#                 cmd_res=res.stdout.read()
#
#             #发
#             if not cmd_res:
#                 cmd_res='执行成功'.encode('gbk')
#             conn.send(cmd_res)
#         except Exception as e:
#             print(e)
#             break

#low版解决粘包版本
# from socket import *
# import subprocess
# ip_port=('127.0.0.1',8080)
# back_log=5
# buffer_size=1024
#
# tcp_server=socket(AF_INET,SOCK_STREAM)
# tcp_server.bind(ip_port)
# tcp_server.listen(back_log)
#
# while True:
#     conn,addr=tcp_server.accept()
#     print('新的客户端链接',addr)
#     while True:
#         #收
#         try:
#             cmd=conn.recv(buffer_size)
#             if not cmd:break
#             print('收到客户端的命令',cmd)
#
#             #执行命令，得到命令的运行结果cmd_res
#             res=subprocess.Popen(cmd.decode('utf-8'),shell=True,
#                                  stderr=subprocess.PIPE,
#                                  stdout=subprocess.PIPE,
#                                  stdin=subprocess.PIPE)
#             err=res.stderr.read()
#             if err:
#                 cmd_res=err
#             else:
#                 cmd_res=res.stdout.read()
#
#             #发
#             if not cmd_res:
#                 cmd_res='执行成功'.encode('gbk')
#
#             length=len(cmd_res)
#             conn.send(str(length).encode('utf-8'))
#             client_ready=conn.recv(buffer_size)
#             if client_ready == b'ready':
#                 conn.send(cmd_res)
#         except Exception as e:
#             print(e)
#             break



from socket import *
import subprocess
import struct
ip_port=('127.0.0.1',8080)
back_log=5
buffer_size=1024

tcp_server=socket(AF_INET,SOCK_STREAM)
tcp_server.bind(ip_port)
tcp_server.listen(back_log)

while True:
    conn,addr=tcp_server.accept()
    print('新的客户端链接',addr)
    while True:
        #收
        try:
            cmd=conn.recv(buffer_size)
            if not cmd:break
            print('收到客户端的命令',cmd)

            #执行命令，得到命令的运行结果cmd_res
            res=subprocess.Popen(cmd.decode('utf-8'),shell=True,
                                 stderr=subprocess.PIPE,
                                 stdout=subprocess.PIPE,
                                 stdin=subprocess.PIPE)
            err=res.stderr.read()
            if err:
                cmd_res=err
            else:
                cmd_res=res.stdout.read()

            #发
            if not cmd_res:
                cmd_res='执行成功'.encode('gbk')

            length=len(cmd_res)

            data_length=struct.pack('i',length)
            conn.send(data_length)
            conn.send(cmd_res)
        except Exception as e:
            print(e)
            break

