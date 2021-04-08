from django.shortcuts import render,redirect,HttpResponse
from django.contrib.auth import authenticate,login,logout
from django.contrib.auth.decorators import login_required
import json
# Create your views here.


def dashboard(request):

    return render(request,'index.html')


def acc_login(request):
    error_msg = ''
    if request.method == "POST":
        username = request.POST.get("username")
        password = request.POST.get("password")
        user = authenticate(username=username,password=password)
        if user:
            login(request,user)
            return redirect("/")
        else:
            error_msg  = "Wrong username or password!"

    return render(request,'login.html',{'error_msg':error_msg})


def web_ssh(request):
    return render(request,'web_ssh.html')

@login_required
def host_mgr(request):
    return render(request,'host_mgr.html')


@login_required
def batch_task_mgr(request):

    print(request.POST)
    task_data= json.loads(request.POST.get('task_data'))
    print("task_data",task_data)

    return HttpResponse('sdddd')