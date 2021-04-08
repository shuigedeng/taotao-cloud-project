import asyncio

"""
@asyncio.coroutine
def task():
    print('before...task......')
    yield from asyncio.sleep(5) # 发送Http请求，支持TCP获取结果..
    print('end...task......')


tasks = [task(), task()]

loop = asyncio.get_event_loop()
loop.run_until_complete(asyncio.gather(*tasks))
loop.close()
"""


"""
import asyncio


@asyncio.coroutine
def task(host, url='/'):
    print('start',host,url)
    reader, writer = yield from asyncio.open_connection(host, 80)

    request_header_content = "GET %s HTTP/1.0\r\nHost: %s\r\n\r\n" % (url, host,)
    request_header_content = bytes(request_header_content, encoding='utf-8')

    writer.write(request_header_content)
    yield from writer.drain()
    text = yield from reader.read()
    print('end',host, url, text)
    writer.close()

tasks = [
    task('www.cnblogs.com', '/wupeiqi/'),
    task('dig.chouti.com', '/pic/show?nid=4073644713430508&lid=10273091')
]

loop = asyncio.get_event_loop()
results = loop.run_until_complete(asyncio.gather(*tasks))
loop.close()
"""

"""
import aiohttp
import asyncio


@asyncio.coroutine
def fetch_async(url):
    print(url)
    response = yield from aiohttp.request('GET', url)
    print(url, response)
    response.close()


tasks = [fetch_async('http://www.baidu.com/'), fetch_async('http://www.chouti.com/')]

event_loop = asyncio.get_event_loop()
results = event_loop.run_until_complete(asyncio.gather(*tasks))
event_loop.close()
"""

import asyncio
import requests


@asyncio.coroutine
def task(func, *args):
    print(func,args)
    loop = asyncio.get_event_loop()
    future = loop.run_in_executor(None, func, *args) # requests.get('http://www.cnblogs.com/wupeiqi/')
    response = yield from future
    print(response.url, response.content)


tasks = [
    task(requests.get, 'http://www.cnblogs.com/wupeiqi/'),
    task(requests.get, 'http://dig.chouti.com/pic/show?nid=4073644713430508&lid=10273091')
]

loop = asyncio.get_event_loop()
results = loop.run_until_complete(asyncio.gather(*tasks))
loop.close()