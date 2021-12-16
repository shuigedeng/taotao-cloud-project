import Taro, {usePullDownRefresh} from "@tarojs/taro";
import {Image, Text, View} from "@tarojs/components";
import "./index.less";
import {collectionItems} from "./service";
import React, {useEffect, useState} from "react";
import useReachBottom = Taro.useReachBottom;

interface IState {
  query: {
    data?: any;
    loading: boolean
  };
  list?: any;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    query: {
      data: null,
      loading: true
    },
    list: []
  })

  useEffect(() => {
    const init = async () => {
      const result = await collectionItems(1);
      const list = result.data.items.list;
      setState({
        query: result,
        list: list
      });
    }
    init()
  }, [])

  usePullDownRefresh(async () => {
    const result = await collectionItems(1);
    const list = result.data.items.list;
    setState({
      query: result,
      list: list
    });
    setTimeout(() => {
      Taro.stopPullDownRefresh(); //停止下拉刷新
    }, 1000);
  })

  useReachBottom(async () => {
    const {query, list} = state;
    const orderPagination = query.data.items.pagination;
    const total = orderPagination.total;
    const pageSize = orderPagination.pageSize;
    const loadCurrent = orderPagination.current + 1;
    const number = Math.ceil(total / pageSize);
    if (loadCurrent > number) {
      Taro.showToast({
        title: "加载完成！",
        icon: "none",
        duration: 500
      });
    } else {
      const loadResult = await collectionItems(loadCurrent);
      const loadList = list.concat(loadResult.data.items.list);
      setState({
        list: loadList,
        query: loadResult
      });
    }
  })

  const handlecLick = (code) => {
    Taro.navigateTo({
      url: `../details/index?code=${code}`
    });
  }

  const {list} = state;
  return (
    <View className="index">
      {list.map(item => (
        <View
          key={item.code}
          className="list-box"
          onClick={() => handlecLick(item.code)}
        >
          <View className="list-box-first">
            <Image src={item.imageUrl} className="listImg"/>
            <View className="list-box-first-bottom">
              <Text className="bottom-name">{item.name}</Text>
            </View>
          </View>
        </View>
      ))}
    </View>
  );
}


export default Index
