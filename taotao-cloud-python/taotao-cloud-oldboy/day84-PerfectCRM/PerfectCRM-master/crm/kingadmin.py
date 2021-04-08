#_*_coding:utf-8_*_

from crm import models
from django import forms

from kingadmin.admin_base import BaseKingAdmin,site


class CustomerAdmin(BaseKingAdmin):
    model = models.Customer
    list_display = ['qq','qq_name','name','phone','source','consultant','status','date','enroll']
    list_editable = ['phone',"source","consultant",'status']
    fk_fields = ('consultant',)
    choice_fields = ('source','status')
    list_filter = ('source','consultant','status')
    readonly_fields = ('consultant','status')
    search_fields = ('qq','consultant__email')
    colored_fields = {
        'status':{'已报名':"rgba(145, 255, 0, 0.78)",
                  '未报名':"#ddd"},
    }

    def enroll(self):
        '''报名'''
        print("customize field enroll",self)
        link_name = "报名"
        if self.instance.status == "signed":
            link_name = "报名新课程"
        return '''<a class="btn-link" href="/crm/enrollment/%s/">%s</a> ''' % (self.instance.id,link_name)
    enroll.display_name = "报名链接"


class EnrollmentAdmin(BaseKingAdmin):
    model = models.Enrollment
    list_display = ['customer','school','course_grade','contract_agreed','contract_approved','enrolled_date']
    fk_fields = ('school','course_grade')


class ClasslistAdmin(BaseKingAdmin):
    list_display = ('branch','course','semester','start_date')
    fk_fields = ('branch','course')
    filter_horizontal = ('teachers',)
    default_actions = ['delete_selected','dd秀d']
    #readonly_table = True
    readonly_fields = ['price','semester']


class PaymentRecordAdmin(BaseKingAdmin):
    model = models.PaymentRecord
    list_filter = ('pay_type','date','consultant')
    list_display = ('id','enrollment','pay_type','paid_fee','date','consultant')
    fk_fields = ('enrollment','consultant')
    choice_fields = ('pay_type')

class CourseRecordAdmin(BaseKingAdmin):
    model = models.CourseRecord
    list_display = ('course','day_num','date','teacher','has_homework','homework_title','study_records')
    fk_fields = ('course','teacher')
    list_filter = ('course','teacher','has_homework','day_num')
    def study_records(self):
        ele = '''<a class="btn-link" href='/kingadmin/crm_studyrecord/?&course_record=%s' >学员成绩</a>''' \
              %(self.instance.id)

        return ele
    study_records.display_name = "学员学习记录"

class StudyRecordAdmin(BaseKingAdmin):
    list_display = ("id",'course_record',"id",'student','record','score','date','note')
    list_filter = ('student','course_record')
    choice_fields = ('record','score')
    fk_fields = ('student','course_record')
    list_editable = ('id','student','record','score','note')




class UserCreationForm(forms.ModelForm):
    """A form for creating new users. Includes all the required
    fields, plus a repeated password."""
    password1 = forms.CharField(label='Password', widget=forms.PasswordInput)
    password2 = forms.CharField(label='Password confirmation', widget=forms.PasswordInput)

    class Meta:
        model = models.UserProfile
        fields = ('email','name')

    def clean_password2(self):
        # Check that the two password entries match
        password1 = self.cleaned_data.get("password1")
        password2 = self.cleaned_data.get("password2")
        if password1 and password2 and password1 != password2:
            raise forms.ValidationError("Passwords don't match")
        if len(password1) < 6:
            raise forms.ValidationError("Passwords takes at least 6 letters")
        return password2

    def save(self, commit=True):
        # Save the provided password in hashed format
        user = super(UserCreationForm, self).save(commit=False)
        user.set_password(self.cleaned_data["password1"])
        if commit:
            user.save()
        return user


class UserProfileAdmin(BaseKingAdmin):
    add_form = UserCreationForm
    model =  models.UserProfile
    list_display = ('id','email','is_staff','is_admin')
    readonly_fields = ['password',]
    change_page_onclick_fields = {
        'password':['password','重置密码']
    }
    filter_horizontal = ('user_permissions','roles')
    list_editable = ['is_admin','is_superuser']

class FirstLayerMenuAdmin(BaseKingAdmin):
    model = models.FirstLayerMenu
    list_display = ('id','url_type','url_name','order')
    choice_fields = ['url_type']

class RoleAdmin(BaseKingAdmin):
    list_display = ('name',)
    filter_horizontal = ('menus',)


class CourseAdmin(BaseKingAdmin):
    list_display = ('id','name','period')


site.register(models.Customer,CustomerAdmin)
site.register(models.ClassList,ClasslistAdmin)
site.register(models.Enrollment,EnrollmentAdmin)
site.register(models.PaymentRecord,PaymentRecordAdmin)
site.register(models.CourseRecord,CourseRecordAdmin)
site.register(models.StudyRecord,StudyRecordAdmin)
site.register(models.UserProfile,UserProfileAdmin)
site.register(models.FirstLayerMenu,FirstLayerMenuAdmin)
site.register(models.Role,RoleAdmin)
site.register(models.Course,CourseAdmin)
site.register(models.Branch)

