# -*- coding: utf-8 -*-
import scrapy
from scrapy.linkextractors import LinkExtractor
from scrapy.spiders import CrawlSpider, Rule
from dong_guan_spider.items import DongGuanSpiderItem


class DongguanSpider(CrawlSpider):
    name = 'dongguan'
    allowed_domains = ['wz.sun0769.com']
    start_urls = ['http://wz.sun0769.com/index.php/question/questionType?type=4&page=0']

    rules = (
        Rule(LinkExtractor(allow=r'type=4&page=\d+'), follow=True),
        Rule(LinkExtractor(allow=r'/question/\d+/\d+.shtml'), callback='parse_dongguan', follow=True),
    )

    def showlinkd(self, links):
        for link in links:
            pass

    def parse_item(self, response):
        i = {}
        #i['domain_id'] = response.xpath('//input[@id="sid"]/@value').extract()
        #i['name'] = response.xpath('//div[@id="name"]').extract()
        #i['description'] = response.xpath('//div[@id="description"]').extract()
        return i
        url = response.url
        title = "//div[@class='pagecenter p3']//strong/text()"
        num = title.split(' ')[-1].split(":")[-1]
        content = "//div[@class='c1 text14_2']/text()"

    def parse_dongguan(self, response):
        item = DongGuanSpiderItem()

        item['url'] = response.url
        title = response.xpath("//div[contains(@class, 'pagecenter p3')]//strong/text()").extract()[0]
        item['title'] = title
        item['num'] = title.split(' ')[-1].split(":")[-1]
        item['content'] = response.xpath("//div[@class='c1 text14_2']/text()").extract()

        yield item