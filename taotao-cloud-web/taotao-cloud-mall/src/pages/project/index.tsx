import Taro, { Component } from "@tarojs/taro";
import { View, Image, Text } from "@tarojs/components";
import "./index.less";
import { handleQuery } from "./service";
import { projects } from "../home/service";

interface IState {
  list?: any;
  currentPage: number;
  pageSize: number;
  projectImg?: string;
}
export default class project extends Component<null, IState> {
  config = {
    enablePullDownRefresh: true
  };

  state = {
    list: [],
    currentPage: 1,
    pageSize: 10,
    projectImg: '',
  };

  async componentWillMount() {
    const project = await projects();
    const myProject = project.data.projects;
    const { currentPage, pageSize } = this.state;
    const { id } = this.$router.params;
    switch (id) {
      case '1':
        Taro.setNavigationBarTitle({
          title: "每周新品"
        });
        this.setState({
          projectImg: myProject[0].imageUrl
        })
        break;
      case '2':
        Taro.setNavigationBarTitle({
          title: "品牌主打"
        });
        this.setState({
          projectImg: myProject[1].imageUrl
        })
        break;
      case '3':
        Taro.setNavigationBarTitle({
          title: "能量必备"
        });
        this.setState({
          projectImg: myProject[2].imageUrl
        })
        break;
      default:
        break;
    }
    const result = await handleQuery(currentPage, pageSize, id);
    const list = result.data.items.list;
    this.setState({
      list,
    });
  }

  // 上拉加载
  async onReachBottom() {
    const { currentPage, pageSize, list } = this.state;
    const { id } = this.$router.params;
    const currentPageAdd = currentPage + 1;
    const result = await handleQuery(currentPageAdd, pageSize, id);
    const { list: itemList } = result.data.items;
    if (itemList.length !== 0) {
      //上拉加载
      Taro.showLoading({
        title: '正在加载',
      })
      this.setState({
        currentPage: currentPageAdd
      })
      if (itemList) {
        for (const iterator of itemList) {
          list.push(iterator);
        }
        this.setState({
          list,
        });
      }
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

  handleDetails(code) {
    Taro.navigateTo({
      url: `../details/index?code=${code}`
    });
  }

  getCoupons() {
    Taro.navigateTo({
      url: `../gifts/index`
    });
  }

  componentDidShow() { }

  componentDidHide() { }

  render() {
    const { list, projectImg } = this.state;
    console.log('projectImg', projectImg);

    return (
      <View>
        <View
          className="topImg"
          onClick={this.getCoupons}
        >
          <Image
            src={projectImg}
          />
        </View>
        <View>
          {list.length > 0 ? (
            <View className="index">
              <View>
                {list.map(item => (
                  <View className="item_box"
                    onClick={this.handleDetails.bind(this, item.code)}
                  >
                    <Image src={item.imageUrl} className="img" />
                    <View className="item_box_right">
                      <Text className="item_box_right_title">{item.name}</Text>
                      <View>
                        <Text className="item_box_bottom_view_price">
                          ￥{item.price / 100}
                        </Text>
                        <Text className="item_box_bottom_view_originalPrice">
                          ￥{item.originalPrice / 100}
                        </Text>
                      </View>
                    </View>
                  </View>
                ))}
              </View>
            </View>
          ) : (
              <View className="nullBox">
                <Image src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/takenGoods.png' className="empty" />
                <Text className="orederNullText">暂无商品！</Text>
              </View>
            )}
        </View>
      </View>
    );
  }
}
