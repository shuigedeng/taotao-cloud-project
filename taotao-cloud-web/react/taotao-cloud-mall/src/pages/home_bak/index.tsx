import React, {useEffect, useState} from 'react'
import {Image, ScrollView, View} from '@tarojs/components'

// import './index.less'
import {useDispatch} from "react-redux";
import AppHeader from "@/components/AppHeader";
import AppBanner from "@/components/AppBanner";
import AppNav from "@/components/AppNav";
import Seckill from "@/components/HomeSeckill";
import ListItem from "@/components/ListItem";
import JsonData from "../../Json";
import h1 from "../../assets/images/temp/h1.png"
import ProductCard from "@/components/ProductCard";
import {getCurrentInstance, usePullDownRefresh,} from "@tarojs/taro";

const Index: Taro.FC = (props) => {
  let [state, setState] = useState({
    list1: JsonData.goodsList,
    isScrollTop: true,
    loading: false,
  });

  // const goodsCount = useSelector(state => state.count.number);
  // const goods = useSelector(state => state.count.number);

  const dispatch = useDispatch();
  const getData = () => {
    dispatch({type: 'home/getIndex'})
    dispatch({type: 'goods/getGoodsCount'})
  }

  usePullDownRefresh(() => {
    Taro.showNavigationBarLoading() //在标题栏中显示加载
    // getData();
    Taro.hideNavigationBarLoading() //完成停止加载
    Taro.stopPullDownRefresh() //停止下拉刷新
  })

  useEffect(() => {
    let router = getCurrentInstance().router;
    // 页面初始化 options为页面跳转所带来的参数
    let {scene, grouponId, goodId, orderId} = router.params;
    if (scene) {
      //这个scene的值存在则证明首页的开启来源于朋友圈分享的图,同时可以通过获取到的goodId的值跳转导航到对应的详情页
      scene = decodeURIComponent(scene);
      console.log("scene:" + scene);

      let info_arr = [];
      info_arr = scene.split(',');
      let _type = info_arr[0];
      let id = info_arr[1];

      if (_type == 'goods') {
        Taro.navigateTo({
          url: '../goods/goods?id=' + id
        });
      } else if (_type == 'groupon') {
        Taro.navigateTo({
          url: '../goods/goods?grouponId=' + id
        });
      } else {
        Taro.navigateTo({
          url: '../index/index'
        });
      }
    }

    // 页面初始化 options为页面跳转所带来的参数
    if (grouponId) {
      //这个pageId的值存在则证明首页的开启来源于用户点击来首页,同时可以通过获取到的pageId的值跳转导航到对应的详情页
      Taro.navigateTo({
        url: '../goods/goods?grouponId=' + grouponId
      });
    }

    // 页面初始化 options为页面跳转所带来的参数
    if (goodId) {
      //这个goodId的值存在则证明首页的开启来源于分享,同时可以通过获取到的goodId的值跳转导航到对应的详情页
      Taro.navigateTo({
        url: '../goods/goods?id=' + goodId
      });
    }

    // 页面初始化 options为页面跳转所带来的参数
    if (orderId) {
      //这个orderId的值存在则证明首页的开启来源于订单模版通知,同时可以通过获取到的pageId的值跳转导航到对应的详情页
      Taro.navigateTo({
        url: '../ucenter/orderDetail/orderDetail?id=' + orderId
      });
    }

    getData();
  }, [getData])


  return (
    <View className='home'>
      {/*<AppHeader className={state.isScrollTop ? "" : "scroll"}/>*/}
      {/*<AppBanner/>*/}
      {/*<AppNav/>*/}
      {/*<Seckill className={state.isScrollTop ? "" : "scroll"}/>*/}

      {/*/!* 精品团购 *!/*/}
      {/*<View className=' bgfff pb30'>*/}
      {/*  <ListItem className='mt20 pt20 pb20'>*/}
      {/*    <Image*/}
      {/*      style={{width: "30px", height: "30px"}}*/}
      {/*      mode='aspectFit'*/}
      {/*      src={h1}*/}
      {/*    />*/}
      {/*    <View className='font30 pl30 color999'>*/}
      {/*      <View className='color333'>精品团购</View>*/}
      {/*      <View className='font16'>Boutique group buying</View>*/}
      {/*    </View>*/}
      {/*  </ListItem>*/}
      {/*  <ScrollView*/}
      {/*    style={{*/}
      {/*      height: "235px",*/}
      {/*      width: "100%"*/}
      {/*    }}*/}
      {/*    className='scrollview'*/}
      {/*    scrollX*/}
      {/*    scrollWithAnimation*/}
      {/*  >*/}
      {/*    <View className='list-container flex'>*/}
      {/*      {state.list1.map((e, index) => (*/}
      {/*        <ProductCard*/}
      {/*          className='ml30'*/}
      {/*          width='200px'*/}
      {/*          key={"key" + index}*/}
      {/*          title={e.title}*/}
      {/*          src={e.image2}*/}
      {/*          price={e.price}*/}
      {/*          // @ts-ignore*/}
      {/*          originalPrice={e.originalPrice}*/}
      {/*        />*/}
      {/*      ))}*/}
      {/*    </View>*/}
      {/*  </ScrollView>*/}
      {/*</View>*/}
      {/*/!* 分类精选 *!/*/}
      {/*<View className=' bgfff pb30'>*/}
      {/*  <ListItem className='mt20 pt20 pb20'>*/}
      {/*    <Image*/}
      {/*      style={{width: "60rpx", height: "60rpx"}}*/}
      {/*      mode='aspectFit'*/}
      {/*      src={h1}*/}
      {/*    />*/}
      {/*    <View className='font30 pl30 color999'>*/}
      {/*      <View className='color333'>分类精选</View>*/}
      {/*      <View className='font16'>Classified selection</View>*/}
      {/*    </View>*/}
      {/*  </ListItem>*/}
      {/*  <ScrollView*/}
      {/*    style={{*/}
      {/*      height: "470rpx",*/}
      {/*      width: "100%"*/}
      {/*    }}*/}
      {/*    className='scrollview'*/}
      {/*    scrollX*/}
      {/*    scrollWithAnimation*/}
      {/*  >*/}
      {/*    <View className='list-container flex'>*/}
      {/*      {state.list1.map((e, index) => (*/}
      {/*        <ProductCard*/}
      {/*          className='ml30'*/}
      {/*          width='240rpx'*/}
      {/*          key={"key" + index}*/}
      {/*          title={e.title}*/}
      {/*          src={e.image3}*/}
      {/*          price={e.price}*/}
      {/*          // @ts-ignore*/}
      {/*          originalPrice={e.originalPrice}*/}
      {/*        />*/}
      {/*      ))}*/}
      {/*    </View>*/}
      {/*  </ScrollView>*/}
      {/*</View>*/}

      {/*<View className=' bgfff pb30'>*/}
      {/*  <ListItem className='mt20 pt20 pb20'>*/}
      {/*    <Image*/}
      {/*      style={{width: "60rpx", height: "60rpx"}}*/}
      {/*      mode='aspectFit'*/}
      {/*      src={h1}/>*/}
      {/*    <View className='font30 pl30 color999'>*/}
      {/*      <View className='color333'>猜你喜欢</View>*/}
      {/*      <View className='font16'>Classified selection</View>*/}
      {/*    </View>*/}
      {/*  </ListItem>*/}
      {/*  <View className='product-list'>*/}
      {/*    {state.list1.map((e, index) => (*/}
      {/*      <ProductCard*/}
      {/*        className='ml30 mb10'*/}
      {/*        width='332rpx'*/}
      {/*        key={"key" + index}*/}
      {/*        title={e.title}*/}
      {/*        src={e.image3}*/}
      {/*        price={e.price}*/}
      {/*        // @ts-ignore*/}
      {/*        originalPrice={e.originalPrice}*/}
      {/*      />*/}
      {/*    ))}*/}
      {/*  </View>*/}
      {/*</View>*/}
    </View>
  )
};

export default Index

