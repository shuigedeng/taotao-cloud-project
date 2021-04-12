from django.shortcuts import render, HttpResponse
from repository import models
from django.conf import settings
from Crypto.Cipher import AES
from api.service import PluginsManage
import json
import hashlib
import time


api_openkey_record = {
    # 'openkey': 'time+10'
}


def api_access(func):
    def wrapper(requset, *args, **kwargs):
        """
        api接口权限验证
        :param requset:
        :param args:
        :param kwargs:
        :return:
        """
        client_openkey = requset.META.get('HTTP_OPENKEY')
        # print(client_openkey, 'client open key.........')
        client_key, client_time = client_openkey.split('|')
        client_time = float(client_time)
        server_time = time.time()
        if server_time - client_time > 30:
            print('接口超时')
            return HttpResponse('接口超时')
        temp = "%s|%s" % (settings.AUTHKEY, client_time)
        m = hashlib.md5()
        m.update(bytes(temp, encoding='utf-8'))
        server_openkey = m.hexdigest()
        # print(server_openkey, client_openkey)
        if server_openkey != client_key:
            print('接口加密规则验证失败')
            return HttpResponse('接口验证失败')
        for openkey in list(api_openkey_record):
            overtime = api_openkey_record[openkey]
            if server_time > overtime:
                del api_openkey_record[openkey]
        # print(api_openkey_record)
        if client_openkey in api_openkey_record:
            print('该接口已被请求过')
            return HttpResponse('该接口已被请求过')
        else:
            api_openkey_record[client_openkey] = client_time + 30
            print(api_openkey_record, 'new...')
            ret = func(requset)
            return ret
    return wrapper


def decrypt(data):
    """
    post数据解密
    :param data:
    :return:
    """
    key = settings.DATAKEY
    cipher = AES.new(key, AES.MODE_CBC, key)
    result = cipher.decrypt(data)
    data = result[0: -result[-1]]
    return str(data, encoding='utf-8')


@api_access
def test(request):
    """
    测试
    :param request:
    :return:
    """
    # data = {
    #     'Basic': {'status': True, 'data': {'hostname': 'a1.com', 'os_platform': 'super-xxx', 'os_version': 'v1.123'}}
    # }
    b_data = request.body
    # print(b_data)
    decrypt_data = decrypt(b_data)
    # print(decrypt_data, type(decrypt_data))
    data = json.loads(decrypt_data)
    print(data,type(data))
    obj = PluginsManage(data=data)
    ret = obj.exec_plugin()
    if ret:
        return HttpResponse('api access successful')
    else:
        return HttpResponse('api access failed....')


@api_access
def asset(request):
    if request.method == 'POST':
        b_data = request.body
        decrypt_data = decrypt(b_data)
        data = json.loads(decrypt_data)
        obj = PluginsManage(data=data)
        ret = obj.exec_plugin()
        if ret:
            return HttpResponse('更新资产成功')
        else:
            return HttpResponse('提交的主机暂未注册')
