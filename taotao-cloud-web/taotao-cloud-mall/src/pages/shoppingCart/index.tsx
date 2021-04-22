import { connect } from '@tarojs/redux';
import Taro, { Component } from '@tarojs/taro'
import { View, Text, ScrollView, Image, Checkbox, CheckboxGroup } from '@tarojs/components'
import { AtInputNumber } from 'taro-ui'
import shoppingScan from '../../img/shoppingScan.png'
import { invitedUsers, singleItem } from '../home/service'
import { userInfo } from "../orderDetails/service"
import './index.less'

@connect(({ common }) => ({ common }))
export default class Buy extends Component<any>{
  //点击修改加减数量
  handleNumberChange(itemId, number) {
    this.setState({
      discount: 0,
      activiId: 0,
    })
    const { dispatch } = this.props;
    dispatch({
      type: 'common/changeCartItemNumer',
      payload: { itemId, number }
    });
  }

  config = {
    navigationBarTitleText: '购物车'
  }

  state = {
    editable: false,
    modal: 'none',
    discount: 0,
    couponId: '',
    cartChecked: true,
    deleteItem: [],
    userMsg: [],
  }

  async componentWillMount() {
    const { cartItems } = this.props.common;
    console.log('shoppingCart cartItems', cartItems);
    const userResult = await userInfo();

    const { deleteItem } = this.state;
    for (const iterator of cartItems) {
      if (iterator.checked) {
        deleteItem.push(iterator);
      }
    }
    this.setState({
      deleteItem,
      userMsg: userResult.data.userInfo,
    })
  }

  //下拉刷新
  async onPullDownRefresh() {
      
    const { cartItems } = this.props.common;
    console.log('shoppingCart cartItems', cartItems);
    const userResult = await userInfo();

    const { deleteItem } = this.state;
    for (const iterator of cartItems) {
      if (iterator.checked) {
        deleteItem.push(iterator);
      }
    }
    this.setState({
      deleteItem,
      userMsg: userResult.data.userInfo,
    })
  }

  //点击编辑与完成
  editor() {
    const { editable } = this.state;
    this.setState({
      editable: !editable
    });
  }
  choose(activiId, amount, require, totalPrice) {
    if (totalPrice >= require) {
      this.setState({
        discount: amount,
        couponId: activiId
      })
    } else {
      this.setState({
        discount: 0,
        couponId: ''
      })
    }
    this.setState({
      activiId,
    })
  }
  open() {
    this.setState({
      modal: 'block'
    })
  }

  prompt() {
    Taro.showToast({
      title: '敬请期待',
      icon: 'none'
    })
  }

  //跳转确认订单
  async handlePay() {

    const token = Taro.getStorageSync('accessToken');
    if (token) {

      // Taro.hideTabBarRedDot({
      //   index: 2
      // })

      // Taro.setTabBarBadge({
      //   index: 2,
      //   text: '18',
      // })

      Taro.navigateTo({
        url: "../orderDetails/index"
      });
    } else {
      Taro.switchTab({
        url: "../mine/index"
      });
    }
  }

  onCheckboxGroupChange(e) {
    this.setState({
      discount: 0,
      activiId: 0,
    })
    const { dispatch } = this.props;
    dispatch({
      type: 'common/changeCartItemsChecked',
      payload: e.detail.value
    })
  }

  deleteCart(deleteItem) {
    const { dispatch } = this.props;
    dispatch({
      type: 'common/delete',
      payload: deleteItem
    })
    Taro.showToast({
      title: '删除完成',
      icon: 'succes',
      duration: 1000,
    });
  }
  changeCheck() {
    const { dispatch } = this.props;
    const { cartChecked } = this.state;
    dispatch({
      type: 'common/changeAllCheckedCartItems',
      payload: cartChecked
    })
    this.setState({
      cartChecked: !cartChecked
    })
  }
  // 扫码加入购物车
  async lookForward() {
    const { dispatch } = this.props;
    const { result } = await Taro.scanCode();
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

  // }
  render() {
    const { cartItems } = this.props.common;
    const { userMsg } = this.state;
    console.log('render cartItems', cartItems);
     // 购物车右上角图标
    if (cartItems.length === 0) {
      // Taro.hideTabBarRedDot({
      //   index: 2
      // })
      Taro.removeTabBarBadge({
        index: 2
      })
    }else{
      let sum = 0;
      let i;
      for( i in cartItems){
        if(cartItems[i].checked){
          sum += parseInt(cartItems[i].number);
        }
      }
      Taro.setTabBarBadge({
        index: 2,
        text: "" + sum + ""	,
      })
    }

    const { couponId, deleteItem } = this.state;
    const check = cartItems.every(item => item.checked === true);
    const totalPrice = {};
    if (userMsg.role === 'member') {
      const total = cartItems.reduce((total, currentValue) => {
        if (currentValue.checked) {
          return total + (currentValue.memberPrice * currentValue.number);
        }
        return total;
      }, 0)
      totalPrice.total = total;
    }else{
      const total = cartItems.reduce((total, currentValue) => {
        if (currentValue.checked) {
          return total + (currentValue.price * currentValue.number);
        }
        return total;
      }, 0)
      totalPrice.total = total;
    }
    const { editable } = this.state;
    return (
      <ScrollView className='buy'>
        {cartItems.length > 0 ? (
          <View>
            <View className='topLine'>
              <Text className='total'>共计{cartItems.length}件商品</Text>
              <View className='editor' onClick={this.editor}>
                <Text className='total'>{editable ? '完成' : '管理'}</Text>
              </View>
            </View>
            {/*中间商品*/}
            <View className='page-body'>
              <CheckboxGroup onChange={this.onCheckboxGroupChange}>
                <View className='quanbu'>
                  {cartItems.map((item) => (
                    <View key={item.itemId} className='Framework'>
                      {/*按钮*/}
                      <Checkbox value={item.itemId} checked={item.checked}></Checkbox>
                      {/*按钮右边的图片文字等*/}
                      <View className='framework'>
                        {/*物品图片*/}
                        <Image src={item.imageUrl} className='goodsOne' />
                        <View className='frame'>
                          {/*物品名称*/}
                          <Text className='name'>{item.name}</Text>
                          <View className='work'>
                            {/*价格*/}
                            <View style="display:flex;flex-direction:column;">
                              <Text className='memberPrice'>￥{(item.memberPrice / 100).toFixed(2)}/{item.unit}</Text>
                              <Text className='price'>￥{(item.price / 100).toFixed(2)}/{item.unit}</Text>
                              <Text className='originalPrice'>￥{(item.originalPrice / 100).toFixed(2)}/{item.unit}</Text>
                            </View>
                            {/*数量选择*/}
                            <AtInputNumber
                              min={1}
                              max={99}
                              step={1}
                              width={100}
                              value={item.number}
                              onChange={this.handleNumberChange.bind(this, item.itemId)}
                              style="margin-top:4PX;"
                            />
                          </View>
                          {/*价格小计*/}
                          <View className='xj'>
                            <Text className='xiaoj'>小计：</Text>
                            {
                              userMsg.role === 'member'?(
                                <Text className='ji'>￥{(item.memberPrice * item.number) / 100}/{item.unit}</Text>
                              ):(
                                <Text className='ji'>￥{(item.price * item.number) / 100}/{item.unit}</Text>
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
                  <Checkbox value='all' checked={check} onClick={this.changeCheck}>全选</Checkbox>
                </View>
                <View className='rightfk'>
                  <View className='YH'>
                    <Text className='yh'>合计：</Text>
                    <Text className='hj'>￥{(totalPrice.total || 0) / 100 - (this.state.discount)}/元</Text>
                  </View>
                  {!editable ? (
                    <View className='doubleButtonBox'>
                      <View className='fk' onClick={this.handlePay.bind(this, couponId)}>
                        <Text className='payment'>付款</Text>
                      </View>
                    </View>
                  ) : (
                      <View className='fk' onClick={this.deleteCart.bind(this, deleteItem)}>
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
        <View className='shoppingScan' onClick={this.lookForward}>
          <Image src={shoppingScan} />
          <Text className='shoppingScanTxt1'>扫一扫</Text>
          <Text className='shoppingScanTxt2'>商品条形码</Text>
        </View>

      </ScrollView>
    )
  }
}
