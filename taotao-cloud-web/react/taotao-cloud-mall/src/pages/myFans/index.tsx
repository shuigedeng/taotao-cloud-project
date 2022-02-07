import Taro from "@tarojs/taro";
import {AtTabs, AtTabsPane} from 'taro-ui'
import {Image, ScrollView, Text, View,} from "@tarojs/components";
import {userInfo} from "../orderDetails/service";
import "./index.less";
import React, {useEffect, useState} from "react";

interface IState {
  current: number;
  current1: number;
  query: any;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    current: 0,
    current1: 0,
    query: '',
  })

  useEffect(() => {
    const init = async () => {
      const {data} = await userInfo();
      setState(prevState => {
        return {...prevState, query: data.userInfo.fanList}
      })
    }
    init()
  }, [])
  const handleClick = (value) => {
    setState(prevState => {
      return {...prevState, current: value}
    })
  }
  const handle = (value1) => {
    setState(prevState => {
      return {...prevState, current1: value1}
    })
  }

  const {query} = state;
  return (
    <ScrollView className="index">
      <AtTabs
        animated={false}
        current={state.current}
        tabList={[
          {title: '直属会员'},
          {title: '间接会员'},
        ]}
        onClick={handleClick.bind(this)}>
        <AtTabsPane current={state.current} index={0}>
          <View className="sortingOptions">
            {/* 时间排序 */}
            <View className="price_stort">
              <Text className="sorted_text">加入时间</Text>
              <Image
                src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/top_sorting.png'
                className="arrow"
              />
            </View>
            {/* 占位排序（无用）  */}
            <View className="price_stort">
              <Text className="sorted_text">团队规模</Text>
              <Image
                src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/top_sorting.png'
                className="arrow"
              />
            </View>
            <View className="price_stort">
              <Text className="sorted_text">全部会员</Text>
            </View>
          </View>
          <View>
            <View className="price_stort1">
              <Text className="sorted_text">{query.id}</Text>
              <Text className="sorted_text">{query.imageUrl}</Text>
              <Text className="sorted_text">{query.nickname}</Text>
              <Text className="sorted_text">{query.point}</Text>
              <Text className="sorted_text">{query.role}</Text>
            </View>
          </View>
        </AtTabsPane>
        <AtTabsPane current={state.current} index={1}/>
      </AtTabs>
    </ScrollView>
  );
}


export default Index
