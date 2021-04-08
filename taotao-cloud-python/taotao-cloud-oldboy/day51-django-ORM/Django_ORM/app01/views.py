from django.shortcuts import render, HttpResponse

# Create your views here.
from app01.models import *


def index(request):
    return render(request, "index.html")


def addbook(request):
    # b=Book(name="python基础",price=99,author="yuan",pub_date="2017-12-12")
    # b.save()

    Book.objects.create(name="老男孩linux", price=78, author="oldboy", pub_date="2016-12-12")
    # Book.objects.create(**dic)

    return HttpResponse("添加成功")


def update(request):
    Book.objects.filter(author="yuan").update(price=999)

    # b=Book.objects.get(author="oldboy")
    # b.price=120
    # b.save()
    # print(b)#<QuerySet [<Book: Book object>]>
    # print(type(b))

    return HttpResponse("修改成功！")


def delete(req):
    Book.objects.filter(author="oldboy").delete()

    return HttpResponse("success")


def select(req):
    # book_list=Book.objects.all()
    # print(book_list)
    # print(book_list[0])#Book object
    # book_list = Book.objects.filter(id=2)
    # book_list = Book.objects.all()[::2]
    # book_list = Book.objects.all()[::-1]
    # book_list = Book.objects.first()
    # book_list = Book.objects.last()
    # book_list = Book.objects.get(id=2)#只能取出一条记录时才不报错

    # ret1=Book.objects.filter(author="oldboy").values("name")
    # ret2=Book.objects.filter(author="yuan").values_list("name","price")

    # book_list=Book.objects.exclude(author="yuan").values("name","price")
    # print(ret1)
    # print(ret2)

    # book_list= Book.objects.all().values("name").distinct()
    # book_count= Book.objects.all().values("name").distinct().count()
    # print(book_count)

    # 万能的  __

    # book_list=Book.objects.filter(price__gt=50).values("name","price")
    book_list = Book.objects.filter(name__contains="P").values_list("name", "price")

    return render(req, "index.html", {"book_list": book_list})
