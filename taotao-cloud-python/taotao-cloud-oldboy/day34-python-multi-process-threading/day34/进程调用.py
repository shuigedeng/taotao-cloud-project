# from multiprocessing import Process
# import time
#
#
# def f(name):
#     time.sleep(1)
#     print('hello', name,time.ctime())
#
# if __name__ == '__main__':
#     p_list=[]
#     for i in range(3):
#
#         p = Process(target=f, args=('alvin',))
#         p_list.append(p)
#         p.start()
#
#     for i in p_list:
#         i.join()
#     print('end')


from multiprocessing import Process
import time

# class MyProcess(Process):
#
#     # def __init__(self):
#     #     super(MyProcess, self).__init__()
#     #     #self.name = name
#
#     def run(self):
#         time.sleep(1)
#         print ('hello', self.name,time.ctime())
#
#
# if __name__ == '__main__':
#     p_list=[]
#
#
#     for i in range(3):
#         p = MyProcess()
#         p.daemon=True
#         p.start()
#         p_list.append(p)
#
#     # for p in p_list:
#     #     p.join()
#
#     print('end')


from multiprocessing import Process
import os
import time


def info(title):
    print("title:", title)
    print('parent process:', os.getppid())
    print('process id:', os.getpid())

def f(name):

    info('function f')
    print('hello', name)


if __name__ == '__main__':

    info('main process line')

    time.sleep(1)
    print("------------------")
    p = Process(target=info, args=('yuan',))
    p.start()
    p.join()