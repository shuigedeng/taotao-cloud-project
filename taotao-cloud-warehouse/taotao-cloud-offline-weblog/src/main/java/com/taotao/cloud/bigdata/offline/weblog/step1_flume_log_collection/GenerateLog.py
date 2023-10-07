#!/bin/python
#coding=utf-8


import time
import datetime
import random

# 日志格式
# valid
# remote-addr
# remote-user
# time_local
# request
# status
# byte_sent
# refer
# user-agent

#       本程序用于生成web访问日志     #######

# ''.join([(string.ascii_letters+string.digits)[x] for x in random.sample(range(0,62),8)])
def random_str(randomlength=8):
    str = ''
    chars = 'AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789'
    length = len(chars) - 1
    for i in range(randomlength):
        str+=chars[random.randint(0, length)]
    return str

pages=['/register','/recommand','/relation','/item/a','/item/b','/item/c','/list/','/search/','/cart','/order/getorder','/order/submitorder','/index','/category/a','/category/b','/category/c','/category/d']
for i in range(100):
    pages.append("/item/"+random_str(8))



refers=['http://www.google.com','www.baidu.com','www.sohu.com']
refers.extend(pages)

minutes=[]
for i in range(00,40):
    t = i+random.randint(0,19)
    minutes.append(str(t) if len(str(t))>1 else "0"+str(t))
print(minutes)

seconds=[]
for i in range(00,40):
    t = i+random.randint(0,19)
    seconds.append(str(t) if len(str(t))>1 else "0"+str(t))
print(minutes)

hours=[]
for i in range(00,24):
    hours.append(str(i) if len(str(i))>1 else "0"+str(i))
print(hours)


ips = open('c:\\000000_0.ips')
file = ips.read()
ips = file.split('\n')

#
# morning=datetime.datetime.strptime('2013-09-18 00:00:00','%Y-%m-%d %H:%M:%S')
# evening=datetime.datetime.strptime('2013-09-18 23:59:59','%Y-%m-%d %H:%M:%S')
#
#
# print(morning)
# print(evening)

logfile = open("c:\\mylog.log",mode='w')

for i in range(0,500000):
    # seed = random.randint(0,7)
    # print(pages[seed])
    ip=random.choice(ips)
    h=random.choice(hours)
    m=random.choice(minutes)
    s=random.choice(minutes)

    page=random.choice(pages)
    refer=random.choice(refers)
    useragent = 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36'
    body_sent = '1800'
    status='200'
    valid = 'true'
    user = '-'

    day='2013-09-20'
    time_local= day + " " +  h + ":" + m + ":" + s
    logfile.write(valid + "\001" + ip + "\001"+ user+ "\001" + time_local +"\001"+page+"\001"+ status +"\001"+ body_sent + "\001"+refer+ "\001" + useragent +"\n")

logfile.close()


