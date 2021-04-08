


import threading


# def foo():
#     print("ok")
#
#
# t1=threading.Thread(target=foo)
# t1.start()
#
# print("ending...")



# class MyThread(threading.Thread):
#
#     def run(self):
#         print("ok")
#
#
# t1=MyThread()
# t1.start()
# print('ending')


import threading
from time import ctime,sleep
import time

def ListenMusic(name):

        print ("Begin listening to %s. %s" %(name,ctime()))
        sleep(2)#  sleep等同于IO操作
        print("end listening %s"%ctime())

def RecordBlog(title):

        print ("Begin recording the %s! %s" %(title,ctime()))
        sleep(5)
        print('end recording %s'%ctime())


threads = []


t1 = threading.Thread(target=ListenMusic,args=('水手',))
t2 = threading.Thread(target=RecordBlog,args=('python线程',))

threads.append(t1)
threads.append(t2)

if __name__ == '__main__':
    #t1.setDaemon(True)
    t2.setDaemon(True)
    for t in threads:
        #t.setDaemon(True) #注意:一定在start之前设置
        t.start()
        # t.join()

    #t1.join()
    #t1.setDaemon(True)

    #t2.join()########考虑这三种join位置下的结果？
    print ("all over %s" %ctime())
