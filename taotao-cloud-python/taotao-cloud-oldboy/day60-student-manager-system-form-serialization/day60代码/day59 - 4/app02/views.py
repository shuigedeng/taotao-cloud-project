from django.shortcuts import render
from django.shortcuts import redirect
from django.shortcuts import HttpResponse
from django import forms
from django.forms import fields
from django.forms import widgets
class TestForm(forms.Form):
    user = fields.CharField(
        required=True, # 是否必填
        max_length=12, # 最大长度
        min_length=3,  # 最小长度
        error_messages={}, # 错误提示
        widget = widgets.TextInput(attrs={'class':'c1'}), # 定制HTML插件
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

    # xdb = fields.CharField(
    #     widget=widgets.Select(choices=[(1,'刚娘'),(2,'铁娘'),(3,'钢弹')])
    # )

    # xdb = fields.IntegerField(
    #     widget=widgets.Select(choices=[(1,'刚娘'),(2,'铁娘'),(3,'钢弹')])
    # )

    # xdb = fields.ChoiceField(
    #     choices=[(1,'刚娘'),(2,'铁娘'),(3,'钢弹')]
    # )

    # xdb = fields.MultipleChoiceField(
    #     choices=[(1, '刚娘'), (2, '铁娘'), (3, '钢弹')],
    #     widget=widgets.SelectMultiple(attrs={'class':'c1'})
    # )

    # xdb = fields.CharField(
    #     widget=widgets.CheckboxInput()
    # )

    # xdb = fields.MultipleChoiceField(
    #     initial=[2, ],
    #     choices=((1, '上海'), (2, '北京'),),
    #     widget=widgets.CheckboxSelectMultiple
    # )

    # xdb =  fields.ChoiceField(
    #     choices=((1, '上海'), (2, '北京'),(3, '北京1'),),
    #     initial=2,
    #     widget=widgets.RadioSelect
    # )
def test(request):
    if request.method == 'GET':
        #obj = TestForm({'city':3})
        obj = TestForm()
        txt = "<input type='text' />"
        # from django.utils.safestring import mark_safe
        # txt = mark_safe(txt)
        return render(request,'test.html',{'obj':obj,'txt':txt})
    else:
        obj = TestForm(request.POST,request.FILES)
        obj.is_valid()
        print(obj.cleaned_data)
        return render(request, 'test.html', {'obj': obj})
from app01 import models
from django.forms.models import ModelChoiceField
class LoveForm(forms.Form):
    price = fields.IntegerField()
    user_id = fields.IntegerField(
        # widget=widgets.Select(choices=[(0,'alex'),(1,'刘皓宸'),(2,'杨建'),])
        widget=widgets.Select()
    )

    user_id2 = ModelChoiceField(
        queryset=models.UserInfo.objects.all(),
        to_field_name='id'
    )

    def __init__(self,*args,**kwargs):
        # 拷贝所有的静态字段，复制给self.fields
        super(LoveForm,self).__init__(*args,**kwargs)
        self.fields['user_id'].widget.choices = models.UserInfo.objects.values_list('id', 'username')


def love(request):
    obj = LoveForm()
    return render(request,'love.html',{'obj':obj})
from django.core.exceptions import NON_FIELD_ERRORS, ValidationError
class AjaxForm(forms.Form):
    username = fields.CharField()
    user_id = fields.IntegerField(
        widget=widgets.Select(choices=[(0,'alex'),(1,'刘皓宸'),(2,'杨建'),])
    )
    # 自定义方法 clean_字段名
    # 必须返回值self.cleaned_data['username']
    # 如果出错：raise ValidationError('用户名已存在')
    def clean_username(self):
        v = self.cleaned_data['username']
        if models.UserInfo.objects.filter(username=v).count():
            # 整体错了
            # 自己详细错误信息
            raise ValidationError('用户名已存在')
        return v
    def clean_user_id(self):
        return self.cleaned_data['user_id']

def ajax(request):
    if request.method == 'GET':
        obj = AjaxForm()
        return render(request,'ajax.html',{'obj':obj})
    else:
        ret = {'status':'杨建','message':None}
        import json
        obj = AjaxForm(request.POST)
        if obj.is_valid():
            # 跳转到百度
            # return redirect('http://www.baidu.com')
            # if ....
            #     obj.errors['username'] = ['用户名已经存在',]
            # if ....
            #     obj.errors['email'] = ['用户名已经存在',]

            ret['status'] = '钱'
            return HttpResponse(json.dumps(ret))
        else:
            # print(type(obj.errors))
            # print(obj.errors)
            from django.forms.utils import ErrorDict
            # print(obj.errors.as_ul())
            # print(obj.errors.as_json())
            # print(obj.errors.as_data())



            ret['message'] = obj.errors
            # 错误信息显示在页面上
            return HttpResponse(json.dumps(ret))




