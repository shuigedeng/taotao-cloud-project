from django.shortcuts import render
from django.shortcuts import HttpResponse
from app01 import models

def students(request):
    cls_list = models.Classes.objects.all()
    stu_list = models.Student.objects.all()

    return render(request,'students.html',{'stu_list':stu_list,'cls_list':cls_list})

def add_student(request):
    response = {'status':True,'message': None}
    try:
        u = request.POST.get('username')
        a = request.POST.get('age')
        g = request.POST.get('gender')
        c = request.POST.get('cls_id')
        models.Student.objects.create(
            username=u,
            age=a,
            gender=g,
            cs_id=c
        )
    except Exception as e:
        response['status'] = False
        response['message'] = '用户输入错误'
    import json
    result = json.dumps(response,ensure_ascii=False)
    return HttpResponse(result)