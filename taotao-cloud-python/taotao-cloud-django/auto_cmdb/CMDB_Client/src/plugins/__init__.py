import importlib
import traceback
from lib.conf.config import settings


class PluginManager(object):
    """资产采集插件管理"""
    def __init__(self, hostname=None):
        self.hostname = hostname
        self.plugin_dict = settings.PLUGIN_DICT
        self.mode = settings.MODE
        self.debug = settings.DEBUG
        if self.mode == 'SSH':
            self.ssh_user = settings.SSH_USER
            self.ssh_pwd = settings.SSH_PWD
            self.ssh_port = settings.SSH_PORT
            self.ssh_key = settings.SSH_KEY

    def exec_plugin(self):
        """
        获取所有插件并执行
        :return: 采集结果
        """
        response = {}
        for k, v in self.plugin_dict.items():
            tmp = {'status': True, 'data': None}
            try:
                plugin_module, plugin_class = v.rsplit('.', 1)
                module = importlib.import_module(plugin_module)
                cls = getattr(module, plugin_class)
                if hasattr(cls, 'initial'):
                    obj = cls().initial()
                else:
                    obj = cls()
                tmp['data'] = obj.process(self.command, self.debug)
            except Exception:
                tmp['status'] = False
                tmp['data'] = '%s主机的%s硬件采集失败，错误信息：%s' % (self.hostname, k, traceback.format_exc())
            response[k] = tmp
        return response

    def command(self, cmd):
        """
        模式选择
        :param cmd: 资产采集命令
        :return:
        """
        if self.mode == 'AGENT':
            return self.__agent(cmd)
        elif self.mode == 'SSH':
            return self.__ssh(cmd)
        elif self.mode == 'SALT':
            return self.__salt(cmd)
        else:
            raise Exception('模式必须为AGENT/SSH/SALT三者之一')

    def __agent(self, cmd):
        """Agent类型"""
        import subprocess
        output = subprocess.getoutput(cmd)
        return output

    def __ssh(self, cmd):
        """SSH类型（paramiko）"""
        import paramiko
        ssh = paramiko.SSHClient()
        ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        ssh.connect(hostname=self.hostname, username=self.ssh_user, password=self.ssh_pwd, port=self.ssh_port)
        stdin, stdout, stderr = ssh.exec_command(cmd)
        output = stdout.read()
        ssh.close()
        return output

    def __salt(self, cmd):
        """
        RPC类型（SaltStack）
        import salt.client
        local = salt.client.LocalClient()
        result = local.cmd(self.hostname, 'cmd.run', [cmd])
        return result[self.hostname]
        由于salt对python3不完全支持，所以借用subprocess模块执行salt命令
        :param cmd:
        :return:
        """
        import subprocess
        salt_cmd = 'salt "%s" cmd.run "%s"' % (self.hostname, cmd)
        output = subprocess.getoutput(salt_cmd)
        return output