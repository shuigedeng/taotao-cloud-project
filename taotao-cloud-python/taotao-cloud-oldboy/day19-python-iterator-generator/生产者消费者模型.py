import time
# def producer():
#     ret=[]
#     for i in range(100):
#         time.sleep(0.1)
#         ret.append('包子%s' %i)
#     return ret
#
# def consumer(res):
#     for index,baozi in enumerate(res):
#         time.sleep(0.1)
#         print('第%s个人，吃了%s' %(index,baozi))
#
# res=producer()
# consumer(res)




#yield 3相当于return 控制的是函数的返回值
#x=yield的另外一个特性，接受send传过来的值,赋值给x
# def test():
#     print('开始啦')
#     firt=yield #return 1   first=None
#     print('第一次',firt)
#     yield 2
#     print('第二次')
#
# t=test()
# res=t.__next__() #next(t)
# print(res)
# # t.__next__()
# # res=t.send(None)
# res=t.send('函数停留在first那个位置，我就是给first赋值的')
# print(res)





# def producer():
#     ret=[]
#     for i in range(100):
#         time.sleep(0.1)
#         ret.append('包子%s' %i)
#     return ret

def consumer(name):
    print('我是[%s],我准备开始吃包子了' %name)
    while True:
        baozi=yield
        time.sleep(1)
        print('%s 很开心的把【%s】吃掉了' %(name,baozi))

def producer():
    c1=consumer('wupeiqi')
    c2=consumer('yuanhao_SB')
    c1.__next__()
    c2.__next__()
    for i in range(10):
        time.sleep(1)
        c1.send('包子 %s' %i)
        c2.send('包子 %s' %i)
producer()


