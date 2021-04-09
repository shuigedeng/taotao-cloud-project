
# -*- coding: utf-8 -*-
"""A script to process items from a redis queue."""

import redis
import pymongo
import json


def process_items():
    rediscli = redis.Redis(host='127.0.0.1', port=6379, db=0)

    mongocli = pymongo.MongoClient(host='114.55.253.31',port=27017)

    dbname = mongocli['dmoz']

    sheetname = dbname['dmoz_items']

    offset = 0
    while True:
        source, data = rediscli.blpop('dmoz:items')

        newData = json.loads(data)
        sheetname.insert(newData)
        offset += 1
        print(offset)


if __name__ == "__main__":
    process_items()