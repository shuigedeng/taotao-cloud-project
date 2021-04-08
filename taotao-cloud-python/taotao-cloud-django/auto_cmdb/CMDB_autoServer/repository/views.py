from django.shortcuts import render, HttpResponse
from django.http import JsonResponse
from rest_framework.views import APIView
from rest_framework.response import Response
from repository import models
from django.views import View
from rest_framework.parsers import JSONParser
from . import serializers

#************ rest_framework 快速搭建api *********************
# from repository import models
# from rest_framework import viewsets
# from rest_framework import serializers
#
#
# class ServerInfoSerializer(serializers.HyperlinkedModelSerializer):
#     class Meta:
#         model = models.Server
#         fields = ('id', 'hostname', 'sn', 'os_platform',)
#         depth = 1
#
#
# class ServerViewSet(viewsets.ModelViewSet):
#     queryset = models.Server.objects.all().order_by('-id')
#     serializer_class = ServerInfoSerializer

# ************ rest_framework 快速搭建api *********************


class Servers(APIView):
    def get(self, request):
        server_list = models.Server.objects.all()
        serializer = serializers.MySerializer(instance=server_list, many=True)
        return JsonResponse(serializer.data, safe=False)

    def post(self, request):
        data = JSONParser().parse(request)
        serializer = serializers.MySerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=400)


class ServerDetail(APIView):
    def get(self, request, nid):
        obj = models.Server.objects.filter(id=nid).first()
        serializer = serializers.MySerializer(instance=obj)
        return JsonResponse(serializer.data, safe=False)

    def delete(self, request, nid):
        obj = models.Server.objects.filter(id=nid).delete()
        return HttpResponse(status=204)

    def put(self, request, nid):
        obj = models.Server.objects.filter(id=nid).delete()
        data = JSONParser().parse(request)
        print(data)
        serializer = serializers.MySerializer(instance=obj, data=data)
        if serializer.is_valid():
            serializer.save()
            return HttpResponse(status=200)