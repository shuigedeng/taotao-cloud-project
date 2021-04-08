import pickle
import hashlib
import time
def create_md5():
    m = hashlib.md5()
    m.update(str(time.time()).encode('utf-8'))
    return  m.hexdigest()
id=create_md5()
time.sleep(1)
id1=create_md5()
time.sleep(1)
id2=create_md5()

print(id)
print(id1)
print(id2)

class Base:
    def save(self):
        with open('school.db','wb') as f:
            pickle.dump(self,f)

class School(Base):
    def __init__(self,name,addr):
        self.id=create_md5()
        self.name=name
        self.addr=addr

class Course(Base):
    def __init__(self,name,price,period,school):
        self.id=create_md5()
        self.name=name
        self.price=price
        self.period=period
        self.school=school

school_obj = pickle.load(open('school.db', 'rb'))
print(school_obj.name,school_obj.addr)
# s1=School('oldboy','北京')
# s1.save()