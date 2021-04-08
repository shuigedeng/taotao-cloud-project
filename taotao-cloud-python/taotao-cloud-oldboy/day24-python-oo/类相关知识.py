# class Chinese:
#     '这是一个中国人的类'
#     pass
#
# print(Chinese)
#
# #实例化到底干了什么？
# p1=Chinese() #实例化
# print(p1)


'''
1.数据属性
2.函数属性
'''

class Chinese:
    '这是一个中国人的类'
    dang='共产党'
    def sui_di_tu_tan(sejl):
        print('朝着墙上就是一口痰')
    def cha_dui(self):
        print('插到了前面')
#
# print(Chinese.dang)
# Chinese.sui_di_tu_tan()
# Chinese.cha_dui('元昊')
#
# # print(dir(Chinese))
print(Chinese.__dict__) #查看属性字典
# print(Chinese.__dict__['dang'])
# Chinese.__dict__['sui_di_tu_tan']()
# Chinese.__dict__['cha_dui'](1)
print(Chinese.__name__)
print(Chinese.__doc__)
print(Chinese.__module__)


