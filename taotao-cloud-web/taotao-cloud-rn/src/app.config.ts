export default {
  pages: [
    'pages/index/index',
    "pages/classify/index",
    "pages/cart/index",
    "pages/home/index",
    "pages/ucenter/index",
    "pages/lifeCircle/index",
  ],
  window: {
    backgroundTextStyle: "light",
    navigationBarBackgroundColor: "#fff",
    navigationBarTitleText: "滔滔商城",
    navigationBarTextStyle: "black",
    enablePullDownRefresh: true,
  },
  requiredBackgroundModes: ["audio"],
  navigateToMiniProgramAppIdList: [
    "wx4a96aca05249ba58",
  ],
  debug: false,
  permission: {
    "scope.userLocation": {
      desc: "获取地理位置信息的用途描述",
    }
  },
  tabBar: {
    // custom: false,
    // color: '#d43c33',
    // selectedColor: '#252323',
    // backgroundColor: '#33d438',
    // borderStyle: 'black',
    color: "#000000",
    selectedColor: "#04304b",
    backgroundColor: "#fff",
    list: [
      {
        text: "首页",
        pagePath: "pages/index/index",
        selectedIconPath: "./assets/images/tab-home-current.png",
        iconPath: "./assets/images/tab-home.png",
      },
      {
        text: "分类",
        pagePath: "pages/classify/index",
        selectedIconPath: "./assets/images/tab-home-current.png",
        iconPath: "./assets/images/tab-home.png",
      },
      {
        text: "品质生活圈",
        pagePath: "pages/lifeCircle/index",
        selectedIconPath: "./assets/images/tab-home-current.png",
        iconPath: "./assets/images/tab-home.png",
      },
      {
        text: "购物车",
        pagePath: "pages/cart/index",
        selectedIconPath: "./assets/images/tab-cart-current.png",
        iconPath: "./assets/images/tab-cart.png"
      },
      {
        text: "我的",
        pagePath: "pages/ucenter/index",
        selectedIconPath: "./assets/images/tab-my-current.png",
        iconPath: "./assets/images/tab-my.png"
      }
    ],
  },
  subPackages: [
    //{
    //  "root": "pages/login",
    //  "pages": [
    //    "index",
    //  ]
    //},
    //{
    //  "root": "pages/auth",
    //  "pages": [
    //    "login/index",
    // ]
    //},
  ],
  rn: {
    screenOptions:{
      shadowOffset: {width: 0, height: 0},
      borderWidth:0,
      elevation: 0,
      shadowOpacity: 1,
      borderBottomWidth: 0
    }
  }
}
