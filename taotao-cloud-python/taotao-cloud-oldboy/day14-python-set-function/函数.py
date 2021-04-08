
'''
Python开发【第四篇】：Python基础之函数
    https://www.cnblogs.com/wupeiqi/articles/5453708.html
'''

'''
y=2*x+1
x=3
y->7
x=3
y->7
'''

# def test(x):
#     '''
#     2*x+1
#     :param x:整形数字
#     :return: 返回计算结果
#     '''
#     y=2*x+1
#     return y
#
# def test():
#     '''
#     2*x+1
#     :param x:整形数字
#     :return: 返回计算结果
#     '''
#     x=3
#     y=2*x+1
#     return y
# a=test()
# print(a)

#过程：就是没有返回值的函数


# def test01():
#     msg = 'test01'
#     print(msg)
#
#
# def test02():
#     msg = 'test02'
#     print(msg)
#     return msg
#
# def test03():
#     msg = 'test03'
#     print(msg)
#     return 1,2,3,4,'a',['alex'],{'name':'alex'},None
#
# def test04():
#     msg = 'test03'
#     print(msg)
#     return {'name':'alex'}
# t1=test01()
# t2=test02()
# t3=test03()
# t4=test04()
# print(t1)
# print(t2)
# print(t3)
# print(t4)


# def calc(x,y): #x=2,y=3
#     res=x**y
#     return x
#     return y
# res=calc(2,3)
# # print(x)
# # print(y)
# print(res)
# # a=10
# # b=10
# # calc(a,b)


# def test(x,y,z):#x=1,y=2,z=3
#     print(x)
#     print(y)
#     print(z)

#位置参数，必须一一对应，缺一不行多一也不行
# test(1,2,3)

#关键字参数，无须一一对应，缺一不行多一也不行
# test(y=1,x=3,z=4)

#位置参数必须在关键字参数左边
# test(1,y=2,3)#报错
# test(1,3,y=2)#报错
# test(1,3,z=2)
# test(1,3,z=2,y=4)#报错
# test(z=2,1,3)#报错

# def handle(x,type='mysql'):
#     print(x)
#     print(type)
# handle('hello')
# handle('hello',type='sqlite')
# handle('hello','sqlite')

# def install(func1=False,func2=True,func3=True):
#     pass

#参数组：**字典 *列表
def test(x,*args):
    print(x)
    print(args)


# test(1)
# test(1,2,3,4,5)
# test(1,{'name':'alex'})
# test(1,['x','y','z'])
# test(1,*['x','y','z'])
# test(1,*('x','y','z'))

# def test(x,**kwargs):
#     print(x)
#     print(kwargs)
# test(1,y=2,z=3)
# test(1,1,2,2,2,2,2,y=2,z=3)
# test(1,y=2,z=3,z=3)#会报错 ：一个参数不能传两个值

def test(x,*args,**kwargs):
    print(x)
    print(args,args[-1])
    print(kwargs,kwargs.get('y'))
# test(1,1,2,1,1,11,1,x=1,y=2,z=3) #报错
# test(1,1,2,1,1,11,1,y=2,z=3)

# test(1,*[1,2,3],**{'y':1})







