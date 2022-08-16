import  sensors from 'sa-sdk-miniprogram'
import Taro from '@tarojs/taro'

sensors.setPara({
  // 神策分析注册在APP全局函数中的变量名，在非app.js中可以通过getApp().sensors(你这里定义的名字来使用)
  name: 'sensors',
  // 如果要通过sdk自动获取openid，需要在神策分析中配置appid和appsercret，并在这里标志appid,不需要的话，不用填。
  appid: '',
  // 神策分析数据接收地址
  // server_url: 'https://xxxxx.datasink.xxxx/sa.gif?project=default&token=27eeee',
  server_url: 'https://log.taotaocloud.top/sa.gif',
  //默认使用队列发数据时候，两条数据发送间的最大间隔
  send_timeout: 1000,
  // 发送事件的时间使用客户端时间还是服务端时间
  use_client_time: false,
  // 是否允许控制台打印查看埋点数据（建议开启查看）
  show_log: true,
  // 是否允许修改onShareMessage里return的path，用来增加（用户id，分享层级，当前的path），在app onshow中自动获取这些参数来查看具体分享来源，层级等
  allow_amend_share_path: true,
  // preset_properties: { location: { type:  'gcj02' }},
  preset_properties: { location: true},
  source_channel: [],
  // 是否自动采集如下事件（建议开启）
  autoTrack: {
    appLaunch: true, // 默认为 true，false 则关闭 $MPLaunch 事件采集
    appShow: true, // 默认为 true，false 则关闭 $MPShow 事件采集
    appHide: true, // 默认为 true，false 则关闭 $MPHide 事件采集
    pageShow: true, // 默认为 true，false 则关闭 $MPViewScreen 事件采集
    pageShare: true, // 默认为 true，false 则关闭 $MPShare 事件采集
    mpClick: true, // 默认为 false，true 则开启 $MPClick 事件采集
    mpFavorite: true // 默认为 true，false 则关闭 $MPAddFavorites 事件采集
  },
  // 是否集成了插件！重要！
  is_plugin: true,
  is_persistent_save: {
    share: false,
    utm: false
  },
  framework: {taro: Taro}
});


export default sensors;

//   email: 'xxx@xx',
//   favoriteFruits: ['苹果', '油桃'],
//   subscribers: 7277
// });

// sa.track('click', {
//   name: '点击'
// });

// 设置用户属性 subscribers 为 7277
// sa.setOnceProfile({
//   email: 'xxx@xx',
//   favoriteFruits: ['苹果', '油桃'],
//   subscribers: 7277
// });

// 设置用户订阅次数属性
// sa.incrementProfile({
//   subscribers: 5
// });

// 对用户订阅次数进行累加，此时在神策系统中查询 subscribers 属性值为 10
//     sa.incrementProfile({
//       subscribers: 5
//     });

// sa.track('login', {
//   event: 'USER_LOGIN'
// })
