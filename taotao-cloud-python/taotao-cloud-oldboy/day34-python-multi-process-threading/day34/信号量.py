import threading,time


class myThread(threading.Thread):
    def run(self):

        if semaphore.acquire():
            print(self.name)
            time.sleep(3)
            semaphore.release()

if __name__=="__main__":
    semaphore=threading.Semaphore()

    thrs=[]
    for i in range(100):
        thrs.append(myThread())
    for t in thrs:
        t.start()