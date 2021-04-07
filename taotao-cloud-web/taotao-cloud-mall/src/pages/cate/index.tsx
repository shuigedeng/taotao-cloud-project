import React, {useEffect, useState} from 'react'
import {Image, ScrollView, View} from '@tarojs/components'

import AppHeader from "../../components/AppHeader";
import AppBanner from "../../components/AppBanner";
import AppNav from "../../components/AppNav";
import ProductCard from "../../components/ProductCard";
import Seckill from "../../components/HomeSeckill";
import ListItem from "../../components/ListItem";
import JsonData from "../../Json";

import "./index.less";
import h1 from "../../assets/images/temp/h1.png"
import Taro, {FC} from "@tarojs/taro";

const usePageScroll = Taro.usePageScroll;
const useReachBottom = Taro.useReachBottom;

const Index: FC = () => {
  let [state, setState] = useState({
    list1: JsonData.goodsList,
    isScrollTop: true,
    loading: false
  })

  useEffect(() => {
    if (process.env.TARO_ENV === "h5") {
      window.addEventListener("scroll", pageScrollFn);
    }
  }, [])

  const pageScrollFn = scrollTop => {
    setState(prevState => {
      return {...prevState, isScrollTop: scrollTop === 0}
    });
  };

  usePageScroll(e => {
    pageScrollFn(e.scrollTop);
  })

  useReachBottom(() => {
    // @ts-ignore
    let list1 = [].concat(state.list1, JsonData.goodsList);
    setState(prevState => {
      return {...prevState, list1: list1}
    });
  })

  return (
    <View className='home'>
      <AppHeader className={state.isScrollTop ? "" : "scroll"}/>
      <AppBanner/>
      <AppNav/>
      <Seckill className={state.isScrollTop ? "" : "scroll"}/>
      {/* 精品团购 */}
      <View className=' bgfff pb30'>
        <ListItem className='mt20 pt20 pb20'>
          <Image
            style={{width: "30px", height: "30px"}}
            mode='aspectFit'
            src={h1}
          />
          <View className='font30 pl30 color999'>
            <View className='color333'>精品团购</View>
            <View className='font16'>Boutique group buying</View>
          </View>
        </ListItem>
        <ScrollView
          style={{
            height: "235px",
            width: "100%"
          }}
          className='scrollview'
          scrollX
          scrollWithAnimation
        >
          <View className='list-container flex'>
            {state.list1.map((e, index) => (
              <ProductCard
                className='ml30'
                width='200px'
                key={"key" + index}
                title={e.title}
                src={e.image2}
                price={e.price}
                // @ts-ignore
                originalPrice={e.originalPrice}
              />
            ))}
          </View>
        </ScrollView>
      </View>
      {/* 分类精选 */}
      <View className=' bgfff pb30'>
        <ListItem className='mt20 pt20 pb20'>
          <Image
            style={{width: "60rpx", height: "60rpx"}}
            mode='aspectFit'
            src={h1}
          />
          <View className='font30 pl30 color999'>
            <View className='color333'>分类精选</View>
            <View className='font16'>Classified selection</View>
          </View>
        </ListItem>
        <ScrollView
          style={{
            height: "470rpx",
            width: "100%"
          }}
          className='scrollview'
          scrollX
          scrollWithAnimation
        >
          <View className='list-container flex'>
            {state.list1.map((e, index) => (
              <ProductCard
                className='ml30'
                width='240rpx'
                key={"key" + index}
                title={e.title}
                src={e.image3}
                price={e.price}
                // @ts-ignore
                originalPrice={e.originalPrice}
              />
            ))}
          </View>
        </ScrollView>
      </View>

      <View className=' bgfff pb30'>
        <ListItem className='mt20 pt20 pb20'>
          <Image
            style={{width: "60rpx", height: "60rpx"}}
            mode='aspectFit'
            src={h1}
          />
          <View className='font30 pl30 color999'>
            <View className='color333'>猜你喜欢</View>
            <View className='font16'>Classified selection</View>
          </View>
        </ListItem>
        <View className='product-list'>
          {state.list1.map((e, index) => (
            <ProductCard
              className='ml30 mb10'
              width='332rpx'
              key={"key" + index}
              title={e.title}
              src={e.image3}
              price={e.price}
              // @ts-ignore
              originalPrice={e.originalPrice}
            />
          ))}
        </View>
      </View>
    </View>
  )
}

export default Index
