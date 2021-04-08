class Lazyproperty:
    def __init__(self,func):
        # print('==========>',func)
        self.func=func
    def __get__(self, instance, owner):
        print('get')
        # print(instance)
        # print(owner)
        if instance is None:
            return self
        res=self.func(instance)
        setattr(instance,self.func.__name__,res)
        return res
    # def __set__(self, instance, value):
    #     pass

class Room:
    def __init__(self,name,width,length):
        self.name=name
        self.width=width
        self.length=length
    # @property #area=property(area)
    @Lazyproperty  #area=Lazypropery(area)
    def area(self):
        return self.width * self.length
    @property  #test=property(test)
    def area1(self):
        return  self.width * self.length
# print(Room.__dict__)
r1=Room('厕所',1,1)
# print(r1.__dict__)

#实例调用
# print(r1.area)
# print(Room.__dict__)

#类调用
# print(Room.area)

# print(r1.test)
# print(Room.test)
# print(r1.area1)
# print(r1.area1)
# print(r1.area1)
# print(r1.area1)

print(r1.area)
print(r1.__dict__)

print(r1.area)
print(r1.area)
print(r1.area)
print(r1.area)
print(r1.area)
print(r1.area)
print(r1.area)
print(r1.area)