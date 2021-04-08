from conf import settings
from . import client


def run():
    if settings.MODE == "Agent":
        client.Agent().process()
    elif settings.MODE == "Ssh":
        client.SSH().process()
    elif settings.MODE == "Salt":
        client.Salt().process()
    else:
        raise Exception('配置文件模式错误')
