from django.shortcuts import render,redirect,HttpResponse
from repository import models
def trouble_list(request):
    # user_info = request.session.get('user_info') # {id:'',}
    current_user_id = 1
    result = models.Trouble.objects.filter(user_id=current_user_id).order_by('status').\
        only('title','status','ctime','processer')

    return render(request,'backend_trouble_list.html',{'result': result})


from django.forms import Form
from django.forms import fields
from django.forms import widgets

class TroubleMaker(Form):
    title = fields.CharField(
        max_length=32,
        widget=widgets.TextInput(attrs={'class': 'form-control'})
    )
    detail = fields.CharField(
        widget=widgets.Textarea(attrs={'id':'detail','class':'kind-content'})
    )

import datetime

def trouble_create(request):
    if request.method == 'GET':
        form = TroubleMaker()
    else:
        form = TroubleMaker(request.POST)
        if form.is_valid():
            # title,content
            # form.cleaned_data
            dic = {}
            dic['user_id'] = 1 # session中获取
            dic['ctime'] = datetime.datetime.now()
            dic['status'] = 1
            dic.update(form.cleaned_data)
            models.Trouble.objects.create(**dic)
            return redirect('/backend/trouble-list.html')
    return render(request, 'backend_trouble_create.html',{'form':form})

def trouble_edit(request,nid):

    if request.method == "GET":
        obj = models.Trouble.objects.filter(id=nid, status=1).only('id', 'title', 'detail').first()
        if not obj:
            return HttpResponse('已处理中的保单章无法修改..')
        # initial 仅初始化
        form = TroubleMaker(initial={'title': obj.title,'detail': obj.detail})
        # 执行error会进行验证
        return render(request,'backend_trouble_edit.html',{'form':form,'nid':nid})
    else:
        form = TroubleMaker(data=request.POST)
        if form.is_valid():
            # 受响应的行数
            v = models.Trouble.objects.filter(id=nid, status=1).update(**form.cleaned_data)
            if not v:
                return HttpResponse('已经被处理')
            else:
                return redirect('/backend/trouble-list.html')
        return render(request, 'backend_trouble_edit.html', {'form': form, 'nid': nid})

def trouble_kill_list(request):
    from django.db.models import Q
    current_user_id = 1
    result = models.Trouble.objects.filter(Q(processer_id=current_user_id)|Q(status=1)).order_by('status')
    return render(request,'backend_trouble_kill_list.html',{'result':result})

class TroubleKill(Form):
    solution = fields.CharField(
        widget=widgets.Textarea(attrs={'id':'solution','class':'kind-content'})
    )

def trouble_kill(request,nid):
    current_user_id = 1
    if request.method == 'GET':
        ret = models.Trouble.objects.filter(id=nid, processer=current_user_id).count()
        # 以前未强盗
        if not ret:
            v = models.Trouble.objects.filter(id=nid,status=1).update(processer=current_user_id,status=2)
            if not v:
                return HttpResponse('手速太慢...')
        obj = models.Trouble.objects.filter(id=nid).first()
        form = TroubleKill(initial={'title': obj.title,'solution': obj.solution})
        return render(request,'backend_trouble_kill.html',{'obj':obj,'form': form,'nid':nid})
    else:

        ret = models.Trouble.objects.filter(id=nid, processer=current_user_id,status=2).count()
        if not ret:
            return HttpResponse('去你妈的')
        form = TroubleKill(request.POST)
        if form.is_valid():
            dic = {}
            dic['status'] = 3
            dic['solution'] = form.cleaned_data['solution']
            dic['ptime'] = datetime.datetime.now()
            models.Trouble.objects.filter(id=nid, processer=current_user_id,status=2).update(**dic)
            return redirect('/backend/trouble-kill-list.html')
        obj = models.Trouble.objects.filter(id=nid).first()
        return render(request, 'backend_trouble_kill.html', {'obj': obj, 'form': form, 'nid': nid})

def trouble_report(request):
    return render(request,'backend_trouble_report.html')

def trouble_json_report(request):
    # 数据库中获取数据
    user_list = models.UserInfo.objects.filter()
    response = []
    for user in user_list:
        from django.db import connection, connections
        cursor = connection.cursor()
        cursor.execute("""select strftime('%%s',strftime("%%Y-%%m-01",ctime)) * 1000,count(id) from repository_trouble where processer_id = %s group by strftime("%%Y-%%m",ctime)""", [user.nid,])
        result = cursor.fetchall()
        print(user.username,result)
        temp = {
            'name': user.username,
            'data':result
        }
        response.append(temp)
    import json
    return HttpResponse(json.dumps(response))
















