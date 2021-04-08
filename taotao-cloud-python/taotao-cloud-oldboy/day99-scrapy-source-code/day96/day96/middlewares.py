# -*- coding: utf-8 -*-

# Define here the models for your spider middleware
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/spider-middleware.html

from scrapy import signals
from scrapy.core.engine import ExecutionEngine

class DownMiddleware1(object):
    def process_request(self, request, spider):
        '''
        请求需要被下载时，经过所有下载器中间件的process_request调用
        :param request:
        :param spider:
        :return:
            None,继续后续中间件去下载；
            Response对象，停止process_request的执行，开始执行process_response
            Request对象，停止中间件的执行，将Request重新调度器
            raise IgnoreRequest异常，停止process_request的执行，开始执行process_exception
        '''
        print('DownMiddleware1.process_request',request.url)

    def process_response(self, request, response, spider):
        '''
        spider处理完成，返回时调用
        :param response:
        :param result:
        :param spider:
        :return:
            Response 对象：转交给其他中间件process_response
            Request 对象：停止中间件，request会被重新调度下载
            raise IgnoreRequest 异常：调用Request.errback
        '''
        print('DownMiddleware1.process_response')
        return response

    def process_exception(self, request, exception, spider):
        '''
        当下载处理器(download handler)或 process_request() (下载中间件)抛出异常
        :param response:
        :param exception:
        :param spider:
        :return:
            None：继续交给后续中间件处理异常；
            Response对象：停止后续process_exception方法
            Request对象：停止中间件，request将会被重新调用下载
        '''
        return None


class DownMiddleware2(object):
    def process_request(self, request, spider):
        '''
        请求需要被下载时，经过所有下载器中间件的process_request调用
        :param request:
        :param spider:
        :return:
            None,继续后续中间件去下载；
            Response对象，停止process_request的执行，开始执行process_response
            Request对象，停止中间件的执行，将Request重新调度器
            raise IgnoreRequest异常，停止process_request的执行，开始执行process_exception
        '''
        print('DownMiddleware2.process_request',request.url)

    def process_response(self, request, response, spider):
        '''
        spider处理完成，返回时调用
        :param response:
        :param result:
        :param spider:
        :return:
            Response 对象：转交给其他中间件process_response
            Request 对象：停止中间件，request会被重新调度下载
            raise IgnoreRequest 异常：调用Request.errback
        '''
        print('DownMiddleware2.process_response')
        return response

class SpiderMiddleware(object):
    def process_spider_input(self, response, spider):
        '''
        下载完成，执行，然后交给parse处理
        :param response:
        :param spider:
        :return:
        '''
        pass

    def process_spider_output(self, response, result, spider):
        '''
        spider处理完成，返回时调用
        :param response:
        :param result:
        :param spider:
        :return: 必须返回包含 Request 或 Item 对象的可迭代对象(iterable)
        '''
        return result

    def process_spider_exception(self, response, exception, spider):
        '''
        异常调用
        :param response:
        :param exception:
        :param spider:
        :return: None,继续交给后续中间件处理异常；含 Response 或 Item 的可迭代对象(iterable)，交给调度器或pipeline
        '''
        return None

    def process_start_requests(self, start_requests, spider):
        '''
        爬虫启动时调用
        :param start_requests:
        :param spider:
        :return: 包含 Request 对象的可迭代对象
        '''
        return start_requests