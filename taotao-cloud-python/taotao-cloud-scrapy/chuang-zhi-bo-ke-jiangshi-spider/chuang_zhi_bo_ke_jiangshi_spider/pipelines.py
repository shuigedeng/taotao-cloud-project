# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
import json


class ChuangZhiBoKeJiangshiSpiderPipeline(object):
    def __init__(self):
        self.filename = open("itcast.json", 'w')

    def process_item(self, item, spider):
        dict_text = json.dumps(dict(item), ensure_ascii=False)
        self.filename.write(dict_text)
        return item

    def close_spider(self, spider):
        self.filename.close()
