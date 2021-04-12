from django.shortcuts import render,HttpResponse
from django.contrib.auth.decorators import login_required
from crm import forms
from crm import models
from crm import admin
from django.core.paginator import Paginator,EmptyPage,PageNotAnInteger
from PerfectCRM import settings
import os,json,time
# Create your views here.

@login_required
def my_courses(request):
    '''每个学员的课程列表'''
    return render(request,"stu/my_courses.html")

@login_required
def my_grade(request):
    return render(request,'stu/my_grade.html')

@login_required
def my_homeworks(request,enroll_id):

    enroll_obj = models.Enrollment.objects.get(id=enroll_id)
    return render(request,'stu/homeworks.html',{'enroll_obj':enroll_obj})


def get_uploaded_fileinfo(file_dic,upload_dir):
    for filename in os.listdir(upload_dir):
        abs_file = '%s/%s' % (upload_dir, filename)
        file_create_time = time.strftime("%Y-%m-%d %H:%M:%S",
                                         time.gmtime(os.path.getctime(abs_file)))
        file_dic['files'][filename] = {'size': os.path.getsize(abs_file) / 1000,
                                           'ctime': file_create_time}


@login_required
def homework_detail(request,customer_id,course_record_id):
    course_record_obj = models.CourseRecord.objects.get(id=course_record_id)
    upload_dir = "%s%s/%s/%s" % (settings.BASE_HOMEWORK_DIR,
                                 customer_id,
                                 course_record_obj.course.id,
                                 course_record_id)
    # print("upload dir",upload_dir)
    if not os.path.isdir(upload_dir):
        os.makedirs(upload_dir, exist_ok=True)

    response_dic = {'files':{}}

    if request.method == "POST":
        if request.FILES:

            print("files:",request.FILES)

            for k,file_obj in request.FILES.items():
                if len(os.listdir(upload_dir)) < 6:
                    with open('%s/%s' %(upload_dir,file_obj.name), 'wb+') as destination:
                        for chunk in file_obj.chunks():
                            destination.write(chunk)
                else:
                    response_dic['error'] = "can only upload no more than 5 files."
            # for filename in os.listdir(upload_dir):
            #     abs_file = '%s/%s' %(upload_dir,filename)
            #     file_create_time = time.strftime("%Y-%m-%d %H:%M:%S" ,
            #                                      time.gmtime(os.path.getctime(abs_file)))
            #     response_dic['files'][filename] = {'size':os.path.getsize(abs_file)/1000,
            #                                    'ctime':file_create_time}
            #
            get_uploaded_fileinfo(response_dic, upload_dir)

            return HttpResponse(json.dumps(response_dic))

    study_record_obj = models.StudyRecord.objects.filter(student_id=customer_id,course_record_id=course_record_id)

    #get uploaded file dic
    get_uploaded_fileinfo(response_dic, upload_dir)

    if study_record_obj:
        study_record_obj = study_record_obj[0]
    else: #此时这个学生还没有本节课的学习记录,但需要学习记录方能交作业,所以在这里创建一条学习纪录为他
        study_record_obj = models.StudyRecord(
            student_id = customer_id,
            course_record_id = course_record_id,
            record='checked',
            score = 0
        )
        study_record_obj.save()


    return render(request,'stu/homework_detail.html',
                  {'study_record_obj':study_record_obj,
                   'course_record_obj':course_record_obj,
                   'uploaded_files':response_dic})

@login_required
def delete_file(request,customer_id,course_record_id):
    response = {}
    if request.method == "POST":
        course_record_obj = models.CourseRecord.objects.get(id=course_record_id)
        upload_dir = "%s%s/%s/%s" % (settings.BASE_HOMEWORK_DIR,
                                     customer_id,
                                     course_record_obj.course.id,
                                     course_record_id)
        filename = request.POST.get('filename')
        file_abs = "%s/%s" %(upload_dir,filename.strip())
        if os.path.isfile(file_abs):
            os.remove(file_abs)
            response['msg'] = "file '%s' got deleted " % filename
        else:
            response["error"] = "file '%s' does not exist on server"% filename
    else:
        response['error'] = "only supoort POST method..."
    return HttpResponse(json.dumps(response))