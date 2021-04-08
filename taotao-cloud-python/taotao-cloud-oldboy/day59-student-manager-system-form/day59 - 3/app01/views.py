from django.shortcuts import render
from django.shortcuts import redirect
from app01 import models
def users(request):
    user_list = models.UserInfo.objects.all()
    return render(request,'users.html',{'user_list':user_list})
from app01.forms import UserForm
def add_user(request):
    if request.method == 'GET':
        obj = UserForm()
        return render(request,'add_user.html',{'obj':obj})
    else:
        obj = UserForm(request.POST)
        if obj.is_valid():
            models.UserInfo.objects.create(**obj.cleaned_data)
            return redirect('/users/')
        else:
            return render(request,'add_user.html',{'obj':obj})
def edit_user(request,nid):
    if request.method == "GET":
        data = models.UserInfo.objects.filter(id=nid).first()
        obj = UserForm({'username':data.username,'email':data.email})
        return render(request,'edit_user.html',{'obj':obj,'nid':nid})
    else:
        obj = UserForm(request.POST)
        if obj.is_valid():
            models.UserInfo.objects.filter(id=nid).update(**obj.cleaned_data)
            return redirect('/users/')
        else:
            return render(request,'edit_user.html',{'obj':obj,'nid':nid})
