
from django.forms import ModelForm
from django import forms
from crm import models




class EnrollmentForm(ModelForm):
    def __new__(cls, *args, **kwargs):
        print("__new__",cls,args,kwargs)
        for field_name in cls.base_fields:
            filed_obj = cls.base_fields[field_name]
            filed_obj.widget.attrs.update({'class':'form-control'})
            if field_name in cls.Meta.readonly_fields:
                filed_obj.widget.attrs.update({'disabled': 'true'})
        return  ModelForm.__new__(cls)

    class Meta:
        model = models.StudentEnrollment
        #fields = ['name','consultant','status']
        fields = "__all__"
        exclude = ['contract_approved_date']
        readonly_fields = ['contract_agreed',]

    def clean(self):
        '''form defautl clean method'''
        # print("\033[41;1mrun form defautl clean method...\033[0m",dir(self))
        # print(self.Meta.admin.readonly_fields)
        print("cleaned_dtat:",self.cleaned_data)

        if self.errors:
            raise forms.ValidationError(("Please fix errors before re-submit."))
        if self.instance.id is not None :#means this is a change form ,should check the readonly fields
            for field in self.Meta.readonly_fields:
                old_field_val = getattr(self.instance,field) #数据库里的数据
                form_val = self.cleaned_data.get(field)
                print("filed differ compare:",old_field_val,form_val)
                if old_field_val != form_val:
                    self.add_error(field,"Readonly Field: field should be '{value}' ,not '{new_value}' ".\
                                         format(**{'value':old_field_val,'new_value':form_val}))



class CustomerForm(ModelForm):
    def __new__(cls, *args, **kwargs):
        print("__new__",cls,args,kwargs)
        for field_name in cls.base_fields:
            filed_obj = cls.base_fields[field_name]
            filed_obj.widget.attrs.update({'class':'form-control'})

            if field_name in cls.Meta.readonly_fields:
                filed_obj.widget.attrs.update({'disabled': 'true'})
                #print("--new meta:",cls.Meta)

        #print(cls.Meta.exclude)
        return  ModelForm.__new__(cls)

    class Meta:
        model = models.CustomerInfo
        #fields = ['name','consultant','status']
        fields = "__all__"
        exclude = ['consult_content','status','consult_courses']
        readonly_fields = ['contact_type','contact','consultant','referral_from','source']


    def clean(self):
        '''form defautl clean method'''
        # print("\033[41;1mrun form defautl clean method...\033[0m",dir(self))
        # print(self.Meta.admin.readonly_fields)
        print("cleaned_dtat:",self.cleaned_data)

        if self.errors:
            raise forms.ValidationError(("Please fix errors before re-submit."))
        if self.instance.id is not None :#means this is a change form ,should check the readonly fields
            for field in self.Meta.readonly_fields:
                old_field_val = getattr(self.instance,field) #数据库里的数据
                form_val = self.cleaned_data.get(field)
                print("filed differ compare:",old_field_val,form_val)
                if old_field_val != form_val:
                    self.add_error(field,"Readonly Field: field should be '{value}' ,not '{new_value}' ".\
                                         format(**{'value':old_field_val,'new_value':form_val}))




