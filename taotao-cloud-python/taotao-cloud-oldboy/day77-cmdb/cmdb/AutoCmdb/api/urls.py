from django.conf.urls import url
from django.conf.urls import include
from django.contrib import admin
from api import views

urlpatterns = [
    url(r'^asset$', views.AssetView.as_view()),
]
