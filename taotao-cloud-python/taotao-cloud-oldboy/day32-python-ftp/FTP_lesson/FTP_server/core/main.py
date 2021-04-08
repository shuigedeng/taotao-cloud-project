
import optparse
import socketserver
from conf import settings
from core import server



class ArgvHandler(object):

    def __init__(self):
        self.op=optparse.OptionParser()

        # op.add_option("-s","--host",dest="host",help="server IP address")
        # op.add_option("-P","--port",dest="port",help="server port")

        options,args=self.op.parse_args()
        # print(options,args)
        # print(options.host,options.port)

        self.verify_argv(options,args)


    def verify_argv(self,options,args):

        if hasattr(self,args[0]):
            func=getattr(self,args[0])
            func()

        else:
            self.op.print_help()


    def start(self):
        print('server is working ....')
        ser=socketserver.ThreadingTCPServer((settings.IP,settings.PORT),server.ServerHandler)
        ser.serve_forever()










