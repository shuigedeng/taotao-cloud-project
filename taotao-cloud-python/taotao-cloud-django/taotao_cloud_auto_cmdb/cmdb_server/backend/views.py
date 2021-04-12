import json
from datetime import datetime, date

from django.shortcuts import render, HttpResponse
from repository import models


class JsonCustomEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, datetime):
            return o.strftime('%Y-%m-%d %H:%M:%S')
        elif isinstance(o, date):
            return o.strftime('%Y-%m-%d')
        else:
            return json.JSONEncoder.default(self, o)


def get_data_list(request, model_cls, table_config):
    val_list = []
    for row in table_config:
        if not row['field']:
            continue
        val_list.append(row['field'])

    from django.db.models import Q
    condition = request.GET.get('condition')
    print(condition, 'condition')
    condition_dict = json.loads(condition)

    con = Q()
    for name, val in condition_dict.items():
        ele = Q()
        ele.connector = 'OR'
        for item in val:
            ele.children.append((name, item))
        con.add(ele, 'AND')

    server_list = model_cls.objects.filter(con).values(*val_list)
    return server_list


def home(request):
    if request.method == 'GET':
        return render(request, 'layout.html')
    return HttpResponse('post')


def show_server(request):
    return render(request, 'server.html')


def show_server_record(request):
    if request.method == 'DELETE':
        id_list = json.loads(str(request.body, encoding='utf-8'))
        print(id_list, 'id list')
        return HttpResponse('server delete')
    elif request.method == 'PUT':
        data_list = json.loads(str(request.body, encoding='utf-8'))
        print(data_list, 'data list')
        return HttpResponse('server put')
    elif request.method == 'POST':
        pass
    elif request.method == 'GET':
        from backend.page_config import server
        server_list = get_data_list(request, models.Server, server.table_config)
        ret = {
            'server_list': list(server_list),
            'table_config': server.table_config,
            'search_config': server.search_config,
            'global_dict': {
                'device_type_choices': models.Asset.device_type_choices,
                'device_status_choices': models.Asset.device_status_choices
            }
        }
        return HttpResponse(json.dumps(ret, cls=JsonCustomEncoder))


def show_asset(request):
    return render(request, 'asset.html')


def show_asset_record(request):
    if request.method == 'DELETE':
        id_list = json.loads(str(request.body, encoding='utf-8'))
        print(id_list, 'id list')
        return HttpResponse('server delete')
    elif request.method == 'PUT':
        data_list = json.loads(str(request.body, encoding='utf-8'))
        print(data_list, 'data list')
        return HttpResponse('server put')
    elif request.method == 'POST':
        pass
    elif request.method == 'GET':
        from backend.page_config import asset
        server_list = get_data_list(request, models.Asset, asset.table_config)
        ret = {
            'server_list': list(server_list),
            'table_config': asset.table_config,
            'search_config': asset.search_config,
            'global_dict': {
                'device_status_choices': models.Asset.device_status_choices,
                'device_type_choices': models.Asset.device_type_choices,
            }

        }
        return HttpResponse(json.dumps(ret, cls=JsonCustomEncoder))


def show_idc(request):
    return render(request, 'idc.html')


def show_idc_record(request):
    if request.method == 'DELETE':
        id_list = json.loads(str(request.body, encoding='utf-8'))
        print(id_list, 'id list')
        return HttpResponse('server delete')
    elif request.method == 'PUT':
        data_list = json.loads(str(request.body, encoding='utf-8'))
        print(data_list, 'data list')
        return HttpResponse('server put')
    elif request.method == 'POST':
        pass
    elif request.method == 'GET':
        from backend.page_config import idc
        server_list = get_data_list(request, models.IDC, idc.table_config)
        ret = {
            'server_list': list(server_list),
            'table_config': idc.table_config,
        }
        return HttpResponse(json.dumps(ret, cls=JsonCustomEncoder))
