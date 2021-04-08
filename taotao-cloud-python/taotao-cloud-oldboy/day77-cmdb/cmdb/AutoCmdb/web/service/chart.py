#!/usr/bin/env python
# -*- coding:utf-8 -*-
from repository import models
from django.db.models import Count, Avg, Max, Min, Sum, Aggregate
from utils.response import BaseResponse


class Business(object):
    @staticmethod
    def chart():
        response = BaseResponse()
        try:
            sql = """
                SELECT
                    id,
                    name,
                    (select count(id) from repository_asset as A where B.id=A.business_unit_id and A.device_type_id=1) as server_count,
                    (select count(id) from repository_asset as A where B.id=A.business_unit_id and A.device_type_id=2) as switch_count,
                    (select count(id) from repository_asset as A where B.id=A.business_unit_id and A.device_type_id=3) as firewall_count
                from repository_businessunit as B"""
            result = models.BusinessUnit.objects.raw(sql)
            ret = {
                'categories': [],
                'series': [
                    {
                        "name": '服务器',
                        "data": []
                    },
                    {
                        "name": '交换机',
                        "data": []
                    }, {
                        "name": '防火墙',
                        "data": []
                    }
                ]
            }
            for row in result:
                ret['categories'].append(row.name)
                ret['series'][0]['data'].append(row.server_count)
                ret['series'][1]['data'].append(row.switch_count)
                ret['series'][2]['data'].append(row.firewall_count)
            response.data = ret
        except Exception as e:
            response.status = False
            response.message = str(e)

        return response


class Dynamic(object):
    @staticmethod
    def chart(last_id):

        response = BaseResponse()
        try:
            import time
            import random

            last_id = int(last_id)
            if last_id == 0:
                end = 100
            else:
                end = random.randint(1, 10)
            ret = []
            for i in range(0, end):
                temp = {'x': time.time() * 1000, 'y': random.randint(1, 1000)}
                ret.append(temp)
            last_id += 10
            response.data = ret
            response.last_id = last_id
        except Exception as e:
            response.status = False
            response.message = str(e)

        return response






