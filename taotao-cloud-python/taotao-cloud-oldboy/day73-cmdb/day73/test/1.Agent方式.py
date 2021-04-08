
# 这个文件放到每台服务器上面部署

import subprocess
import requests
# pip3 install requests

# 1 ################## 采集数据 ##################
# net_result = subprocess.getoutput('ipconfig')
# mem_result = subprocess.getoutput('free -mh')

# 2 result正则处理获取想要数据

# 3 整理资产信息
# data_dict ={
#     'nic': {},
#     'disk':{},
#     'mem':{}
# }

# 4 ##################  发送数据 ##################
# requests.post('http://www.127.0.0.1:8000/assets.html',data=data_dict)