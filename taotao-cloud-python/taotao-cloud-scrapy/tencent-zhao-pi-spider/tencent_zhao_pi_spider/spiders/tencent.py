# -*- coding: utf-8 -*-
import scrapy
from tencent_zhao_pi_spider.items import TencentZhaoPiSpiderItem


class TencentSpider(scrapy.Spider):
    name = 'tencent'
    allowed_domains = ['tencent.com']

    url = "http://hr.tencent.com/position.php?&start="
    offset = 200

    start_urls = [url + str(offset)]

    def parse(self, response):
        for each in response.xpath("//tr[@class='even'] | //tr[@class='odd']"):
            item = TencentZhaoPiSpiderItem()

            positionName = each.xpath("./td[1]/a/text()").extract()[0]
            positionLink = each.xpath("./td[1]/a/@href").extract()[0]
            positionType = each.xpath("./td[2]/text()").extract()[0]
            peopleNum = each.xpath("./td[3]/text()").extract()[0]
            positionAddr = each.xpath("./td[4]/text()").extract()[0]
            pushTime = each.xpath("./td[5]/text()").extract()[0]

            item['positionName'] = positionName
            item['positionLink'] = positionLink
            item['positionType'] = positionType
            item['positionAddr'] = positionAddr
            item['peopleNum'] = peopleNum
            item['pushTime'] = pushTime

            yield item

        if self.offset < 2550:
            self.offset += 10

        yield scrapy.Request(self.url + str(self.offset), callback=self.parse)