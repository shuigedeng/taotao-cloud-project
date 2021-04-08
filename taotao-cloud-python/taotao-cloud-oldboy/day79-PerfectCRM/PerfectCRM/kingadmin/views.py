from django.shortcuts import render,redirect
from django.contrib.auth import authenticate,login,logout
from django.contrib.auth.decorators import login_required
from django import conf
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
        if val:
            filter_conditions[key] =  val


    print("filter_conditions",filter_conditions)
    return querysets.filter(**filter_conditions),filter_conditions

@login_required
def table_obj_list(request,app_name,model_name):
    """取出指定model里的数据返回给前端"""
    #print("app_name,model_name:",site.enabled_admins[app_name][model_name])
    admin_class = site.enabled_admins[app_name][model_name]
    querysets = admin_class.model.objects.all()

    querysets,filter_condtions  = get_filter_result(request,querysets)
    admin_class.filter_condtions = filter_condtions

    print(request.GET)
    #print("admin class",admin_class.model )

    return render(request,'kingadmin/table_obj_list.html', {'querysets':querysets,'admin_class':admin_class})


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
