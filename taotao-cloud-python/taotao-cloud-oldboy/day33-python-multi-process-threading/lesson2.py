


# import threading
# import time
#
#
# def music():
#     print("begin to listen %s"%time.ctime())
#     time.sleep(3)
#     print("stop to listen %s" % time.ctime())
#
#
# def game():
#     time.sleep(4)
#     t3=threading.Thread(target=music)
#     t3.start()
#
#     print("begin to play game %s"%time.ctime())
#     time.sleep(5)
#     print("stop to play game %s" % time.ctime())
#
#
# if __name__ == '__main__':
#
#     t1=  threading.Thread(target=music)
#
#     t2 = threading.Thread(target=game)
#
#     t1.start()
#     t2.start()
#
#     t1.join()
#     t2.join()
#
#     print("ending")
#


import threading
from time import ctime,sleep
import time

def ListenMusic(name):

        print ("Begin listening to %s. %s" %(name,ctime()))
        sleep(3)
        print("end listening %s"%ctime())


def RecordBlog(title):

        print ("Begin recording the %s! %s" %(title,ctime()))
        sleep(5)
        print('end recording %s'%ctime())
#
# threads = []
#
# t1 = threading.Thread(target=ListenMusic,args=('水手',))
# t2 = threading.Thread(target=RecordBlog,args=('python线程',))
#
# threads.append(t1)
# threads.append(t2)

# if __name__ == '__main__':
#     #t1.setDaemon(True)
#     t2.setDaemon(True)
#
#     for t in threads:
#         #t.setDaemon(True) #注意:一定在start之前设置
#         t.start()
#         print(t.getName())
#         print("count:",threading.active_count())
#         #t.join()#串行
#     #t.join()
#
#     #t1.join()
#     #t1.setDaemon(True)
#
#     #t2.join()########考虑这三种join位置下的结果？
#
#     while threading.active_count()==1:
#
#         print ("all over %s" %ctime())


# 调用方式2：#######################################

import threading
import time


class MyThread(threading.Thread):

    def __init__(self, num):
        threading.Thread.__init__(self)
        self.num = num

    def run(self):  # 定义每个线程要运行的函数

        print("running on number:%s" % self.num)

        time.sleep(3)

if __name__ == '__main__':

    t1 = MyThread(1)
    t2 = MyThread(2)
    t1.start()
    t2.start()
    print("ending......")










