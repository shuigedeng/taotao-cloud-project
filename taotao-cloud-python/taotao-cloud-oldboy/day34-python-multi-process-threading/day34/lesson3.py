# import threading,time
#
# li=[1,2,3,4]
#
#
# def pri():
#     while li:
#         a=li[-1]
#         print(a)
#         time.sleep(1)
#         li.remove(a)
#         # try:
#         #     li.remove(a)
#         # except Exception as e:
#         #     print('----',a,e)
#
# t1=threading.Thread(target=pri,args=())
# t1.start()
# t2=threading.Thread(target=pri,args=())
# t2.start()



import queue      #  线程 队列

q=queue.Queue(3)  # FIFO模式

q.put(12)
q.put("hello")
q.put({"name":"yuan"})
q.put_nowait(56)#  q.put(block=False)

print(q.qsize())
print(q.empty())
print(q.full())
# q.put(34,False)


while 1:
    data=q.get()
    print(data)
    print("----------")



#先进后出
import queue

# q=queue.LifoQueue()
#
# q.put(12)
# q.put("hello")
# q.put({"name":"yuan"})
#
# while 1:
#     data=q.get()
#     print(data)
#     print("----------")

# q=queue.PriorityQueue()
#
# q.put([3,12])
# q.put([2,"hello"])
# q.put([4,{"name":"yuan"}])
#
# while 1:
#     data=q.get()
#     print(data[1])
#     print("----------")