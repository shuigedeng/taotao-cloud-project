from django.shortcuts import render, HttpResponse
from django.http import JsonResponse
from app01 import models
import json


def imgs(request):
    return render(request, 'img.html')


def get_imgs(request):
    nid = request.GET.get('nid')
    img_list = models.Img.objects.filter(id__gt=nid).values('id', 'src', 'title')
    img_list = list(img_list)
    ret = {
        'status': True,
        'data': img_list
    }
    return JsonResponse(ret)
