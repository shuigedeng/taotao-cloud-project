from django.shortcuts import render,redirect

# Create your views here.
import datetime

def login(request):
    print("COOKIES",request.COOKIES)
    print("SESSION",request.session)

    if request.method=="POST":
        name=request.POST.get("user")
        pwd=request.POST.get("pwd")
        if name=="yuan" and pwd=="123":

            # ret=redirect("/index/")
            # ret.set_cookie("username",{"11":"22"},max_age=10,expires=datetime.datetime.utcnow()+datetime.timedelta(days=3))
            # return ret

            # COOKIE SESSION
            request.session["is_login"]=True
            request.session["user"]=name

            return redirect("/index/")

    return render(request,"login.html")

def index(request):

    if request.COOKIES.get("username",None):
        name = request.COOKIES.get("username",None)
        return render(request, "index.html", locals())


    # if request.session.get("is_login",None):
    #     name=request.session.get("user",None)
    #     return render(request,"index.html",locals())

    else:
        return redirect("/login/")




