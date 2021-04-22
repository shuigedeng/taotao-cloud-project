import Taro, { Component, Config } from "@tarojs/taro";
import { View, Text, Image, } from "@tarojs/components";
import { AtTabs, AtTabsPane } from "taro-ui";
import "./index.less";
import { orders } from "./service";

interface IState {
  current: number;
  list?: any;
  currentPage: number;
  status: string;
  type: string;
}
export default class Home extends Component<null, IState> {
  config: Config = {
    navigationBarTitleText: "我的订单"
  };

  state = {
    current: 0,
    list: [],
    currentPage: 1,
    status: 'completed',
    type: 'unmanned',
  };

  handleOrderInformation(id) {
    // let = orderType;
    // if(id===1){

    // }
    Taro.navigateTo({
      url: `../orderInformation/index?id=${id}`
    });
  }

  // 初始加载
  async componentWillMount() {
    const { id } = this.$router.params;
    const { currentPage } = this.state;
    const newCurrent = Number(id);
    this.setState({
      current: newCurrent
    });
    let status;
    let type;
    switch (newCurrent) {
      case 0:
        break;
      case 1:
        status = "fetch";
        type = "unmanned";
        break;
      case 2:
        status = "fetch";
        type = "distribution";
        break;
      case 3:
        status = "completed";
        break;
      default:
        break;
    }
    const result = await orders(status, currentPage, type);
    const list = result.data.orders.list;
    this.setState({
      list
    });
  }

  // tab跳转
  async handleClick(value) {
    const { currentPage } = this.state;
    this.setState({
      current: value
    });
    let statusNow;
    let typeNow;
    switch (value) {
      case 0:
        break;
      case 1:
        statusNow = "fetch";
        typeNow = "unmanned";
        break;
      case 2:
        statusNow = "fetch";
        typeNow = "distribution";
        break;
      case 3:
        statusNow = "completed";
        break;
      default:
        break;
    }
    const result = await orders(statusNow, currentPage, typeNow);
    const list = result.data.orders.list;
    console.log('orders list', list);

    this.setState({
      status: statusNow,
      type: typeNow,
      list
    });
  }

  // 下拉加载
  async onPullDownRefresh() {
    const { status, type } = this.state;
    const result = await orders(status, 1, type);
    const list = result.data.orders.list;
    this.setState({
      list,
      currentPage: 1,
    });
    setTimeout(() => {
      Taro.stopPullDownRefresh(); //停止下拉刷新
    }, 1000);
  }

  // 上拉加载
  async onReachBottom() {
    const { currentPage, list, } = this.state;
    const { id } = this.$router.params;
    const newCurrent = Number(id);
    let status;
    let type;
    switch (newCurrent) {
      case 0:
        break;
      case 1:
        status = "fetch";
        type = "unmanned";
        break;
      case 2:
        status = "fetch";
        type = "distribution";
        break;
      case 3:
        status = "completed";
        break;
      default:
        break;
    }
    console.log('status', status);
    console.log('currentPage', currentPage);
    console.log('type', type);

    const currentPageAdd = currentPage + 1;
    const result = await orders(status, currentPageAdd, type);
    console.log('上拉加载result', result);

    const newList = result.data.orders.list;
    console.log('上啦加载newList', newList);

    if (newList.length !== 0) {
      //上拉加载
      Taro.showLoading({
        title: '正在加载',
      })
      this.setState({
        currentPage: currentPageAdd
      })
      for (const iterator of newList) {
        console.log('iterator', iterator);
        list.push(iterator);
      }
      this.setState({
        list
      });
      setTimeout(function () {
        Taro.hideLoading()
      }, 1000)
    } else {
      setTimeout(function () {
        Taro.showToast({
          title: '已全部加载',
          icon: 'error',
          mask: true,
        })
      }, 10)
    }
  }
  componentDidMount() { }

  componentWillUnmount() { }

  render() {
    const { list } = this.state;
    console.log('list', list);

    const tabList = [
      { title: "全部", id: 0 },
      { title: "待取货", id: 1 },
      { title: "待配送", id: 2 },
      { title: "已完成", id: 3 },
    ];
    return (
      <View className="index">
        <AtTabs
          current={this.state.current}
          tabList={tabList}
          onClick={this.handleClick.bind(this)}
        >
          {tabList.map(item => (
            <AtTabsPane
              current={this.state.current}
              index={item.id}
              key={item.id}
              className="atTabs"
            >
              {list.length > 0 ? (
                <View>
                  {list.map(order => (
                    <View
                      key={order.id}
                      className="one_order"
                      onClick={this.handleOrderInformation.bind(this, order.id)}
                    >
                      <View className="one_order_top">
                        <Text className="shop_text">{order.store.name}</Text>
                      </View>
                      {/* <View className="one_order_top_right">
                        <Text className="order_status">订单编号: {order.id}</Text>
                      </View> */}

                      <View className="imgs_box">
                        {order.orderItem.map(orderItms => (
                          <View>
                            {/* <Text className="order_status">
                              {orderItms.title}
                            </Text> */}
                            <View key={orderItms.id} className="oneItemImage">
                              <Image src={orderItms.imageUrl} className="img" />
                              <Text className="oneItemNumber">
                                ×{orderItms.number}
                              </Text>
                            </View>
                          </View>
                        ))}
                      </View>
                      <View className="one_order_bottom">
                        <Text className="one_order_bottom_priceTotal">
                          实付金额：￥{(order.amount / 100).toFixed(2)}
                        </Text>
                      </View>
                    </View>
                  ))}
                </View>
              ) : (
                  <View className="nullBox">
                    <Image src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/takenGoods.png' className="empty" />
                    <Text className="orederNullText">暂无订单！</Text>
                  </View>
                )}
            </AtTabsPane>
          ))}
        </AtTabs>
      </View>
    );
  }
}
