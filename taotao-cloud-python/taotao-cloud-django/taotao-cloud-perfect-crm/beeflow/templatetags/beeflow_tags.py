#_*_coding:utf-8_*_
from django import template


register = template.Library()


@register.simple_tag
def get_flow_status(flow_obj):

    return  flow_obj.flowrecord_set.last()


