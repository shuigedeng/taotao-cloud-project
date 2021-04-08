class School:
    def __init__(self,name,addr):
        self.name=name
        self.addr=addr
        self.course_list=[]
    def zhao_sheng(self):
        print('%s 正在招生' %self.name)
class Course:
    def __init__(self,name,price,period):
        self.name=name
        self.price=price
        self.period=period

s1=School('oldboy','北京')
s2=School('oldboy','南京')
s3=School('oldboy','东京')

c1=Course('linux',10,'1h')
c2=Course('python',10,'1h')

s1.course_list.append(c1)
s1.course_list.append(c2)
print(s1.__dict__)

for course_obj in s1.course_list:
    print(course_obj.name,course_obj.price)