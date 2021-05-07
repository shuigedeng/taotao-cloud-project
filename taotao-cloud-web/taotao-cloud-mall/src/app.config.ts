export default {
  pages: [
    // 首页
    "pages/home/index",
    // 分类
    "pages/classify/index",
    // 购物车
    "pages/cart/index",
    "pages/home_bak/index",
    "pages/ucenter/index",
    "pages/lifeCircle/index",
  ],
  // window: {
  //   backgroundTextStyle: "dark",
  //   navigationBarBackgroundColor: "#d43c33",
  //   navigationBarTitleText: "滔滔商城",
  //   navigationBarTextStyle: "black",
  //   backgroundColor: '#b6a7a6',
  //   enablePullDownRefresh: true
  // },
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
        pagePath: "pages/home/index",
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
};
