
# def test1():
#     print('in the test1')
# def test():
#     print('in the test')
#     return test1
#
# # print(test)
# res=test()
# print(res()) #test1()

#函数的作用域只跟函数声明时定义的作用域有关，跟函数的调用位置无任何关系
# name = 'alex'
# def foo():
#     name='linhaifeng'
#     def bar():
#         # name='wupeiqi'
#         print(name)
#     return bar
# a=foo()
# print(a)
# a() #bar()


name='alex'

def foo():
    name='lhf'
    def bar():
        name='wupeiqi'
        print(name)
        def tt():
            print(name)
        return tt
    return bar

# bar=foo()
# tt=bar()
# print(tt)
# tt()
r1 = foo()
r2 = r1()  # tt
r3 = r2()
foo()()()