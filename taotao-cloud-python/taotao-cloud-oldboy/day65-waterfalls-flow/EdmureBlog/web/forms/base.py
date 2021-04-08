#!/usr/bin/env python
# -*- coding:utf-8 -*-

class BaseForm(object):
    def __init__(self, request, *args, **kwargs):
        self.request = request
        super(BaseForm, self).__init__(*args, **kwargs)

