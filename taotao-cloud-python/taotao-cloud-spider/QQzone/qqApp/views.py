from django.shortcuts import render, HttpResponse
from selenium import webdriver
import time
COOKIES_LIST = {}
CURRENT_USERNAME = ''

def show_login(request):
    return render(request, 'login.html')

# Create your views here.
def login(request):

    username = request.POST.get('username')
    password = request.POST.get('password')

    global COOKIES_LIST
    global CURRENT_USERNAME
    return_message = {'status': 400, 'data': None}
    if username is None or password is None:
        return_message['data'] = '用户名和密码都不能为空'
    else:
        CURRENT_USERNAME = username

        import time

        driver = webdriver.PhantomJS(service_args=['--ignore-ssl-errors=true', '--ssl-protocol=any'])  # 驱动的路径

        driver.get('http://i.qq.com/')
        driver.maximize_window()
        driver.save_screenshot("one.png")
        driver.switch_to_frame('login_frame')
        driver.find_element_by_id('switcher_plogin').click()

        driver.find_element_by_name('u').clear()
        driver.find_element_by_name('u').send_keys(username)
        driver.find_element_by_name('p').clear()
        driver.find_element_by_name('p').send_keys(password)

        driver.execute_script("document.getElementById('login_button').parentNode.hidefocus=false;")
        driver.find_element_by_xpath('//*[@id="loginform"]/div[4]/a').click()
        driver.find_element_by_id('login_button').click()
        time.sleep(3)

        driver.save_screenshot("two.png")
        time.sleep(3)
        print(driver.current_url)
        driver.save_screenshot('three.png')
        print(driver.current_url)

        if driver.current_url != ('https://user.qzone.qq.com/' + username):
            print('trying get captcha...')

            driver.switch_to_frame(driver.find_element_by_tag_name("iframe"))
            captcha_src = driver.execute_script("return document.getElementsByTagName('img')['capImg'].src")

            from urllib import request
            request.urlretrieve(captcha_src, "captcha.jpg")

            import subprocess
            subprocess.call(["xdg-open", "captcha.jpg"])

            captcha = input("please input captcha: ")
            driver.find_element_by_id('capAns').clear()
            driver.find_element_by_id('capAns').send_keys(captcha)

            driver.find_element_by_id('submit').click()

            time.sleep(2)
            if driver.current_url != ('https://user.qzone.qq.com/' + username):
                print('incorrect verification code, please try again.')

            driver.get('https://user.qzone.qq.com/' + username)
            cookies = driver.get_cookies()
            COOKIES_LIST.update(cookies)
        else:
            return_message['status'] = 200
            return_message['data'] = '登录成功'
            COOKIES_LIST = driver.get_cookies()

    return render(request, 'index.html', {'data': return_message})

def get_all_friends(request):
    base_url ='https://user.qzone.qq.com/' + CURRENT_USERNAME
    print(base_url)
    driver = webdriver.PhantomJS(service_args=['--ignore-ssl-errors=true', '--ssl-protocol=any'])
    for i in COOKIES_LIST:
        driver.add_cookie(i)
    driver.get(base_url)
    driver.maximize_window()

    time.sleep(30)

    driver.find_element_by_id('aMyFriends').click()
    time.sleep(3)
    driver.save_screenshot('four.png')

    driver.switch_to.frame(driver.find_element_by_xpath("//iframe[@class='app_canvas_frame']"))

    next_page = 'page'
    page = 1
    try:
        while next_page:
            picturl = driver.find_elements_by_xpath("//a[@class='avatar-img q_namecard']/img[@src]")
            name = driver.find_elements_by_xpath("//a[@class='textoverflow ']")
            print(picturl)
            print(name)
            for con, sti in zip(picturl, name):
                data = {
                    'time': sti.text,
                    'shuos': con.text
                }
                print(data)
            next_page = driver.find_element_by_xpath("//div[@id='mecarewho_pager']/a[@class='qz-button btn-pager-next']")
            page = page + 1

            next_page.click()

            time.sleep(3)
            driver.implicitly_wait(3)

        driver.quit()
    except Exception as e:
        print(e)
        driver.quit()

    return HttpResponse('ok')






