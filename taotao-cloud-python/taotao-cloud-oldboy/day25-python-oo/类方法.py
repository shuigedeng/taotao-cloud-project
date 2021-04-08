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
