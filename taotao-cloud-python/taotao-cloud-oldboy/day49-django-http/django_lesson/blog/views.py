from django.shortcuts import render, HttpResponse
import time


# Create your views here.


def show_time(requset):
    # return HttpResponse("hello")
    t = time.ctime()

    return render(requset, "index.html", {"time": t})


def article_year(request, y):
    return HttpResponse(y)


def article_year_month(request, year, month):
    return HttpResponse("year:%s  month:%s" % (year, month))


def register(request):
    if request.method == "POST":
        print(request.POST.get("user"))
        print(request.POST.get("age"))
        return HttpResponse("success!")

    return render(request, "register.html")
