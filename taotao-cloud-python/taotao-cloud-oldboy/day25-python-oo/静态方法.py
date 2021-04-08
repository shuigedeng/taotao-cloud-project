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

    @classmethod
    def tell_info(cls,x):
        print(cls)
        print('--》',cls.tag,x)#print('--》',Room.tag)
    # def tell_info(self):
    #     print('---->',self.tag)

    @staticmethod
    def wash_body(a,b,c):
        print('%s %s %s正在洗澡' %(a,b,c))

    def test(x,y):
        print(x,y)

# Room.wash_body('alex','yuanhao','wupeiqi')

print(Room.__dict__)


r1=Room('厕所','alex',100,100,100000)

print(r1.__dict__)
# r1.wash_body('alex','yuanhao','wupeiqi')

# Room.test(1,2)
# r1.test(1,2)

