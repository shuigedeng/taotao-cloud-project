# agent形式
# 1.采集资产
# 2.将资产数据发送到API（POST）

# SSH形式
# 1.获取今日未采集主机列表
# 2.采集资产
# 3.将资产数据发送到API（POST）

# Salt形式
# 1.获取今日未采集主机列表
# 2.采集资产
# 3.将资产数据发送到API（POST）

class BaseClient(object):
    def send_data(self, data_dict):
        pass


class Agent(BaseClient):
    def file_host(self):
        f = open('nid')
        data = f.read()
        f.close()
        if data:
            return data

    def process(self):
        # 1.采集资产
        from .plugins import pack
        data_dict = pack()
        hostname = self.file_host()
        if hostname:
            data_dict['hostname'] = hostname
        else:
            # 获取当前主机名
            # 写入 nid文件
            data_dict['hostname'] = "asdfasdf"

        # 2.将资产数据发送到API（POST）
        self.send_data(data_dict)


class SBaseClient(BaseClient):
    def get_host(self):
        pass


class SSH(SBaseClient):

    def process(self):
        # 1.获取今日未采集主机列表
        host_list = self.get_host()
        for host in host_list:
            # 2.采集资产
            data_dict = {}
            # 3.将资产数据发送到API（POST）
            self.send_data(data_dict)


class Salt(SBaseClient):

    def process(self):
        # 1.获取今日未采集主机列表
        host_list = self.get_host()
        for host in host_list:
            # 2.采集资产
            data_dict = {}
            # 3.将资产数据发送到API（POST）
            self.send_data(data_dict)
