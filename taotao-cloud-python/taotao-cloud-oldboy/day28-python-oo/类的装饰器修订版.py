def Typed(**kwargs):
    def deco(obj):
        for key,val in kwargs.items():
            # obj.key=val
            setattr(obj,key,val)
        return obj
    return deco

@Typed(x=1,y=2,z=3)   #1.Typed(x=1,y=2,z=3) --->deco   2.@deco---->Foo=deco(Foo)
class Foo:
    pass
print(Foo.__dict__)

# @Typed(name='egon')  #@deco   ---->Bar=deco(Bar)
# class Bar:
#     pass
# print(Bar.name)