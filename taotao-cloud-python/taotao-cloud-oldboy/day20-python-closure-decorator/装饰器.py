import time
def cal(l):
    start_time=time.time()
    res=0
    for i in l:
        time.sleep(0.1)
        res+=i
    stop_time = time.time()
    print('函数的运行时间是%s' %(stop_time-start_time))
    return res



print(cal(range(100)))


def index():
    pass

def home():
    pass

