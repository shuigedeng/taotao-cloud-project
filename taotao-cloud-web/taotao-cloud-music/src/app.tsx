import Taro from '@tarojs/taro'
import {Provider} from '@tarojs/redux'
import Index from './pages/index'
import configStore from './store'
// eslint-disable-next-line import/first
import sa from 'sa-sdk-miniprogram'
import './app.less'

// eslint-disable-next-line import/first
import "taro-ui/dist/style/index.scss";
import "./assets/iconFont/icon.less";

const store = configStore()

sa.setPara({
  // 神策分析注册在APP全局函数中的变量名，在非app.js中可以通过getApp().sensors(你这里定义的名字来使用)
  name: 'sensors',
  // 如果要通过sdk自动获取openid，需要在神策分析中配置appid和appsercret，并在这里标志appid,不需要的话，不用填。
  appid: 'xxxxx',
  // 神策分析数据接收地址
  // server_url: 'https://xxxxx.datasink.xxxx/sa.gif?project=default&token=27eeee',
  server_url:'https://test-syg122.datasink.sensorsdata.cn/sa.gif',
  //默认使用队列发数据时候，两条数据发送间的最大间隔
  send_timeout: 1000,
  // 发送事件的时间使用客户端时间还是服务端时间
  use_client_time: false,
  // 是否允许控制台打印查看埋点数据（建议开启查看）
  show_log: true,
  // 是否允许修改onShareMessage里return的path，用来增加（用户id，分享层级，当前的path），在app onshow中自动获取这些参数来查看具体分享来源，层级等
  allow_amend_share_path : true,
  // 是否自动采集如下事件（建议开启）
  autoTrack:{
    appLaunch:true, //是否采集 $MPLaunch 事件，true 代表开启。
    appShow:true, //是否采集 $MPShow 事件，true 代表开启。
    appHide:true, //是否采集 $MPHide 事件，true 代表开启。
    pageShow:true, //是否采集 $MPViewScreen 事件，true 代表开启。
    pageShare:true //是否采集 $MPShare 事件，true 代表开启。
  },
  // 是否集成了插件！重要！
  is_plugin: false
});

sa.init();

const App: Taro.FC = () => {
  sa.sensors.track('click',{
    name: '点击'
  });
  return (
    <Provider store={store}>
      <Index />
    </Provider>
  )
}

App.config = {
  pages: [
    "pages/index/index",
    "pages/login/index",
  ],
  window: {
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#d43c33',
    navigationBarTitleText: '滔滔云音乐',
    navigationBarTextStyle: 'black'
  },
  requiredBackgroundModes: ["audio"],
  debug: false,
  subPackages: [
    {
      "root": "pages/details",
      "pages": [
        "pages/djprogramListDetail/index",
        "pages/playListDetail/index",
        "pages/songDetail/index",
        "pages/videoDetail/index",
      ]
    },
    {
      "root": "pages/user",
      "pages": [
        "pages/user/index",
        "pages/userFans/index",
        "pages/userFocus/index",
        "pages/userEvents/index",
      ]
    },
    {
      "root": "pages/search",
      "pages": [
        "pages/searchResult/index",
        'pages/search/index',
      ]
    },
    {
      "root": "pages/dailyRecommend",
      "pages": [
        "pages/dailyRecommend/index",
      ]
    },
    {
      "root": "pages/recentPlay",
      "pages": [
        "pages/recentPlay/index",
      ]
    }
  ],
}

Taro.render(<App />, document.getElementById('app'))
