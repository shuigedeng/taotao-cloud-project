export default {
  pages: [
    'pages/index/index'
  ],
  window: {
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#fff',
    navigationBarTitleText: 'WeChat',
    navigationBarTextStyle: 'black'
  },
  tabBar: {
    // custom: false,
    // color: '#d43c33',
    // selectedColor: '#252323',
    // backgroundColor: '#33d438',
    // borderStyle: 'black',
    // color: "#000000",
    // selectedColor: "#04304b",
    // backgroundColor: "#fff",
    list: [
      {
        text: "首页",
        pagePath: "pages/index/index",
        selectedIconPath: "./assets/images/tab-home-current.png",
        iconPath: "./assets/images/tab-home.png",
      },
      {
        text: "分类",
        pagePath: "pages/index/index",
        selectedIconPath: "./assets/images/tab-home-current.png",
        iconPath: "./assets/images/tab-home.png",
      },
      {
        text: "品质生活圈",
        pagePath: "pages/index/index",
        selectedIconPath: "./assets/images/tab-home-current.png",
        iconPath: "./assets/images/tab-home.png",
      },
      // {
      //   text: "购物车",
      //   pagePath: "pages/cart/index",
      //   selectedIconPath: "./assets/images/tab-cart-current.png",
      //   iconPath: "./assets/images/tab-cart.png"
      // },
      // {
      //   text: "我的",
      //   pagePath: "pages/ucenter/index",
      //   selectedIconPath: "./assets/images/tab-my-current.png",
      //   iconPath: "./assets/images/tab-my.png"
      // }
    ],
  },
}
