from django.shortcuts import render,HttpResponse
from django.views.decorators.csrf import csrf_exempt,csrf_protect
import hashlib
import time
ck = "8kasoimnasodn8687asdfkmasdf"

auth_list=[]

@csrf_exempt
def asset(request):


    # print(request.method)
    # print(request.POST)
    # print(request.GET)
    # print(request.body)
    auth_key_time = request.META['HTTP_AUTHKEY']
    auth_key_client,client_ctime = auth_key_time.split('|')
    server_current_time = time.time()
    if server_current_time-5 > float(client_ctime):
        # 太久远
        return HttpResponse('时间太久远了')
    if auth_key_time in auth_list:
        # 已经访问过
        return HttpResponse('你来晚了')

    # auth_key:9ff77e3f67f4baf3e44d7c957d80bc9a
    # client_ctime:1492395317.8952246
    # 9ff77e3f67f4baf3e44d7c957d80bc9a|

    # 173b81be05cd997eeac31e2fa99eff1c|1492395689.3360105
    key_time = "%s|%s" %(ck,client_ctime,)
    m = hashlib.md5()
    m.update(bytes(key_time, encoding='utf-8'))
    authkey = m.hexdigest()

    if authkey != auth_key_client:
        return HttpResponse('授权失败')
    auth_list.append(auth_key_time)


    if request.method == 'POST':
        import json
        host_info = json.loads(str(request.body,encoding='utf-8'))
        print(host_info)
    return HttpResponse('....')


def test(request):
    # print(request.POST,type(request.POST))
    # from django.http.request import QueryDict
    response = render(request,'index.html')
    response.set_signed_cookie('kkkk','vvvv',salt='asdf')
    return response