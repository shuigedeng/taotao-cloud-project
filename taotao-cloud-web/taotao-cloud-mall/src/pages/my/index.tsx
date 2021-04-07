import {Component} from "@tarojs/taro";
import {Image, ScrollView, View} from "@tarojs/components";

import AppHeader from "../../components/AppHeader";
import AppBanner from "../../components/AppBanner";
import AppNav from "../../components/AppNav";
import ProductCard from "../../components/ProductCard";
import Seckill from "../../components/HomeSeckill";
import ListItem from "../../components/ListItem";
import JsonData from "../../Json";

import h1 from "../../assets/images/temp/h1.png"

import "./index.less";
import React from "react";

export default class Index extends Component {
  // config = {
  //   addGlobalClass: true,
  //   navigationBarTitleText: "首页"
  // };

  state = {
    list1: JsonData.goodsList,
    isScrollTop: true
  };

  pageScrollFn = scrollTop => {
    console.log("TCL: Index -> scrollTop", scrollTop);

    this.setState({isScrollTop: scrollTop === 0}, () => {
      console.log("TCL: Index -> isScrollTop", this.state.isScrollTop);
    });
    // do something
  };

  onPageScroll(e) {
    this.pageScrollFn(e.scrollTop);
  }

  componentWillMount() {
  }

  componentDidMount() {
    // 只有编译为h5时下面代码才会被编译
    if (process.env.TARO_ENV === "h5") {
      window.addEventListener("scroll", this.pageScrollFn);
    }
  }

  componentWillUnmount() {
  }

  componentDidShow() {
  }

  componentDidHide() {
  }

  render() {
    let {isScrollTop} = this.state;
    return (
      <View className='home'>
        // @ts-ignore
        <AppHeader className={isScrollTop ? "" : "scroll"}/>
        <AppBanner/>
        <AppNav/>
        <Seckill/>
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
          // @ts-ignore
          <ScrollView
            // @ts-ignore
            scrollStyle={{
              height: "235px",
              width: "100%"
            }}
            className='scrollview'
            scrollX
            scrollWithAnimation
          >
            <View className='list-container flex'>
              {this.state.list1.map((e, index) => (
                <ProductCard
                  className='ml30'
                  width='200px'
                  key={"key" + index}
                  // @ts-ignore
                  taroKey={index}
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
              style={{width: "30px", height: "30px"}}
              mode='aspectFit'
              src={h1}
            />
            <View className='font30 pl30 color999'>
              <View className='color333'>分类精选</View>
              <View className='font16'>Classified selection</View>
            </View>
          </ListItem>
          <ScrollView
            // @ts-ignore
            scrollStyle={{
              height: "235px",
              width: "100%"
            }}
            className='scrollview'
            scrollX
            scrollWithAnimation
          >
            <View className='list-container flex'>
              {this.state.list1.map((e, index) => (
                <ProductCard
                  className='ml30'
                  width='120px'
                  key={"key" + index}
                  // @ts-ignore
                  taroKey={index}
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
              style={{width: "30px", height: "30px"}}
              mode='aspectFit'
              src={h1}
            />
            <View className='font30 pl30 color999'>
              <View className='color333'>猜你喜欢</View>
              <View className='font16'>Classified selection</View>
            </View>
          </ListItem>
          <View className='product-list'>
            {this.state.list1.map((e, index) => (
              <ProductCard
                className='ml30 mb10'
                width='166px'
                key={"key" + index}
                // @ts-ignore
                taroKey={index}
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
    );
  }
}
