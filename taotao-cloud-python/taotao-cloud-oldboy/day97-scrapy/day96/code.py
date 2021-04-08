from twisted.internet import defer
from twisted.web.client import getPage
from twisted.internet import reactor
# from scrapy.core.engine import ExecutionEngine
"""
def download(*args,**kwargs):
    print(args,kwargs)

@defer.inlineCallbacks
def task(url):
    v = getPage(url.encode('utf-8'))
    v.addCallback(download)
    yield v

if __name__ == '__main__':
    url_list = [
        "http://www.baidu.com",
        "http://www.bing.com",
        "http://dig.chouti.com",
    ]
    for url in url_list:
        ret = task(url)
    reactor.run()
"""

"""
def download(*args,**kwargs):
    print(args,kwargs)

def stop(*args,**kwargs):
    reactor.stop()

@defer.inlineCallbacks
def task(url):
    v = getPage(url.encode('utf-8'))
    v.addCallback(download)
    yield v

if __name__ == '__main__':
    url_list = [
        "http://www.baidu.com",
        "http://www.bing.com",
        "http://dig.chouti.com",
    ]
    li = []
    for url in url_list:
        ret = task(url)
        li.append(ret)

    all_task = defer.DeferredList(li)
    all_task.addBoth(stop)
    reactor.run()

"""

def download(response,*args,**kwargs):
    print(response)
    return response

def stop(*args,**kwargs):
    print(args,kwargs)
    reactor.stop()

@defer.inlineCallbacks
def task(url):
    while url:
        v = getPage(url.encode('utf-8'))
        v.addCallback(download)
        response = yield v




if __name__ == '__main__':
    url_list = [
        "http://www.baidu.com",
        "http://www.bing.com",
        "http://dig.chouti.com",
    ]
    li = []
    for url in url_list:
        ret = task(url)
        print(ret)
        li.append(ret)

    all_task = defer.DeferredList(li)
    all_task.addBoth(stop)
    reactor.run()
