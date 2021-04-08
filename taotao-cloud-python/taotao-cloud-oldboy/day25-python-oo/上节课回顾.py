class School:
    x=1
    def __init__(self,name,addr,type):
        self.Name=name
        self.Addr=addr
        self.Type=type

    def tell_info(self):
        print('学校的详细信息是：name:%s addr:%s' %(self.Name,self.Addr))


s1=School('oldboy','沙河','私立')

print(s1.__dict__)

print(School.__dict__)

s1.tell_info()
School.tell_info(s1)