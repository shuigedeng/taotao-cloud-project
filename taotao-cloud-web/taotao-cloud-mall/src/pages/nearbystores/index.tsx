import Taro, { Component, Config } from "@tarojs/taro";
import {
  View,
  Text,
  Image,
  Input,
} from "@tarojs/components";
import {
  nearbyStore,
} from "./service";
import scan from "../../img/scan.png";
import "./index.less";
import "../home/index.less";


// 引入moment
import moment from "moment";
moment.locale("zh-cn");

interface IState {
  authorization: boolean;
  myLatitude: number;
  myLongitude: number;
  sotreQuery: {
    data: any;
  };
  input: string;
  list?: any;
}
export default class Home extends Component<null, IState> {
  config: Config = {
    navigationBarTitleText: "附近店铺"
  };

  state = {
    authorization: false,
    sotreQuery: {
      data: null,
      loading: true
    },

    myLatitude: 1,
    myLongitude: 1,
    input: "",
    list: []
  };

  async componentDidShow() {
    const geographicResult = await new Promise(resolve => {
      Taro.getLocation({
        type: "wgs84",
        success(res) {
          resolve(res);
        }
      });
    });
    const pageSize = Taro.getStorageSync("pageSize");
    const storeResult = await nearbyStore(
      geographicResult.longitude,
      geographicResult.latitude,
      1,
      pageSize
    );
    this.setState({
      sotreQuery: storeResult
    });
    const list = storeResult.data.nearbyStore.list;
    this.setState({
      list: list
    });
  }
  componentDidMount() { }

  // 跳转到首页
  /**
   * 
   * @param id 
   * @param name 
   * @param address 
   * @param longitude 
   * @param latitude 
   */
  chooseCity(id, name, address, longitude, latitude) {
    Taro.setStorage({
      key: "storeId",
      data: id
    });
    Taro.setStorage({
      key: "nearbyStoreName",
      data: name
    });
    Taro.setStorage({
      key: "storeAddress",
      data: address
    });
    Taro.setStorage({
      key: "storeLngAndLat",
      data: { longitude, latitude },
    });
    Taro.switchTab({
      url: "../home/index"
    });
  }

  // 跳转到地图
  handleGoToMap(longitude, latitude, address) {
    const newlatitude = parseFloat(latitude);
    const newlongitude = parseFloat(longitude);
    Taro.openLocation({
      latitude: newlatitude,
      longitude: newlongitude,
      name: address
    });
  }

  // 上拉加载
  async onReachBottom() {
    const geographicResult = await new Promise(resolve => {
      Taro.getLocation({
        type: "wgs84",
        success(res) {
          resolve(res);
        }
      });
    });
    const { sotreQuery, list } = this.state;
    const total = sotreQuery.data.nearbyStore.pagination.total;
    const pageSize = sotreQuery.data.nearbyStore.pagination.pageSize;
    const loadCurrent = sotreQuery.data.nearbyStore.pagination.current + 1;
    const number = Math.ceil(total / pageSize);
    if (loadCurrent > number) {
      Taro.showToast({
        title: "加载完成！",
        icon: "none",
        duration: 500
      });
    } else {
      const loadCurrent = sotreQuery.data.nearbyStore.pagination.current + 1;
      const loadResult = await nearbyStore(
        geographicResult.longitude,
        geographicResult.latitude,
        loadCurrent,
        1
      );
      const loadList = list.concat(loadResult.data.nearbyStore.list);
      this.setState({
        list: loadList,
        sotreQuery: loadResult
      });
    }
  }

  // 搜索框内容修改
  handleOnChange(e) {
    this.setState({
      input: e.detail.value
    });
  }

  // 搜索按钮
  clickSearch() {
    // const { input } = this.state;
    // Taro.navigateTo({
    //   url: `../searchPage/index?nameLike=${input}`
    // });
  }

  render() {
    const { list } = this.state;
    console.log('选择店铺list',list);
    
    return (
      <View className="optimization">
        {/* <View className='SearchAndAddress'>

        </View> */}
        <View className='searchAscan'>
          <View className="rightSearchBox AddressInput">
            <Image
              src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/searchimg.png'
              className="searchImg"
            />
            <Input
              type="text"
              placeholder="搜索你想要找到的门店"
              className="input"
              value={this.state.input}
              onInput={this.handleOnChange}
              // disabled
              onConfirm={this.clickSearch}
            />
          </View>
          <View className="firstLineLeft">
            <View className="firstLineLeft">
              <Image 
                src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/distance.png' 
                className="scanIcon" 
              />
              <Text className="province">安徽</Text>
            </View>
          </View>
        </View>
        {/* 商家主列表 */}
        <View>
          {list.map((item,index) => (
            <View className='storeBorder' key={item.id}>
              {
                index===0?
                <Text>距离你最近的门店</Text>:null
              }
              <View className="storeInformation">
                <View
                  className="storeLeft"
                  onClick={this.chooseCity.bind(
                    this,
                    item.id,
                    item.name,
                    item.address,
                    item.longitude,
                    item.latitude)}
                // onClick={this.handleStoreDetail.bind(this, item.id)}
                >
                  {/* 左边店铺图片 */}
                  <Image src={item.imageUrl} className="leftStoreImg" />
                  {/* 中间店铺信息(名称，营业时间，类型，距离) */}
                  <View className="centerBox">
                    <Text className="itemTitle">{item.name}</Text>
                    <Text className="itemTime">
                      营业时间:9:00--18:00
                    </Text>
                    <Text className="itemDistance">
                      {item.address}
                    </Text>
                  </View>
                </View>

                {/* 右边去这里按钮 */}
                <View className='RightDistance'>
                  <Text
                    className="rightButton hereText"
                    onClick={this.handleGoToMap.bind(
                      this,
                      item.longitude,
                      item.latitude,
                      item.address
                    )}
                  >门店导航</Text>
                  <Text className="nowDistance">
                    {item.distance / 1000}KM
                  </Text>
                </View>
              </View>
            </View>
          ))}
        </View>
      </View>

    );
  }
}
