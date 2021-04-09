#!/usr/bin/env python
# -*- coding:utf-8 -*-
import os
from lib.conf.config import settings


class Basic(object):
    """
    基本信息采集
    """
    def __init__(self):
        pass

    @classmethod
    def initial(cls):
        return cls()

    def process(self, command_func, debug):
        if debug:
            output = {
                'os_platform': "linux",
                'os_version': "CentOS release 6.6 (Final)\nKernel \r on an \m",
                'hostname': 'c1.com'
            }
        else:
            output = {
                'os_platform': command_func("uname").strip(),
                'os_version': command_func("cat /etc/issue").strip().split('\n')[0],
                'hostname': command_func("hostname").strip(),
            }
        return output
