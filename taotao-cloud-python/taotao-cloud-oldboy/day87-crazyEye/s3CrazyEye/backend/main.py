

class ArgvHandler(object):
    """接收用户参数,并调用相应的功能"""
    def __init__(self,sys_args):
        self.sys_args = sys_args
    def help_msg(self,error_msg=''):
        """打印帮助"""
        msgs = """
        %s
        run    启动用户交互程序
        """ % error_msg

        exit(msgs)
    def call(self):
        """根据用户参数,调用对应的方法"""
        if len(self.sys_args) == 1:
            self.help_msg()

        if hasattr(self,self.sys_args[1]):
            func = getattr(self,self.sys_args[1])
            func()
        else:
            self.help_msg("没有方法:%s"% self.sys_args[1])


    def run(self):
        """启动用户交互程序"""
        from  backend.ssh_interactive import SshHandler
        obj = SshHandler(self)
        obj.interactive()

