#!/usr/bin/env python
# -*- coding:utf-8 -*-


def get_intersection(*args):
    '''
    获取所有set的并集
    :param args: set集合
    :return:并集列表
    '''
    base = args[0]
    result = base.intersection(*args)
    return list(result)


def get_exclude(total,part):
    result = []
    for item in total:
        if item in part:
            pass
        else:
            result.append(item)
    return result


