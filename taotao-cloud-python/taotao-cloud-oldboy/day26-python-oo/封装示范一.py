#_*_coding:utf-8_*_
__author__ = 'Linhaifeng'

class Room:
    def __init__(self,name,owner,width,length,high):
        self.name=name
        self.owner=owner
        self.__width=width
        self.__length=length
        self.__high=high

    def tell_area(self): #此时我们想求的是面积
        return self.__width * self.__length *self.__high

    def tell_width(self):
        return self.__width


r1=Room('卫生间','alex',100,100,10000)

# arear=r1.__width * r1.__length
print(r1.tell_area())