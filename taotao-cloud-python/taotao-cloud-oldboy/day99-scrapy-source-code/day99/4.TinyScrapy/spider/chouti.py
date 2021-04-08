from engine import Request
class ChoutiSpider(object):

    name = 'chouti'

    def start_requests(self):
        start_url = ['http://www.baidu.com','http://www.bing.com',]
        for url in start_url:
            yield Request(url,self.parse)

    def parse(self,response):
        print(response) #response是下载的页面
        yield Request('http://www.cnblogs.com',callback=self.parse)