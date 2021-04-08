from django.shortcuts import render
from django.shortcuts import HttpResponse
from app01 import models
import json
def xuliehua(request):

    return render(request,'xuliehua.html')

"""
def get_data(request):
    user_list = models.UserInfo.objects.all()
    return render(request,'get_data.html',{'user_list':user_list})
"""
def get_data(request):
    from django.core import serializers

    ret = {'status':True,'data':None}
    try:
        # user_list = models.UserInfo.objects.all()
        # QuerySet【obj,obj,obj】
        # ret['data'] = serializers.serialize("json",user_list)
        # // var
        # v = JSON.parse(arg.data);
        # // console.log(v);

        # user_list = models.UserInfo.objects.all().values('id','username')
        # ret['data'] = list(user_list)
        # console.log(arg.data);

        # user_list = models.UserInfo.objects.all().values_list('id', 'username')
        # ret['data'] = list(user_list)
        # console.log(arg.data);
        pass
    except Exception as e:
        ret['status'] = False
    result = json.dumps(ret)
    return HttpResponse(result)