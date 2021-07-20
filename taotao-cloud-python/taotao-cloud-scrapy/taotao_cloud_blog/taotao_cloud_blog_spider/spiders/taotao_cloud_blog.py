import scrapy


class TaoTaoCloudBlogSpider(scrapy.Spider):
    name = 'taotao_cloud_blog'
    allowed_domains = ['blog.taotaocloud.top']
    start_urls = ['https://blog.taotaocloud.top/']
    def parse(self, response):
        print("response:",response.body.decode(response.encoding))
