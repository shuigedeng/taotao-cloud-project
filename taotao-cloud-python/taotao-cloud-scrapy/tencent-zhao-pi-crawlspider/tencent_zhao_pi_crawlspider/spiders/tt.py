# -*- coding: utf-8 -*-
import scrapy
from scrapy.linkextractors import LinkExtractor
from scrapy.spiders import CrawlSpider, Rule
from tencent_zhao_pi_crawlspider.items import TencentZhaoPiCrawlspiderItem


class TtSpider(CrawlSpider):
    name = 'tt'
    allowed_domains = ['hr.tencent.com']
    start_urls = ['http://hr.tencent.com/position.php?&start=0']

    rules = (
        Rule(LinkExtractor(allow=r'start=\d+'), callback='parse_item', follow=True),
    )

    def parse_item(self, response):
        for each in response.xpath("//tr[@class='even'] | //tr[@class='odd']"):
            item = TencentZhaoPiCrawlspiderItem()

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
