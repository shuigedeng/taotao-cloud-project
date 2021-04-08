# name='lhf'
#
#
# def change_name():
#     global name
#     name='帅了一比'
#     print('change_name',name)
#
#
# change_name()
# print(name)

# name='lhf'
#
# def change_name():
#     name1='帅了一比'
#     name2='帅了一比'
#     name3='帅了一比'
#     print('change_name',name)

# globals

# NAME = "杠娘"
#
# def yangjian():
#     # NAME = "史正文"
#     global NAME
#     NAME = "小东北"
#     print('我要搞', NAME)
#
# def qupengfei():
#     # NAME = "基"
#     print('我要搞', NAME)
#
# yangjian()
# qupengfei()


# NAME = "产品经理"
#
# def yangjian():
#     # NAME = "史正文"
#     global NAME # 已经声明，ＮＡＭＥ就是全局的的那个变量
#     print('我要搞', NAME)
#     NAME = "小东北"  # 修改 全局的变量
#     print('我要搞', NAME)
#
# def qupengfei():
#     NAME = "基"
#     print('我要搞', NAME)

# 如果函数的内容无global关键字，
#   - 有声明局部变量
        # NAME = ["产品经理","廖波湿"]
        # def qupengfei():
        #     NAME = "自己"
        #     print('我要搞', NAME)
        # qupengfei()
#   - 无声明局部变量
        # NAME = ["产品经理","廖波湿"]
        # def qupengfei():
        #     NAME.append('XXOO')
        #     print('我要搞', NAME)
        # qupengfei()

# 如果函数的内容有global关键字
#   - 有声明局部变量
        # NAME = ["产品经理","廖波湿"]
        # def qupengfei():
        #     global NAME
        #     NAME = "自己"
        #     print('我要搞', NAME)
        # qupengfei()
        # 错误示例
        # NAME = ["产品经理","廖波湿"]
        # def qupengfei():
        #     NAME = "自己"
        #     global NAME
        #     print('我要搞', NAME)
        # qupengfei()
#   - 无声明局部变量
        # NAME = ["产品经理","廖波湿"]
        # def qupengfei():
        #     global NAME
        #     NAME = ["阿毛"]
        #     NAME.append('XXOO')
        #     print('我要搞', NAME)
        # qupengfei()

######## 全局变量变量名大写
######## 局部变量变量名小写


# 优先读取局部变量，能读取全局变量，无法对全局变量重新赋值 NAME=“fff”，
#     但是对于可变类型，可以对内部元素进行操作
# 如果函数中有global关键字，变量本质上就是全局的那个变量，可读取可赋值 NAME=“fff”
# qupengfei()
# yangjian()


# NAME = ["产品经理","廖波湿"]
#
# def yangjian():
#     # NAME = "史正文"
#     global NAME # 已经声明，ＮＡＭＥ就是全局的的那个变量
#     print('我要搞', NAME)
#     NAME = "小东北"  # 修改 全局的变量
#     print('我要搞', NAME)
#
# def qupengfei():
#     # NAME = "aS"
#     NAME.append('天扎龙')
#     print('我要搞', NAME)
#
# qupengfei()


# NAME = ["产品经理","廖波湿"]
# def qupengfei():
#     name = "自己"
#     global NAME
#     print('我要搞', NAME)
# qupengfei()

# NAME = '海风'
#
# def huangwei():
#     name = "黄伟"
#     print(name)
#     def liuyang():
#         name = "刘洋"
#         print(name)
#         def nulige():
#             name = '沪指花'
#             print(name)
#         print(name)
#         nulige()
#     liuyang()
#     print(name)
#
# huangwei()

name = "刚娘"

def weihou():
    name = "陈卓"
    def weiweihou():
        nonlocal name   # nonlocal，指定上一级变量，如果没有就继续往上直到找到为止
        name = "冷静"

    weiweihou()
    print(name)

print(name)
weihou()
print(name)
# 刚娘
# 冷静
# 刚娘

















