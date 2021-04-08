name = 'alex' #name=‘lhf’


def change_name():
    # name='lhf'
    # global name
    # name = 'lhf'
    # print(name)
    # name='aaaa' #name='bbb'
    def foo():
        # name = 'wu'
        nonlocal name
        name='bbbb'
        print(name)
    print(name)
    foo()
    print(name)


change_name()
