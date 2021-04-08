"""
将用户配置和程序默认配置整合在一起
"""
import os
import importlib
from . import global_settings


class Settings(object):
    def __init__(self):
        # 获取程序默认配置
        for name in dir(global_settings):
            if name.isupper():
                value = getattr(global_settings, name)
                setattr(self, name, value)
        # 获取用户自定义配置，若和程序默认配置冲突，则覆写
        settings_module = os.environ.get('USER_SETTINGS')
        if not settings_module:
            return
        module = importlib.import_module(settings_module)
        for name in dir(module):
            if name.isupper():
                value = getattr(module, name)
                setattr(self, name, value)


settings = Settings()
