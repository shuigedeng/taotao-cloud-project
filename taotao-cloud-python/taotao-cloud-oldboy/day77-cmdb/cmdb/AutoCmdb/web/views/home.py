#!/usr/bin/env python
# -*- coding:utf-8 -*-
from django.views import View
from django.shortcuts import render
from django.http import JsonResponse
from web.service import chart


class IndexView(View):
    def get(self, request, *args, **kwargs):
        return render(request, 'index.html')


class CmdbView(View):
    def get(self, request, *args, **kwargs):
        return render(request, 'cmdb.html')


class ChartView(View):
    def get(self, request, chart_type):
        if chart_type == 'business':
            response = chart.Business.chart()
        if chart_type == 'dynamic':
            last_id = request.GET.get('last_id')
            response = chart.Dynamic.chart(last_id)
        return JsonResponse(response.__dict__, safe=False, json_dumps_params={'ensure_ascii': False})
