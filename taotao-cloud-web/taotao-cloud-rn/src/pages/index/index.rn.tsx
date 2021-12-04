import React from "react";
import {Text, View} from "@tarojs/components";

const Index = () => {
  return (
    <View className="wrapper">

      <Text className="title">为Taro而设计的Hooks Library</Text>
      <Text className="desc">
        目前覆盖70%官方API. 抹平部分API在H5端短板. 提供近40+Hooks!
        并结合ahook适配Taro!
      </Text>
      <View className="list">
        <Text className="label">运行环境</Text>
      </View>
    </View>
  );
};

export default Index;
