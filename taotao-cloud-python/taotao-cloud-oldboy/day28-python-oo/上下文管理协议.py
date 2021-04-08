class Foo:
    def __init__(self,name):
        self.name=name

    def __enter__(self):
        print('执行enter')
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        print('执行exit')
        print(exc_type)
        print(exc_val)
        print(exc_tb)
        return True
with Foo('a.txt') as f:
    print(f)
    print(asdfsaasdfasdfasdfasdfasfasdfasdfasdfasdfasfdasfd)  #触发__exit__
    print(f.name)
    print('-----------------')
    print('-----------------')
    print('-----------------')
    print('-----------------')
    print('-----------------')
    print('-----------------')
    print('-----------------')
print('000000000000000000000000000000000000000000000')