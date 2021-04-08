from django.template import Library
from django.utils.safestring import mark_safe
import datetime ,time
register = Library()

@register.simple_tag
def build_filter_ele(filter_column,admin_class):

    column_obj = admin_class.model._meta.get_field(filter_column)
    print("column obj:",column_obj)
    try:
        filter_ele = "<select name='%s'>" % filter_column
        for choice in column_obj.get_choices():
            selected = ''
            if filter_column in admin_class.filter_condtions:#当前字段被过滤了
                # print("filter_column", choice,
                #       type(admin_class.filter_condtions.get(filter_column)),
                #       admin_class.filter_condtions.get(filter_column))
                if str(choice[0]) == admin_class.filter_condtions.get(filter_column):#当前值被选中了
                    selected = 'selected'
                    print('selected......')

            option = "<option value='%s' %s>%s</option>" % (choice[0],selected,choice[1])
            filter_ele += option
    except AttributeError as e:
        print("err",e)
        filter_ele = "<select name='%s__gte'>" % filter_column
        if column_obj.get_internal_type() in ('DateField','DateTimeField'):
            time_obj = datetime.datetime.now()
            time_list = [
                ['','------'],
                [time_obj,'Today'],
                [time_obj - datetime.timedelta(7),'七天内'],
                [time_obj.replace(day=1),'本月'],
                [time_obj - datetime.timedelta(90),'三个月内'],
                [time_obj.replace(month=1,day=1),'YearToDay(YTD)'],
                ['','ALL'],
            ]

            for i in time_list:
                selected = ''
                time_to_str = ''if not i[0] else  "%s-%s-%s"%(i[0].year,i[0].month,i[0].day)
                if  "%s__gte"% filter_column in admin_class.filter_condtions:  # 当前字段被过滤了
                    print('-------------gte')
                    if time_to_str == admin_class.filter_condtions.get("%s__gte"% filter_column):  # 当前值被选中了
                        selected = 'selected'
                option = "<option value='%s' %s>%s</option>" % \
                         (time_to_str ,selected,i[1])
                filter_ele += option

    filter_ele += "</select>"
    return mark_safe(filter_ele)



@register.simple_tag
def  build_table_row(obj,admin_class):
    """生成一条记录的html element"""

    ele = ""
    for column_name in admin_class.list_display:

        column_obj = admin_class.model._meta.get_field(column_name)
        if column_obj.choices: #get_xxx_display
            column_data = getattr(obj,'get_%s_display'% column_name)()
        else:
            column_data = getattr(obj,column_name)

        td_ele = "<td>%s</td>"% column_data
        ele += td_ele

    return mark_safe(ele)


