#_*_coding:utf-8_*_

from django import conf


for app in conf.settings.INSTALLED_APPS:
    try:
        admin_module = __import__("%s.kingadmin" % app)
    except ImportError:
        pass
