# def bar():
#     print('from bar')
#
# def foo():
#     print('from foo')
#     def test():
#         pass

def father(auth_type):
    # print('from father %s' %name)
    def son():
        # name='linhaifeng_1'
        # print('我的爸爸是%s' %name)
        def grandson():
            print('我的爷爷是%s' %auth_type)
        grandson()
    # print(locals())
    son()
# father('linhaifeng')
father('filedb')

