import React, { useEffect, useState } from "react";
import Taro, { FC, useDidShow } from "@tarojs/taro";
import { View } from "@tarojs/components";
//
// import AppHeader from "../../components/AppHeader";
// import AppBanner from "../../components/AppBanner";
// import AppNav from "../../components/AppNav";
// import ProductCard from "../../components/ProductCard";
// import Seckill from "../../components/HomeSeckill";
// import ListItem from "../../components/ListItem";
// import h1 from "../../assets/images/temp/h1.png"
import JsonData from "../../Json";
// import api from "../../api"
import "./index.less";

// const usePageScroll = Taro.usePageScroll;
const useReachBottom = Taro.useReachBottom;

const Index: FC = () => {
  let [state, setState] = useState({
    list1: JsonData.goodsList,
    isScrollTop: true,
    loading: false,
  });

  let [guideShowState, setGuideShowState] = useState({ guideShow: "" });

  useEffect(() => {
    // checkUpdate();

    try {
      let guideShow = Taro.getStorageSync("guideShow") || "first";
      Taro.setStorageSync("guideShow", guideShow);
      setGuideShowState({
        guideShow: guideShow,
      });
      console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>");
      console.log(guideShowState);
    } catch (e) {
      // Do something when catch error
    }

    // let {shareId} = getParams();

    // console.log(shareId)
    // Taro.login({
    //   success: (code) => {
    //     const appId = Taro.getAccountInfoSync().miniProgram.appId;
    //     console.log(appId)
    //     Request('/uc/wechat/customer/auth', {
    //       "userInfo": {},
    //       "code": code.code,
    //       "appId": appId,
    //       "systemInfo": Taro.getSystemInfoSync(),
    //       shareId
    //     }, "POST", (res) => {
    //       res = res.data;
    //       if (res.success) {
    //         console.log(res.data)
    //         // 保存用户信息
    //         // this.props.saveLoginInfo(res.data);
    //         // this.initPage();
    //       } else {
    //         Taro.showToast({
    //           title: res.msg,
    //           icon: 'none',
    //           duration: 1000
    //         });
    //       }
    //     })
    //   }
    // })

    if (process.env.TARO_ENV === "h5") {
      window.addEventListener("scroll", pageScrollFn);
    }

    // const getCaptcha = async () => {
    //   const result = await api.auth.getCaptcha({t: new Date().getTime()})
    //   if (result) {
    //     console.log(result.data)
    //   }
    // }
    //
    // getCaptcha()
  }, []);

  // const getParams = () => {
  //   //解析分享参数
  //   let route = useRouter(true);
  //   let shareId = route.params.shareId;
  //   let shopId = route.params.shopId;
  //   let scene = route.params.scene;
  //   if (scene) {
  //     let arr = scene.split("-");
  //     if (arr.length === 1) {
  //       shopId = scene;
  //     } else {
  //       shopId = arr[0];
  //       shareId = arr[1];
  //     }
  //   }
  //   return {shopId, shareId};
  // }

  const pageScrollFn = (scrollTop) => {
    setState((prevState) => {
      return { ...prevState, isScrollTop: scrollTop === 0 };
    });
  };

  useDidShow(() => {
    console.log("useDidShow");
  });

  // usePageScroll(e => {
  //   pageScrollFn(e.scrollTop);
  // })

  useReachBottom(() => {
    // @ts-ignore
    let list1 = [].concat(state.list1, JsonData.goodsList);
    setState((prevState) => {
      return { ...prevState, list1: list1 };
    });
  });

  return (
    // <View className='home'>
    //   <AppHeader className={state.isScrollTop ? "" : "scroll"}/>
    //   <AppBanner/>
    //   <AppNav/>
    //   <Seckill className={state.isScrollTop ? "" : "scroll"}/>
    //   {/* 精品团购 */}
    //   <View className=' bgfff pb30'>
    //     <ListItem className='mt20 pt20 pb20'>
    //       <Image
    //         style={{width: "30px", height: "30px"}}
    //         mode='aspectFit'
    //         src={h1}
    //       />
    //       <View className='font30 pl30 color999'>
    //         <View className='color333'>精品团购</View>
    //         <View className='font16'>Boutique group buying</View>
    //       </View>
    //     </ListItem>
    //     <ScrollView
    //       style={{
    //         height: "235px",
    //         width: "100%"
    //       }}
    //       className='scrollview'
    //       scrollX
    //       scrollWithAnimation
    //     >
    //       <View className='list-container flex'>
    //         {state.list1.map((e, index) => (
    //           <ProductCard
    //             className='ml30'
    //             width='200px'
    //             key={"key" + index}
    //             title={e.title}
    //             src={e.image2}
    //             price={e.price}
    //             // @ts-ignore
    //             originalPrice={e.originalPrice}
    //           />
    //         ))}
    //       </View>
    //     </ScrollView>
    //   </View>
    //   {/* 分类精选 */}
    //   <View className=' bgfff pb30'>
    //     <ListItem className='mt20 pt20 pb20'>
    //       <Image
    //         style={{width: "60rpx", height: "60rpx"}}
    //         mode='aspectFit'
    //         src={h1}
    //       />
    //       <View className='font30 pl30 color999'>
    //         <View className='color333'>分类精选</View>
    //         <View className='font16'>Classified selection</View>
    //       </View>
    //     </ListItem>
    //     <ScrollView
    //       style={{
    //         height: "470rpx",
    //         width: "100%"
    //       }}
    //       className='scrollview'
    //       scrollX
    //       scrollWithAnimation
    //     >
    //       <View className='list-container flex'>
    //         {state.list1.map((e, index) => (
    //           <ProductCard
    //             className='ml30'
    //             width='240rpx'
    //             key={"key" + index}
    //             title={e.title}
    //             src={e.image3}
    //             price={e.price}
    //             // @ts-ignore
    //             originalPrice={e.originalPrice}
    //           />
    //         ))}
    //       </View>
    //     </ScrollView>
    //   </View>
    //
    //   <View className=' bgfff pb30'>
    //     <ListItem className='mt20 pt20 pb20'>
    //       <Image
    //         style={{width: "60rpx", height: "60rpx"}}
    //         mode='aspectFit'
    //         src={h1}
    //       />
    //       <View className='font30 pl30 color999'>
    //         <View className='color333'>猜你喜欢</View>
    //         <View className='font16'>Classified selection</View>
    //       </View>
    //     </ListItem>
    //     <View className='product-list'>
    //       {state.list1.map((e, index) => (
    //         <ProductCard
    //           className='ml30 mb10'
    //           width='332rpx'
    //           key={"key" + index}
    //           title={e.title}
    //           src={e.image3}
    //           price={e.price}
    //           // @ts-ignore
    //           originalPrice={e.originalPrice}
    //         />
    //       ))}
    //     </View>
    //   </View>
    // </View>

    <View>hello taro</View>
  );
};

export default Index;
