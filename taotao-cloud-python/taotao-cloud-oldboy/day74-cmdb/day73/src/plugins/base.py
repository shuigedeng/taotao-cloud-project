from conf import settings

class BasePlugin(object):

    def __init__(self):
        mode_list = ['SSH','Salt',"Agent"]
        if settings.MODE in mode_list:
            self.mode = settings.MODE
        else:
            raise Exception('配置文件错误')

    def ssh(self,cmd):
        pass

    def agent(self,cmd):
        pass

    def salt(self,cmd):
        pass

    def shell_cmd(self,cmd):
        if self.mode == 'SSH':
            ret = self.ssh(cmd)
        elif self.mode == 'Salt':
            ret = self.salt(cmd)
        else:
            ret = self.agent(cmd)
        return ret

    def execute(self):

        ret = self.shell_cmd('查看平台命令')

        if ret == 'win':
            return self.windows()
        elif ret == 'linux':
            return self.linux()
        else:
            raise Exception('只支持windows和linux')

    def linux(self):
        raise Exception('....')

    def windows(self):
        raise Exception('....')
