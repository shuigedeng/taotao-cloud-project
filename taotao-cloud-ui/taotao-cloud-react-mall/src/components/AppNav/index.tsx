import React, {useState} from "react";
import {Image, View} from "@tarojs/components";
import {FC} from "@tarojs/taro";

import "./index.less";
import c1 from "../../assets/images/temp/c6.png"
import c2 from "../../assets/images/temp/c7.png"
import c3 from "../../assets/images/temp/c3.png"
import c4 from "../../assets/images/temp/c4.png"
import c5 from "../../assets/images/temp/c5.png"
import ad1 from "../../assets/images/temp/ad1.jpg"

const AppNav: FC = () => {
  let [state] = useState({
    list: [
      {icon: c1, text: "营养保健"},
      {icon: c2, text: "速食生鲜"},
      {icon: c3, text: "速食生鲜"},
      {icon: c4, text: "家居厨卫"},
      {icon: c5, text: "个护美妆"}
    ]
  })

  return (
    <View className='app-nav'>
      <View className='app-nav-list'>
        {state.list.map((e, index) => (
          <View key={index} className='app-nav-list-item'>
            <View className='img'>
              <Image
                src={e.icon}
                mode='aspectFill'
                style='width: 100%;height: 100%;'
              />
            </View>
            <View className='font20 pt10'>{e.text}</View>
          </View>
        ))}
      </View>
      <Image
        src={ad1}
        style={{height: "95px", width: "100%"}}
        className='mt20'
      />
    </View>
  );
}

export default AppNav
