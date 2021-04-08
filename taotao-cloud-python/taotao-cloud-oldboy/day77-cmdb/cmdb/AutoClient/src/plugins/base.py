#!/usr/bin/env python
# -*- coding:utf-8 -*-
from lib.log import Logger
from config import settings


class BasePlugin(object):
    def __init__(self, hostname=''):
        self.logger = Logger()
        self.test_mode = settings.TEST_MODE
        self.mode_list = ['agent', 'salt', 'ssh']
        if hasattr(settings, 'MODE'):
            self.mode = settings.MODE
        else:
            self.mode = 'agent'
        self.hostname = hostname

    def salt(self, cmd, ):
        import salt.client

        local = salt.client.LocalClient()
        result = local.cmd(self.hostname, 'cmd.run', [cmd])
        return result[self.hostname]

    def ssh(self, cmd):
        import paramiko

        private_key = paramiko.RSAKey.from_private_key_file(settings.SSH_PRIVATE_KEY)
        ssh = paramiko.SSHClient()
        ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        ssh.connect(hostname=self.hostname, port=settings.SSH_PORT, username=settings.SSH_USER, pkey=private_key)
        stdin, stdout, stderr = ssh.exec_command(cmd)
        result = stdout.read()
        ssh.close()
        return result

    def agent(self, cmd):
        import subprocess

        output = subprocess.getoutput(cmd)
        return output

    def exec_shell_cmd(self, cmd):
        if self.mode not in self.mode_list:
            raise Exception("settings.mode must be one of ['agent', 'salt', 'ssh']")
        func = getattr(self, self.mode)
        output = func(cmd)
        return output

    def execute(self):
        return self.linux()

    def linux(self):
        raise Exception('You must implement linux method.')
