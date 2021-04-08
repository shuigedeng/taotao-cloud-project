from scrapy.commands import ScrapyCommand
from scrapy.utils.project import get_project_settings


class Command(ScrapyCommand):

    requires_project = True

    def syntax(self):
        return '[options]'

    def short_desc(self):
        return 'Runs all of the spiders'

    def run(self, args, opts):
        # 找到所有的爬虫名称
        print(type(self.crawler_process))

        # 1. 执行CrawlerProcess构造方法
        # 2. CrawlerProcess对象(含有配置文件)的spiders
            # 2.1，为每个爬虫创建一个Crawler
            # 2.2，执行 d = Crawler.crawl(...)   # ************************ #
            #           d.addBoth(_done)
            # 2.3, CrawlerProcess对象._active = {d,}

        # 3. dd = defer.DeferredList(self._active)
        #    dd.addBoth(self._stop_reactor)  # self._stop_reactor ==> reactor.stop()

        #    reactor.run



        from scrapy.crawler import CrawlerProcess
        from scrapy.crawler import Crawler
        from scrapy.core.engine import ExecutionEngine
        # 获取当前所有爬虫的名称
        spider_list = self.crawler_process.spiders.list()
        # spider_list = ["chouti",'cnblogs']
        for name in spider_list:
            self.crawler_process.crawl(name, **opts.__dict__)
        self.crawler_process.start()