from django.shortcuts import render
from django.shortcuts import redirect
from app01 import models

def get_students(request):
    stu_list = models.Student.objects.all()
    for row in stu_list:
        print(row.id,row.username,row.cs.id,row.cs.titile)
    return render(request,'get_students.html',{'stu_list': stu_list})

def add_students(request):
    if request.method == 'GET':
        cs_list = models.Classes.objects.all()
        # # [(id=1,title='') ]
        for row in cs_list:
            print(row.id,row.titile)
        return render(request,'add_students.html',{'cs_list':cs_list})
    elif request.method == 'POST':
        u = request.POST.get('username')
        a = request.POST.get('age')
        g = request.POST.get('gender')
        c = request.POST.get('cs')
        # root 12 1 7
        # username = models.CharField(max_length=32)
        # age = models.IntegerField()
        # gender = models.BooleanField()
        # cs = models.ForeignKey(Classes)
        models.Student.objects.create(
            username=u,
            age=a,
            gender=g,
            cs_id=c
        )
        return redirect('/students.html')

def del_students(request):
    nid = request.GET.get('nid')
    #删除id=1的那条数据
    models.Student.objects.filter(id=nid).delete()
    return redirect('/students.html')

def edit_students(request):
    if request.method == 'GET':
        nid = request.GET.get('nid')
        obj = models.Student.objects.filter(id=nid).first()
        # obj中有当前学生的班级ID=2  obj.cs_id
        cls_list = models.Classes.objects.values('id','titile')
        # 所有班级ID   班级名称
        #   1          1班
        #   2          2班 selected
        # cls_list是什么类型？QuerySet相当于列表
        # 【{'id':'cc',title:'xx'},{'id':'',title:'xx'},】
        # for row in cls_list:
        #     print(row['id'])
        return render(request, 'edit_students.html',{'obj':obj,'cls_list': cls_list})
    elif request.method == "POST":
        nid = request.GET.get('nid')
        u = request.POST.get('username')
        a = request.POST.get('age')
        g = request.POST.get('gender')
        class_id = request.POST.get('class_id')
        models.Student.objects.filter(id=nid).update(username=u,age=a,gender=g,cs_id=class_id)
        return redirect('/students.html')