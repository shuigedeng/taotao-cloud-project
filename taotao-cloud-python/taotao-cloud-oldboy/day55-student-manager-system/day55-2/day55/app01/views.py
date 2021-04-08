from django.shortcuts import render
from django.shortcuts import HttpResponse
from app01 import models

def test(request):
    # models.Classes.objects.create(titile='2班')
    # models.Classes.objects.create(titile='3班')
    # models.Classes.objects.create(titile='4班')
    #
    # models.Student.objects.create(username='小东北',age=73,gender=True,cs_id=1)
    # models.Student.objects.create(username='中东北', age=73, gender=True, cs_id=1)
    # models.Student.objects.create(username='大东北',age=73,gender=True,cs_id=1)
    # models.Student.objects.create(username='老东北',age=73,gender=True,cs_id=2)

    # ret = models.Student.objects.filter(cs__titile='2班')
    # print(ret)
    # obj = models.Classes.objects.filter(titile='2班').first()
    # print(obj.id)
    # print(obj.titile)
    # print(obj.ssss.all())
    # ret = models.Classes.objects.all().values('id', 'titile','ssss',"ssss__username")
    # print(ret)
    # # 谁是主表？
    # models.Student.objects.all().values('username', 'cs__titile')
    # models.Classes.objects.all().values('titile', 'ssss__username')
    # #################### 多对多 ####################
    # models.Teachers.objects.create(name='Alex')
    # models.Teachers.objects.create(name='SB')
    # models.Teachers.objects.create(name='瞎驴')
    # models.Teachers.objects.create(name='wupeiqi')

    # cls_list = models.Classes.objects.all()
    # for obj in cls_list:
    #     print(obj.id,obj.titile)
    #     for row in obj.m.all():
    #         print('----',row.name)

    # obj = models.Classes.objects.filter(id=1).first()
    # obj.m.add(3)

    # obj = models.Teachers.objects.filter(id=2).first()
    # obj.sssss.set([1,2])


    # v = models.Classes.objects.all().values('id','titile','m','m__name')
    # print(v)
    # v = models.Classes.objects.all().values('id','titile','m','m__name')
    # print(v)
    # for item in v:
    #     print(item['m'],type(item['m']))

    # v = models.Teachers.objects.all().values('name','sssss__titile')
    # print(v)
    return HttpResponse('...')