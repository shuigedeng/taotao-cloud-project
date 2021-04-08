import time
def timmer(func): #func=test1
    def wrapper(*args,**kwargs): #test('linhaifeng',age=18)  args=('linhaifeng')  kwargs={'age':18}
        start_time=time.time()
        res=func(*args,**kwargs) #就是在运行test()         func(*('linhaifeng'),**{'age':18})
        stop_time = time.time()
        print('运行时间是%s' %(stop_time-start_time))
        return res
    return wrapper

# @timmer #test=timmer(test)
def test(name,age):
    time.sleep(3)
    print('test函数运行完毕,名字是【%s】 年龄是【%s】' %(name,age))
    return '这是test的返回值'

@timmer
def test1(name,age,gender):
    time.sleep(1)
    print('test1函数运行完毕,名字是【%s】 年龄是【%s】 性别【%s】' %(name,age,gender))
    return '这是test的返回值'

# res=test('linhaifeng',age=18)  #就是在运行wrapper
# # print(res)
# test1('alex',18,'male')

test1('alex',18,'male')


















# def test2(name,age,gender): #test2(*('alex',18,'male','x','y'),**{})
#     #name,age,gender=('alex',18,'male','x','y')
#     print(name)
#     print(age)
#     print(gender)
#
# def test1(*args,**kwargs):
#     test2(*args,**kwargs)  #args=('alex',18,'male','x','y') kwargs={}
#
# # test2('alex',18,gender='male')
#
# test1('alex',18,'male')