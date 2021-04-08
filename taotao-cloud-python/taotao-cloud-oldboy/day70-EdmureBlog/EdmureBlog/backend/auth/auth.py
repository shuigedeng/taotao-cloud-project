#!/usr/bin/env python
# -*- coding:utf-8 -*-
from django.shortcuts import redirect


def check_login(func):
    def inner(request, *args, **kwargs):
        if request.session.get('user_info'):
            return func(request, *args, **kwargs)
        else:
            return redirect('/login.html')
    return inner