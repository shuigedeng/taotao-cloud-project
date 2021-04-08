import  threading
import time


class MyThread(threading.Thread):

    def actionA(self):

        r_lcok.acquire() #count=1
        print(self.name,"gotA",time.ctime())
        time.sleep(2)
        r_lcok.acquire() #count=2

        print(self.name, "gotB", time.ctime())
        time.sleep(1)

        r_lcok.release() #count=1
        r_lcok.release() #count=0


    def actionB(self):

        r_lcok.acquire()
        print(self.name, "gotB", time.ctime())
        time.sleep(2)

        r_lcok.acquire()
        print(self.name, "gotA", time.ctime())
        time.sleep(1)

        r_lcok.release()
        r_lcok.release()


    def run(self):

        self.actionA()
        self.actionB()


if __name__ == '__main__':

    # A=threading.Lock()
    # B=threading.Lock()

    r_lcok=threading.RLock()
    L=[]

    for i in range(5):
        t=MyThread()
        t.start()
        L.append(t)


    for i in L:
        i.join()

    print("ending....")








