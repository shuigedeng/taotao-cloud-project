#_*_coding:utf-8_*_

from django.shortcuts import render,HttpResponseRedirect,Http404,redirect

from django.core.exceptions import ObjectDoesNotExist
from django.db import IntegrityError

from django.contrib.auth.decorators import login_required

from django.core.paginator import Paginator,EmptyPage,PageNotAnInteger
from  crm.king_admin_old.king_admin import enabled_admins
from crm.king_admin_old import tables
from crm.king_admin_old import forms
from crm.king_admin_old.permissions import check_permission
from crm import models
import re



@check_permission
@login_required
def configure_index(request):

    return render(request,'king_admin/index.html', {'enabled_admins':enabled_admins,
                                                    })




@check_permission
@login_required
def configure_url_dispatch(request,url):
    print('---url dispatch',url)
    #print(enabled_admins)
    if url in enabled_admins:
        #print(enabled_admins[url])

        if request.method == "POST":
            print('post-->', request.POST)

            delete_tag = request.POST.get("_delete_confirm")
            if delete_tag == "yes":
                del_ids = request.POST.getlist("deleted_objs")

                enabled_admins[url].model.objects.filter(id__in=del_ids).delete()

            else:

                admin_action = request.POST.get('admin_action')

                admin_obj = enabled_admins[url]
                if hasattr(admin_obj, admin_action):
                    admin_action_func = getattr(admin_obj, admin_action)
                    return admin_action_func(request)
                else:
                    raise NotImplementedError("admin_action %s cannot find" % admin_action)


        querysets = tables.table_filter(request, enabled_admins[url],
                                        enabled_admins[url].model)
        searched_querysets = tables.search_by(request,querysets,enabled_admins[url])
        order_res = tables.get_orderby(request, searched_querysets, enabled_admins[url])

        paginator = Paginator(order_res[0], enabled_admins[url].list_per_page)

        page = request.GET.get('page')
        try:
            table_obj_list = paginator.page(page)
        except PageNotAnInteger:
            table_obj_list = paginator.page(1)
        except EmptyPage:
            table_obj_list = paginator.page(paginator.num_pages)

        table_obj = tables.TableHandler(request,
                                        enabled_admins[url].model,
                                        enabled_admins[url],
                                        table_obj_list,
                                        order_res)



        return render(request,'king_admin/model_obj_list.html',
                                                {'table_obj':table_obj,
                                                 'active_url': '/kingadmin/',
                                                 'paginator':paginator})

    else:
        raise Http404("url %s not found" % url )



@check_permission
@login_required
def table_change(request,table_name,obj_id):
    print("table change:",table_name ,obj_id)
    if table_name in enabled_admins:
        #print(enabled_admins[table_name])
        obj = enabled_admins[table_name].model.objects.get(id=obj_id)
        #print("obj....change",obj)
        fields = []
        for field_obj in enabled_admins[table_name].model._meta.fields:
            if field_obj.editable :
                fields.append(field_obj.name)

        for field_obj in enabled_admins[table_name].model._meta.many_to_many:
            fields.append(field_obj.name)
        #print('fields', fields)
        model_form = forms.create_form(enabled_admins[table_name].model, fields,enabled_admins[table_name],request=request)

        if request.method == "GET":
            form_obj = model_form(instance=obj)

        elif request.method == "POST":
            print("post:",request.POST)
            form_obj = model_form(request.POST,instance=obj)
            if form_obj.is_valid():
                form_obj.validate_unique()
                if form_obj.is_valid():
                    form_obj.save()

        return render(request,'king_admin/table_change.html',
                      {'form_obj':form_obj,
                       'active_url': '/kingadmin/',
                      'model_name':enabled_admins[table_name].model._meta.verbose_name,
                      'model_db_table': enabled_admins[table_name].model._meta.db_table,
                       'admin_class':enabled_admins[table_name]

                        })
    else:
        raise Http404("url %s not found" % table_name )



def table_del(request,table_name,obj_id):

    if table_name in enabled_admins:
        obj = enabled_admins[table_name].model.objects.get(id=obj_id)
        if enabled_admins[table_name].readonly_table is True:
            return render(request, 'king_admin/table_delete.html')
        return render(request,'king_admin/table_delete.html',{
            'model_name': enabled_admins[table_name].model._meta.verbose_name,
            'model_table_name':enabled_admins[table_name].model._meta.model_name,
            'model_db_table':enabled_admins[table_name].model._meta.db_table,
            'obj':obj,
            'app_label':obj._meta.app_label
                                })



def table_add(request,table_name):
    print("request path:",request.path)
    if table_name in enabled_admins:
        fields = []
        for field_obj in enabled_admins[table_name].model._meta.fields:
            if field_obj.editable:
                fields.append(field_obj.name)
        for field_obj in enabled_admins[table_name].model._meta.many_to_many:
            fields.append(field_obj.name)
        if enabled_admins[table_name].add_form == None:
            model_form = forms.create_form(enabled_admins[table_name].model,
                                           fields,enabled_admins[table_name],
                                           form_create=True,request=request)
        else: #this admin has customized  creation form defined
            model_form = enabled_admins[table_name].add_form

        if request.method == "GET":
            form_obj = model_form()
        elif request.method == "POST":
            form_obj = model_form(request.POST)
            if form_obj.is_valid():
                form_obj.validate_unique()
                if form_obj.is_valid():
                    form_obj.save()
                    print("form obj:",form_obj.cleaned_data,form_obj.instance.id)
                    redirect_url = '%s/%s/' %( re.sub("add/$", "change",request.path), form_obj.instance.id)
                    print('redirect url',redirect_url)
                    return redirect(redirect_url)

            # if request.POST.get('_continue') is not None: #save and add another button
            #     form_obj = model_form()

        return render(request, 'king_admin/table_add.html',
                      {'form_obj': form_obj,
                       'model_name': enabled_admins[table_name].model._meta.verbose_name,
                       'model_db_table':enabled_admins[table_name].model._meta.db_table,
                       'admin_class': enabled_admins[table_name],
                       'active_url': '/kingadmin/',
                       })

    else:
        raise Http404("url %s not found" % table_name)



@login_required
def password_reset_form(request,table_db_name,user_id):
    user_obj = models.UserProfile.objects.get(id=user_id)
    if request.method == "GET":
        change_form = enabled_admins[table_db_name].add_form(instance=user_obj)
    else:
        change_form = enabled_admins[table_db_name].add_form(request.POST,instance=user_obj)
        if change_form.is_valid():
            change_form.save()
            url = "/%s/" %request.path.strip("/password/")
            return redirect(url)

    return render(request,'king_admin/password_change.html',{'user_obj':user_obj,
                                                  'form':change_form})

