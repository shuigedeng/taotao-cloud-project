# -*- coding: utf-8 -*-
import scrapy
from chuang_zhi_bo_ke_jiangshi_spider.items import ChuangZhiBoKeJiangshiSpiderItem


class ItcastSpider(scrapy.Spider):
    name = 'itcast'
    allowed_domains = ['itcast.cn']
    start_urls = ['http://www.itcast.cn/channel/teacher.shtml']

    def parse(self, response):
        response_list = response.xpath("//div[@class='li_txt']")

        for each in response_list:
            item = ChuangZhiBoKeJiangshiSpiderItem()

            name = each.xpath("./h3/text()").extract()
            level = each.xpath("./h4/text()").extract()
            info = each.xpath("./p/text()").extract()

            item['name'] = name[0]
            item['level'] = level[0]
            item['info'] = info[0]

            yield item