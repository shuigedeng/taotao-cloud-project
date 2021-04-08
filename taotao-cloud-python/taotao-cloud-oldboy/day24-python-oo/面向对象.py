
# name='元昊'
#
# gender='母'
#
# type='藏獒'

#狗的特征
dog1={
    'name':'元昊',
    'gender':'母',
    'type':'藏獒'
}
dog2={
    'name':'alex',
    'gender':'母',
    'type':'腊肠',
}
person1={
    'name':'武sir',
    'gender':'母',
    'type':'人'
}

# def dog(name,gender,type):
#     # 狗的动作
#     def jiao(dog):
#         print('一条狗[%s]，汪汪汪' % dog['name'])
#     def chi_shi(dog):
#         print('一条[%s] 正在吃屎' % dog['type'])
#     dog1 = {
#         'name':name,
#         'gender': gender,
#         'type': type,
#         'jiao':jiao,
#         'chi_shi':chi_shi,
#     }
#     return dog1
# d1=dog('元昊','母','中华田园犬')
# d2=dog('alex','母','藏敖')
# print(d1)
# print(d2)
# d1['jiao'](d1)
# d2['chi_shi'](d2)


# jiao(dog1)
# chi_shi(dog1)
# chi_shi(dog2)
#
# jiao(person1)

def dog(name,gender,type):
    # 狗的动作
    def jiao(dog):
        print('一条狗[%s]，汪汪汪' % dog['name'])
    def chi_shi(dog):
        print('一条[%s] 正在吃屎' % dog['type'])
    def init(name,gender,type):
        dog1 = {
            'name':name,
            'gender': gender,
            'type': type,
            'jiao':jiao,
            'chi_shi':chi_shi,
        }
        return dog1
    return init(name,gender,type)

d1=dog('元昊','母','中华田园犬')
d2=dog('alex','母','藏敖')
print(d1)
print(d2)
d1['jiao'](d1)
d2['chi_shi'](d2)
