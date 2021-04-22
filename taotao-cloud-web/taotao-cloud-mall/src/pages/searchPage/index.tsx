import { View, Text, Image } from "@tarojs/components";
import Taro, { Component, Config } from "@tarojs/taro";
import "./index.less";
import { connect } from '@tarojs/redux';
import { items } from "./service";

// 引入moment
import moment from "moment";
moment.locale("zh-cn");

interface IState {
  list: any;
  currentPage: number;
  pageSize: number;
}
@connect(({ common }) => ({ common }))
export default class storeLists extends Component<any, IState> {
  config: Config = {
    navigationBarTitleText: "商品列表"
  };

  state = {
    list: null,
    currentPage: 1,
    pageSize: 10,
  };

  // 页面加载前
  async componentWillMount() {
    const { nameLike } = this.$router.params;
    const { currentPage, pageSize } = this.state;
    const result = await items(nameLike, currentPage, pageSize);
    const { list } = result.data.items;
    this.setState({
      list
    });
  }

  // 上拉加载
  async onReachBottom() {
    const { nameLike } = this.$router.params;
    const { currentPage, pageSize, list } = this.state;
    const currentPageAdd = currentPage + 1;
    const result = await items(nameLike, currentPageAdd, pageSize);
    const { list:newList } = result.data.items;
    if (newList.length !== 0) {
      //上拉加载
      Taro.showLoading({
        title: '正在加载',
      })
      this.setState({
        currentPage: currentPageAdd
      })
      if (newList) {
        for (const iterator of newList) {
          list.push(iterator);
        }
        this.setState({
          list
        });
      }
      setTimeout(function () {
        Taro.hideLoading()
      }, 1000)
    }else {
      setTimeout(function(){
        Taro.showToast({
          title: '已全部加载',
          icon: 'error',
          mask: true,
        })
      }, 10)
    }
  }
  /**
   * 
   * @param a 商品的 ID
   * @param b 名称
   * @param c 数量
   * @param d 价格
   * @param e 图片
   */
  onOpenDoor(code, name, number, price, unit, imageUrl) {
    const data: any = {};
    data.itemId = code;
    data.name = name;
    data.number = number;
    data.price = price;
    data.unit = unit;
    data.imageUrl = imageUrl;
    const { dispatch } = this.props;
    dispatch({
      type: 'common/addToCartByCode',
      payload: data
    });
  }

  // 跳转商品详情
  handleDetails(code) {
    Taro.navigateTo({
      url: `../details/index?code=${code}`
    });
  }

  render() {
    const { list } = this.state;
    return (
      <View className="index">
        {/* 店铺列表 */}
        <View className="items_box">
          {list.map(item => (
            <View
              className="item_box"
              key={item.code}
            >
              <Image src={item.imageUrl} className="image" onClick={this.handleDetails.bind(this, item.code)} />
              <View className="item_bottom_box">
                <Text className="title">{item.name}</Text>
                <Text className="title1">{item.content}</Text>
                <View className="item_right_box">
                  <View className="priceBox">
                    <Text className="price">
                      ￥{(item.price / 100).toFixed(2)}/{item.unit}
                    </Text>
                    <Text className="originalPrice">
                      ￥{(item.originalPrice / 100).toFixed(2)}
                    </Text>
                    <Text className="memberPrice">
                      会员价:￥{(item.memberPrice / 100).toFixed(2)}
                    </Text>
                  </View>
                  <Image
                    src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/shopping_cart.png'
                    className="shoppingCart"
                    onClick={this.onOpenDoor.bind(this,
                      item.code,
                      item.name,
                      1,
                      item.price,
                      item.unit,
                      item.imageUrl
                    )}
                  />
                </View>
              </View>
            </View>
          ))}
        </View>
      </View>
    );
  }
}
