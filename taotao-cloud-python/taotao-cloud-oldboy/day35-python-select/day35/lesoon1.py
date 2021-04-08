import time
from multiprocessing import Process

class MyProcess(Process):

    def __init__(self,num):
        super(MyProcess,self).__init__()

        self.num=num

    def run(self):
        time.sleep(1)
        print(self.pid)
        print(self.is_alive())
        print(self.num)
        time.sleep(1)

if __name__ == '__main__':
    p_list=[]
    for i in range(10):
        p = MyProcess(i)
        #p.daemon=True
        p_list.append(p)

    for p in p_list:
        p.start()
    # for p in p_list:
    #     p.join()

    print('main process end')

