# print('hello'

# a=1
# if a
#     print('ok')

# def test:
#     print('adsf')


#逻辑错误
# res=1/0
# l=[1,2]
# l[10]

# age=input('>>: ')
# age=int(age)
# res=1/0

# l=[]
# l[10000]

# dic={}
# dic['name']

# class Foo:
#     pass
# Foo.x
# msg=input('>>: ')

# if

# age=input('>>: ')
# if age.isdigit():
#     int(age) #主逻辑
#
# elif age.isspace():
#     print('---->用户输入的空格')
# elif len(age) == 0:
#     print('----》用户输入的为空')
# else:
#     print('其他的非法输入')
#
# #第二段代码
# num2=input('>>: ') #输入一个字符串试试
# # int(num2)
#
# if num2.isdigit():
#     int(num2) #主逻辑
# elif num2.isspace():
#     print('---->用户输入的空格')
# elif len(num2) == 0:
#     print('----》用户输入的为空')
# else:
#     print('其他的非法输入')





# age = input('>>: ')
# num2 = input('>>: ')  # 输入一个字符串试试
# if age.isdigit() and num2.isdigit() and  num3.isdigit() and  num4.isdigit() and :
#     int(age)  # 主逻辑
#     int(num2)  # 主逻辑
# elif age.isspace():
#     print('---->用户输入的空格')
# elif len(age) == 0:
#     print('----》用户输入的为空')
# else:
#     print('其他的非法输入')



# def test():
#     print('test running')
# choice_dic={
#     '1':test
# }
# while True:
#     choice=input('>>: ').strip()
#     if not choice or choice not in choice_dic:
#         continue #这便是一种异常处理机制啊
#     choice_dic[choice]()

# age = input('>>: ')
# int(age)  # 主逻辑
#invalid literal for int() with base 10: 'asdf'

try:
    age=input('1>>: ')
    int(age)  # 主逻辑


    num2=input('2>>: ')
    int(num2)  # 主逻辑
except ValueError as e:
    print(e)