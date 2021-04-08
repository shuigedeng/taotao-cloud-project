"""
import gevent

import requests
from gevent import monkey
monkey.patch_all()


def task(method, url, req_kwargs):
    print(method, url, req_kwargs)
    response = requests.request(method=method, url=url, **req_kwargs)
    print(response.url, response.content)

# ##### 发送请求 #####
# gevent.joinall([
#     gevent.spawn(task, method='get', url='https://www.python.org/', req_kwargs={}),
#     gevent.spawn(task, method='get', url='https://www.yahoo.com/', req_kwargs={}),
#     gevent.spawn(task, method='get', url='https://github.com/', req_kwargs={}),
# ])
# ##### 发送请求（协程池控制最大协程数量） #####
from gevent.pool import Pool
pool = Pool(5)
gevent.joinall([
    pool.spawn(task, method='get', url='https://www.python.org/', req_kwargs={}),
    pool.spawn(task, method='get', url='https://www.yahoo.com/', req_kwargs={}),
    pool.spawn(task, method='get', url='https://www.github.com/', req_kwargs={}),
])
"""

import grequests


request_list = [
    grequests.get('http://httpbin.org/delay/1', timeout=0.001),
    grequests.get('http://fakedomain/'),
    grequests.get('http://httpbin.org/status/500')
]


# ##### 执行并获取响应列表 #####
response_list = grequests.map(request_list,size=5)
print(response_list)