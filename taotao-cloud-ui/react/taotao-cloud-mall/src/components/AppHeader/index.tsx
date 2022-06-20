import React from "react";
import {View, Input} from "@tarojs/components";
import "./index.less";
import {FC} from "@tarojs/taro";

type IProps = {
  className: string
}

const AppHeader: FC<IProps> = ({className}) => {
  return (
    <View className={"app-header " + className}>
      <View className='icon iconfont icon-scan'/>
      <View className='input-container'>
        <View className='iconfont icon-search'/>
        <Input className='input' placeholder='请输入地址'/>
      </View>
      <View className='icon iconfont icon-message'/>
    </View>
  )
}

export default AppHeader
