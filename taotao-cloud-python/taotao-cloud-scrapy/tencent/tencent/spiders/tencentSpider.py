# -*- coding: utf-8 -*-
import scrapy
from scrapy.linkextractors import LinkExtractor
from scrapy.spiders import CrawlSpider, Rule


class TencentspiderSpider(CrawlSpider):
    name = 'tencentSpider'
    allowed_domains = ['hr.tencent.com']
    start_urls = ['http://hr.tencent.com/position.php?lid=&tid=&start=0']

    rules = (
        Rule(LinkExtractor(allow=r"position.php?lid=&tid=&start=\d+#a"), callback="next_parse", follow=True),
        Rule(LinkExtractor(allow=r"position_detail.php?id=\d+"), callback="in_parse", follow=True),
    )

    def next_parse(self, response):
        print("------------"+response.text)
        with open('next_parse', 'rb') as f:
            f.write(response.text)

    def in_parse(self, response):
        print("===========" + response.text)
        with open('in_parse', 'rb') as f:
            f.write(response.text)