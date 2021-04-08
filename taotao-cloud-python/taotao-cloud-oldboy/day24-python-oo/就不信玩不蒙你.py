# class Chinese:
#     country='China'
#     def __init__(self,name):
#         self.name=name
#
#     def play_ball(self,ball):
#         print('%s 正在打 %s' %(self.name,ball))
# p1=Chinese('alex')
# print(p1.country)
# p1.country='日本'
# print('类的--->',Chinese.country)
# print('实例的',p1.country)


# country='中国'
# class Chinese:
#     def __init__(self,name):
#         self.name=name
#
#     def play_ball(self,ball):
#         print('%s 正在打 %s' %(self.name,ball))
# p1=Chinese('alex')
# # print(p1.country)



# country='中国'
# class Chinese:
#     def __init__(self,name):
#         self.name=name
#
#     def play_ball(self,ball):
#         print('%s 正在打 %s' %(self.name,ball))
#
# def shi_li_hua():
#     name=input('>>: ')
#     p1=Chinese(name)
#     # print(p1.country)
#     print(p1.name)
# shi_li_hua()



country='中国－－－－－－－－－－－－－－－－－－－'
class Chinese:
    country='中国'
    def __init__(self,name):
        self.name=name
        print('--->',country)

    def play_ball(self,ball):
        print('%s 正在打 %s' %(self.name,ball))

print(Chinese.__dict__)
print(Chinese.country)
p1=Chinese('alex')
# print('实例--------》',p1.country)



# Chinese.
# p.