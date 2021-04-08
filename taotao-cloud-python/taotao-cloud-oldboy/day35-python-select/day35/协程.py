# import time
# import queue
#
# def consumer(name):
#
#     print("--->ready to eat baozi...")
#     while True:
#         new_baozi = yield
#         print("[%s] is eating baozi %s" % (name,new_baozi))
#         #time.sleep(1)

# def producer():
#
#     r = con.__next__()
# #     r = con2.__next__()
# #
# #     n = 0
# #     while 1:
# #         time.sleep(1)
# #         print("\033[32;1m[producer]\033[0m is making baozi %s and %s" %(n,n+1) )
# #         con.send(n)
# #         con2.send(n+1)
# #         n +=2
# #
# #
# # if __name__ == '__main__':
# #
# #     con = consumer("c1")
# #     con2 = consumer("c2")
# #     producer()


# from greenlet import greenlet
#
# def test1():
#     print(12)
#     gr2.switch()
#     print(34)
# def test2():
#     print(56)
#     gr1.switch()
#     print(78)
#     gr1.switch()
#
# gr1 = greenlet(test1)
# gr2 = greenlet(test2)
# gr2.switch()


import gevent
import requests,time
start=time.time()
def f(url):
    print('GET: %s' % url)
    resp =requests.get(url)
    data = resp.text
    print('%d bytes received from %s.' % (len(data), url))

f('https://www.python.org/')
f('https://www.yahoo.com/')
f('https://www.baidu.com/')
f('https://www.sina.com.cn/')
f("http://www.xiaohuar.com/hua/")

# gevent.joinall([
#         gevent.spawn(f, 'https://www.python.org/'),
#         gevent.spawn(f, 'https://www.yahoo.com/'),
#         gevent.spawn(f, 'https://www.baidu.com/'),
#         gevent.spawn(f, 'https://www.sina.com.cn/'),
#         gevent.spawn(f, 'http://www.xiaohuar.com/hua/'),
# ])

# f('https://www.python.org/')
#
# f('https://www.yahoo.com/')
#
# f('https://baidu.com/')

# f('https://www.sina.com.cn/')

print("cost time:",time.time()-start)


