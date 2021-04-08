class Vehicle:
    Country='China'
    def __init__(self,name,speed,load,power):
        self.name=name
        self.speed=speed
        self.load=load
        self.power=power
    def run(self):
        print('开动啦')
        print('开动啦')
class Subway(Vehicle):
        def __init__(self,name,speed,load,power,line):
           Vehicle.__init__(self,name,speed,load,power)
           self.line=line

        def show_info(self):
            print(self.name,self.speed,self.load,self.power,self.line)

        def run(self):
            Vehicle.run(self)
            print('%s %s 线，开动啦' %(self.name,self.line))
line13=Subway('北京地铁','10km/s',1000000000,'电',13)

line13.show_info()

line13.run()