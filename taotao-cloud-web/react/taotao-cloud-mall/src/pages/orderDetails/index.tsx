import Taro from "@tarojs/taro";
import {Button, Image, Picker, ScrollView, Text, View} from "@tarojs/components";
import "./index.less";
import {AtIcon, AtTabs, AtTabsPane} from 'taro-ui'
import {
  addressToLngAndLat,
  couponsList,
  createBalanceOrder,
  createOrders,
  distanceCalculation,
  free,
  freightPrice,
  userInfo
} from "./service";
import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";

interface IState {
  current: number;
  number: number;
  modal: string;
  couponId: number;
  storeId: number;
  couponPrice: number;
  require: number;
  activiId: number;
  discount: number;
  myCheck: boolean;
  query: {
    data?: any;
    loading: boolean
  };
  StoreName: {
    data: any;
    loading: boolean
  };
  storeAddress: {
    data: any;
    loading: boolean
  };
  selector: string[]
  choose: string[]
  selectorChecked: string
  timeSeltimeSel: string
  couponsQuery: {
    data?: any;
  };
  lngAndLat: {
    data?: any;
  };
  calculation: {
    data?: any;
  };
  freightQuery: {
    data?: any;
  };
  freeQuery: {
    data?: any;
  };
  myShoppingCart?: any;
}


const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    couponPrice: 0,
    require: 0,
    activiId: 0,
    current: 0,
    discount: 0,
    number: 0,
    couponId: 0,
    storeId: 0,
    modal: "none",
    query: {
      loading: true,
      data: null
    },
    StoreName: {
      data: null,
      loading: true,
    },
    storeAddress: {
      data: null,
      loading: true,
    },
    couponsQuery: {
      data: {
        coupons: {
          list: [],
        }
      }
    },
    lngAndLat: {
      data: null
    },
    calculation: {
      data: null
    },
    selector: [
      '09:00-18:00',
    ],
    choose: [
      '09:00-18:00',
    ],
    selectorChecked: '',
    timeSeltimeSel: '',
    freightQuery: {
      data: {
        config: {
          value: 1,
        }
      }
    },
    freeQuery: {
      data: {
        config: {
          value: 1,
        }
      }
    },
    myShoppingCart: [],
    myCheck: true,
  })

  const dispatch = useDispatch();
  // @ts-ignore
  const cartItems = useSelector(({cart}) => cart.cartItems);
  const receiverInfo = {receiverAddress: ''}
  const distance = 50

  useEffect(() => {
    const init = async () => {
      const userResult = await userInfo();

      const couponsResult = await couponsList("useable", 1);
      const freightResult = await freightPrice();
      const freeResult = await free();

      const nearbyStoreName = Taro.getStorageSync("nearbyStoreName");
      const storeAddress = Taro.getStorageSync("storeAddress");
      const {myShoppingCart} = state;
      if (cartItems) {
        if (userResult.data.userInfo.role === 'member') {

          for (const iterator of cartItems) {
            if (iterator.checked) {
              if (iterator.memberPrice !== 0) {
                iterator.price = iterator.memberPrice;
                myShoppingCart.push(iterator);
              } else {
                myShoppingCart.push(iterator);
              }
            }
          }
        } else {
          for (const iterator of cartItems) {
            if (iterator.checked) {
              myShoppingCart.push(iterator);
            }
          }
        }
      }
      setState(prevState => {
        return {
          ...prevState,
          query: userResult,
          couponsQuery: couponsResult,
          freightQuery: freightResult,
          freeQuery: freeResult,
          StoreName: nearbyStoreName,
          storeAddress: storeAddress.slice(),
          myShoppingCart,
        }
      })
    }
    init()
  }, [])

  const onChange = e => {
    setState(prevState => {
      return {
        ...prevState,
        selectorChecked: state.selector[e.detail.value]
      }
    })
  }

  const onTimeChange = e => {
    setState(prevState => {
      return {
        ...prevState,
        timeSeltimeSel: state.choose[e.detail.value]
      }
    })
  }

  const couponsChoose = (id, require, amount) => {
    const {myShoppingCart} = state;
    const totalPrice = myShoppingCart.reduce((total, currentValue) => {
      if (currentValue.checked) {
        return total + (currentValue.price * currentValue.number);
      }
      return total;
    }, 0)
    if (totalPrice >= require) {
      setState(prevState => {
        return {
          ...prevState,
          activiId: id,
          couponPrice: amount,
          require
        }
      })
    } else {
      Taro.showToast({
        title: "商品总价不足",
        icon: "none"
      });
    }
  }

  const handleSpecifications = () => {
    setState(prevState => {
      return {
        ...prevState,
        modal: "block"
      }
    })
  }
  const handleClose = () => {
    setState(prevState => {
      return {
        ...prevState,
        modal: "none"
      }
    })
  }
  // 支付
  const buyNow = async (itemTotalPrice) => {
    console.log('立即支付itemTotalPrice', itemTotalPrice)
    const {myShoppingCart, activiId, myCheck, query} = state;
    const userBalance = query.data.userInfo.balance;
    console.log('activiId', activiId);
    const {timeSeltimeSel, selectorChecked, current} = state;

    const myDate = new Date();//获取系统当前时间
    myDate.setTime(myDate.getTime() + 24 * 60 * 60 * 1000);
    const pickupTime = myDate.getFullYear() + "年" + (myDate.getMonth() + 1) + "月" + myDate.getDate() + "日";

    const time = timeSeltimeSel || selectorChecked
    const storeId = Taro.getStorageSync("storeId");
    const itemIds: any = [];
    myShoppingCart.forEach(element => {
      itemIds.push({itemId: element.itemId, number: element.number});
    })

    const cancel = "requestPayment:fail cancel";
    if (state.current === 2 && !receiverInfo) {
      Taro.showToast({
        title: "请添加收货地址",
        icon: "none"
      });
    } else {
      if (current === 2 && distance > 500) {
        Taro.showToast({
          title: "暂只支持500M内的订单配送！",
          icon: "none"
        });
      } else {
        if (current === 2 && !time || current === 1 && !time) {
          Taro.showToast({
            title: "请添加时间",
            icon: "none"
          });
        } else {
          console.log('执行了调用接口吗');

          try {
            let result: any = {};
            let resultBalance: any = {};
            switch (current) {
              case 0:
                if (myCheck) {
                  result = await createOrders(
                    itemIds,
                    storeId,
                    activiId === 0 ? null : activiId,
                    null,
                    null,
                    'storeBuy',
                    'wechatPay',
                    false
                  );
                  console.log('case0 mycheck result', result);

                } else {
                  if (userBalance > itemTotalPrice) {
                    resultBalance = await createBalanceOrder(
                      itemIds,
                      storeId,
                      activiId === 0 ? null : activiId,
                      null,
                      null,
                      'storeBuy',
                      'balance',
                      false
                    );
                  } else {
                    Taro.showToast({
                      title: "余额不足，请充值！",
                      icon: "none",
                      duration: 1000
                    });
                  }

                }
                break;

              case 1:
                const tomorrow = pickupTime;
                const pickUpTheGoods = `${tomorrow}&${time}`;
                console.log('pickUpTheGoods', pickUpTheGoods);

                if (myCheck) {
                  result = await createOrders(
                    itemIds,
                    storeId,
                    activiId === 0 ? null : activiId,
                    pickUpTheGoods,
                    null,
                    'unmanned',
                    'wechatPay',
                    false
                  );
                } else {
                  if (userBalance > itemTotalPrice) {
                    resultBalance = await createBalanceOrder(
                      itemIds,
                      storeId,
                      activiId === 0 ? null : activiId,
                      pickUpTheGoods,
                      null,
                      'unmanned',
                      'balance',
                      false
                    );
                  } else {
                    Taro.showToast({
                      title: "余额不足，请充值！",
                      icon: "none",
                      duration: 1000
                    });
                  }
                }
                break;

              case 2:
                console.log('case 2 配送上门');

                if (myCheck) {
                  result = await createOrders(
                    itemIds,
                    storeId,
                    activiId === 0 ? null : activiId,
                    time,
                    receiverInfo,
                    'distribution',
                    'wechatPay',
                    false
                  );
                } else {
                  if (userBalance > itemTotalPrice) {
                    resultBalance = await createBalanceOrder(
                      itemIds,
                      storeId,
                      activiId === 0 ? null : activiId,
                      time,
                      receiverInfo,
                      'distribution',
                      'balance',
                      false
                    );
                  } else {
                    Taro.showToast({
                      title: "余额不足，请充值！",
                      icon: "none",
                      duration: 1000
                    });
                  }
                }

                break;
              default:
                break;
            }
            Taro.setTabBarBadge({
              index: 2,
              text: cartItems.length,
            })
            const response1 = result.data;
            const responseBalance1 = resultBalance.data;
            if (responseBalance1) {
              const responseBalance = responseBalance1.createBalanceOrder;
              if (responseBalance) {
                dispatch({
                  type: 'common/delete',
                  payload: myShoppingCart
                })
                Taro.navigateTo({
                  url: "../results/index"
                });
              }
            }
            if (response1) {
              const response = response1.createOrder;
              const {nonceStr, paySign, signType, timeStamp} = response;
              const packageer = response.package;
              console.log('response', response);
              await Taro.showModal({
                title: '是否授权订阅消息',
                success() {
                  Taro.requestSubscribeMessage({
                    tmplIds: ['8_r9fJI0Qwz326pRDZHgVPci3nn93sZBGCoaWNhaoLE'],
                    success(res) {
                      console.log('requestSubscribeMessage success', res);
                      Taro.requestPayment({
                        nonceStr: nonceStr,
                        package: packageer,
                        signType: signType,
                        paySign: paySign,
                        timeStamp,
                        success(res) {
                          dispatch({
                            type: 'common/delete',
                            payload: myShoppingCart
                          })
                          console.log('success res', res);
                          Taro.navigateTo({
                            url: "../results/index"
                          });
                        }
                      });
                    },
                    fail(error) {
                      console.log('requestSubscribeMessage error', error);

                    }
                  })
                },
                fail() {
                  Taro.requestPayment({
                    nonceStr: nonceStr,
                    package: packageer,
                    signType: signType,
                    paySign: paySign,
                    timeStamp,
                    success(res) {
                      dispatch({
                        type: 'common/delete',
                        payload: myShoppingCart
                      })
                      console.log('success res', res);
                      Taro.navigateTo({
                        url: "../results/index"
                      });
                    }
                  });
                }
              })
            }


          } catch (e) {
            if (e.errMsg === cancel) {
              Taro.showToast({
                title: "用户取消了支付",
                icon: "none",
                duration: 1000
              });
            } else {
              Taro.showToast({
                title: "支付失败",
                icon: "none",
                duration: 1000,
                mask: true
              });
            }
          }
        }
      }
    }
  }

  // 选择地址
  const handleAddAddress = async () => {
    Taro.chooseAddress({
      async success(e) {
        const receiverAddress = e.provinceName + e.cityName + e.countyName + e.detailInfo;
        const {data} = await addressToLngAndLat(receiverAddress);
        const {lng, lat} = data.addressToLngAndLat;
        const from: any = {};
        const to: any = {};
        const {longitude, latitude} = Taro.getStorageSync("storeLngAndLat");
        from.lng = longitude;
        from.lat = latitude;
        to.lng = lng;
        to.lat = lat;
        const {data: result} = await distanceCalculation(from, to);
        const {distance} = result.distanceCalculation;
        dispatch({
          type: 'common/preAddress',
          payload: {
            distance: distance,
            receiverInfo: {
              receiverName: e.userName,
              receiverPhone: e.telNumber,
              receiverAddress: receiverAddress,
            }
          }
        });
      }
    });
  }
  const handleClick = value => {
    setState(prevState => {
      return {
        ...prevState,
        current: value
      }
    })
  }
  const changeCheck = value => {
    setState(prevState => {
      return {
        ...prevState,
        myCheck: value
      }
    })
  }


  const totalPrice = cartItems.reduce((total, currentValue) => {
    if (currentValue.checked) {
      return total + (currentValue.price * currentValue.number);
    }
    return total;
  }, 0)
  const {
    activiId,
    StoreName,
    couponsQuery,
    couponPrice,
    storeAddress,
    freightQuery,
    freeQuery,
    current,
    require,
    myShoppingCart,
    myCheck,
  } = state;
  const couponsLists = couponsQuery.data.coupons.list;
  const total = (totalPrice ? totalPrice : 0) / 100 - (state.discount);
  // 定义运费
  let freight;
  console.log('freightQuery,freightQuery,freightQuery', freightQuery);

  // 若订单总金额大于应配送的金额
  if (total >= freeQuery.data.config.value / 100) {
    freight = 0;
  } else {
    freight = freightQuery.data.config.value / 100;
  }
  console.log('couponPrice / 100', typeof (couponPrice / 100));
  console.log('total', typeof (total));
  let itemTotalPrice;
  const thisTotalPrice = total - couponPrice / 100;
  if (current === 2) {
    itemTotalPrice = thisTotalPrice + freight;
  } else {
    itemTotalPrice = thisTotalPrice;
  }

  const myDate = new Date();//获取系统当前时间
  myDate.setTime(myDate.getTime() + 24 * 60 * 60 * 1000);
  const pickupTime = myDate.getFullYear() + "年" + (myDate.getMonth() + 1) + "月" + myDate.getDate() + "日";

  return (
    <ScrollView className="index">
      {/* 分割线 */}
      <View className="topLine"/>
      <AtTabs
        animated={false}
        current={state.current}
        tabList={[
          {title: '门店现购'},
          {title: '门店自提'},
          {title: '配送上门'},
        ]}
        onClick={handleClick.bind(this)}
        className='attab'
      >

        {/* 门店现购 */}
        <AtTabsPane current={state.current} index={0}>
          <View className="lainx">
            <Text className='textTitle'>门店信息</Text>
            <View className="dizhi">
              <View className="index1">
                <Text className="zit">{StoreName}</Text>
                <Text className="zit">{storeAddress}</Text>
              </View>
              <AtIcon value='check-circle' size='30' color='#006D75'/>
            </View>
          </View>
          {/* 分割线 */}
          <View className="coarseLine"/>
          {/* 配送 */}
          <View className="yuy">
            <View className="yhui" style='margin-left:5px'>
              <Text className="zit3">商品清单</Text>
            </View>
            {myShoppingCart.map(item => (
              <View key={item.id} className="goodBox">
                <Image src={item.imageUrl} className="img"/>
                <View>
                  <View>
                    <Text className='goodName'>{item.name}</Text>
                  </View>
                  <Text className='goodName1'>
                    ￥{(item.price * item.number / 100)}/元
                  </Text>
                </View>
                <Text className='shuliang'>数量：{item.number}</Text>
              </View>
            ))}
          </View>
          {/* 分割线 */}
          <View className="coarseLine"/>
          {/* 优惠劵 */}
          <View className="yuy" onClick={handleSpecifications}>
            <View className="yhui">
              <Text className="coupons_text">优惠券选择</Text>
              <View className="choose_coupons">
                {
                  require ?
                    (<Text className="coupons">满{require / 100}减{couponPrice / 100}</Text>)
                    :
                    (<AtIcon value='chevron-right'/>)
                }
              </View>
            </View>
          </View>
          {/* 分割线 */}
          <View className="coarseLine"/>
          {/* 商品金额 */}
          <View className="yuy">
            <View className="yhui">
              <Text className="coupons_text">商品总额:</Text>
              <View className="choose_coupons">
                <Text className='goodName1'>
                  {total}/元
                </Text>
              </View>
            </View>
            <View className="yhui">
              <Text className="coupons_text">优惠金额:</Text>
              <View className="choose_coupons">
                <Text className='goodName1'>
                  {couponPrice ? couponPrice / 100 : '0'}/元
                </Text>
              </View>
            </View>
            <View className="yhui">
              <Text className="coupons_text">支付金额:</Text>
              <View className="choose_coupons">
                <Text className='goodName1'>
                  {itemTotalPrice.toFixed(2)}/元
                </Text>
              </View>
            </View>
          </View>
          {/* 分割线 */}
          <View className="coarseLine"/>
          <View className="lainx">
            <Text className='textTitle'>支付方式</Text>
            <View className="myPay">
              <View className="Payment" onClick={changeCheck.bind(this, true)}>
                <Text className="zit">微信支付</Text>
                {
                  myCheck ?
                    (<AtIcon value='check-circle' className='checked'/>)
                    :
                    (<AtIcon value='check-circle' className='unChecked'/>)
                }
              </View>
              <View className="Payment" onClick={changeCheck.bind(this, false)}>
                <Text className="zit">余额支付</Text>
                {
                  myCheck ?
                    (<AtIcon value='check-circle' className='unChecked'/>)
                    :
                    (<AtIcon value='check-circle' className='checked'/>)
                }
              </View>
            </View>
          </View>
        </AtTabsPane>

        {/* 门店自提 */}
        <AtTabsPane current={state.current} index={1}>
          <View className="lainx">
            <Text className='textTitle'>门店信息</Text>
            <View className="dizhi">
              <View className="index1">
                <Text className="zit">{StoreName}</Text>
                <Text className="zit">{storeAddress}</Text>
              </View>
              <AtIcon value='check-circle' size='30' color='#006D75'/>
            </View>
          </View>
          {/* 分割线 */}
          <View className="coarseLine"/>
          {/* 自提 */}
          <View className="yuy">
            <View className="yhui">
              <Text className="zit3">商品清单</Text>
            </View>
            {myShoppingCart.map(item => (
              <View key={item.id} className="goodBox">
                <Image src={item.imageUrl} className="img"/>
                <View>
                  <View>
                    <Text className='goodName'>{item.name}</Text>
                  </View>
                  <Text className='goodName1'>
                    ￥{(item.price * item.number / 100)}/元
                  </Text>
                </View>
                <Text className='shuliang'>数量：{item.number}</Text>
              </View>
            ))}
          </View>
          {/* 分割线 */}
          <View className="coarseLine"/>
          {/* 优惠劵 */}
          <View className="yuy" onClick={handleSpecifications}>
            <View className="yhui">
              <Text className="coupons_text">优惠券选择</Text>
              <View className="choose_coupons">
                {
                  require ?
                    (<Text className="coupons">满{require / 100}减{couponPrice / 100}</Text>)
                    :
                    (<AtIcon value='chevron-right' className='rightIcon'/>)
                }
              </View>
            </View>
          </View>
          {/* 分割线 */}
          <View className="coarseLine"/>
          {/* 优惠劵 */}
          <View className="yuy">
            <View className="yhui">
              <Text className="coupons_text">取货日期:</Text>
              <View className="choose_coupons">
                <Text className='coupons_text1'>
                  {pickupTime}
                </Text>
              </View>
            </View>
            <View className="yhui">
              <Text className="coupons_text">取货时间:</Text>
              <View className="yhui1">
                <View className='page-section'>
                  <Picker mode='selector' range={state.choose} onChange={onTimeChange}>
                    <View className='picker'>
                      {state.timeSeltimeSel ? state.timeSeltimeSel : '请选择取货时间!'}
                    </View>
                  </Picker>
                </View>
              </View>
            </View>
          </View>

          {/* 分割线 */}
          <View className="coarseLine"/>
          {/* 商品金额 */}
          <View className="yuy">
            <View className="yhui">
              <Text className="coupons_text">商品总额:</Text>
              <View className="choose_coupons">
                <Text className='goodName1'>
                  {total}/元
                </Text>
              </View>
            </View>
            <View className="yhui">
              <Text className="coupons_text">优惠金额:</Text>
              <View className="choose_coupons">
                <Text className='goodName1'>
                  {couponPrice ? couponPrice / 100 : '0'}/元
                </Text>
              </View>
            </View>
            <View className="yhui">
              <Text className="coupons_text">支付金额:</Text>
              <View className="choose_coupons">
                <Text className='goodName1'>
                  {itemTotalPrice}/元
                </Text>
              </View>
            </View>
          </View>
          {/* 分割线 */}
          <View className="coarseLine"/>
          <View className="lainx">
            <Text className='textTitle'>支付方式</Text>
            <View className="myPay">
              <View className="Payment" onClick={changeCheck.bind(this, true)}>
                <Text className="zit">微信支付</Text>
                {
                  myCheck ?
                    (<AtIcon value='check-circle' className='checked'/>)
                    :
                    (<AtIcon value='check-circle' className='unChecked'/>)
                }
              </View>
              <View className="Payment" onClick={changeCheck.bind(this, false)}>
                <Text className="zit">余额支付</Text>
                {
                  myCheck ?
                    (<AtIcon value='check-circle' className='unChecked'/>)
                    :
                    (<AtIcon value='check-circle' className='checked'/>)
                }
              </View>
            </View>
          </View>
        </AtTabsPane>

        {/* 预约配送 */}
        <AtTabsPane current={state.current} index={2}>
          <View className="lainx">
            <Text className='textTitle'>门店信息</Text>
            <View className="dizhi">
              <View className="index1">
                <Text className="zit">{StoreName}</Text>
                <Text className="zit">{storeAddress}</Text>
              </View>
              <AtIcon value='check-circle' size='30' color='#006D75'></AtIcon>
            </View>
          </View>
          {/* 分割线 */}
          <View className="coarseLine"/>
          <View className="lainx">
            <Text className='textTitle'>配送地址</Text>
            <View className="dizhi" onClick={handleAddAddress}>
              <View className="index1">
                <Text className="zit">
                  {receiverInfo ? `${receiverInfo.receiverAddress}` : '添加收货地址!'}
                </Text>
              </View>
              <AtIcon value='chevron-right' className='rightIcon'/>
            </View>
          </View>
          {/* 分割线 */}
          <View className="coarseLine"/>
          {/* 配送 */}
          <View className="yuy">
            <View className="yhui">
              <Text className="zit3">商品清单</Text>
            </View>
            {myShoppingCart.map(item => (
              <View key={item.id} className="goodBox">
                <Image src={item.imageUrl} className="img"/>
                <View>
                  <View>
                    <Text className='goodName'>{item.name}</Text>
                  </View>
                  <Text className='goodName1'>
                    ￥{(item.price * item.number / 100)}/元
                  </Text>
                </View>
                <Text className='shuliang'>数量：{item.number}</Text>
              </View>
            ))}
          </View>
          {/* 分割线 */}
          <View className="coarseLine"/>
          {/* 优惠劵 */}
          <View className="yuy" onClick={handleSpecifications}>
            <View className="yhui">
              <Text className="coupons_text">优惠券选择</Text>
              <View className="choose_coupons">
                {
                  require ?
                    (<Text className="coupons">满{require / 100}减{couponPrice / 100}</Text>)
                    :
                    (<AtIcon value='chevron-right' className='rightIcon'/>)
                }
              </View>
            </View>
          </View>
          {/* 分割线 */}
          <View className="coarseLine"/>
          <View className="yuy">
            <View className="yhui">
              <Text className="zit3">配送时间:</Text>
              <View className="yhui1">
                <View className='page-section'>
                  <Picker mode='selector' range={state.selector} onChange={onChange}>
                    <View className='picker'>
                      {state.selectorChecked ? state.selectorChecked : '请选择配送时间!'}
                    </View>
                  </Picker>
                </View>
                <AtIcon value='chevron-right' className='rightIcon'/>
              </View>
            </View>
            {/* 分割线 */}
            <View className="coarseLine"/>
            {/* 商品金额 */}
            <View className="yuy">
              <View className="yhui">
                <Text className="coupons_text">商品总额:</Text>
                <View className="choose_coupons">
                  <Text className='goodName1'>
                    {total}/元
                  </Text>
                </View>
              </View>
              <View className="yhui">
                <Text className="coupons_text">优惠金额:</Text>
                <View className="choose_coupons">
                  <Text className='goodName1'>
                    {couponPrice ? couponPrice / 100 : '0'}/元
                  </Text>
                </View>
              </View>
              <View className="yhui">
                <Text className="coupons_text">配送费:</Text>
                <View className="choose_coupons">
                  <Text className='goodName1'>
                    {freight > 0 ? `￥${freight}` : "免配送费"}
                  </Text>
                </View>
              </View>
              <View className="yhui">
                <Text className="coupons_text">距离:</Text>
                <View className="choose_coupons">
                  <Text className='coupons_text'>
                    {distance / 1000} KM
                  </Text>
                </View>
              </View>
              <View className="yhui">
                <Text className="coupons_text">支付金额:</Text>
                <View className="choose_coupons">
                  <Text className='goodName1'>
                    {itemTotalPrice}/元
                  </Text>
                </View>
              </View>
            </View>
            {/* 分割线 */}
            <View className="coarseLine"/>
            <View className="lainx">
              <Text className='textTitle'>支付方式</Text>
              <View className="myPay">
                <View className="Payment" onClick={changeCheck.bind(this, true)}>
                  <Text className="zit">微信支付</Text>
                  {
                    myCheck ?
                      (<AtIcon value='check-circle' className='checked'></AtIcon>)
                      :
                      (<AtIcon value='check-circle' className='unChecked'></AtIcon>)
                  }
                </View>
                <View className="Payment" onClick={changeCheck.bind(this, false)}>
                  <Text className="zit">余额支付</Text>
                  {
                    myCheck ?
                      (<AtIcon value='check-circle' className='unChecked'/>)
                      :
                      (<AtIcon value='check-circle' className='checked'/>)
                  }
                </View>
              </View>
            </View>
          </View>
          {/* 分割线 */}
          <View className="coarseLine"/>
        </AtTabsPane>
      </AtTabs>

      <View className="settlem">
        合计总额:
        <View className="settlement1">
          ￥{itemTotalPrice.toFixed(2)}/元
        </View>
        <Button
          size='mini'
          className="settlement2"
          onClick={buyNow.bind(this, itemTotalPrice)}
        >
          立即支付
        </Button>
      </View>

      {/**-------优惠券选择*/}
      <View style={{display: state.modal}}>
        {/*-------底部透明背景------*/}
        <View className="transparent">
          {/*--------弹窗主体框-------*/}
          <View className="bodyBox">
            {/*----------第一行优惠券以及关闭按钮------*/}
            <View className="topFirstLine">
              <Text>优惠券</Text>
              <View onClick={handleClose}>
                <AtIcon value='close' size='20'></AtIcon>
              </View>
            </View>
            {/*--------第二行选择优惠券---------*/}
            <View className="secondLine">
              <Text className="secondText">请选择优惠券</Text>
            </View>
            <ScrollView className="grayBack" scrollY>
              {couponsLists.map(item => (
                <View className="box" key={item.id}>
                  <View
                    className="bottomBox"
                    onClick={couponsChoose.bind(
                      this,
                      item.id,
                      item.require,
                      item.amount,
                    )}
                  >
                    <View className="Left">
                      <View className="leftView">
                        <Text className="symbol">￥</Text>
                        <Text className="amount">{item.amount / 100}</Text>
                      </View>
                      <View className="centerView">
                        <View className="viewBox">
                          <Image
                            src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/background.png'
                            className="background"
                          />
                          <Text className="fullAmount">
                            满{item.require / 100}元可用
                          </Text>
                        </View>
                        <Text className="timeData">待使用</Text>
                      </View>
                    </View>
                    <View className="rightView">
                      {activiId === item.id ? (
                        <Image
                          src="https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/choose.png"
                          className="radio"/>
                      ) : null}
                    </View>
                  </View>
                </View>
              ))}
            </ScrollView>
            <View className="determineBox" onClick={handleClose}>
              <Text className="determine">确定</Text>
            </View>
          </View>
        </View>
      </View>
    </ScrollView>
  );
}


export default Index
