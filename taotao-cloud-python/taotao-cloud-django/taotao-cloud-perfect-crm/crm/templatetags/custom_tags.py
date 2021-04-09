#_*_coding:utf-8_*_
__author__ = 'Alex Li'
from django import template
from django.utils.safestring import mark_safe
import re

register = template.Library()
@register.simple_tag
def render_page_num(request,paginator_obj,loop_counter):
    abs_full_url = request.get_full_path()

    if "?page=" in abs_full_url:
        url = re.sub("page=\d+", "page=%s" % loop_counter, request.get_full_path())
    elif "?" in abs_full_url:
        url = "%s&page=%s" % (request.get_full_path(), loop_counter)
    else:
        url = "%s?page=%s" % (request.get_full_path(), loop_counter)


    if loop_counter == paginator_obj.number: #current page
        return mark_safe('''<li class='active'><a href="{abs_url}">{page_num}</a></li>'''\
            .format(abs_url=url,page_num=loop_counter))


    if abs(loop_counter - paginator_obj.number) <2 or \
        loop_counter == 1 or loop_counter == paginator_obj.paginator.num_pages: #the first page or last

        return mark_safe('''<li><a href="{abs_url}">{page_num}</a></li>'''\
            .format(abs_url= url,page_num=loop_counter))
    elif abs(loop_counter - paginator_obj.number) <3:
        return mark_safe('''<li><a href="{abs_url}">...</a></li>'''\
            .format(abs_url=url ,page_num=loop_counter))
    else:
        return ''

@register.simple_tag
def display_orderby_arrow(table_obj,loop_counter):
    if table_obj.orderby_col_index == loop_counter:
        if table_obj.orderby_field.startswith('-'):#降序
            orderby_icon = '''<i class="fa fa-caret-up" aria-hidden="true"></i>'''
        else:
            orderby_icon = '''<i class="fa fa-caret-down" aria-hidden="true"></i>'''
        return mark_safe(orderby_icon)
    return ''
@register.simple_tag
def build_table_row(row_obj,table_obj):
    row_ele = "<tr>"
    for column_name in table_obj.list_display:
        column_data = row_obj._meta.get_field(column_name)._get_val_from_obj(row_obj)
        if column_name in table_obj.choice_fields:
            column_data = getattr(row_obj,'get_%s_display'%column_name)()
        if column_name in table_obj.fk_fields:
            column_data = getattr(row_obj,column_name).__str__()
        column = "<td>%s</td>" % column_data
        row_ele +=column

    row_ele += "</tr>"
    return mark_safe(row_ele)

@register.filter
def to_string(value):
    return '%s' %value


@register.simple_tag
def load_menus(request):
    menus = []
    for role in request.user.roles.select_related():
        menus.extend(role.menus.select_related())
    print("menus",menus)

    return sorted(set(menus) , key=lambda x:x.order )