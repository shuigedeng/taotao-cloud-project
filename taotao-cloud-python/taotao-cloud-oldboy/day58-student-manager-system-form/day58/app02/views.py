from django.shortcuts import render
from django.shortcuts import redirect
from django.shortcuts import HttpResponse


from django import forms
from django.forms import fields
class F1Form(forms.Form):
    user = fields.CharField(
        max_length=18,
        min_length=6,
        required=True,
        error_messages={'required': '用户名不能为空','max_length': '太长了','min_length': '太短了','invalid':'..'}
    )
    pwd = fields.CharField(required=True,min_length=32)
    age = fields.IntegerField(
        required=True
    )
    email = fields.EmailField(
        required=True,
        min_length=8
    )


def f1(request):
    if request.method == 'GET':
        obj = F1Form()
        return render(request,'f1.html',{'obj':obj})
    else:
        obj = F1Form(request.POST)
        # 是否全部验证成功
        if obj.is_valid():
            # 用户提交的数据
            print('验证成功',obj.cleaned_data)
            return redirect('http://www.xiaohuar.com')
        else:
            print('验证失败',obj.errors)
            return render(request, 'f1.html',{'obj':obj})

