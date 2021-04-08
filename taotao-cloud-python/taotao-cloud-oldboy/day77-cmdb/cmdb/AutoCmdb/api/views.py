#!/usr/bin/env python
# -*- coding:utf-8 -*-
import json
import importlib
from django.views import View
from django.http import JsonResponse
from django.shortcuts import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator

from utils import auth
from api import config
from repository import models
from api.service import asset

@auth.api_auth
def asset(request):
    pass


class AssetView(View):
    @method_decorator(csrf_exempt)
    def dispatch(self, request, *args, **kwargs):
        return super(AssetView, self).dispatch(request, *args, **kwargs)

    @method_decorator(auth.api_auth)
    def get(self, request, *args, **kwargs):
        """
        获取今日未更新的资产 - 适用SSH或Salt客户端
        :param request:
        :param args:
        :param kwargs:
        :return:
        """

        # test = {'user': '用户名', 'pwd': '密码'}
        # result = json.dumps(test,ensure_ascii=True)
        # result = json.dumps(test,ensure_ascii=False)
        # return HttpResponse(result)

        # test = {'user': '用户名', 'pwd': '密码'}
        # result = json.dumps(test,ensure_ascii=True)
        # result = json.dumps(test,ensure_ascii=False)
        # return HttpResponse(result,content_type='application/json')

        # test = {'user': '用户名', 'pwd': '密码'}
        # return JsonResponse(test, json_dumps_params={"ensure_ascii": False})

        response = asset.get_untreated_servers()
        return JsonResponse(response.__dict__)

    # @method_decorator(auth.api_auth)
    # def post(self, request, *args, **kwargs):
    #     """
    #     更新或者添加资产信息
    #     :param request:
    #     :param args:
    #     :param kwargs:
    #     :return: 1000 成功;1001 接口授权失败;1002 数据库中资产不存在
    #     """
    #
    #     server_info = json.loads(request.body.decode('utf-8'))
    #     server_info = json.loads(server_info)
    #     # ret = {'code': 1000, 'message': ''}
    #     # print(server_info)
    #     hostname = server_info['hostname']
    #
    #     ret = {'code': 1000, 'message': '[%s]更新完成' % hostname}
    #     # server_info 最新汇报服务器所有信息
    #
    #     # 根据主机名去数据库中获取相关信息
    #     server_obj = models.Server.objects.filter(hostname=hostname).select_related('asset').first()
    #     if not server_obj:
    #         ret['code'] = 1002
    #         ret['message'] = '[%s]资产不存在' % hostname
    #         return JsonResponse(ret)
    #
    #     for k, v in config.PLUGINS_DICT.items():
    #         module_path, cls_name = v.rsplit('.', 1)
    #         cls = getattr(importlib.import_module(module_path), cls_name)
    #         response = cls.process(server_obj, server_info, None)
    #         if not response.status:
    #             ret['code'] = 1003
    #             ret['message'] = "[%s]资产更新异常" % hostname
    #         if hasattr(cls, 'update_last_time'):
    #             cls.update_last_time(server_obj, None)
    #
    #     return JsonResponse(ret)

    @method_decorator(auth.api_auth)
    def post(self, request, *args, **kwargs):
        """
        更新或者添加资产信息
        :param request:
        :param args:
        :param kwargs:
        :return: 1000 成功;1001 接口授权失败;1002 数据库中资产不存在
        """

        server_info = json.loads(request.body.decode('utf-8'))
        server_info = json.loads(server_info)
        # ret = {'code': 1000, 'message': ''}
        # print(server_info)
        hostname = server_info['hostname']

        ret = {'code': 1000, 'message': '[%s]更新完成' % hostname}
        # server_info 最新汇报服务器所有信息

        # 根据主机名去数据库中获取相关信息
        server_obj = models.Server.objects.filter(hostname=hostname).select_related('asset').first()
        if not server_obj:
            ret['code'] = 1002
            ret['message'] = '[%s]资产不存在' % hostname
            return JsonResponse(ret)

        # ========》 server_obj服务器对象 ；server_info  《==========
        # 硬盘 或 网卡 或 内存
        # 硬盘：增删改
        # 1. server_obj反向关联硬盘表，获取数据库中硬盘信息
        # [
        #     {'slot': "#1", 'size': '100'},
        #     {'slot': "#2", 'size': '60'},
        #     {'slot': "#3", 'size': '88'},
        # ]
        # old_list = ['#1','#2','#3']
        # 2. server_info['disk'] 新汇报的硬盘数据
        # {
        #     "#1":{'slot': "#1", 'size': '90'},
        #     "#4":{'slot': "#4", 'size': '40'},
        # }
        # new_list = ['#1','#4']
        #3. 更新['#1'] 删除['#2','#3'] 增加 ['#4']

        #4. # 增加 ['#4']
        """
            for i in  ['#4']:
                data_dict = dic[i]
                models.Diks.objces.create(**data_dict)


       """




        return JsonResponse(ret)