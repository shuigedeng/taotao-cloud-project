#_*_coding:utf-8_*_
__author__ = 'Alex Li'
#通过生成器实现协程并行运算
import time
def consumer(name):
    print("%s 准备吃包子啦!" %name)
    while True:
       baozi = yield 1

       print("包子[%s]来了,被[%s]吃了!" %(baozi,name))


def producer(name):
    c = consumer('A')
    c2 = consumer('B')
    print( c.__next__())
    print(c2.__next__())
    print("老子开始准备做包子啦!")
    for i in range(10):
        time.sleep(1)
        print("做了2个包子!")
        c.send(i)
        c2.send(i)

producer("alex")



