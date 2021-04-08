class Foo:
    def __init__(self,name):
        self.name=name
    def __del__(self):
        print('我执行啦')

f1=Foo('alex')

# del f1    #删除实例会触发__del__
del f1.name #删除实例的属性不会触发__del__
print('--------------------->')

#程序运行完毕会自动回收内存，触发__del__