
'''
1.数据属性
2.函数属性
'''
class Chinese:
    '这是一个中国人的类'

    dang='共产党'

    # def __init__(name,age,gender):
    #     dic={
    #         'name':name,
    #         'age':age,
    #         'gender':gender
    #     }
    #     return dic
    def __init__(self,name,age,gender):
        # print('我是初始化函数，我开始运行了')
        self.mingzi=name  #p1.mingzi=name
        self.nianji=age   #p1.nianji=age
        self.xingbie=gender
        # print('我结束啦')

    def sui_di_tu_tan(self):
        print('%s 朝着墙上就是一口痰' %self.mingzi)
    def cha_dui(self):
        print(self)
        print('%s 插到了前面' %self.mingzi)

    def eat_food(self,food):
        print('%s 正在吃%s' %(self.mingzi,food))

p1=Chinese('元昊',18,'female') #--->__init__(self,name,age,gender)
p1.sui_di_tu_tan()
p1.eat_food('屎')

p2=Chinese('武sir',10000,'姑娘')
p2.eat_food('韭菜馅饼')
print(dir(p2))
#
# # p1=Chinese.__init__(p1,name,age,gender)
#
# print(p1.__dict__)
# # print(p1.__dict__['xingbie'])
# print(p1.mingzi)
# # print(p1.mingzi111111111111111)
#
# print(p1.dang)
#
#
# print(Chinese.__dict__)
# Chinese.sui_di_tu_tan()
# Chinese.cha_dui(p1)
#
# # p1.sui_di_tu_tan()
#
# print('[------------------->')
# p1.cha_dui()