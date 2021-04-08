
import optparse
import socket
import json,os,sys,time,hashlib

STATUS_CODE  = {
    250 : "Invalid cmd format, e.g: {'action':'get','filename':'test.py','size':344}",
    251 : "Invalid cmd ",
    252 : "Invalid auth data",
    253 : "Wrong username or password",
    254 : "Passed authentication",

    800 : "the file exist,but not enough ,is continue? ",
    801 : "the file exist !",
    802 : " ready to receive datas",

    900 : "md5 valdate success"

}

class ClientHanlder(object):

    def __init__(self):

        self.op=optparse.OptionParser()
        self.op.add_option("-s","--server",dest="server")
        self.op.add_option("-P","--port",dest="port")
        self.op.add_option("-u","--username",dest="username")
        self.op.add_option("-p","--password",dest="password")
        self.options , self.args=self.op.parse_args()
        self.verify_args()
        self.make_connection()
        self.mainPath=os.path.dirname(os.path.abspath(__file__))
        self.last=0



    def verify_args(self,):
        if self.options.server and self.options.port:
            #print(options)
            if int(self.options.port) >0 and int(self.options.port) <65535:
                return True
            else:
                exit("Err:host port must in 0-65535")

    def make_connection(self):

        self.sock = socket.socket()
        self.sock.connect((self.options.server,int(self.options.port)))


    def interactive(self):

        if self.authenticate():

            print("---start interactive with you ...")
            while 1:
                cmd_info=input("[%s]"%self.current_path).strip()
                if len(cmd_info)==0:continue
                cmd_list = cmd_info.split()
                if hasattr(self,"_%s"%cmd_list[0]):
                    func=getattr(self,"_%s"%cmd_list[0])
                    func(cmd_list)

                else:
                    print("Invalid cmd")




    def authenticate(self):

        if self.options.username and self.options.password:
            return self.get_auth_result(self.options.username,self.options.password)
        else:

            username=input("username: ")
            password=input("password: ")
            return self.get_auth_result(username,password)

    def get_auth_result(self,username,password):

        data = {'action':'auth',
                'username':username,
                'password':password}

        self.sock.send(json.dumps(data).encode())
        response = self.get_response()
        print("response",response)
        if response.get('status_code') == 254:
            print("Passed authentication!")
            self.user = username
            self.current_path = '/'+username

            return True
        else:
            print(response.get("status_msg"))

    def get_response(self):

        data = self.sock.recv(1024)
        data = json.loads(data.decode())
        return data

    def progress_percent(self,has,total,):


        rate = float(has) / float(total)
        rate_num = int(rate * 100)

        if self.last!=rate_num:

            sys.stdout.write("%s%% %s\r"%(rate_num,"#"*rate_num))

        self.last=rate_num


    def _post(self,cmd_list):

        action,local_path,target_path=cmd_list

        if "/" in local_path:
            local_path=os.path.join(self.mainPath,local_path.split("/"))
        local_path=os.path.join(self.mainPath,local_path)

        file_name=os.path.basename(local_path)
        file_size=os.stat(local_path).st_size

        data_header = {
            'action':'post',
            'file_name': file_name,
            'file_size': file_size,
            'target_path':target_path
         }

        self.sock.send(json.dumps(data_header).encode())

        result_exist=str(self.sock.recv(1024),"utf8")
        has_sent=0

        if result_exist=="800":
            choice=input("the file exist ,is_continue?").strip()
            if choice.upper()=="Y":
                self.sock.sendall(bytes("Y","utf8"))
                result_continue_pos=str(self.sock.recv(1024),"utf8")
                print(result_continue_pos)
                has_sent=int(result_continue_pos)

            else:
                self.sock.sendall(bytes("N","utf8"))

        elif result_exist=="801":
            print(STATUS_CODE[801])
            return


        file_obj=open(local_path,"rb")
        file_obj.seek(has_sent)
        start=time.time()

        while has_sent<file_size:

            data=file_obj.read(1024)
            self.sock.sendall(data)
            has_sent+=len(data)

            self.progress_percent(has_sent,file_size)

        file_obj.close()
        end=time.time()
        print("cost %s s"% (end-start))
        print("post success!")

    def _post_md5(self,cmd_list):


        if len(cmd_list)==3:

            action,local_path,target_path=cmd_list
        else:
            action,local_path,target_path,is_md5=cmd_list

        if "/" in local_path:
            local_path=os.path.join(self.mainPath,local_path.split("/"))
        local_path=os.path.join(self.mainPath,local_path)

        file_name=os.path.basename(local_path)
        file_size=os.stat(local_path).st_size

        data_header = {
            'action':'post_md5',
            'file_name': file_name,
            'file_size': file_size,
            'target_path':target_path
         }

        if self.__md5_required(cmd_list):
            data_header['md5'] = True


        self.sock.send(json.dumps(data_header).encode())

        result_exist=str(self.sock.recv(1024),"utf8")
        has_sent=0

        if result_exist=="800":
            choice=input("the file exist ,is_continue?").strip()
            if choice.upper()=="Y":
                self.sock.sendall(bytes("Y","utf8"))
                result_continue_pos=str(self.sock.recv(1024),"utf8")
                print(result_continue_pos)
                has_sent=int(result_continue_pos)

            else:
                self.sock.sendall(bytes("N","utf8"))

        elif result_exist=="801":
            print(STATUS_CODE[801])
            return


        file_obj=open(local_path,"rb")
        file_obj.seek(has_sent)
        start=time.time()

        if self.__md5_required(cmd_list):
                md5_obj = hashlib.md5()

                while has_sent<file_size:

                    data=file_obj.read(1024)
                    self.sock.sendall(data)
                    has_sent+=len(data)
                    md5_obj.update(data)
                    self.progress_percent(has_sent,file_size)

                else:
                    print("post success!")
                    md5_val = md5_obj.hexdigest()
                    self.sock.recv(1024)#解决粘包
                    self.sock.sendall(md5_val.encode("utf8"))
                    response=self.sock.recv(1024).decode("utf8")
                    print("response",response)
                    if response=="900":
                        print(STATUS_CODE[900])

        else:
                while has_sent<file_size:

                    data=file_obj.read(1024)
                    self.sock.sendall(data)
                    has_sent+=len(data)

                    self.progress_percent(has_sent,file_size)

                else:
                    file_obj.close()
                    end=time.time()
                    print("\ncost %s s"% (end-start))
                    print("post success!")



    def __md5_required(self,cmd_list):
        '''检测命令是否需要进行MD5验证'''
        if '--md5' in cmd_list:
            return True


    def _ls(self,cmd_list):

        data_header = {
            'action':'ls',
        }
        self.sock.send(json.dumps(data_header).encode())

        data = self.sock.recv(1024)

        print(data.decode("utf8"))


    def _cd(self,cmd_list):

        data_header = {

             'action':'cd',

             'path':cmd_list[1]
         }

        self.sock.send(json.dumps(data_header).encode())

        data = self.sock.recv(1024)
        print(data.decode("utf8"))
        self.current_path='/'+os.path.basename(data.decode("utf8"))

    def _mkdir(self,cmd_list):

        data_header = {
            'action':'mkdir',
             'dirname':cmd_list[1]
         }

        self.sock.send(json.dumps(data_header).encode())
        data = self.sock.recv(1024)
        print(data.decode("utf8"))

    def _rmdir(self,cmd_list):
        data_header = {
             'action':'rm',
             'target_path':cmd_list[1]
         }
        self.sock.send(json.dumps(data_header).encode())
        data = self.sock.recv(1024)
        print(data.decode("utf8"))


    def _pwd(self,cmd_list):

        data_header = {
            'action':'pwd',
         }

        self.sock.send(json.dumps(data_header).encode())
        data = self.sock.recv(1024)
        print(data.decode("utf8"))


ch=ClientHanlder()
ch.interactive()


