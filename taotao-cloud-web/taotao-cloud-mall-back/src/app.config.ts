export default {
  pages: [
    "pages/home/index",
    "pages/cate/index",
    // "pages/cart/index",
    // "pages/my/index",
  ],
  window: {
    backgroundTextStyle: "light",
    navigationBarBackgroundColor: "#d43c33",
    navigationBarTitleText: "滔滔商城",
    navigationBarTextStyle: "black",
  },
  requiredBackgroundModes: ["audio"],
  debug: false,
  rn: {
    screenOptions: {
      // 设置页面的options，参考https://reactnavigation.org/docs/stack-navigator/#options
      shadowOffset: { width: 0, height: 0 },
      borderWidth: 0,
      elevation: 0,
      shadowOpacity: 1,
      borderBottomWidth: 0,
    },
  },
  permission: {
    "scope.userLocation": {
      desc: "获取地理位置信息的用途描述",
    },
    "scope.record": {
      desc: "授权scope.record",
    },
    "scope.userInfo": {
      desc: "授权scope.userInfo",
    },
  },
  tabBar: {
    list: [
      {
        text: "首页",
        pagePath: "pages/home/index",
        selectedIconPath: "./assets/images/tab-home-current.png",
        iconPath: "./assets/images/tab-home.png",
      },
      {
        text: "分类",
        pagePath: "pages/cate/index",
        selectedIconPath: "./assets/images/tab-cate-current.png",
        iconPath: "./assets/images/tab-cate.png",
      },
      // {
      //   text: "购物车",
      //   pagePath: "pages/cart/index",
      //   selectedIconPath: "./assets/images/tab-cart-current.png",
      //   iconPath: "./assets/images/tab-cart.png"
      // },
      // {
      //   text: "我的",
      //   pagePath: "pages/my/index",
      //   selectedIconPath: "./assets/images/tab-my-current.png",
      //   iconPath: "./assets/images/tab-my.png"
      // }
    ],
  },
  subPackages: [
    // {
    //   "root": "pages/details",
    //   "pages": [
    //     "pages/djprogramListDetail/index",
    //     "pages/playListDetail/index",
    //     "pages/songDetail/index",
    //     "pages/videoDetail/index",
    //   ]
    // },
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
