import {Image, Swiper, SwiperItem, View} from "@tarojs/components";
import React, {useState} from "react";
import "./index.less";

import banner1 from "../../assets/images/temp/banner1.jpg"
import banner2 from "../../assets/images/temp/banner2.jpg"
import banner3 from "../../assets/images/temp/banner3.jpg"

const AppBanner: Taro.FC = () => {
  let [state, setState] = useState({
    current: 1,
    list: [
      {src: banner1, bgColor: "rgb(203, 87, 60)"},
      {src: banner2, bgColor: "rgb(205, 215, 218)"},
      {src: banner3, bgColor: "rgb(183, 73, 69)"}
    ]
  })

  const onChange = e => {
    setState(prevState => {
      return {...prevState, current: e.detail.current}
    });
  };

  return (
    <View
      className='app-banner'
      style={{backgroundColor: state.list[state.current].bgColor}}
    >
      <Swiper
        indicatorDots
        indicatorColor='#ccc'
        className='app-banner-swiper'
        autoplay
        circular
        onChange={onChange}
        current={state.current}
      >
        {state.list.map(e => (
          <SwiperItem>
            <View className='banner-item'>
              <Image
                className='banner-item-img'
                style='width: 100%;height: 100%;'
                mode='widthFix'
                src={e.src}
              />
            </View>
          </SwiperItem>
        ))}
      </Swiper>
    </View>
  )
}

export default AppBanner
