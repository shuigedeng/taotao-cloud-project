import Taro, {usePullDownRefresh} from '@tarojs/taro';
import {Button, Image, Input, Text, View} from '@tarojs/components';
import {AtCheckbox} from 'taro-ui';
import {cartChecked, cartDelete, cartUpdate, getCartListApi} from '../../services/cart';

import './index.less';
import React, {useEffect, useState} from "react";

const Index: Taro.FC = () => {
  const [state, setState] = useState({
    cartGoods: [],
    cartTotal: {
      'goodsCount': 0,
      'goodsAmount': 0.00,
      'checkedGoodsCount': 0,
      'checkedGoodsAmount': 0.00
    },
    isEditCart: false,
    checkedAllStatus: true,
    editCartList: [],
    hasLogin: false
  })

  const getCartList = () => {
    getCartListApi().then(res => {
      setState(prevState => {
        return {
          ...prevState,
          cartGoods: res.cartList,
          cartTotal: res.cartTotal
        }
      })

      setState(prevState => {
        return {...prevState, checkedAllStatus: isCheckedAll()}
      })
    })
  }

  useEffect(() => {
    const hasLogin = getGlobalData('hasLogin')
    if (hasLogin) {
      getCartList();
    }

    setState(prevState => {
      return {...prevState, hasLogin: hasLogin}
    })
  }, [getCartList, setState])

  usePullDownRefresh(() => {
    Taro.showNavigationBarLoading() //在标题栏中显示加载
    getCartList();
    Taro.hideNavigationBarLoading() //完成停止加载
    Taro.stopPullDownRefresh() //停止下拉刷新
  })

  const deleteCart = () => {
    let productIds = state.cartGoods.filter(function (element, index, array) {
      return element.checked == true;
    });

    if (productIds.length <= 0) {
      return false;
    }

    productIds = productIds.map(function (element, index, array) {
      if (element.checked == true) {
        return element.productId;
      }
    });

    cartDelete({
      productIds: productIds
    }).then(res => {
      console.log(res.data);
      let cartList = res.cartList.map(v => {
        v.checked = false;
        return v;
      });

      setState(prevState => {
        return {
          ...prevState,
          cartGoods: cartList,
          cartTotal: res.cartTotal
        }
      })

      setState(prevState => {
        return {...prevState, checkedAllStatus: isCheckedAll()}
      })
    })
  }

  const isCheckedAll = () => {
    //判断购物车商品已全选
    return state.cartGoods.every(function (element, index, array) {
      return element.checked == true;
    });
  }

  const doCheckedAll = () => {
    setState(prevState => {
      return {...prevState, checkedAllStatus: isCheckedAll()}
    })
  }

  const goLogin = () => {
    Taro.navigateTo({
      url: "/pages/auth/login/login"
    });
  }

  const cutNumber = (event) => {
    let itemIndex = event.target.dataset.itemIndex;
    let cartItem = state.cartGoods[itemIndex];
    let number = (cartItem.number - 1 > 1) ? cartItem.number - 1 : 1;
    cartItem.number = number;
    setState(prevState => {
      updateCart(cartItem.productId, cartItem.goodsId, number, cartItem.id);
      return {...prevState, cartGoods: state.cartGoods}
    })
  }

  const addNumber = (event) => {
    let itemIndex = event.target.dataset.itemIndex;
    let cartItem = state.cartGoods[itemIndex];
    let number = cartItem.number + 1;
    cartItem.number = number;
    setState(prevState => {
      updateCart(cartItem.productId, cartItem.goodsId, number, cartItem.id);
      return {...prevState, cartGoods: state.cartGoods}
    })
  }

  const updateCart = (productId, goodsId, number, id) => {
    cartUpdate({
      productId: productId,
      goodsId: goodsId,
      number: number,
      id: id
    }).then(() => {
      setState(prevState => {
        return {...prevState, checkedAllStatus: isCheckedAll()}
      })
    })
  }

  const checkoutOrder = () => {
    //获取已选择的商品
    var checkedGoods = state.cartGoods.filter(function (element, index, array) {
      return element.checked == true;
    });

    if (checkedGoods.length <= 0) {
      return false;
    }

    // storage中设置了cartId，则是购物车购买
    try {
      Taro.setStorageSync('cartId', 0);
      Taro.navigateTo({
        url: '/pages/checkout/checkout'
      })
    } catch (e) {
    }
  }

  const editCart = () => {
    if (state.isEditCart) {
      getCartList();
      setState(prevState => {
        return {...prevState, isEditCart: !prevState.isEditCart}
      })
    } else {
      //编辑状态
      let tmpCartList = state.cartGoods.map(function (v) {
        v.checked = false;
        return v;
      });

      setState(prevState => {
        return {
          ...prevState,
          editCartList: prevState.cartGoods,
          cartGoods: tmpCartList,
          isEditCart: !prevState.isEditCart,
          checkedAllStatus: isCheckedAll(),
          'cartTotal.checkedGoodsCount': getCheckedGoodsCount()
        }
      })
    }
  }

  const checkedItem = (event) => {
    let itemIndex = event.target.dataset.itemIndex;
    let productIds = [];
    productIds.push(state.cartGoods[itemIndex].productId);
    if (!state.isEditCart) {
      cartChecked({
        productIds: productIds,
        isChecked: state.cartGoods[itemIndex].checked ? 0 : 1
      }).then(res => {
        setState(prevState => {
          return {
            ...prevState,
            cartGoods: res.cartList,
            cartTotal: res.cartTotal
          }
        })

        setState(prevState => {
          return {...prevState, checkedAllStatus: isCheckedAll()}
        })
      })

    } else {
      //编辑状态
      let tmpCartData = state.cartGoods.map(function (element, index, array) {
        if (index == itemIndex) {
          element.checked = !element.checked;
        }
        return element;
      });

      setState(prevState => {
        return {
          ...prevState,
          cartGoods: tmpCartData,
          checkedAllStatus: isCheckedAll(),
          'cartTotal.checkedGoodsCount': getCheckedGoodsCount()
        }
      })
    }
  }

  const getCheckedGoodsCount = () => {
    let checkedGoodsCount = 0;
    state.cartGoods.forEach(function (v) {
      if (v.checked === true) {
        checkedGoodsCount += v.number;
      }
    });
    return checkedGoodsCount;
  }

  const {hasLogin, isEditCart, cartGoods, cartTotal, checkedAllStatus} = state;

  return (
    <View className='container'>
      {
        !hasLogin ? <View className='no-login'>
          <View className='c'>
            <Text className='text'>还没有登录</Text>
            <Button className='button' style='background-color:#A9A9A9'
                    onClick={goLogin}>去登录</Button>
          </View>
        </View> : <View className='login'>
          <View className='service-policy'>
            <View className='item'>30天无忧退货</View>
            <View className='item'>48小时快速退款</View>
            <View className='item'>满88元免邮费</View>
          </View>
          {
            cartGoods.length <= 0 ? <View className='no-cart'>
              <View className='c'>
                <Text>空空如也~</Text>
                <Text>去添加点什么吧</Text>
              </View>
            </View> : <View className='cart-view'>
              <View className='list'>
                <View className='group-item'>
                  <View className='goods'>
                    {
                      cartGoods.map((item, index) => {
                        return <View className={`item ${isEditCart ? 'edit' : ''}`} key='id'>
                          <AtCheckbox onChange={checkedItem}
                                      options={[{value: item, label: item}]}
                                      selectedList={item}/>
                          {/* <van-checkbox value='{ item.checked }' bind:change='checkedItem' data-item-index='{index}'></van-checkbox> */}
                          <View className='cart-goods'>
                            <Image className='img' src={item.picUrl}/>
                            <View className='info'>
                              <View className='t'>
                                <Text className='name'>{item.goodsName}</Text>
                                <Text className='num'>x{item.number}</Text>
                              </View>
                              <View
                                className='attr'>{isEditCart ? '已选择:' : ''}{item.specifications || ''}</View>
                              <View className='b'>
                                <Text className='price'>￥{item.price}</Text>
                                <View className='selnum'>
                                  <View className='cut' onClick={cutNumber}
                                        data-item-index={index}>-</View>
                                  <Input value={item.number} className='number' disabled={true}
                                         type='number'/>
                                  <View className='add' onClick={addNumber}
                                        data-item-index={index}>+</View>
                                </View>
                              </View>
                            </View>
                          </View>
                        </View>
                      })
                    }
                  </View>
                </View>

              </View>
              <View className='cart-bottom'>
                {/* <van-checkbox value='{ checkedAllStatus }' bind:change='checkedAll'>全选（{cartTotal.checkedGoodsCount}）</van-checkbox> */}
                <View
                  className='total'>{!isEditCart ? '￥' + cartTotal.checkedGoodsAmount : ''}</View>
                <View className='action_btn_area'>
                  <View className={!isEditCart ? 'edit' : 'sure'}
                        onClick={editCart}>{!isEditCart ? '编辑' : '完成'}</View>

                  {isEditCart && <View className='delete'
                                       onClick={deleteCart}>删除({cartTotal.checkedGoodsCount})</View>}
                  {!isEditCart && <View className='checkout' onClick={checkoutOrder}>下单</View>}
                </View>
              </View>
            </View>
          }

        </View>
      }
    </View>
  )
}

export default Index
