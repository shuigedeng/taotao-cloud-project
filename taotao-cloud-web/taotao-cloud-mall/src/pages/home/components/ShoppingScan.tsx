import {Image, Text, View} from "@tarojs/components";
import React from "react";
import Taro from "@tarojs/taro";
import shoppingScan from "@/assets/img/shoppingScan.png";

interface IProps {
  lookForward: () => {}
}

const ShoppingScan: Taro.FC<IProps> = (props) => {
  return (
    <View className='shoppingScan' onClick={props.lookForward}>
      <Image src={shoppingScan}/>
      <Text className='shoppingScanTxt1'>扫一扫</Text>
      <Text className='shoppingScanTxt2'>商品条形码</Text>
    </View>
  )
}

export default ShoppingScan
