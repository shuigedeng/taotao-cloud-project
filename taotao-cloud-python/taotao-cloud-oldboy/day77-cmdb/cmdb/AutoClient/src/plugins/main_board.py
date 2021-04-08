#!/usr/bin/env python
# -*- coding:utf-8 -*-
import os
import traceback
from .base import BasePlugin
from lib.response import BaseResponse


class MainBoardPlugin(BasePlugin):
    def linux(self):
        response = BaseResponse()
        try:
            if self.test_mode:
                from config.settings import BASEDIR

                output = open(os.path.join(BASEDIR, 'files/board.out'), 'r').read()
            else:
                shell_command = "sudo dmidecode -t1"
                output = self.exec_shell_cmd(shell_command)
            response.data = self.parse(output)
        except Exception as e:
            msg = "%s linux mainboard plugin error: %s"
            self.logger.log(msg %(self.hostname, traceback.format_exc()), False)
            response.status = False
            response.error = msg %(self.hostname, traceback.format_exc())
        return response

    def parse(self, content):

        result = {}
        key_map = {
            'Manufacturer': 'manufacturer',
            'Product Name': 'model',
            'Serial Number': 'sn',
        }

        for item in content.split('\n'):
            row_data = item.strip().split(':')
            if len(row_data) == 2:
                if row_data[0] in key_map:
                    result[key_map[row_data[0]]] = row_data[1].strip() if row_data[1] else row_data[1]

        return result