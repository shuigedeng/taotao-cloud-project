#高阶函数1。函数接收的参数是一个函数名  2#返回值中包含函数
# 把函数当作参数传给另外一个函数
# def foo(n): #n=bar
#     print(n)
#
# def bar(name):
#     print('my name is %s' %name)
#
# # foo(bar)
# # foo(bar())
# foo(bar('alex'))

#返回值中包含函数
def bar():
    print('from bar')
def foo():
    print('from foo')
    return bar
n=foo()
n()
def hanle():
    print('from handle')
    return hanle
h=hanle()
h()



def test1():
    print('from test1')
def test2():
    print('from handle')
    return test1()
