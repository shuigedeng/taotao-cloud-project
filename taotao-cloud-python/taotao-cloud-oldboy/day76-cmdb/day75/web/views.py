from django.shortcuts import render,HttpResponse
from django.views import View
import json

class AssetView(View):

    def get(self,request,*args,**kwargs):
        # 数据库中获取数据
        return render(request,'asset.html')

class AssetJsonView(View):
    def get(self,request,*args,**kwargs):
        # 数据库中获取数据
        table_config = [
            {
                'q': 'id',
                'title': 'ID',
                'display': False,
                'text':{},
                'attrs': {}
            },
            {
                'q': 'device_type_id',
                'title': '资产类型',
                'display': True,
                'text': {'content': "{n}", 'kwargs': {'n': "@@device_type_choices"}},
                'attrs': {}
            },
            {
                'q': 'device_status_id',
                'title': '状态',
                'display': True,
                'text': {'content': "{n}", 'kwargs': {'n': "@@device_status_choices"}},
                'attrs': {'edit-enable': 'true', 'edit-type': 'select'}
            },
            {
                'q': 'idc__name',
                'title': 'IDC',
                'display': True,
                'text': {'content': "{n}", 'kwargs': {'n': "@idc__name"}},
                'attrs': {'edit-enable': 'true', 'edit-type': 'select'}
            },
            {
                'q': 'cabinet_order',
                'title': '机柜位置',
                'display': True,
                'text': {'content': "{n}",'kwargs': {'n': "@cabinet_order"}},
                'attrs': {'edit-enable': 'true', 'edit-type': 'input'}
            },
            {
                'q': 'cabinet_num',
                'title': '机柜号',
                'display': True,
                'text': {'content': "{n}", 'kwargs': {'n': "@cabinet_num"}},
                'attrs': {},
            },
            {
                'q': None,
                'title': '操作',
                'display': True,
                'text': {'content': "<a href='/assetdetail-{m}.html'>{n}</a>", 'kwargs': {'n': '查看详细','m': '@id'}},
                'attrs': {},
            }
        ]

        q_list = []
        for i in table_config:
            if not i['q']:
                continue
            q_list.append(i['q'])

        from repository import models

        data_list = models.Asset.objects.all().values(*q_list)
        data_list = list(data_list)


        result = {
            'table_config':table_config,
            'data_list':data_list,
            'global_dict': {
                'device_type_choices': models.Asset.device_type_choices,
                'device_status_choices': models.Asset.device_status_choices
            }

        }
        return HttpResponse(json.dumps(result))