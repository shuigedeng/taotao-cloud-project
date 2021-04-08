import time
def timmer(func): #func=test
    def wrapper():
        start_time=time.time()
        res=func() #就是在运行test()
        stop_time = time.time()
        print('运行时间是%s' %(stop_time-start_time))
        return res
    return wrapper

@timmer #test=timmer(test)
def test():
    time.sleep(3)
    print('test函数运行完毕')
    return '这是test的返回值'

res=test()  #就是在运行wrapper
print(res)