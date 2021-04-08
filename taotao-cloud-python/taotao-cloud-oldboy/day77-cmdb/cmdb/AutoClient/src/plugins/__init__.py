#!/usr/bin/env python
# -*- coding:utf-8 -*-
from src.plugins.basic import BasicPlugin
from config import settings
import importlib


def get_server_info(hostname=None):
    """
    获取服务器基本信息
    :param hostname: agent模式时，hostname为空；salt或ssh模式时，hostname表示要连接的远程服务器
    :return:
    """
    response = BasicPlugin(hostname).execute()
    if not response.status:
        return response
    for k, v in settings.PLUGINS_DICT.items():
        module_path, cls_name = v.rsplit('.', 1)
        cls = getattr(importlib.import_module(module_path), cls_name)
        obj = cls(hostname).execute()
        response.data[k] = obj
    return response


if __name__ == '__main__':
    ret = get_server_info()
    print(ret.__dict__)