

import threading #线程
import time

def Hi(num):
    print("hello %d"%num)
    time.sleep(3)


if __name__ == '__main__':

    t1=threading.Thread(target=Hi,args=(10,))#创建了一个线程对象t1
    t1.start()

    t2 = threading.Thread(target=Hi, args=(9,))  # 创建了一个线程对象t1
    t2.start()

    print("ending..........")









