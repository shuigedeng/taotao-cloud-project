#用面向对象编程独有的语法class去实现面向对象设计
class Dog:
    def __init__(self,name,gender,type):
        self.name=name
        self.gender=gender
        self.type=type

    def bark(self):
        print('一条名字为[%s]的[%s],狂吠不止' %(self.name,self.type))

    def yao_ren(self):
        print('[%s]正在咬人' %(self.name))

    def chi_shi(self):
        print('[%s]正在吃屎' %(self.type))


dog1=Dog('alex','female','京巴')
print(dog1.__dict__)
# dog2=Dog('wupeiqi','female','腊肠')
# dog3=Dog('yuanhao','female','藏獒')
#
# dog1.bark()
# dog2.yao_ren()
# dog3.chi_shi()

