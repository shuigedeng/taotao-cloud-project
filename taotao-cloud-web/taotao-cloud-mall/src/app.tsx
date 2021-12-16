import { Component } from 'react'
import './app.less'
import sr from 'sr-sdk-wxapp'
import { Provider } from 'react-redux'
import configStore from './store'
import React from 'react'

// import 'taro-ui/dist/style/index.scss'

/**
 * 有数埋点SDK 默认配置
 * 使用方法请参考文档 https://mp.zhls.qq.com/youshu-docs/develop/sdk/Taro.html
 * 如对有数SDK埋点接入有任何疑问，请联系微信：sr_data_service
 */
sr.init({
  /**
   * 有数 - ka‘接入测试用’ 分配的 app_id，对应的业务接口人负责
   */
  token: 'bi6cdbda95ae2640ec',

  /**
   * 微信小程序appID，以wx开头
   */
  appid: 'touristappid',

  /**
   * 如果使用了小程序插件，需要设置为 true
   */
  usePlugin: false,

  /**
   * 开启打印调试信息， 默认 false
   */
  debug: true,

  /**
   * 建议开启-开启自动代理 Page， 默认 false
   * sdk 负责上报页面的 browse 、leave、share 等事件
   * 可以使用 sr.page 代替 Page(sr.page(options))
   * 元素事件跟踪，需要配合 autoTrack: true
   */
  proxyPage: true,
  /**
   * 建议开启-开启组件自动代理， 默认 false
   * sdk 负责上报页面的 browse 、leave、share 等事件
   */
  proxyComponent: true,
  // 建议开启-是否开启页面分享链路自动跟踪
  openSdkShareDepth: true,
  // 建议开启-元素事件跟踪，自动上报元素事件，入tap、change、longpress、confirm
  autoTrack: true,
  installFrom: 'Taro@v3'
})

const store = configStore()

class App extends Component {

  componentDidMount () {}

  componentDidShow () {}

  componentDidHide () {}

  componentDidCatchError () {}

  // this.props.children 是将要会渲染的页面
  render () {
    return (
      <Provider store={store}>
        {this.props.children}
        </Provider>
    )
  }
}
export default App
