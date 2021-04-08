class Dad:
    '这个是爸爸类'
    money=10
    def __init__(self,name):
        print('爸爸')
        self.name=name
    def hit_son(self):
        print('%s 正在打儿子' %self.name)

class Son(Dad):
    money = 1000000000009
    def __init__(self,name,age):
        self.name=name
        self.age=age

    def hit_son(self):
        print('来自儿子类')
# print(Son.money)
# Son.hit_son()
# print(Dad.__dict__)
# print(Son.__dict__)
s1=Son('alex',18)
s1.hit_son()
# print(s1.money)
# print(Dad.money)
# print(s1.name)
# print(s1.money)
# print(s1.__dict__)
# s1.hit_son()