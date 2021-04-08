from django.conf.urls import url,include
from django.contrib import admin

from blog import views

urlpatterns = [

    url(r'article/(\d{4})$',views.article_year),

    url(r'article/(?P<year>\d{4})/(?P<month>\d{2})',views.article_year_month),
    url(r'article/(?P<year>\d{4})/(?P<month>\d{2})/\d+',views.article_year_month),

    url(r"register",views.register,name="reg"),

]