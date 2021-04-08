from django.shortcuts import render,HttpResponseRedirect,Http404

from crm import forms
from crm import models
from crm import admin
from django.contrib.auth import logout,login,authenticate
from django.contrib.auth.decorators import login_required

from PerfectCRM import settings
from django.core.cache import cache

from django.core.paginator import Paginator,EmptyPage,PageNotAnInteger
from  crm.kingadmin import site
from kingadmin import forms as king_admin_forms
import datetime,os,random,string
from crm import verify_code

# Create your views here.

@login_required
def sales_dashboard(request):
    return render(request,'crm/dashboard.html')

@login_required
def customers(request):
    '''sales role home page'''
    print(request.GET)
    customer_list = models.Customer.objects.all()

    order_res = forms.get_orderby(request,customer_list,admin.CustomerAdmin)
    #print('----->',order_res)
    paginator = Paginator(order_res[0],admin.CustomerAdmin.list_per_page)

    page = request.GET.get('page')
    try:
        customer_objs = paginator.page(page)
    except PageNotAnInteger:
        customer_objs = paginator.page(1)
    except EmptyPage:
        customer_objs = paginator.page(paginator.num_pages)


    table_obj = forms.TableHandler(request,
                                   models.Customer,
                                   customer_objs,
                                   admin.CustomerAdmin,
                                   order_res)
    #print("list_filter",table_obj.list_filter)
    return render(request, "crm/customers.html", {"customer_table":table_obj,
                                                'paginator': paginator})
@login_required
def my_customers(request):
    '''每个销售自己的客户列表'''
    return render(request,"crm/my_customer.html")

@login_required
def sales_report(request):
    '''销售报表'''
    return render(request, "crm/sales_report.html")



def acc_login(request):
    err_msg = {}
    today_str = datetime.date.today().strftime("%Y%m%d")
    verify_code_img_path = "%s/%s" %(settings.VERIFICATION_CODE_IMGS_DIR,
                                     today_str)
    if not os.path.isdir(verify_code_img_path):
        os.makedirs(verify_code_img_path,exist_ok=True)
    print("session:",request.session.session_key)
    #print("session:",request.META.items())
    random_filename = "".join(random.sample(string.ascii_lowercase,4))
    random_code = verify_code.gene_code(verify_code_img_path,random_filename)
    cache.set(random_filename, random_code,30)

    if request.method == "POST":

        username = request.POST.get('username')
        password = request.POST.get('password')
        _verify_code = request.POST.get('verify_code')
        _verify_code_key  = request.POST.get('verify_code_key')

        print("verify_code_key:",_verify_code_key)
        print("verify_code:",_verify_code)
        if cache.get(_verify_code_key) == _verify_code:
            print("code verification pass!")

            user = authenticate(username=username,password=password)
            if user is not None:
                login(request,user)
                request.session.set_expiry(60*60)
                return HttpResponseRedirect(request.GET.get("next") if request.GET.get("next") else "/")

            else:
                err_msg["error"] = 'Wrong username or password!'

        else:
            err_msg['error'] = "验证码错误!"

    return render(request,'login.html',{"filename":random_filename, "today_str":today_str, "error":err_msg})





def acc_logout(request):

    logout(request)
    return HttpResponseRedirect("/account/login/")
    #return render(request,'login.html')




@login_required
def enrollment(request,customer_id):

    #form = forms.EnrollmentForm()

    fields = []
    for field_obj in  models.Enrollment._meta.fields:
        if field_obj.editable:
            fields.append(field_obj.name)

    print('site.enabled_admins:',site.enabled_admins)

    customer_obj = models.Customer.objects.get(id=customer_id)
    model_form  = king_admin_forms.create_form(models.Enrollment,
                                 fields,
                                 site.enabled_admins[models.Enrollment._meta.app_label][models.Enrollment._meta.model_name])

    form = model_form()
    response_msg = {}
    if request.method == "POST":

        if request.POST.get('paid_fee'): #payment form
            # 交费纪录
            fields = []
            for field_obj in models.PaymentRecord._meta.fields:
                if field_obj.editable:
                    fields.append(field_obj.name)

            model_form = king_admin_forms.create_form(models.PaymentRecord,
                                                      fields,
                                                      site.enabled_admins[models.PaymentRecord._meta.db_table])

            form = model_form(request.POST)
            if form.is_valid():
                form.save()
                enroll_obj = form.instance.enrollment
                customer_obj.status = "signed" #
                customer_obj.save()
                response_msg = {'msg': 'payment record got created,enrollment process is done', 'code': 4,
                                'step': 5,}
            else:
                enroll_obj = None
            return render(request, 'crm/enrollment.html',
                          {'response': response_msg,
                           'payment_form': form,
                           'customer_obj': customer_obj,
                           'enroll_obj': enroll_obj})

        post_data = request.POST.copy()
        print("post:",request.POST)
        form = model_form(post_data)
        exist_enrollment_objs = models.Enrollment.objects.filter(customer=customer_obj,course_grade=request.POST.get('course_grade'))
        if exist_enrollment_objs:

            if exist_enrollment_objs.filter(contract_agreed=True):
                #学生已填写完报名表
                enroll_obj = exist_enrollment_objs.get(contract_agreed=True)

                if enroll_obj.contract_approved or request.POST.get('contract_approved') == "on":
                    enroll_obj.contract_approved = True
                    enroll_obj.save()

                    if enroll_obj.paymentrecord_set.select_related().count() >0: #already has payment record
                        response_msg = {'msg': '已报名成功', 'code': 5, 'step': 6}
                        return render(request, 'crm/enrollment.html',
                                      {'response': response_msg,
                                       'customer_obj':customer_obj})
                    else:
                        response_msg = {'msg': 'contract approved, waiting for payment record to be created', 'code': 3, 'step': 4}

                        #交费纪录
                        fields = []
                        for field_obj in models.PaymentRecord._meta.fields:
                            if field_obj.editable:
                                fields.append(field_obj.name)

                        model_form = king_admin_forms.create_form(models.PaymentRecord,
                                                                  fields,
                                                                  site.enabled_admins[models.PaymentRecord._meta.db_table])

                        form = model_form()

                        return render(request, 'crm/enrollment.html',
                                      {'response': response_msg,
                                       'payment_form': form,
                                       'customer_obj': customer_obj,
                                       'enroll_obj':enroll_obj})

                else:
                    response_msg = {'msg': 'waiting for contract approval',
                                    'code':2,'step':3,
                                    'enroll_boj':enroll_obj}
                form = model_form(post_data, instance=enroll_obj)

            else:

                response_msg = {'msg':'enrollment_form already exist',
                                'code':1,'step':2,
                                'enroll_obj':exist_enrollment_objs[0],
                                }

            form.add_error('customer', '报名表已存在')
        #form.cleaned_data['customer'] = customer_obj

        if form.is_valid():
            form.save()
            response_msg = {'msg': 'enrollment_form created',
                            'enroll_obj':form.instance,'code': 1, 'step': 2}
    else:
        response_msg = {'msg': 'create enrollment form', 'code': 0, 'step': 1}
    return render(request,'crm/enrollment.html',{'response':response_msg,
                                                 'enrollment_form':form,
                                                 'customer_obj':customer_obj,
                                                 })



def stu_enrollment(request,enrollment_id):

    enroll_obj = models.Enrollment.objects.get(id=enrollment_id)
    customer_form = forms.CustomerForm(instance=enroll_obj.customer)
    return render(request,'crm/stu_enrollment.html', {'enroll_obj':enroll_obj,
                                                      'customer_form':customer_form})