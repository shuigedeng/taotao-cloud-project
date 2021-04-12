import requests
import json
import hashlib
import time
from Crypto.Cipher import AES
from src.plugins import PluginManager
from lib.conf.config import settings
from concurrent.futures import ThreadPoolExecutor


class Base(object):
    pool = ThreadPoolExecutor(10)

    def api_encrypt(self):
        """
        api接口验证
        :return:
        """
        key = settings.AUTHKEY
        ctime = time.time()
        auth_key = "%s|%s" % (key, ctime)
        m = hashlib.md5()
        m.update(bytes(auth_key, encoding='utf-8'))
        md5_key = m.hexdigest()
        md5_time_key = "%s|%s" % (md5_key, ctime)
        return md5_time_key

    def data_encrypt(self, data):
        """
        post数据加密
        :param data:
        :return:
        """
        key = settings.DATAKEY
        cipher = AES.new(key, AES.MODE_CBC, key)
        ba_data = bytearray(data, encoding='utf-8')
        v1 = len(ba_data)
        v2 = v1 % 16
        if v2 == 0:
            v3 = 16
        else:
            v3 = 16 - v2
        for i in range(v3):
            ba_data.append(v3)
        final_data = ba_data.decode('utf-8')
        # print(type(final_data), 'fina_data')
        cipher_data = cipher.encrypt(bytes(final_data, encoding='utf-8'))
        return cipher_data

    def post_asset(self, server_info):
        """向API发送数据"""
        server_info = json.dumps(server_info)
        requests.post(settings.API, headers={'OpenKey': self.api_encrypt()}, data=self.data_encrypt(server_info))


class Agent(Base):
    """
    Agent方式采集资产信息
    """
    def execute(self):
        server_info = PluginManager().exec_plugin()
        hostname = server_info['Basic']['data']['hostname']
        cert_hostname = open(settings.CERT_PATH, 'r', encoding='utf-8').read().strip()
        if not cert_hostname:
            with open(settings.CERT_PATH, 'w', encoding='utf-8') as f: f.write(hostname)
        else:
            server_info['Basic']['data']['hostname'] = cert_hostname
        self.post_asset(server_info)
        # print(server_info)


class SSH_SALT(Base):
    """
    SSH或RPC方式采集资产信息
    """
    def get_host(self):
        # 需要先从后台获取需要收集信息的主机列表
        response = requests.get(settings.API)
        # {'stauts': True, 'data': ['host1', 'host2',]}
        result = json.loads(response.text)
        if not result['status']:
            return
        return result['data']

    def run(self, host):
        server_info = PluginManager(hostname=host).exec_plugin()
        self.post_asset(server_info)

    def execute(self):
        host_list = self.get_host()
        for host in host_list:
            self.pool.submit(self.run, host)
