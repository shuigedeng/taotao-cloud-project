


def add():
    sum=0

    for i in xrange(10000000):
        sum+=i
    print("sum",sum)

def mul():
    sum2=1
    for i in xrange(1,100000):
        sum2*=i
    print("sum2",sum2)

import threading,time


start=time.time()

t1=threading.Thread(target=add)
t2=threading.Thread(target=mul)

l=[]
l.append(t1)
l.append(t2)


# for t in l:
#     t.start()
#
#
#
# for t in l:
#     t.join()

add()
mul()

print("cost time %s"%(time.time()-start))




