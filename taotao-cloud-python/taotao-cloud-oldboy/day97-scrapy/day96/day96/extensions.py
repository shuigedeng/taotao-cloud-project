from scrapy import signals
class MyExtend:

    def __init__(self,crawler):
        self.crawler = crawler
        # 钩子上挂障碍物
        # 在指定信号上注册操作
        crawler.signals.connect(self.start, signals.engine_started)
        crawler.signals.connect(self.close, signals.spider_closed)

    @classmethod
    def from_crawler(cls, crawler):
        return cls(crawler)

    def start(self):
        print('signals.engine_started.start')

    def close(self):
        print('signals.spider_closed.close')