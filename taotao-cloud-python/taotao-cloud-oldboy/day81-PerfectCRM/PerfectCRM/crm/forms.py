
from django.forms import ModelForm
from crm import models

class CustomerForm(ModelForm):
    class Meta:
        model = models.CustomerInfo
        #fields = ['name','consultant','status']
        fields = "__all__"






