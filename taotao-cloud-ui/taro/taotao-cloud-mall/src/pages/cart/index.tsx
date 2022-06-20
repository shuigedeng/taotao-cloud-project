import Taro, {usePullDownRefresh} from '@tarojs/taro'
import {Checkbox, CheckboxGroup, Image, ScrollView, Text, View} from '@tarojs/components'
import {AtInputNumber} from 'taro-ui'
import shoppingScan from '@/assets/img/shoppingScan.png'
import {invitedUsers, singleItem} from '../home/service'
import './index.less'
import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {cartTabBarBadge} from "@/utils/cart";

const Index: Taro.FC = () => {
  let [state, setState] = useState({
    editable: false,
    modal: 'none',
    discount: 0,
    couponId: '',
    cartChecked: true,
    deleteItem: [],
    userMsg: {role: ''},
    totalPrice: {total: 0}
  })

  // @ts-ignore
  const cartItems = useSelector(({cart}) => cart.cartItems);
  const dispatch = useDispatch();

  useEffect(() => {
    const initData = async () => {
      console.log('shoppingCart cartItems', cartItems);
      // const userResult = await userInfo();

      const {deleteItem} = state;
      for (const iterator of cartItems) {
        if (iterator.checked) {
          // @ts-ignore
          deleteItem.push(iterator);
        }
      }
      // setState(prevState => {
      //   return {...prevState, deleteItem: deleteItem, userMsg: userResult.data.userInfo,}
      // })
    }
    initData()
  }, [])

  usePullDownRefresh(async () => {
    console.log('shoppingCart cartItems', cartItems);
    // const userResult = await userInfo();

    const {deleteItem} = state;
    for (const iterator of cartItems) {
      if (iterator.checked) {
        // @ts-ignore
        deleteItem.push(iterator);
      }
    }
    // setState(prevState => {
    //   return {...prevState, deleteItem: deleteItem, userMsg: userResult.data.userInfo,}
    // })
  })

  //点击编辑与完成
  const editor = () => {
    setState(prevState => {
      return {...prevState, editable: !prevState.editable}
    })
  }

  // const choose = (activiId, amount, require, totalPrice) => {
  //   if (totalPrice >= require) {
  //     setState(prevState => {
  //       return {...prevState, discount: amount, couponId: activiId}
  //     })
  //   } else {
  //     setState(prevState => {
  //       return {...prevState, discount: 0, couponId: ''}
  //     })
  //   }
  //   setState(prevState => {
  //     return {...prevState, activiId: activiId}
  //   })
  // }
  //
  // const open = () => {
  //   setState(prevState => {
  //     return {...prevState, modal: 'block'}
  //   })
  // }
  //
  // const prompt = () => {
  //   Taro.showToast({
  //     title: '敬请期待',
  //     icon: 'none'
  //   })
  // }

  //跳转确认订单
  const handlePay = async (couponId) => {
    console.log(couponId)
    const token = Taro.getStorageSync('accessToken');
    if (token) {
      Taro.hideTabBarRedDot({
        index: 2
      })

      Taro.setTabBarBadge({
        index: 2,
        text: '18',
      })

      Taro.navigateTo({
        url: "../orderDetails/index"
      });
    } else {
      // Taro.switchTab({
      //   url: "../mine/index"
      // });
    }
  }

  const onCheckboxGroupChange = (e) => {
    setState(prevState => {
      return {...prevState, discount: 0, activiId: 0,}
    })

    dispatch({
      type: 'common/changeCartItemsChecked',
      payload: e.detail.value
    })
  }

  const deleteCart = (deleteItem) => {
    dispatch({
      type: 'common/delete',
      payload: deleteItem
    })
    Taro.showToast({
      title: '删除完成',
      icon: 'success',
      duration: 1000,
    });
  }

  const changeCheck = () => {
    const {cartChecked} = state;
    dispatch({
      type: 'common/changeAllCheckedCartItems',
      payload: cartChecked
    })

    setState(prevState => {
      return {...prevState, cartChecked: !cartChecked}
    })
  }

  // 扫码加入购物车
  const lookForward = async () => {
    const {result} = await Taro.scanCode({});
    console.log('扫码result', result);

    const obj = JSON.parse(result);
    if (obj.userId) {
      console.log('扫码邀请');
      await invitedUsers(obj.userId);
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

  //点击修改加减数量
  const handleNumberChange = (itemId, number) => {
    setState(prevState => {
      return {...prevState, discount: 0, activiId: 0,}
    })
    dispatch({
      type: 'common/changeCartItemNumer',
      payload: {itemId, number}
    });
  }

  useEffect(() => {
    cartTabBarBadge(cartItems)

    const totalPrice = {total: 0};
    if (state.userMsg.role === 'member') {
      totalPrice.total = cartItems.reduce((total, currentValue) => {
        if (currentValue.checked) {
          return total + (currentValue.memberPrice * currentValue.number);
        }
        return total;
      }, 0);
    } else {
      totalPrice.total = cartItems.reduce((total, currentValue) => {
        if (currentValue.checked) {
          return total + (currentValue.price * currentValue.number);
        }
        return total;
      }, 0);
    }
    setState(prevState => {
      return {...prevState, totalPrice: totalPrice}
    })

  }, [])

  return (
    <ScrollView className='buy'>
      {cartItems.length > 0 ? (
        <View>
          <View className='topLine'>
            <Text className='total'>共计{cartItems.length}件商品</Text>
            <View className='editor' onClick={editor}>
              <Text className='total'>{state.editable ? '完成' : '管理'}</Text>
            </View>
          </View>
          {/*中间商品*/}
          <View className='page-body'>
            <CheckboxGroup onChange={onCheckboxGroupChange}>
              <View className='quanbu'>
                {cartItems.map((item) => (
                  <View key={item.itemId} className='Framework'>
                    {/*按钮*/}
                    <Checkbox value={item.itemId} checked={item.checked}/>
                    {/*按钮右边的图片文字等*/}
                    <View className='framework'>
                      {/*物品图片*/}
                      <Image src={item.imageUrl} className='goodsOne'/>
                      <View className='frame'>
                        {/*物品名称*/}
                        <Text className='name'>{item.name}</Text>
                        <View className='work'>
                          {/*价格*/}
                          <View style="display:flex;flex-direction:column;">
                            <Text
                              className='memberPrice'>￥{(item.memberPrice / 100).toFixed(2)}/{item.unit}</Text>
                            <Text
                              className='price'>￥{(item.price / 100).toFixed(2)}/{item.unit}</Text>
                            <Text
                              className='originalPrice'>￥{(item.originalPrice / 100).toFixed(2)}/{item.unit}</Text>
                          </View>
                          {/*数量选择*/}
                          <AtInputNumber
                            min={1}
                            max={99}
                            step={1}
                            width={100}
                            value={item.number}
                            onChange={() => handleNumberChange(item.itemId, item.number)}
                            style="margin-top:4PX;"
                            type="number"/>
                        </View>
                        {/*价格小计*/}
                        <View className='xj'>
                          <Text className='xiaoj'>小计：</Text>
                          {
                            state.userMsg.role === 'member' ? (
                              <Text
                                className='ji'>￥{(item.memberPrice * item.number) / 100}/{item.unit}</Text>
                            ) : (
                              <Text
                                className='ji'>￥{(item.price * item.number) / 100}/{item.unit}</Text>
                            )
                          }

                        </View>
                      </View>

                    </View>
                  </View>
                ))}
              </View>
            </CheckboxGroup>
            {cartItems.length !== 0 ? (<View className='boTo'>
              <View className='qx'>
                <Checkbox value='all' checked={cartItems.every(item => item.checked === true)}
                          onClick={changeCheck}>全选</Checkbox>
              </View>
              <View className='rightfk'>
                <View >
                  <Text >合计：</Text>
                  <Text
                    className='hj'>￥{(state.totalPrice.total || 0) / 100 - (state.discount)}/元</Text>
                </View>
                {!state.editable ? (
                  <View className='doubleButtonBox'>
                    <View className='fk' onClick={() => handlePay(state.couponId)}>
                      <Text className='payment'>付款</Text>
                    </View>
                  </View>
                ) : (
                  <View className='fk' onClick={() => deleteCart(state.deleteItem)}>
                    <Text className='delete'>删除</Text>
                  </View>
                )}
              </View>
            </View>) : ''}
          </View>
        </View>
      ) : (
        <View className="nullBox">
          {/* <Image src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/takenGoods.png' className="empty" /> */}
          <Text className="orederNullText">购物车暂无商品</Text>
        </View>
      )}
      <View className='shoppingScan' onClick={lookForward}>
        <Image src={shoppingScan}/>
        <Text className='shoppingScanTxt1'>扫一扫</Text>
        <Text className='shoppingScanTxt2'>商品条形码</Text>
      </View>

    </ScrollView>
  )
}

export default Index
