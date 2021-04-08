from django.shortcuts import render
from django import forms
from django.forms import fields
from django.forms import widgets
class TestForm(forms.Form):
    user = fields.CharField(
        required=True, # 是否必填
        max_length=12, # 最大长度
        min_length=3,  # 最小长度
        error_messages={}, # 错误提示
        #widget = widgets.Select(), # 定制HTML插件
        label='用户名',
        initial='请输入用户',
        help_text='asdfasdf',
        show_hidden_initial=False,
        # validators=[]
        disabled=True,
        label_suffix='->'
    )
    age = fields.IntegerField(
        label='年龄',
        max_value= 12,
        min_value=5,
        error_messages={
            'max_value':'太大了'
        }
    )

    email = fields.EmailField(
        label='邮箱'
    )

    img = fields.FileField()

    city = fields.TypedChoiceField(
        coerce=lambda x: int(x),
        choices=[(1,'上海',),(2,'北京'),(3,'沙河'),],
        initial=2
    )
    bobby = fields.MultipleChoiceField(
        choices=[(1,'刚娘'),(2,'铁娘'),(3,'钢弹')],
        initial=[1,2]
    )

    xoo = fields.FilePathField(
        path='app01'
    )

def test(request):
    if request.method == 'GET':
        #obj = TestForm({'city':3})
        obj = TestForm()
        return render(request,'test.html',{'obj':obj})
    else:
        obj = TestForm(request.POST,request.FILES)
        obj.is_valid()
        print(obj.cleaned_data)
        return render(request, 'test.html', {'obj': obj})