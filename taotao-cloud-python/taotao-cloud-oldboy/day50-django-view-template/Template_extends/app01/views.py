from django.shortcuts import render

# Create your views here.


def backend(request):


    #return render(request,"index.html")
    return render(request,"base.html")



def student(req):
    student_list=["志超","正文","浩辰","邵晨"]

    return render(req,"student2.html",locals())