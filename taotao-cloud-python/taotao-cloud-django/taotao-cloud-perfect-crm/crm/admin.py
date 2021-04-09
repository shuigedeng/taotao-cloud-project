from django.contrib import admin
from django import forms
# Register your models here.

from crm import models

from django.contrib.auth.admin import UserAdmin
from django.contrib.auth.forms import ReadOnlyPasswordHashField

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
        return password2

    def save(self, commit=True):
        # Save the provided password in hashed format
        user = super(UserCreationForm, self).save(commit=False)
        user.set_password(self.cleaned_data["password1"])
        if commit:
            user.save()
        return user


class UserChangeForm(forms.ModelForm):
    """A form for updating users. Includes all the fields on
    the user, but replaces the password field with admin's
    password hash display field.
    """
    password = ReadOnlyPasswordHashField(label="Password",
        help_text=("Raw passwords are not stored, so there is no way to see "
                    "this user's password, but you can change the password "
                    "using <a href=\"password/\">this form</a>."))

    class Meta:
        model = models.UserProfile
        fields = ('email','password','is_active', 'is_admin')

    def clean_password(self):
        # Regardless of what the user provides, return the initial value.
        # This is done here, rather than on the field, because the
        # field does not have access to the initial value
        return self.initial["password"]


class UserProfileAdmin(UserAdmin):
    # The forms to add and change user instances
    form = UserChangeForm
    add_form = UserCreationForm

    # The fields to be used in displaying the User model.
    # These override the definitions on the base UserAdmin
    # that reference specific fields on auth.User.
    list_display = ('id','email','is_admin','is_active')
    list_filter = ('is_admin',)
    list_editable = ['is_admin']

    fieldsets = (
        (None, {'fields': ('email','name', 'password')}),
        ('Personal info', {'fields': ('memo',)}),

        ('用户权限', {'fields': ('is_active','is_staff','is_admin','roles','user_permissions','groups')}),

    )
    # add_fieldsets is not a standard ModelAdmin attribute. UserAdmin
    # overrides get_fieldsets to use this attribute when creating a user.
    add_fieldsets = (
        (None, {
            'classes': ('wide',),
            'fields': ('email',  'password1', 'password2','is_active','is_admin')}
        ),
    )
    search_fields = ('email',)
    ordering = ('email',)
    filter_horizontal = ('user_permissions','groups')


class CustomerAdmin(admin.ModelAdmin):
    list_display = ('qq','name','source','phone','course','class_type','consultant','status','date')
    choice_fields = ('status','source','class_type')
    fk_fields = ('consultant','course')
    list_per_page = 10
    list_filter = ('name','source','course','status','date','class_type','consultant')
    list_editable = ['phone',]

class MenuAdmin(admin.ModelAdmin):
    list_display = ('name','url_type','url_name','order')
    filter_horizontal = ('sub_menus',)

class SubMenuAdmin(admin.ModelAdmin):
    list_display = ('name','url_name','order')

class RoleAdmin(admin.ModelAdmin):
    list_display = ('name',)
    filter_horizontal = ('menus',)

class EnrollmentAdmin(admin.ModelAdmin):
    list_display = ('customer','course_grade','school','enrolled_date','contract_agreed','contract_approved')


class PaymentRecordAdmin(admin.ModelAdmin):
    list_display = ('enrollment','pay_type','paid_fee','date','consultant')

class StudyRecordAdmin(admin.ModelAdmin):
    list_display = ('id','student','course_record','record','score','date','note')
    list_editable = ('student','score','record','note')


class CourseAdmin(admin.ModelAdmin):
    list_display = ('id','name','period')

admin.site.register(models.Customer,CustomerAdmin)
admin.site.register(models.CustomerFollowUp)
admin.site.register(models.Branch)
admin.site.register(models.ClassList)
admin.site.register(models.Course,CourseAdmin)
admin.site.register(models.Role,RoleAdmin)
admin.site.register(models.UserProfile,UserProfileAdmin)
admin.site.register(models.Enrollment,EnrollmentAdmin)
admin.site.register(models.StuAccount)
admin.site.register(models.CourseRecord)
admin.site.register(models.StudyRecord,StudyRecordAdmin)
admin.site.register(models.FirstLayerMenu,MenuAdmin)
admin.site.register(models.SubMenu,SubMenuAdmin)
admin.site.register(models.PaymentRecord,PaymentRecordAdmin)