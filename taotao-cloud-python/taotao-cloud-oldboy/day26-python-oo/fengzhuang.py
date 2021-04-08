#_*_coding:utf-8_*_
__author__ = 'Linhaifeng'

class People:
    __star='earth111111111111'
    __star1='earth111111111111'
    __star2='earth111111111111'
    __star3='earth111111111111'
    def __init__(self,id,name,age,salary):
        print('----->',self.__star)
        self.id=id
        self.name=name
        self.age=age
        self.salary=salary

    def get_id(self):
        print('我是私有方法啊,我找到的id是[%s]' %self.id)

    #访问函数
    def get_star(self):
        print(self.__star)



p1=People('123123123123','alex','18',100000000)
# print(p1.__star)
print(People.__dict__)
# print(p1.__star)
print(p1._People__star)
#
# p1.get_star()
p1.get_star()