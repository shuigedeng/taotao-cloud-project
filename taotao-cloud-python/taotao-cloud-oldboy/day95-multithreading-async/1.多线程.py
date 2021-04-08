"""
可以实现并发
但是，请求发送出去后和返回之前，中间时期线程空闲
编写方式：
    - 直接返回处理
    - 通过回调函数处理
"""

########### 编写方式一 ###########
"""
from concurrent.futures import ThreadPoolExecutor
import requests
import time

def task(url):
    response = requests.get(url)
    print(url,response)
    # 写正则表达式


pool = ThreadPoolExecutor(7)
url_list = [
    'http://www.cnblogs.com/wupeiqi',
    'http://huaban.com/favorite/beauty/',
    'http://www.bing.com',
    'http://www.zhihu.com',
    'http://www.sina.com',
    'http://www.baidu.com',
    'http://www.autohome.com.cn',
]
for url in url_list:
    pool.submit(task,url)

pool.shutdown(wait=True)
"""

########### 编写方式二 ###########
from concurrent.futures import ThreadPoolExecutor
import requests
import time

def task(url):
    """
    下载页面
    :param url:
    :return:
    """
    response = requests.get(url)
    return response

def done(future,*args,**kwargs):
    response = future.result()
    print(response.status_code,response.content)

pool = ThreadPoolExecutor(7)
url_list = [
    'http://www.cnblogs.com/wupeiqi',
    'http://huaban.com/favorite/beauty/',
    'http://www.bing.com',
    'http://www.zhihu.com',
    'http://www.sina.com',
    'http://www.baidu.com',
    'http://www.autohome.com.cn',
]
for url in url_list:
    v = pool.submit(task,url)
    v.add_done_callback(done)

pool.shutdown(wait=True)




