from django import forms as dforms
from django.forms import fields


class UserForm(dforms.Form):
    username = fields.CharField()
    email = fields.EmailField()