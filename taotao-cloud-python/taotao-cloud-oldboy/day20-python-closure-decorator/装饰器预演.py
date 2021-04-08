import time
def timmer(func):
    def wrapper(*args,**kwargs):
        start_time=time.time()
        res=func(*args,**kwargs)
        stop_time = time.time()
        print('函数运行时间是%s' %(stop_time-start_time))
        return res
    return wrapper













@timmer
def cal(l):
    res=0
    for i in l:
        time.sleep(0.1)
        res+=i
    return res

res=cal(range(20))
print(res)