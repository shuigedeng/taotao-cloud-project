#!/usr/bin/env python
# -*- coding:utf-8 -*-
from twisted.internet import defer
from twisted.web.client import getPage
from twisted.internet import reactor

def one_done(arg):
    print(arg)

def all_done(arg):
    print('done')
    reactor.stop()

@defer.inlineCallbacks
def task(url):
    res = getPage(bytes(url, encoding='utf8')) # 发送Http请求
    res.addCallback(one_done)
    yield res

url_list = [
    'http://www.cnblogs.com',
    'http://www.cnblogs.com',
    'http://www.cnblogs.com',
    'http://www.cnblogs.com',
]

defer_list = [] # [特殊，特殊，特殊(已经向url发送请求)]
for url in url_list:
    v = task(url)
    defer_list.append(v)

d = defer.DeferredList(defer_list)
d.addBoth(all_done)


reactor.run() # 死循环