export default {
  pages: [
    "pages/home/index",
    "pages/ucenter/index",
    "pages/classify/index",
    "pages/cart/index",
    "pages/lifeCircle/index",
  ],
  window: {
    backgroundTextStyle: "dark",
    navigationBarBackgroundColor: "#d43c33",
    navigationBarTitleText: "滔滔商城",
    navigationBarTextStyle: "black",
    backgroundColor: '#b6a7a6',
    enablePullDownRefresh: true
  },
  requiredBackgroundModes: ["audio"],
  debug: false,
  permission: {
    "scope.userLocation": {
      desc: "获取地理位置信息的用途描述",
    }
  },
  tabBar: {
    custom: false,
    color: '#d43c33',
    selectedColor: '#252323',
    backgroundColor: '#33d438',
    borderStyle: 'black',
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
    {
      "root": "pages/login",
      "pages": [
        "index",
      ]
    },
    {
      "root": "pages/auth",
      "pages": [
        "login/index",
      ]
    },
    // {
    //   "root": "pages/user",
    //   "pages": [
    //     "pages/user/index",
    //     "pages/userFans/index",
    //     "pages/userFocus/index",
    //     "pages/userEvents/index",
    //   ]
    // },
    // {
    //   "root": "pages/search",
    //   "pages": [
    //     "pages/searchResult/index",
    //     'pages/search/index',
    //   ]
    // },
    // {
    //   "root": "pages/dailyRecommend",
    //   "pages": [
    //     "pages/dailyRecommend/index",
    //   ]
    // },
    // {
    //   "root": "pages/recentPlay",
    //   "pages": [
    //     "pages/recentPlay/index",
    //   ]
    // }
  ],
};
