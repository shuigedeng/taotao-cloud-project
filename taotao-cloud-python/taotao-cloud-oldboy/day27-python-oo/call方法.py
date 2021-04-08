class Foo:
    def __call__(self, *args, **kwargs):
        print('实例执行啦　ｏｂｊ（）')

f1=Foo()

f1() #f1的类Foo 下的__call__

Foo() #Foo的类 xxx下的__call__