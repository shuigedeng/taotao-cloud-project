import threading
import time
def sub():
    global num
   # num-=1
    print ("ok")
    lock.acquire()
    temp=num
    time.sleep(0.001)
    num=temp-1
    lock.release()

num=100

l=[]
lock=threading.Lock()

for i in range(100):
    t=threading.Thread(target=sub)
    t.start()
    l.append(t)

for t in l:
    t.join()

print num









