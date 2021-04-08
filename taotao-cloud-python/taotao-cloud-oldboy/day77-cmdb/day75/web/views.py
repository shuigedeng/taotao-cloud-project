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
                'q': None,
                'title': "选项",
                'display': True,
                'text': {'content': "<input type='checkbox' />","kwargs": {}},
                'attrs': {}
            },
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
                'attrs': {'name':'device_status_id','origin':"@device_status_id",'edit-enable': 'true', 'edit-type': 'select',"global-name": 'device_status_choices'}
            },
            {
                'q': 'idc__id',
                'title': 'IDC',
                'display': False,
                'text': {},
                'attrs': {}
            },
            {
                'q': 'idc__name',
                'title': 'IDC',
                'display': True,
                'text': {'content': "{n}", 'kwargs': {'n': "@idc__name"}},
                'attrs': {'name':'idc_id','origin':"@idc__id",'edit-enable': 'true', 'edit-type': 'select',"global-name": 'idc_choices'}
            },
            {
                'q': 'cabinet_order',
                'title': '机柜位置',
                'display': True,
                'text': {'content': "{n}",'kwargs': {'n': "@cabinet_order"}},
                'attrs': {'name':'cabinet_order','origin':"@cabinet_order",'edit-enable': 'true', 'edit-type': 'input'}
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
        # 分页组件用户获取数据
        data_list = models.Asset.objects.all().values(*q_list)
        data_list = list(data_list)

        result = {
            'table_config':table_config,
            'data_list':data_list,
            'global_dict': {
                'device_type_choices': models.Asset.device_type_choices,
                'device_status_choices': models.Asset.device_status_choices,
                'idc_choices': list(models.IDC.objects.values_list('id','name'))
            },
            # 分页组件生成页码信息
            'pager': """<li><a>1</a></li><li><a>2</a></li><li><a>3</a></li><li><a>4</a></li><li><a>5</a></li>"""

        }
        return HttpResponse(json.dumps(result))

    def put(self,request,*args,**kwargs):
        content = request.body
        v = json.loads(str(content,encoding='utf-8'))
        print(v)
        ret = {
            'status':True
        }
        return HttpResponse(json.dumps(ret))

class BuinessUnitView(View):

    def get(self,request,*args,**kwargs):
        # 数据库中获取数据
        return render(request,'business_unit.html')

class BuinessUnitJsonView(View):
    def get(self,request,*args,**kwargs):
        # 数据库中获取数据
        table_config = [
            {
                'q': None,
                'title': "选项",
                'display': True,
                'text': {'content': "<input type='checkbox' />","kwargs": {}},
                'attrs': {}
            },
            {
                'q': 'id',
                'title': 'ID',
                'display': False,
                'text':{},
                'attrs': {}
            },
            {
                'q': 'name',
                'title': '业务线名称',
                'display': True,
                'text': {'content': "{n}", 'kwargs': {'n': "@name"}},
                'attrs': {'edit-enable':'true','edit-type':'input','origin': '@name'}
            },
            {
                'q': 'contact_id',
                'title': '联系人组',
                'display': False,
                'text': {},
                'attrs': {}
            },
            {
                'q': 'contact__name',
                'title': '联系人组',
                'display': True,
                'text': {'content': "{n}", 'kwargs': {'n': "@contact__name"}},
                'attrs': {'edit-enable':'true','edit-type':'select','origin': '@contact_id',"global-name": 'contact_choices'}
            },
            {
                'q': 'manager__name',
                'title': '管理员组',
                'display': True,
                'text': {'content': "{n}", 'kwargs': {'n': "@manager__name"}},
                'attrs': {}
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

        data_list = models.BusinessUnit.objects.all().values(*q_list)
        data_list = list(data_list)

        result = {
            'table_config':table_config,
            'data_list':data_list,
            'global_dict': {
                # 'device_type_choices': models.Asset.device_type_choices,
                # 'device_status_choices': models.Asset.device_status_choices,
                # 'idc_choices': list(models.IDC.objects.values_list('id','name'))
                'contact_choices':list(models.UserGroup.objects.values_list('id','name'))
            },
            'pager': """<li><a>1</a></li><li><a>2</a></li><li><a>3</a></li><li><a>4</a></li><li><a>5</a></li>"""

        }
        return HttpResponse(json.dumps(result))

    def put(self,request,*args,**kwargs):
        content = request.body
        v = json.loads(str(content,encoding='utf-8'))
        print(v)
        ret = {
            'status':True
        }
        return HttpResponse(json.dumps(ret))