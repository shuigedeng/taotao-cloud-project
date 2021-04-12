"""
用户自定义配置
"""
import os

BASEDIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
USER = 'JACK'
PASSWORD = '123'

SSH_USER = 'root'
SSH_PWD = '123456'
SSH_PORT = 22
SSH_KEY = 'SSH_KEY.FILE'

MODE = 'AGENT'  # AGENT, SSH, SALT三种
DEBUG = True  # 开启调试模式，并不在服务器执行数据采集命令，而是从本地文件中获取采集信息

# 需要采集的硬件信息
PLUGIN_DICT = {
    'Basic': 'src.plugins.basic.Basic',
    'Board': 'src.plugins.board.Board',
    'Cpu': 'src.plugins.cpu.Cpu',
    'Disk': 'src.plugins.disk.Disk',
    'Memory': 'src.plugins.memory.Memory',
    'Nic': 'src.plugins.nic.Nic',
}

CERT_PATH = os.path.join(BASEDIR, 'config', 'cert')
API = 'http://127.0.0.1:8000/api/asset.html/'
# API = 'http://127.0.0.1:8000/api/test.html/'

AUTHKEY = 'thisisanauthkeyforapiaccess'  # API加密Key
DATAKEY = b'thiskeymustbesix'  # 数据通信加密Key，必须为16位
