from django.shortcuts import render
from django.shortcuts import HttpResponse

def ajax1(request):
    return render(request, 'ajax1.html')

def ajax2(request):
    user = request.GET.get('username')
    pwd = request.GET.get('password')
    import time
    time.sleep(5)
    return HttpResponse('我愿意')

def ajax3(request):
    v1 = request.POST.get('v1')
    v2 = request.POST.get('v2')
    try:
        v3 = int(v1) + int(v2)
    except Exception as e:
        v3 = "输入格式错误"
    return HttpResponse(v3)

from app01 import models
def ajax4(request):
    nid = request.GET.get('nid')
    msg = '成功'
    try:
        models.Student.objects.filter(id=nid).delete()
    except Exception as e:
        msg = str(e)
    return HttpResponse(msg)
