#-*-coding:UTF-8-*-
"""
Author: Gray Snail
Date: 2020-06-30

最新行政区划获取
基于高德地图API获取数据
https://restapi.amap.com/v3/config/district?keywords=中国&subdistrict=3&key=5b138cc729f37d29702ff904ca9cedeb

"""
import json
import requests

def parse_district(districtObj : dict, idx=1, parent_id=0):
    res = []
    if 'name' in districtObj.keys():
        if districtObj['level'] == 'street':
            return res
        
        lng, lat = districtCenter(districtObj['center'])
        level = districtLevel(districtObj['level'])
        citycode = districtObj['citycode'] if isinstance(districtObj['citycode'], str) else ''
        
        # {"citycode":"0379","adcode":"410300","name":"洛阳市","center":"112.434468,34.663041","level":"city"}
        # idx, districtObj['adcode'], districtObj['name'], level, citycode, lng, lat, parent_id
        item = {
            'id'        : idx,
            'adcode'    : districtObj['adcode'],
            'name'      : districtObj['name'],
            'level'     : level,
            'citycode'  : citycode,
            'lng'       : lng,
            'lat'       : lat,
            'parent_id' : parent_id
        }
        res.append(item)
        parent_id = idx
        idx = idx + 1

    if isinstance(districtObj.get('districts'), list) and len(districtObj['districts']) > 0:
        for subitem in districtObj['districts']:
            subs = parse_district(subitem, idx, parent_id)
            res += subs
            idx += len(subs)
    return res

def districtLevel(levelStr):
    map_val = {
        'country': 0,
        'province': 1,
        'city': 2,
        'district': 3
    }
    return map_val[levelStr]

def districtCenter(center):
    items = center.split(',')
    return float(items[0]), float(items[1])

# 结果保存为json数组
def saveJson(data):
    with open('common_region.json', 'w', encoding='utf-8') as fp:
        json.dump(data, fp, ensure_ascii=False, indent=4)
    print('Save json file: common_region.json')

# 保存为SQL脚本
def saveSqlFile(data, includeCreate=True):
    # +--------------+-------------+------+-----+---------+----------------+
    # | Field        | Type        | Null | Key | Default | Extra          |
    # +--------------+-------------+------+-----+---------+----------------+
    # | region_id      | int(11)     | NO   | PRI | NULL    | auto_increment |
    # | region_code    | char(6)     | NO   | MUL | NULL    |                |
    # | region_name    | varchar(20) | NO   | MUL | NULL    |                |
    # | level        | tinyint(1)  | NO   | MUL | 0       |                |
    # | city_code    | char(4)     | YES  |     | NULL    |                |
    # | longitudinal | varchar(20)     | YES  |     | 0       |                |
    # | lateral      | varchar(20)     | YES  |     | 0       |                |
    # | parent_id    | int(11)     | NO   | MUL | -1      |                |
    # +--------------+-------------+------+-----+---------+----------------+
    createCode = """
CREATE TABLE `common_region` (
    `region_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '地区Id',
    `region_code` char(6) NOT NULL COMMENT '地区编码',
    `region_name` varchar(20) NOT NULL COMMENT '地区名',
    `level` tinyint(1) NOT NULL DEFAULT '0' COMMENT '地区级别（1:省份province,2:市city,3:区县district,4:街道street）',
    `city_code` char(4) DEFAULT NULL COMMENT '城市编码',
    `lng` varchar(20) DEFAULT '0' COMMENT '城市中心经度',
    `lat` varchar(20) DEFAULT '0' COMMENT '城市中心纬度',
    `parent_id` int(11) NOT NULL DEFAULT '-1' COMMENT '地区父节点',
    PRIMARY KEY (`region_id`),
    KEY `regionCode` (`region_code`),
    KEY `parentId` (`parent_id`),
    KEY `level` (`level`),
    KEY `regionName` (`region_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3261 DEFAULT CHARSET=utf8 COMMENT='省市区地区码表';
"""
    with open('common_region.sql', 'w', encoding='utf-8') as fp:
        if includeCreate:
            fp.write(createCode)
        for item in data:
            sql = "INSERT INTO common_region(`region_id`,`region_code`,`region_name`,`level`,`city_code`,`lng`,`lat`,`parent_id`) " + \
                "VALUES({id},'{adcode}','{name}',{level},'{citycode}',{lng},{lat},{parent_id});\n".format(**item)

            fp.write(sql)
            
    print('Save sql file: common_region.sql')

if __name__ == "__main__":
    url = 'https://restapi.amap.com/v3/config/district?keywords=中国&subdistrict=3&key=5b138cc729f37d29702ff904ca9cedeb'

    response = requests.get(url)
    if response.ok and response.status_code == 200:
        data = response.json()
        data = parse_district(data)
        print('Download data successful, total:{0}!'.format(len(data)))
        saveJson(data)
        saveSqlFile(data)
    else:
        print('Request error!')