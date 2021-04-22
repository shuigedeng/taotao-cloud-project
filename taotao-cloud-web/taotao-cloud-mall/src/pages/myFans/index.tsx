import Taro, { Component, Config } from "@tarojs/taro";
import { AtTabs, AtTabsPane } from 'taro-ui'
import { ScrollView, View, Text, Image, } from "@tarojs/components";
import { userInfo } from "../orderDetails/service";
import "./index.less";

interface IState {
  current: number;
  current1: number;
  query:  any;
}
export default class certificates extends Component<any, IState>  {
  config: Config = {
    navigationBarTitleText: "粉丝"
  };
  state = {
    current: 0,
    current1: 0,
    query: '',
  };
  handleClick(value) {
    this.setState({
      current: value
    })
  }
  handle(value1) {
    this.setState({
      current1: value1
    })
  }

  async componentWillMount() {
    const {data} = await userInfo();
    this.setState({
      query: data.userInfo.fanList
    });
  }

  render() {
    const { query } = this.state;
    return (
      <ScrollView className="index">
        <AtTabs
          animated={false}
          current={this.state.current}
          tabList={[
            { title: '直属会员' },
            { title: '间接会员' },
          ]}
          onClick={this.handleClick.bind(this)}>
          <AtTabsPane current={this.state.current} index={0} >
            <View className="sortingOptions">
              {/* 时间排序 */}
              <View className="price_stort">
                <Text className="sorted_text">加入时间</Text>
                <Image 
                  src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/top_sorting.png'
                  className="arrow" 
                />
              </View>
              {/* 占位排序（无用）  */}
              <View className="price_stort">
                <Text className="sorted_text">团队规模</Text>
                <Image 
                  src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/top_sorting.png'
                  className="arrow" 
                />
              </View>
              <View className="price_stort">
                <Text className="sorted_text">全部会员</Text>
              </View>
            </View>
            <View>
                <View className="price_stort1">
                  <Text className="sorted_text">{query.id}</Text>
                  <Text className="sorted_text">{query.imageUrl}</Text>
                  <Text className="sorted_text">{query.nickname}</Text>
                  <Text className="sorted_text">{query.point}</Text>
                  <Text className="sorted_text">{query.role}</Text>
                </View>
            </View>
          </AtTabsPane>
          <AtTabsPane current={this.state.current} index={1} ></AtTabsPane>
        </AtTabs>
      </ScrollView>
    );
  }
}
