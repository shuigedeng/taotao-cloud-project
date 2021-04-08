#!/usr/bin/env python
# -*- coding:utf-8 -*-
import json as default_json
from json.encoder import JSONEncoder
from .response import BaseResponse


class JsonEncoder(JSONEncoder):
    def default(self, o):
        if isinstance(o, BaseResponse):
            return o.__dict__
        return JSONEncoder.default(self, o)


class Json(object):

    @staticmethod
    def dumps(response, ensure_ascii=True):

        return default_json.dumps(response, ensure_ascii=ensure_ascii, cls=JsonEncoder)