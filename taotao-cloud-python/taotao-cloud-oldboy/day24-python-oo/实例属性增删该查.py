class Chinese:
    country='China'
    def __init__(self,name):
        self.name=name

    def play_ball(self,ball):
        print('%s 正在打 %s' %(self.name,ball))
p1=Chinese('alex')
print(p1.__dict__)

#查看
# print(p1.name)
# print(p1.play_ball)

#增加
p1.age=18
print(p1.__dict__)
print(p1.age)

#不要修改底层的属性字典
# p1.__dict__['sex']='male'
# print(p1.__dict__)
# print(p1.sex)

#修改
p1.age=19
print(p1.__dict__)
print(p1.age)

#删除
del p1.age
print(p1.__dict__)