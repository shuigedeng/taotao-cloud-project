from django.shortcuts import render
from django.shortcuts import HttpResponse

from django import forms
from django.forms import fields
class UploadForm(forms.Form):
    user = fields.CharField()
    img = fields.FileField()
def upload(request):
    if request.method == 'GET':
        return render(request,'upload.html')
    else:
        # obj = UploadForm(request.POST,request.FILES)
        # if obj.is_valid():
        #     user = obj.cleaned_data['user']
        #     img = obj.cleaned_data['img']
        user = request.POST.get('user')
        img  = request.FILES.get('img')
        # img是对象（文件大小，文件名称,文件内容。。。）
        print(img.name)
        print(img.size)
        f = open(img.name,'wb')
        for line in img.chunks():
            f.write(line)
        f.close()
        return HttpResponse('...')

def test(request):
    from app01 import models
    # 3
    # person_list = models.Person.objects.all()
    # select * from person
    # person_list = models.Person.objects.all().select_related('ut','te')
    # # select * from person left join usertype on person.ut_id = usertype.id
    # for row in person_list:
    #     print(row.user,row.id,row.ut_id)
    #     # SQL请求
    #     print(row.ut.title)
    #     print(row.te.title)


    # v = models.Person.objects.all()
    # v = models.Person.objects.all().reverse()
    #
    #
    # v = models.Person.objects.all().order_by('-id')
    # v = models.Person.objects.all().order_by('-id').reverse()
    #
    # v = models.Person.objects.all().order_by('-id','name')
    # v = models.Person.objects.all().order_by('-id','name').reverse()
    # v = models.Person.objects.all().order_by('id', '-name')

    # models.Person.objects.all().order_by('-id', 'name').using('default1')

    return HttpResponse('...')