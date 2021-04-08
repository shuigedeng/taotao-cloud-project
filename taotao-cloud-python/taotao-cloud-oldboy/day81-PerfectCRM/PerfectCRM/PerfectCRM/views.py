from django.shortcuts import render,redirect
from django.contrib.auth import authenticate,login,logout


# Create your views here.


def acc_login(request):
    error_msg = ''
    if request.method == "POST":
        username = request.POST.get('username')
        password = request.POST.get('password')

        user = authenticate(username=username,password=password)
        if user:
            print("passed authencation",user)
            login(request,user)
            #request.user = user

            return  redirect( request.GET.get('next','/') )
        else:
            error_msg = "Wrong username or password!"
    return render(request, 'login.html', {'error_msg':error_msg})


def acc_logout(request):
    logout(request)
    return redirect("/login/")

