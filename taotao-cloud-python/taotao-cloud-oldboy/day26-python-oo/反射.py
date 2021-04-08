class BlackMedium:
    feture='Ugly'
    def __init__(self,name,addr):
        self.name=name
        self.addr=addr

    def sell_hourse(self):
        print('【%s】 正在卖房子，傻逼才买呢' %self.name)

    def rent_hourse(self):
        print('【%s】 正在租房子，傻逼才租呢' % self.name)


print(hasattr(BlackMedium,'feture'))
getattr()
#
# b1=BlackMedium('万成置地','天露园')
# b1.name--->b1.__dic__['name']
# print(b1.__dict__)
#
# # b1.name
# # b1.sell_hourse
# print(hasattr(b1,'name'))
# print(hasattr(b1,'sell_hourse'))
# print(hasattr(b1,'selasdfasdfsadfasdfasdfasdfasdl_hourse'))
#
#
#
# print(getattr(b1,'name'))
# print(getattr(b1,'rent_hourse'))
# func=getattr(b1,'rent_hourse')
# func()
# # print(getattr(b1,'rent_hourseasdfsa')) #没有则报错
# print(getattr(b1,'rent_hourseasdfsa','没有这个属性')) #没有则报错
#
#
# # b1.sb=True
# setattr(b1,'sb',True)
# setattr(b1,'sb1',123)
# setattr(b1,'name','SB')
# setattr(b1,'func',lambda x:x+1)
# setattr(b1,'func1',lambda self:self.name+'sb')
# print(b1.__dict__)
# print(b1.func)
# print(b1.func(10))
# print(b1.func1(b1))
# del b1.sb
# del b1.sb1
# delattr(b1,'sb')
# print(b1.__dict__)





