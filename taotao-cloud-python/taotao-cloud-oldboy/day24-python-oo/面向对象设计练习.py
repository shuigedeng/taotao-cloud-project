
def school(name,addr,type):
    def init(name, addr, type):
        sch = {
            'name': name,
            'addr': addr,
            'type': type,
            'kao_shi': kao_shi,
            'zhao_sheng': zhao_sheng,
        }
        return sch
    def kao_shi(school):
        print('%s 学校正在考试' %school['name'])
    def zhao_sheng(school):
        print('%s %s 正在招生' %(school['type'],school['name']))
    return  init(name,addr,type)

s1=school('oldboy','沙河','私立学校')
print(s1)
print(s1['name'])

s1['zhao_sheng'](s1)

s2=school('清华','北京','公立学校')

print(s2)
print(s2['name'],s2['addr'],s2['type'])
s2['zhao_sheng'](s2)