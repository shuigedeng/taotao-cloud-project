import requests
import hashlib
import time
current_time = time.time()
app_id = "8kasoimnasodn8687asdfkmasdf"
app_id_time = "%s|%s" %(app_id,current_time,)

m = hashlib.md5()
m.update(bytes(app_id_time,encoding='utf-8'))
authkey = m.hexdigest()

authkey_time = "%s|%s" %(authkey,current_time,)
print(authkey_time)



host_data = {
    'status': True,
    'data':{
        'hostname': 'c1.com',
        'disk': {'status':True,'data': 'xxx'},
        'mem': {'status':True,'data': 'xxx'},
        'nic': {'status':True,'data': 'xxx'},
    }
}

response = requests.post(
    url='http://127.0.0.1:8000/api/asset/',
    json=host_data,
    headers={'authkey': authkey_time}
)
print(response.text)


# requests.get(url='http://127.0.0.1:8000/api/asset/?k1=123')
# requests.get(url='http://127.0.0.1:8000/api/asset/',params={'k1':'v1','k2':'v2'})
# requests.post(
#     url='http://127.0.0.1:8000/api/asset/',
#     params={'k1':'v1','k2':'v2'}, # GET形式传值
#     data={'username':'1123','pwd': '666'}, # POST形式传值
#     headers={'a':'123'} # 请求头数据
# )

