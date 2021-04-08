from django.shortcuts import render,redirect
from django.contrib.auth import authenticate,login,logout
from django.contrib.auth.decorators import login_required
from django import conf
from django.db.models import Q
from django.core.paginator import Paginator,PageNotAnInteger,EmptyPage
from kingadmin import app_setup
from crm import models
app_setup.kingadmin_auto_discover()


from kingadmin.sites import  site
print("sites.",site.enabled_admins)

# for k,v in site.enabled_admins.items():
#     for table_name,admin_class in v.items():
#         print(table_name,id(admin_class))
# # Create your views here.


def app_index(request):
    #enabled_admins =

    return render(request,'kingadmin/app_index.html', {'site':site})

def get_filter_result(request,querysets):
    filter_conditions = {}
    for key,val in request.GET.items():
        if key in ('_page','_o','_q'):continue
        if val:
            filter_conditions[key] =  val


    print("filter_conditions",filter_conditions)
    return querysets.filter(**filter_conditions),filter_conditions

def get_orderby_result(request,querysets,admin_class):
    """排序"""

    current_ordered_column = {}
    orderby_index = request.GET.get('_o')
    if orderby_index:
        orderby_key =  admin_class.list_display[ abs(int(orderby_index)) ]
        current_ordered_column[orderby_key] = orderby_index #为了让前端知道当前排序的列

        if orderby_index.startswith('-'):
            orderby_key =  '-'+ orderby_key

        return querysets.order_by(orderby_key),current_ordered_column
    else:
        return querysets,current_ordered_column


def get_serached_result(request,querysets,admin_class):


    search_key = request.GET.get('_q')
    if search_key :
        q = Q()
        q.connector = 'OR'

        for search_field in admin_class.search_fields:
            q.children.append(("%s__contains"% search_field,search_key))


        return  querysets.filter(q)
    return querysets




@login_required
def table_obj_list(request,app_name,model_name):
    """取出指定model里的数据返回给前端"""
    #print("app_name,model_name:",site.enabled_admins[app_name][model_name])
    admin_class = site.enabled_admins[app_name][model_name]
    querysets = admin_class.model.objects.all()

    querysets,filter_condtions  = get_filter_result(request,querysets)
    admin_class.filter_condtions = filter_condtions

    #searched queryset result
    querysets = get_serached_result(request,querysets,admin_class)
    admin_class.search_key = request.GET.get('_q','')

    #sorted querysets
    querysets,sorted_column = get_orderby_result(request,querysets,admin_class)


    paginator = Paginator(querysets, 2) # Show 25 contacts per page

    page = request.GET.get('_page')
    try:
        querysets = paginator.page(page)
    except PageNotAnInteger:
        # If page is not an integer, deliver first page.
        querysets = paginator.page(1)
    except EmptyPage:
        # If page is out of range (e.g. 9999), deliver last page of results.
        querysets = paginator.page(paginator.num_pages)
    print(request.GET)
    #print("admin class",admin_class.model )

    return render(request,'kingadmin/table_obj_list.html', {'querysets':querysets,
                                                            'admin_class':admin_class,
                                                            'sorted_column':sorted_column})


def acc_login(request):
    error_msg = ''
    if request.method == "POST":
        username = request.POST.get('username')
        password = request.POST.get('password')

        user = authenticate(username=username,password=password)
        if user:
            print("passed authencation",user)
            login(request,user)
            #request.user = user

            return  redirect( request.GET.get('next','/kingadmin/') )
        else:
            error_msg = "Wrong username or password!"
    return render(request, 'kingadmin/login.html', {'error_msg':error_msg})


def acc_logout(request):
    logout(request)
    return redirect("/login/")
