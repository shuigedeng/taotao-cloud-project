from selenium import webdriver
import time

driver = webdriver.PhantomJS(service_args=['--ignore-ssl-errors=true', '--ssl-protocol=any'])         # 驱动的路径

driver.get('http://i.qq.com/')
driver.maximize_window()
driver.save_screenshot("one.png")
driver.switch_to_frame('login_frame')
driver.find_element_by_id('switcher_plogin').click()

driver.find_element_by_name('u').clear()
driver.find_element_by_name('u').send_keys('981376577')
driver.find_element_by_name('p').clear()
driver.find_element_by_name('p').send_keys('dengtao762762762')

driver.execute_script("document.getElementById('login_button').parentNode.hidefocus=false;")
driver.find_element_by_xpath('//*[@id="loginform"]/div[4]/a').click()
driver.find_element_by_id('login_button').click()
time.sleep(3)

driver.save_screenshot("two.png")
time.sleep(3)
print(driver.current_url)
driver.save_screenshot('three.png')


if driver.current_url != ('https://user.qzone.qq.com/' + '981376577'):
    print('trying get captcha...')

    driver.switch_to_frame(driver.find_element_by_tag_name("iframe"))
    captcha_src = driver.execute_script("return document.getElementsByTagName('img')['capImg'].src")

    from urllib import request
    request.urlretrieve(captcha_src, "captcha.jpg")
    print('captcha saved in ./captcha.jpg')

    import subprocess
    subprocess.call(["xdg-open", "captcha.jpg"])

    captcha = input("please input captcha: ")
    driver.find_element_by_id('capAns').clear()
    driver.find_element_by_id('capAns').send_keys(captcha)

    driver.find_element_by_id('submit').click()

    time.sleep(2)
    if driver.current_url != ('https://user.qzone.qq.com/' + '981376577'):
        print('incorrect verification code, please try again.')

    driver.get('https://user.qzone.qq.com/' + '981376577')
    cookies = driver.get_cookies()

# if len(cookies) < 10:
#     print('username or password was wrong, please try again.')
#     import sys
#     sys.exit(0)

print(driver.get_cookies())