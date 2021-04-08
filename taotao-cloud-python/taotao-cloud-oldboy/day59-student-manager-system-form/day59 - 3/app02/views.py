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