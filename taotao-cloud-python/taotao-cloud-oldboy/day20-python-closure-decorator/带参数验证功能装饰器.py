user_list=[
    {'name':'alex','passwd':'123'},
    {'name':'linhaifeng','passwd':'123'},
    {'name':'wupeiqi','passwd':'123'},
    {'name':'yuanhao','passwd':'123'},
]
current_dic={'username':None,'login':False}

def auth(auth_type='filedb'):
    def auth_func(func):
        def wrapper(*args,**kwargs):
            print('认证类型是',auth_type)
            if auth_type == 'filedb':
                if current_dic['username'] and current_dic['login']:
                    res = func(*args, **kwargs)
                    return res
                username=input('用户名：').strip()
                passwd=input('密码：').strip()
                for user_dic in user_list:
                    if username == user_dic['name'] and passwd == user_dic['passwd']:
                        current_dic['username']=username
                        current_dic['login']=True
                        res = func(*args, **kwargs)
                        return res
                else:
                    print('用户名或者密码错误')
            elif auth_type == 'ldap':
                print('鬼才特么会玩')
                res = func(*args, **kwargs)
                return res
            else:
                print('鬼才知道你用的什么认证方式')
                res = func(*args, **kwargs)
                return res

        return wrapper
    return auth_func

@auth(auth_type='filedb') #auth_func=auth(auth_type='filedb')-->@auth_func 附加了一个auth_type  --->index=auth_func(index)
def index():
    print('欢迎来到京东主页')

@auth(auth_type='ldap')
def home(name):
    print('欢迎回家%s' %name)
#
@auth(auth_type='sssssss')
def shopping_car(name):
    print('%s的购物车里有［%s,%s,%s］' %(name,'奶茶','妹妹','娃娃'))

# print('before-->',current_dic)
# index()
# print('after--->',current_dic)
# home('产品经理')
shopping_car('产品经理')
