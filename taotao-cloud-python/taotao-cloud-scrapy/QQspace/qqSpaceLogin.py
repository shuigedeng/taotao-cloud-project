from selenium import webdriver
import time, re
from PIL import Image

def QR_login():

    def getGTK(cookie):
        """ 根据cookie得到GTK """
        hashes = 5381
        for letter in cookie['p_skey']:
            hashes += (hashes << 5) + ord(letter)

        return hashes & 0x7fffffff

    browser = webdriver.PhantomJS()
    url = "https://qzone.qq.com/"#QQ登录网址

    browser.get(url)
    browser.maximize_window()

    time.sleep(3)
    print("---------")
    browser.get_screenshot_as_file('QR.png')
    print("=============")
    img = Image.open('QR.png')
    img.show()

    time.sleep(30)
    print(browser.title)

    cookie = {}
    for elem in browser.get_cookies():
        cookie[elem['name']] = elem['value']

    print('Get the cookie of QQlogin successfully!(共%d个键值对)' % (len(cookie)))

    html = browser.page_source
    g_qzonetoken=re.search(r'window\.g_qzonetoken = \(function\(\)\{ try\{return (.*?);\} catch\(e\)',html)#从网页源码中提取g_qzonetoke

    gtk = getGTK(cookie)
    browser.quit()

    return cookie, gtk , g_qzonetoken.group(1)

if __name__ == '__main__':
    s1, s2,s3 = QR_login()
    print(s1)
    print(s2)
    print(s3)