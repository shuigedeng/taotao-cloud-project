#_*_coding:utf-8_*_

from django.forms import forms,ModelForm
from django.utils import timezone
from crm import models
from django.db.models import Count

class CustomerForm(ModelForm):
    class Meta:
        model = models.Customer
        exclude = ()
        readonly_fields = ['qq', 'consultant']
    def __init__(self,*args,**kwargs):
        super(CustomerForm, self).__init__(*args, **kwargs)
        #self.fields['customer_note'].widget.attrs['class'] = 'form-control'

        for field_name in self.base_fields:
            field = self.base_fields[field_name]

            field.widget.attrs.update({'class': 'form-control'})
            if field_name in CustomerForm.Meta.readonly_fields:
                field.widget.attrs.update({'disabled': 'disabled'})

                #print("required:",field.required)
        #else:


class EnrollmentForm(ModelForm):
    class Meta:
        model = models.Enrollment
        exclude = ()


def get_orderby(request,model_objs,admin_form):
    orderby_field = request.GET.get('orderby')
    if orderby_field:
        orderby_field =orderby_field.strip()
        orderby_column_index = admin_form.list_display.index(orderby_field.strip('-'))
        objs = model_objs.order_by(orderby_field)
        #print("orderby",orderby_field)
        if orderby_field.startswith('-'):
            orderby_field = orderby_field.strip('-')
        else:
            orderby_field = '-%s' % orderby_field

        return [objs, orderby_field,orderby_column_index]
    else:
        return  [model_objs,orderby_field,None]

class TableHandler(object):
    def __init__(self,request,model_class,model_objs,AdminForm,order_res):
        self.request = request
        self.model_objs = model_objs
        self.admin_form = AdminForm
        self.model = model_class
        self.list_display = AdminForm.list_display
        self.choice_fields = AdminForm.choice_fields
        self.list_filter = self.get_list_filter(AdminForm.list_display)
        self.fk_fields = AdminForm.fk_fields
        self.orderby_field = order_res[1]
        self.orderby_col_index = order_res[2]
        #self._get_orderby()

    def get_list_filter(self,list_filter):
        filters = []
        for i in list_filter:
            col_obj = self.model._meta.get_field( i )
            print("col obj",col_obj)
            data = {
                'verbose_name'  : col_obj.verbose_name,
                'column_name' : i,
                #'choices' : col_obj.get_choices()
            }
            if col_obj.deconstruct()[1] not in ('django.db.models.DateField',):
                try:
                    choices = col_obj.get_choices()

                except AttributeError as e:
                    choices_list = col_obj.model.objects.values(i).annotate(count=Count(i))
                    choices =[ [obj[i],obj[i]] for obj in choices_list]
                    choices.insert(0,['','----------'])
            else: #特殊处理datefield
                today_obj = timezone.datetime.now()
                choices = [
                    ('','---------'),
                    (today_obj.strftime("%Y-%m-%d"),'今天'),
                    ((today_obj - timezone.timedelta(days=7)).strftime("%Y-%m-%d"),'过去7天'),
                    ((today_obj - timezone.timedelta(days=today_obj.day)).strftime("%Y-%m-%d"),'本月'),
                    ((today_obj - timezone.timedelta(days=90)).strftime("%Y-%m-%d"),'过去3个月'),
                    ((today_obj - timezone.timedelta(days=180)).strftime("%Y-%m-%d"),'过去6个月'),
                    ((today_obj - timezone.timedelta(days=365)).strftime("%Y-%m-%d"),'过去1年'),
                ]
            data['choices'] = choices

            #handle selected data
            if self.request.GET.get(i):
               data['selected'] =  self.request.GET.get(i)
            filters.append(data)
        #print(filters)

        return filters