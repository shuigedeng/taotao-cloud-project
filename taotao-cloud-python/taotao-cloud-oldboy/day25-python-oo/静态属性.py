class Room:
    tag=1
    def __init__(self,name,owner,width,length,heigh):
        self.name=name
        self.owner=owner
        self.width=width
        self.length=length
        self.heigh=heigh

    @property
    def cal_area(self):
        # print('%s 住的 %s 总面积是%s' % (self.owner,self.name, self.width * self.length))
        return  self.width * self.length

    def test(self):
        print('from test',self.name)

    @classmethod
    def tell_info(cls,x):
        print(cls)
        print('--》',cls.tag,x)#print('--》',Room.tag)
    # def tell_info(self):
    #     print('---->',self.tag)


# print(Room.tag)

# Room.test(1) #1.name
# r1=Room('厕所','alex',100,100,100000)
Room.tell_info(10)



# r1=Room('厕所','alex',100,100,100000)
# r2=Room('公共厕所','yuanhao',1,1,1)
# # print('%s 住的 %s 总面积是%s' %(r1.owner,r1.name,r1.width*r1.length))
# # print('%s 住的 %s 总面积是%s' %(r2.owner,r2.name,r2.width*r2.length))
# # r1.cal_area()
# # r2.cal_area()
# print(r1.cal_area)
# print(r2.cal_area)
# print(r1.name)
# print(r2.name)