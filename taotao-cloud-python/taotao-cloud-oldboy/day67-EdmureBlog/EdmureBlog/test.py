import requests

v1 = requests.get('http://127.0.0.1:8000/register.html')
check_code = 'asdf'
requests.post(
    url='http://127.0.0.1:8000/register.html',
    data={'username':'root','password':'123','cpwd': '123','check_code':check_code}
)