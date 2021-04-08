import time

import requests
from bs4 import BeautifulSoup

session = requests.Session()

i1 = session.get(
    url='https://www.zhihu.com/#signin',
    headers={
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36',
    }
)
print(i1.text)