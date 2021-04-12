import threading
from queue import Queue
from lxml import html as h
import requests
import time

'''多线程爬取页面'''

PARSE_EXIT = False
CRAWL_EXIT = False

class ThreadCrawl(threading.Thread):
    def __init__(self, thread_name, page_queue, data_queue):
        super(ThreadCrawl, self).__init__()
        self.thread_name = thread_name
        self.page_queue = page_queue
        self.data_queue = data_queue
        self.headers = {
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                          "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36",
        }

    def run(self):
        while not CRAWL_EXIT:
            try:
                page = self.page_queue.get(False)
                url = "http://www.qiushibaike.com/8hr/page/" + str(page) + "/"
                content = requests.get(url, headers=self.headers).text
                time.sleep(1)
                self.data_queue.put(content)
            except Exception as e:
                pass
        print("ok爬取完成")


class ThreadParse(threading.Thread):
    def __init__(self, threadName, dataQueue, filename, lock):
        super(ThreadParse, self).__init__()
        self.threadName = threadName
        self.dataQueue = dataQueue
        self.filename = filename
        self.lock = lock

    def run(self):
        while not PARSE_EXIT:
            try:
                html = self.dataQueue.get(False)
                self.parse(html)
            except:
                pass

    def parse(self, html):
        html = h.etree.HTML(html)

        node_list = html.xpath('//div[contains(@id, "qiushi_tag")]')

        for node in node_list:
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
                "username": username,
                "image": image,
                "content": content,
                "zan": zan,
                "comments": comments
            }

            # with 后面有两个必须执行的操作：__enter__ 和 _exit__
            # 不管里面的操作结果如何，都会执行打开、关闭
            # 打开锁、处理内容、释放锁
            with self.lock:
                # 写入存储的解析后的数据
                self.filename.write(str(items) + "\n")

def main():
    page_queue = Queue(20)
    for i in range(1, 21):
        page_queue.put(i)

    data_queue = Queue()

    filename = open('duanzhi.json', 'a')

    lock = threading.Lock()

    crawl_list = ['1好', '2好', '3好']
    thread_crawl_list = []

    for thread_name in crawl_list:
        thread = ThreadCrawl(thread_name, page_queue, data_queue)
        thread.start()
        thread_crawl_list.append(thread)

    parseList = ["解析线程1号", "解析线程2号", "解析线程3号"]
    threadparse = []
    for threadName in parseList:
        thread = ThreadParse(threadName, data_queue, filename, lock)
        thread.start()
        threadparse.append(thread)

    while not page_queue.empty():
        pass
    global CRAWL_EXIT
    CRAWL_EXIT = True

    for crawl in thread_crawl_list:
        crawl.join()

    while not data_queue.empty():
        pass
    global PARSE_EXIT
    PARSE_EXIT = True

    for parse in threadparse:
        parse.join()

    with lock:
        filename.flush()
        filename.close()

if __name__ == '__main__':
    main()
