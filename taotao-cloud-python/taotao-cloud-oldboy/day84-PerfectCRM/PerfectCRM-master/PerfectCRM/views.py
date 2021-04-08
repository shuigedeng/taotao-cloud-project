#_*_coding:utf-8_*_

from django.shortcuts import render,HttpResponse

from django.views.generic import TemplateView

class PortalView(TemplateView):
    template_name = "portal.html"