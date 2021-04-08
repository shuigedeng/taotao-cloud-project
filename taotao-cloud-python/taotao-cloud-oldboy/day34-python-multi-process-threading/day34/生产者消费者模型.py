import time,random
import queue,threading

q = queue.Queue()

def Producer(name):
  count = 0
  while count <10:
    print("making........")
    time.sleep(5)
    q.put(count)
    print('Producer %s has produced %s baozi..' %(name, count))
    count +=1
    #q.task_done()
    q.join()
    print("ok......")

def Consumer(name):
  count = 0
  while count <10:
        time.sleep(random.randrange(4))
    # if not q.empty():
    #     print("waiting.....")
        #q.join()
        data = q.get()
        print("eating....")
        time.sleep(4)

        q.task_done()
        #print(data)
        print('\033[32;1mConsumer %s has eat %s baozi...\033[0m' %(name, data))
    # else:
    #     print("-----no baozi anymore----")
        count +=1

p1 = threading.Thread(target=Producer, args=('A君',))
c1 = threading.Thread(target=Consumer, args=('B君',))
c2 = threading.Thread(target=Consumer, args=('C君',))
c3 = threading.Thread(target=Consumer, args=('D君',))

p1.start()
c1.start()
c2.start()
c3.start()