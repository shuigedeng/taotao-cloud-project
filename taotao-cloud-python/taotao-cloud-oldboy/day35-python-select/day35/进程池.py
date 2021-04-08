from  multiprocessing import Process,Pool
import time,os

def Foo(i):

    time.sleep(1)
    print(i)
    print("son",os.getpid())

    return "HELLO %s"%i


def Bar(arg):
    print(arg)
    # print("hello")
    # print("Bar:",os.getpid())

if __name__ == '__main__':

    pool = Pool(5)
    print("main pid",os.getpid())
    for i in range(100):
        #pool.apply(func=Foo, args=(i,))  #同步接口
        #pool.apply_async(func=Foo, args=(i,))

        #回调函数：  就是某个动作或者函数执行成功后再去执行的函数

        pool.apply_async(func=Foo, args=(i,),callback=Bar)

    pool.close()
    pool.join()         # join与close调用顺序是固定的

    print('end')