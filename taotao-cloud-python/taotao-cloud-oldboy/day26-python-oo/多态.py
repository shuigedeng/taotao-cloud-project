#_*_coding:utf-8_*_
__author__ = 'Linhaifeng'
class H2O:
    def __init__(self,name,temperature):
        self.name=name
        self.temperature=temperature
    def turn_ice(self):
        if self.temperature < 0:
            print('[%s]温度太低结冰了' %self.name)
        elif self.temperature > 0 and self.temperature < 100:
            print('[%s]液化成水' %self.name)
        elif self.temperature > 100:
            print('[%s]温度太高变成了水蒸气' %self.name)
    def aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa(self):
        pass

class Water(H2O):
    pass
class Ice(H2O):
    pass
class Steam(H2O):
    pass

w1=Water('水',25)
i1=Ice('冰',-20)
s1=Steam('蒸汽',3000)

# w1.turn_ice()
# i1.turn_ice()
# s1.turn_ice()

def func(obj):
    obj.turn_ice()

func(w1)  #---->w1.turn_ice()
func(i1)  #---->i1.turn_ice()
# def func(obj):
#     obj.turn_ice()
#
# func(w1)
# func(i1)
# func(s1)



