# -*-coding:utf-8-*-
import requests
import json
import time

def main():
    url = "http://192.168.31.24:5500/api/nlp/punish"
    start_time = time.time()
    data = {"content": '''
    
    决定书文号： 呼卫计传罚〔2016〕2号
    处罚名称：  内蒙古中安类风湿骨关节病专科医院有限责任公司
    法人代表人姓名：   — —
    处罚类别：  罚款
    处罚结果：  警告并处罚款叁仟元
    处罚事由：  医疗废物暂时贮存地点警示标识不清
    处罚依据：  《医疗废物管理条例》第四十六条第一项
    处罚机关：  呼和浩特市卫生和计划生育委员会
    处罚决定日期：    5897/11/18
    处罚期限：  — —
    数据更新时间 2016/12/05
    
    '''
            }
    header = {"Content-type": "application/json"}
    result = requests.post(url, json=data, headers=header)

    print(result.content)
    response_time = time.time() - start_time
    r = result.json()
    # print(response_time)
    # print(json.dumps(r, ensure_ascii=False, indent=2))

if __name__ == "__main__":
    main()