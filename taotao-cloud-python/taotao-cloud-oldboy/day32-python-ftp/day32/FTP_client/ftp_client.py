
import optparse
import socket
import configparser
import json
import os,sys

STATUS_CODE  = {
    250 : "Invalid cmd format, e.g: {'action':'get','filename':'test.py','size':344}",
    251 : "Invalid cmd ",
    252 : "Invalid auth data",
    253 : "Wrong username or password",
    254 : "Passed authentication",
    255 : "Filename doesn't provided",
    256 : "File doesn't exist on server",
    257 : "ready to send file",
    258 : "md5 verification",

    800 : "the file exist,but not enough ,is continue? ",
    801 : "the file exist !",
    802 : " ready to receive datas",

    900 : "md5 valdate success"

}

class ClientHandler():

    def __init__(self):
        self.op=optparse.OptionParser()

        self.op.add_option("-s","--server",dest="server")
        self.op.add_option("-P","--port",dest="port")
        self.op.add_option("-u","--username",dest="username")
        self.op.add_option("-p","--password",dest="password")

        self.options,self.args=self.op.parse_args()
        self.verify_args(self.options,self.args)
        self.make_connection()
        self.mainPath=os.path.dirname(os.path.abspath(__file__))
        self.last = 0

    def verify_args(self,options,args):
        server=options.server
        port=options.port
        if int(port)>0 and int(port)<65535:
            return True
        else:
            exit("the port is in 0-65535")

    def make_connection(self):
        self.sock=socket.socket()
        self.sock.connect((self.options.server,int(self.options.port)))


    def interactive(self):
        print("begin to interactive.......")

        if self.authenticate():
            while 1:
                cmd_info=input("[%s]"%self.current_dir).strip()# put 12.png images

                cmd_list=cmd_info.split()
                if hasattr(self,cmd_list[0]):
                    func=getattr(self,cmd_list[0])
                    func(*cmd_list)

    def put(self,*cmd_list):
        # put 12.png images
        action,local_path,target_path=cmd_list
        local_path=os.path.join(self.mainPath,local_path)

        file_name=os.path.basename(local_path)
        file_size=os.stat(local_path).st_size

        data={
            "action":"put",
            "file_name":file_name,
            "file_size":file_size,
            "target_path":target_path
        }

        self.sock.send(json.dumps(data).encode("utf8"))

        is_exist=self.sock.recv(1024).decode("utf8")
        ###################################################
        has_sent=0
        if is_exist=="800":
            #文件不完整
            choice=input("the file exist,but not enough,is continue?[Y/N]").strip()
            if choice.upper()=="Y":
                self.sock.sendall("Y".encode("utf8"))
                continue_position=self.sock.recv(1024).decode("utf8")
                has_sent+=int(continue_position)

            else:
                self.sock.sendall("N".encode("utf8"))

        elif is_exist=="801":
            #文件完全存在
           print("the file exist")
           return

        f=open(local_path,"rb")
        f.seek(has_sent)
        while has_sent<file_size:
            data=f.read(1024)
            self.sock.sendall(data)
            has_sent+=len(data)
            self.show_progress(has_sent,file_size)

        f.close()

        print("put success!")

    def show_progress(self,has,total):
        rate=float(has)/float(total)
        rate_num=int(rate*100)
        # if self.last!=rate_num:
        sys.stdout.write("%s%% %s\r"%(rate_num,"#"*rate_num))
        # self.last=rate_num


    def ls(self,*cmd_list):
        data={
            "action":"ls",
        }
        self.sock.sendall(json.dumps(data).encode("utf8"))

        data=self.sock.recv(1024).decode("utf8")
        print(data)


    def cd(self,*cmd_list):
        #cd images
        data={
            "action":"cd",
            "dirname":cmd_list[1]
        }
        self.sock.sendall(json.dumps(data).encode("utf8"))

        data = self.sock.recv(1024).decode("utf8")
        print(os.path.basename(data))
        self.current_dir=os.path.basename(data)


    def mkdir(self,*cmd_list):
        data={
            "action":"mkdir",
            "dirname":cmd_list[1]
        }

        self.sock.sendall(json.dumps(data).encode("utf8"))
        data = self.sock.recv(1024).decode("utf8")






















    def authenticate(self):

        if self.options.username is None or self.options.password is None:
            username=input("username: ")
            password=input("password: ")
            return self.get_auth_result(username,password)

        return self.get_auth_result(self.options.username,self.options.password)

    def response(self):
        data = self.sock.recv(1024).decode("utf8")
        data = json.loads(data)
        return data

    def get_auth_result(self,user,pwd):

        data={
            "action":"auth",
            "username":user,
            "password":pwd
        }

        self.sock.send(json.dumps(data).encode("utf8"))
        response=self.response()
        print("response:",response["status_code"])
        if response["status_code"]==254:
            self.user=user
            self.current_dir=user
            print(STATUS_CODE[254])
            return True
        else:
            print(STATUS_CODE[response["status_code"]])


ch=ClientHandler()

ch.interactive()




