// @ts-ignore
import sensors from 'sa-sdk-javascript'

sensors.init({
  name: 'sensorsdata',
  server_url: 'http://106.13.201.31:9999/sa.gif',
  //表示是否开启单页面自动采集 $pageview 功能，SDK 会在 url 改变之后自动采集web页面浏览事件 $pageview。
  is_track_single_page: true,
  is_track_device_id: true,
  source_channel: ['hmsr'],
  source_type: {
    search: ['.baidu.com', '.google.'],
    social: ['.renren.com', '.kaixin001.com'],
    keyword: { baidu: ['wd', 'word', 'keyword'], sogou: 'query' }
  },
  show_log: true,
  //Web 视区停留相关参数
  //scrollmap: {},
  //点击图配置
  heatmap: {
    //是否开启点击图，default 表示开启，自动采集 $WebClick 事件，可以设置 'not_collect' 表示关闭。
    clickmap: 'default',
    //是否开启触达注意力图，not_collect 表示关闭，不会自动采集 $WebStay 事件，可以设置 'default' 表示开启。
    scroll_notice_map: 'default',
    loadTimeout: 3000,
    element_selector: 'not_use_id',
    renderRefreshTime: 1000,
    scroll_delay_time: 4000,
    scroll_event_duration: 18000,
    collect_tags: {
      //在原来的全埋点（采集 a、button、input 、textarea 标签）基础上新增对 div 标签的采集
      div: true
    }
  },
  //是否开启 $latest 最近一次相关事件属性采集以及配置$url作为公共属性，默认值为一个对象。
  preset_properties: {
    //是否采集 $latest_utm 最近一次广告系列相关参数，默认值 true。
    latest_utm: true,
    //是否采集 $latest_traffic_source_type 最近一次流量来源类型，默认值 true。
    latest_traffic_source_type: true,
    //是否采集 $latest_search_keyword 最近一次搜索引擎关键字，默认值 true。
    latest_search_keyword: true,
    //是否采集 $latest_referrer 最近一次前向地址，默认值 true。
    latest_referrer: true,
    //是否采集 $latest_referrer_host 最近一次前向地址，1.14.8 以下版本默认是true，1.14.8 及以上版本默认是 false，需要手动设置为 true 开启。
    latest_referrer_host: true,
    //是否采集 $latest_landing_page 最近一次落地页地址，默认值 false。
    latest_landing_page: true,
    //是否采集 $url 页面地址作为公共属性，默认值 false。
    url: true,
    //是否采集 $title 页面标题作为公共属性，默认值 false。
    title: true
  },
  is_track_latest: {
    utm: true,
    traffic_source_type: true,
    search_keyword: true,
    referrer: true,
    referrer_host: true,
    landing_page: true
  },
  // 表示不开启批量发送，设置为 true 表示开启批量采集。
  // 开启批量发送
  batch_send: false
  // batch_send: {
  //   datasend_timeout: 6000, //一次请求超过多少毫秒的话自动取消，防止请求无响应。
  //   send_interval: 6000, //间隔多少毫秒发一次数据。
  //   one_send_max_length: 20 //一次请求最大发送几条数据，防止数据太大。
  // },
})

// 设置之后，SDK 就会自动收集页面浏览事件，以及设置初始来源, 用于采集 $pageview 事件。
sensors.quick('autoTrack')
//sensors.getPresetProperties();;
//sensors.quick('autoTrackSinglePage');
//sensors.quick("autoTrackSinglePage",{platForm:"H5"});;

// 注册公共属性
// sensors.registerPage({
//   project: 'taotao-cloud-backend',
//   current_url: location.href
// })

export default sensors
