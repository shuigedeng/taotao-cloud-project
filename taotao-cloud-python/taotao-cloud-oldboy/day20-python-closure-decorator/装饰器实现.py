import time
def timmer(func): #func=test
    def wrapper():
        # print(func)
        start_time=time.time()
        func() #就是在运行test()
        stop_time = time.time()
        print('运行时间是%s' %(stop_time-start_time))
    return wrapper

@timmer #test=timmer(test)
def test():
    time.sleep(3)
    print('test函数运行完毕')
test()

# res=timmer(test)  #返回的是wrapper的地址
# res()  #执行的是wrapper()

# test=timmer(test)  #返回的是wrapper的地址
# test()  #执行的是wrapper()

#  @timmer  就相当于 test=timmer(test)
