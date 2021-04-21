import Taro, {useDidShow, usePageScroll, usePullDownRefresh} from "@tarojs/taro";
import {Image, Text, View,} from "@tarojs/components";
import {
  banners,
  classify,
  homeCenter,
  homeCenterQuery,
  invitedUsers,
  items,
  projectItems,
  singleItem
} from "./service";
import {AtIcon} from "taro-ui";
// import {userInfo} from "../orderDetails/service";
import shoppingScan from '@/assets/img/shoppingScan.png'
import "./index.less";

import diamond from "@/assets/img/diamond.png";

import * as moment from "moment";
import React, {useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import CustomSwiper from "@/pages/home/components/CustomSwiper";
import ChooseStore from "@/pages/home/components/ChooseStore";

moment.locale("zh-cn");

export interface Banner {
  id: number;
  title: string;
  position: string;
  imageUrl: string;
}

interface Classify {
  id: number;
  title: string;
  imageUrl: string;
}

enum ItemType {
  ordinary,
  special
}

interface Item {
  code: number;
  name: string;
  imageUrl: string;
  content: string;
  originalPrice: number;
  commission: number;
  price: number;
  memberPrice: number;
  pointDiscountPrice: number;
  unit: string;
  stock: number;
  type: ItemType;
  kind: ItemType;
  status: string;
  followed: Boolean;
}

interface IState {
  tabbarFix: boolean;
  fixTop: number,
  authorization: boolean;
  topBannerList: {
    data: Banner[];
    loading: boolean;
  };
  homeCenterBannerList: {
    data: Banner[];
    loading: boolean;
  };
  bottomBannerList: {
    data: Banner[];
    loading: boolean;
  };
  classifyList: {
    data: Classify[];
    loading: boolean;
  };
  itemList: {
    data: Item[];
    loading: boolean;
  };
  projectItems1: {
    data: string;
  }
  projectItems2: {
    data: Item[];
  }
  projectItems3: {
    data: Item[];
  }
  userDetail: {
    data: any;
    loading: boolean;
  };
  myLatitude: number;
  myLongitude: number;
  storeQuery: {
    data: any;
    loading: boolean;
  };
  addressQuery: {
    data: any;
  };
  storeName: string;

  list?: any;
  //状态栏的高度，单位px
  statusBarHeight: number;
  toUpper: boolean;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    statusBarHeight: 0,
    fixTop: 0,
    tabbarFix: false,
    authorization: false,
    topBannerList: {
      data: null,
      loading: true,
    },
    homeCenterBannerList: {
      data: [],
      loading: true,
    },
    bottomBannerList: {
      data: [],
      loading: true,
    },
    classifyList: {
      data: [],
      loading: true,
    },
    userDetail: {
      data: [],
      loading: true,
    },
    storeName: "",
    storeQuery: {
      data: [],
      loading: true
    },
    addressQuery: {
      data: []
    },
    itemList: {
      data: [],
      loading: true
    },
    projectItems1: {
      data: ''
    },
    projectItems2: {
      data: []
    },
    projectItems3: {
      data: []
    },
    myLatitude: 1,
    myLongitude: 1,
    toUpper: false
  });

  // const cartItems = useSelector(state => state.cartItems);
  const cartItems = [];
  const dispatch = useDispatch();

  const getData = async () => {
    // 获取top Banner
    const top = await banners("top");
    setState(prevState => {
      return {...prevState, topBannerList: {data: top.data, loading: false}}
    })

    // 获取 home center Banner
    const center = await homeCenterQuery("center");
    setState(prevState => {
      return {...prevState, homeCenterBannerList: {data: center.data, loading: false}}
    })

    // 获取 bottom center Banner
    const bottom = await homeCenter("bottom");
    setState(prevState => {
      return {...prevState, bottomBannerList: {data: bottom.data, loading: false}}
    })

    // 商品分类
    const classResult = await classify();
    setState(prevState => {
      return {...prevState, classifyList: {data: classResult.data, loading: false}}
    })

    // 首页商品goods
    const goods = await items(18, 1);
    setState(prevState => {
      return {...prevState, itemList: {data: goods.data, loading: false}}
    })
  }

  useDidShow(() => {
    //会获取组件到最上面的距离(就是我菜单的class)
    if (state.fixTop === 0) {
      Taro.createSelectorQuery()
      .select('#topTabBar')
      .boundingClientRect(res => {
        console.log('componentDidMount res', res);
        if (res && res.top) {
          setState(prevState => {
            return {...prevState, fixTop: res.top}
          })
        }
      }).exec();
    }

    //店铺名称
    const nearbyStoreName = Taro.getStorageSync("nearbyStoreName");
    setState(prevState => {
      return {...prevState, storeName: nearbyStoreName}
    })
  })

  useEffect(() => {
    const initData = async () => {
      Taro.getSystemInfo({
        success(e) {
          console.log('设备状态栏高度e', e);
          console.log('设备状态栏高度this', this);
          setState(prevState => {
            return {...prevState, statusBarHeight: e.statusBarHeight}
          })
        }
      })
      getData()
    }
    initData()

  }, [])

  useEffect(() => {
    // 购物车右上角图标
    if (cartItems.length === 0) {
      Taro.removeTabBarBadge({
        index: 2
      })
    } else {
      let sum = 0;
      let i;
      for (i in cartItems) {
        if (cartItems[i].checked) {
          sum += parseInt(cartItems[i].number);
        }
      }
      Taro.setTabBarBadge({
        index: 2,
        text: "" + sum + "",
      })
    }

    // 状态栏高度大于20 定义barHeight
    let barHeight = 32;
    if (state.statusBarHeight > 20) {
      barHeight = barHeight + (state.statusBarHeight - 20)
    }

    console.log('barHeight', barHeight);
    console.log('状态栏高度', state.statusBarHeight);

    if (state.topBannerList.loading) {
      Taro.showLoading({
        title: "加载中"
      });
    } else {
      Taro.hideLoading();
    }

    if (state.toUpper) {
      Taro.showLoading({
        title: "加载中"
      });
    } else {
      Taro.hideLoading();
    }
  }, [state.toUpper, state.statusBarHeight, state.topBannerList])

  useEffect(() => {
    const initData = async () => {
      if (process.env.TARO_ENV === 'weapp') {
        Taro.showShareMenu({
          withShareTicket: true
        });
      }

      if (!Taro.getStorageSync("storeId")) {
        Taro.navigateTo({
          url: "../nearbystores/index"
        });
      }

      const projectItems1 = await projectItems(1, 1)
      const {imageUrl} = projectItems1.data.items.list[0];
      setState(prevState => {
        return {...prevState, projectItems1: imageUrl}
      })

      const projectItems2 = await projectItems(2, 2)
      const {list: projectItems2List} = projectItems2.data.items;
      setState(prevState => {
        return {...prevState, projectItems2: {data: projectItems2List}}
      })

      const projectItems3 = await projectItems(3, 2)
      const {list: projectItems3List} = projectItems3.data.items;
      setState(prevState => {
        return {...prevState, projectItems3: {data: projectItems3List}}
      })

      // const userDetail = await userInfo();
      // setState(prevState => {
      //   return {...prevState, userDetail: userDetail.data.userInfo}
      // })
    }
    initData()

  }, [])

  usePullDownRefresh(async () => {
    getData()

    setTimeout(() => {
      Taro.stopPullDownRefresh(); //停止下拉刷新
      setState(prevState => {
        return {...prevState, toUpper: false}
      })
    }, 1500);
  })

  usePageScroll(e => {
    // console.log('监听用户滑动onPageScrollonPageScroll', e);
    const scrollTop = e.scrollTop;
    const isSatisfy = scrollTop >= state.fixTop;
    if (state.tabbarFix === isSatisfy) {
      return false;
    }
    setState(prevState => {
      return {...prevState, tabbarFix: isSatisfy}
    })
  })

  const lookForward = async () => {
    const {result} = await Taro.scanCode({});
    console.log('扫码result', result);

    const obj = JSON.parse(result);
    if (obj.userId) {
      console.log('扫码邀请');
      const {data} = await invitedUsers(obj.userId);
      console.log('lookForward data', data);

      if (data) {
        Taro.showToast({
          title: "绑定成功",
          icon: "success"
        })
      }
    } else {
      console.log('扫码加购');
      const itemResult = await singleItem(result);
      console.log('扫码加入购物车itemResult', itemResult);
      const data: any = {};
      data.itemId = itemResult.data.item.code;
      data.name = itemResult.data.item.name;
      data.number = 1;
      data.price = itemResult.data.item.price;
      data.unit = itemResult.data.item.unit;
      data.imageUrl = itemResult.data.item.imageUrl;
      data.pointDiscountPrice = itemResult.data.item.pointDiscountPrice;
      data.originalPrice = itemResult.data.item.originalPrice;
      data.memberPrice = itemResult.data.item.memberPrice;

      await dispatch({
        type: 'common/addToCartByCode',
        payload: data
      });
    }
  }

  // 跳转取货码
  const itemDetails = () => {
    Taro.switchTab({
      url: "../certificates/index"
    });
  }

// 专题
  const handleProject = (id) => {
    Taro.navigateTo({
      url: `../project/index?id=${id}`
    });
  }

  const onOpenDoor = (code, name, number, price, unit, imageUrl, pointDiscountPrice, originalPrice, memberPrice) => {
    const data: any = {};
    data.itemId = code;
    data.name = name;
    data.number = number;
    data.price = price;
    data.unit = unit;
    data.imageUrl = imageUrl;
    data.pointDiscountPrice = pointDiscountPrice;
    data.originalPrice = originalPrice;
    data.memberPrice = memberPrice;
    console.log('data', data);

    dispatch({
      type: 'common/addToCartByCode',
      payload: data
    });
  }

// 跳转商品详情
  const handleDetails = (code) => {
    Taro.navigateTo({
      url: `../details/index?code=${code}`
    });
  }

//分类跳转
  const handleItemLists = (id, title) => {
    Taro.navigateTo({
      url: `../itemLists/index?id=${id}&title=${title}`
    });
  }


// 会员活动
  const TopUPGetMember = () => {
    Taro.checkSession({
      success() {
        // if (state.userDetail.role === 'member') {
        //   Taro.navigateTo({
        //     url: "../theMemberCenter/index"
        //   });
        // } else {
        //   Taro.navigateTo({
        //     url: '../../packageA/pages/topup/index'
        //   });
        // }
      },
      fail() {
        Taro.showToast({
          title: "请跳转我的界面进行登录",
          icon: "none"
        });
      }
    })
  }

// 手势触摸开始
// onTouchStart(e){
//   console.log('onTouchStart e',e);
// }

  const {
    classifyList,
    itemList,
    storeName,
    projectItems1,
    projectItems2,
    projectItems3,
    tabbarFix,
    statusBarHeight,
  } = state;
// const upper = 10

  return (
    <View
      className="index"
      // onTouchStart={onTouchStart}
    >
      {/* 轮播图 */}
      <CustomSwiper statusBarHeight={state.statusBarHeight}
                    bannerDataList={state.topBannerList.data}/>


      <ChooseStore tabbarFix={state.tabbarFix}
                   statusBarHeight={state.statusBarHeight}
                   storeName={state.storeName}
                   lookForward={lookForward}/>

      {/* 分类 */}
      <View className="Grid">
        {classifyList && classifyList.data.map(gir => (
          gir &&
          <View
            className="gird"
            onClick={handleItemLists.bind(this, gir.id, gir.title)}
            key={gir.id}
          >
            <Image src={gir.imageUrl} className="gird_img"/>
            <Text className="grid_title">{gir.title}</Text>
          </View>
        ))}
      </View>

      {/* 横线 */}
      <View className="boldLine"/>

      {/* 充值 */}
      <View
        className='bg_f5'
        onClick={TopUPGetMember}
      >
        <View className='TopUP'>
          <View style="flex-direction:row;align-items:center;display:flex;">
            <Image src={diamond} className="diamondImg"/>
            <Text className='leftTopUp'>加入有地道会员·每月领99元专享券</Text>
          </View>
          <Text className='rightTopUp'>丨1折开卡</Text>
        </View>
      </View>


      {/* 横线 */}
      <View className="boldLine"/>

      {/* 特色专区 */}
      <View className="SpecialZone">
        <View
          className="WithinSpecialZone"
          onClick={handleProject.bind(this, 1)}
        >
          <View className="TodaySpecial">
            <View className="TodaySpecialCode">
              <Text className="TodaySpecialTitle">每周新品</Text>
              <Text className="TodaySpecialLittle">低至一元起</Text>
            </View>
            <Text className="TodaySpecialCon">精选低价 持续热销</Text>
          </View>
          <View className="specialLeftBox">
            <Image src={projectItems1.data} className="leftSpecial"/>
          </View>
        </View>
        <View className="WithinSpecialZone2">
          <View
            className="specialLeftBox1"
            onClick={handleProject.bind(this, 2)}
          >
            <View className="TodaySpecial">
              <View className="TodaySpecialCode">
                <Text className="TodaySpecialTitle">品牌主打</Text>
                <Text className="TodaySpecialLittle SpecialCoupon">领券超优惠</Text>
              </View>
              <Text className="TodaySpecialBrand">品牌特色 味蕾释放</Text>
            </View>
            <View className="SpecialZoneImg">
              <Image src={projectItems2[0] && projectItems2[0].imageUrl} className="leftSpecial1"/>
              <Image src={projectItems2[1] && projectItems2[1].imageUrl} className="leftSpecial1"/>
            </View>
          </View>
          <View className='divider'/>
          <View
            className="specialLeftBox2"
            onClick={handleProject.bind(this, 3)}
          >
            <View className="TodaySpecial">
              <View className="TodaySpecialCode">
                <Text className="TodaySpecialTitle">能量必备</Text>
              </View>
              <Text className="TodaySpecialBrand">能量加油站 精神整天</Text>
            </View>
            <View className="SpecialZoneImg">
              <Image src={projectItems3[0] && projectItems3[0].imageUrl} className="leftSpecial1"/>
              <Image src={projectItems3[1] && projectItems3[1].imageUrl} className="leftSpecial1"/>
            </View>
          </View>
        </View>
      </View>

      {/* 横线 */}
      <View className="boldLine"/>

      {/* 为你优选 */}
      <View className="optimizationBox">
        <View className="optimizationLine">
          <View className="line"/>
          <Text className="optimizationText">为你推荐</Text>
          <View className="line"/>
        </View>
        <View className="items_box">
          {itemList && itemList.data.map(item => (
            item &&
            <View
              className="item_box"
              key={item.code}
            >
              <Image src={item.imageUrl} className="image"
                     onClick={handleDetails.bind(this, item.code)}/>
              <View className="item_bottom_box">
                <Text className="title"
                      onClick={handleDetails.bind(this, item.code)}>{item.name}</Text>
                <View className="item_right_box">
                  <View className="priceBox">
                    <Text className="price">
                      ￥{(item.price / 100).toFixed(2)}/{item.unit}
                    </Text>
                    <Text className="originalPrice">
                      ￥{(item.originalPrice / 100).toFixed(2)}
                    </Text>
                  </View>
                  <View
                    className="shoppingCart"
                    onClick={onOpenDoor.bind(this,
                      item.code,
                      item.name,
                      1,
                      item.price,
                      item.unit,
                      item.imageUrl,
                      item.pointDiscountPrice,
                      item.originalPrice,
                      item.memberPrice,
                    )}
                  >
                    <AtIcon value='shopping-cart' size='20' color='#fff'/>
                  </View>
                </View>
              </View>
            </View>
          ))}
        </View>
      </View>
      <View className='shoppingScan' onClick={lookForward}>
        <Image src={shoppingScan}/>
        <Text className='shoppingScanTxt1'>扫一扫</Text>
        <Text className='shoppingScanTxt2'>商品条形码</Text>
      </View>
    </View>
  );
}

export default Index;
