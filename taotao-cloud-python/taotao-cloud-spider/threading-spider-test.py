from queue import Queue
from lxml import html as h
import threading
import time
import requests
import json

CRAWL_EXEIT = False
PARSE_EXEIT = False


class CrawlSpider(threading.Thread):
    def __init__(self, crawl_name, page_queue, crawl_data_queue):
        super(CrawlSpider, self).__init__()
        self.crawl_name = crawl_name
        self.page_queue = page_queue
        self.crawl_data_queue = crawl_data_queue
        self.headers = {
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                          "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36",
        }

    def run(self):
        while not CRAWL_EXEIT:
            try:
                page = self.page_queue.get(False)
                url = "http://www.qiushibaike.com/8hr/page/" + str(page) + "/"
                context = requests.get(url=url, headers=self.headers).text
                time.sleep(1)
                self.crawl_data_queue.put(context)
            except Exception as e:
                pass
        print('ok爬取完成')


class ParseSpider(threading.Thread):
    def __init__(self, parse_name, crawl_data_queue, filename, lock):
        super(ParseSpider, self).__init__()
        self.parse_name = parse_name
        self.crawl_data_queue = crawl_data_queue
        self.filename = filename
        self.lock = lock

    def run(self):
        while not PARSE_EXEIT:
            try:
                html = self.crawl_data_queue.get(False)
                self.parse(html)
            except:
                pass

    def parse(self, html):
        html = h.etree.HTML(html)
        node_list = html.xpath('//div[contains(@id, "qiushi_tag")]')
        for node in  node_list:
            # xpath返回的列表，这个列表就这一个参数，用索引方式取出来，用户名
            username = node.xpath('./div/a/@title')[0]
            # 图片连接
            image = node.xpath('.//div[@class="thumb"]//@src')  # [0]
            # 取出标签下的内容,段子内容
            content = node.xpath('.//div[@class="content"]/span')[0].text
            # 取出标签里包含的内容，点赞
            zan = node.xpath('.//i')[0].text
            # 评论
            comments = node.xpath('.//i')[1].text

            items = {
                'username': username,
                'image': image,
                'content': content,
                'zan': zan,
                'comments': comments
            }

            with self.lock:
                self.filename.write(str(json.dumps(items)) + '\n')
                # json.dump(items, self.filename)

def main():
    #采集的页数
    page_queue = Queue(20)
    for i in range(1, 21):
        page_queue.put(i)

    #创建采集线程
    crawl_name_list = ['crawl_1', 'crawl_2', 'crawl_3']
    #采集队列
    crawl_data_queue = Queue(20)
    crawl_list = []
    for crawl_name in crawl_name_list:
        crawl_thread = CrawlSpider(crawl_name=crawl_name, page_queue=page_queue, crawl_data_queue=crawl_data_queue)
        crawl_thread.start()
        crawl_list.append(crawl_thread)

    #解析列表
    parse_name_list = ['parse_1', 'parse_2', 'parse_3']
    parse_list = []
    filename = open('duanzi.json', 'a')
    lock = threading.Lock()
    for parse_name in parse_name_list:
        parse_thread = ParseSpider(parse_name, crawl_data_queue, filename, lock)
        parse_thread.start()
        parse_list.append(parse_thread)

    while not page_queue.empty():
        pass
    global CRAWL_EXEIT
    CRAWL_EXEIT = True
    for crawl in crawl_list:
        crawl.join()

    while not crawl_data_queue.empty():
        pass
    global PARSE_EXEIT
    PARSE_EXEIT = True
    for parse in parse_list:
        parse.join()

    with lock:
        filename.close()

if __name__ == '__main__':
    main()