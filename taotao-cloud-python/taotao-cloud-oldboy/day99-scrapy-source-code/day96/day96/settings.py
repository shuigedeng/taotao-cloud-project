# -*- coding: utf-8 -*-

# Scrapy settings for day96 project
#
# For simplicity, this file contains only settings considered important or
# commonly used. You can find more settings consulting the documentation:
#
#     http://doc.scrapy.org/en/latest/topics/settings.html
#     http://scrapy.readthedocs.org/en/latest/topics/downloader-middleware.html
#     http://scrapy.readthedocs.org/en/latest/topics/spider-middleware.html

BOT_NAME = 'day96'

SPIDER_MODULES = ['day96.spiders']
NEWSPIDER_MODULE = 'day96.spiders'


# Crawl responsibly by identifying yourself (and your website) on the user-agent
#USER_AGENT = 'day96 (+http://www.yourdomain.com)'
#
USER_AGENT = 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36'

# Obey robots.txt rules
ROBOTSTXT_OBEY = False

# Configure maximum concurrent requests performed by Scrapy (default: 16)
# CONCURRENT_REQUESTS = 32

# Configure a delay for requests for the same website (default: 0)
# See http://scrapy.readthedocs.org/en/latest/topics/settings.html#download-delay
# See also autothrottle settings and docs
# DOWNLOAD_DELAY = 2
# The download delay setting will honor only one of:
# CONCURRENT_REQUESTS_PER_DOMAIN = 16
# CONCURRENT_REQUESTS_PER_IP = 16

# Disable cookies (enabled by default)
# COOKIES_ENABLED = True
# COOKIES_DEBUG = True

# Disable Telnet Console (enabled by default)
# TELNETCONSOLE_ENABLED = True

# Override the default request headers:
#DEFAULT_REQUEST_HEADERS = {
#   'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
#   'Accept-Language': 'en',
#}

# Enable or disable spider middlewares
# See http://scrapy.readthedocs.org/en/latest/topics/spider-middleware.html
SPIDER_MIDDLEWARES = {
   'day96.middlewares.SpiderMiddleware': 543,
}

# Enable or disable downloader middlewares
# See http://scrapy.readthedocs.org/en/latest/topics/downloader-middleware.html
DOWNLOADER_MIDDLEWARES = {
   'day96.middlewares.DownMiddleware1': 540,
   'day96.middlewares.DownMiddleware2': 543,
}

# Enable or disable extensions
# See http://scrapy.readthedocs.org/en/latest/topics/extensions.html
# EXTENSIONS = {
#    # 'scrapy.extensions.telnet.TelnetConsole': None,
#     'day96.extensions.MyExtend': 300,
# }
from scrapy.extensions.telnet import TelnetConsole

# Configure item pipelines
# See http://scrapy.readthedocs.org/en/latest/topics/item-pipeline.html
# ITEM_PIPELINES = {
#    'day96.pipelines.Day96Pipeline': 300,
#    'day96.pipelines.Day97Pipeline': 200,
# }

# Enable and configure the AutoThrottle extension (disabled by default)
# See http://doc.scrapy.org/en/latest/topics/autothrottle.html
#AUTOTHROTTLE_ENABLED = True
# The initial download delay
#AUTOTHROTTLE_START_DELAY = 5
# The maximum download delay to be set in case of high latencies
#AUTOTHROTTLE_MAX_DELAY = 60
# The average number of requests Scrapy should be sending in parallel to
# each remote server
#AUTOTHROTTLE_TARGET_CONCURRENCY = 1.0
# Enable showing throttling stats for every response received:
#AUTOTHROTTLE_DEBUG = False

# Enable and configure HTTP caching (disabled by default)
# See http://scrapy.readthedocs.org/en/latest/topics/downloader-middleware.html#httpcache-middleware-settings
#HTTPCACHE_ENABLED = True
#HTTPCACHE_EXPIRATION_SECS = 0
#HTTPCACHE_DIR = 'httpcache'
#HTTPCACHE_IGNORE_HTTP_CODES = []
#HTTPCACHE_STORAGE = 'scrapy.extensions.httpcache.FilesystemCacheStorage'

# DEPTH_LIMIT = 4
# DEPTH_PRIORITY = 0 # 1
#
# DUPEFILTER_CLASS = "day96.duplication.RepeatFilter"
# DUPEFILTER_CLASS = "scrapy.dupefilters.RFPDupeFilter"

# DB = "a.log"

# from scrapy.core.downloader.handlers.http import HttpDownloadHandler
# from scrapy.contrib.throttle import AutoThrottle

# 是否启用缓存策略
# HTTPCACHE_ENABLED = True

# 缓存策略：所有请求均缓存，下次在请求直接访问原来的缓存即可
# HTTPCACHE_POLICY = "scrapy.extensions.httpcache.DummyPolicy"

# 缓存保存路径
# HTTPCACHE_DIR = 'httpcache'

# HTTPCACHE_STORAGE = 'scrapy.extensions.httpcache.FilesystemCacheStorage'
# from scrapy.extensions.httpcache import FilesystemCacheStorage
# from scrapy.extensions.httpcache import DummyPolicy
# from scrapy.contrib.downloadermiddleware.httpproxy import HttpProxyMiddleware
# from scrapy.core.downloader.handlers.http import HttpDownloadHandler
# from scrapy.contrib.downloadermiddleware.robotstxt import RobotsTxtMiddleware
# from scrapy.core.downloader.webclient import ScrapyHTTPClientFactory


# DOWNLOADER_HTTPCLIENTFACTORY = "scrapy.core.downloader.webclient.ScrapyHTTPClientFactory"
# DOWNLOADER_CLIENTCONTEXTFACTORY = "scrapy.core.downloader.contextfactory.ScrapyClientContextFactory"
#
# from scrapy.core.downloader.webclient import ScrapyHTTPClientFactory
# from scrapy.core.downloader.contextfactory import ScrapyClientContextFactory

COMMANDS_MODULE= "day96.commands"


SCHEDULER = "xx.xx.xx"