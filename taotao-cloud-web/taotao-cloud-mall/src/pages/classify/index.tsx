import Taro, {usePullDownRefresh, useReachBottom} from "@tarojs/taro";
import {Image, Input, Text, View,} from "@tarojs/components";
import {classify, items} from "./service";
import {AtIcon, AtTabs, AtTabsPane} from "taro-ui";
import "./index.less";
import React, {useEffect, useState} from "react";
import {Classify} from "@/pages/home";
import {useDispatch, useSelector} from "react-redux";
import {ICartState} from "@/store/state/cart";

interface IState {
  current: number;
  authorization: boolean;
  query: {
    data?: {
      classify: Classify[]
    };
    loading: boolean;
  };
  list?: any;
  input: string;
  currentPage: number;
  pageSize: number;
  tableId: number;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    current: 0,
    authorization: false,
    query: {
      data: {
        classify: [],
      },
      loading: false
    },
    list: [],
    input: "",
    currentPage: 1,
    pageSize: 10,
    tableId: 1,
  })

  const cartItems = useSelector<ICartState, any[]>(({cartItems}) => cartItems);
  const dispatch = useDispatch();

  useEffect(() => {
    // 购物车右上角图标
    if (cartItems.length === 0) {
      Taro.removeTabBarBadge({
        index: 2
      })
    } else {
      let sum = 0;
      let i;
      for (i in cartItems) {
        if (cartItems[i].checked) {
          sum += parseInt(cartItems[i].number);
        }
      }
      Taro.setTabBarBadge({
        index: 2,
        text: "" + sum + "",
      })
    }
  }, [])

  useEffect(() => {
    const initData = async () => {
      const {currentPage, pageSize} = state;
      const result = await classify();
      setState(prevState => {
        return {...prevState, query: {data: {classify: result.data}, loading: false}}
      })
      const id = result.data.classify[0].id;
      const resultItems = await items(id, currentPage, pageSize);
      const list = resultItems.data.items.list;
      setState(prevState => {
        return {...prevState, list: list}
      })
    }
    initData()
  }, [])

  usePullDownRefresh(async () => {
    const {pageSize, tableId} = state;
    const resultItems = await items(tableId, 1, pageSize);
    const list = resultItems.data.items.list;
    setState(prevState => {
      return {...prevState, list: list, currentPage: 1}
    })
    setTimeout(() => {
      Taro.stopPullDownRefresh(); //停止下拉刷新
    }, 1000);
  })

  useReachBottom(async () => {
    const {currentPage, pageSize, list, tableId} = state;
    const currentPageAdd = currentPage + 1;
    const resultItems = await items(tableId, currentPageAdd, pageSize);
    const {list: newList} = resultItems.data.items;
    if (newList.length !== 0) {
      //上拉加载
      Taro.showLoading({
        title: '正在加载',
      })
      setState(prevState => {
        return {...prevState, currentPage: currentPageAdd}
      })
      if (newList) {
        for (const iterator of newList) {
          list.push(iterator);
        }

        setState(prevState => {
          return {...prevState, list: list}
        })
      }
      setTimeout(function () {
        Taro.hideLoading()
      }, 1000)
    } else {
      setTimeout(function () {
        Taro.showToast({
          title: '已全部加载',
          icon: 'success',
          mask: true,
        })
      }, 10)
    }
  })

  // tab切换
  const handleClick = async (value) => {
    setState(prevState => {
      return {...prevState, current: value}
    })
    const tabList = state.query.data.classify;
    const id = tabList[value].id;
    setState(prevState => {
      return {...prevState, tableId: id}
    })
    const resultItems = await items(id, state.currentPage, state.pageSize);
    const list = resultItems.data.items.list;
    setState(prevState => {
      return {...prevState, list: list}
    })
  }

  // 加入购物车
  const onOpenDoor = (item) => {
    const data: any = {};
    data.itemId = item.code;
    data.name = item.name;
    data.number = 1;
    data.price = item.price;
    data.unit = item.unit;
    data.content = item.content;
    data.imageUrl = item.imageUrl;
    data.originalPrice = item.originalPrice;
    data.memberPrice = item.memberPrice;

    dispatch({
      type: 'common/addToCartByCode',
      payload: data
    });
  }

  // 跳转商品详情
  const handleDetails = (code) => {
    Taro.navigateTo({
      url: `../details/index?code=${code}`
    });
  }

  // 搜索按钮
  const clickSearch = () => {
    const {input} = state;
    Taro.navigateTo({
      url: `../searchPage/index?nameLike=${input}`
    });
  }

  // 搜索框内容修改
  const handleOnChange = (e) => {
    setState(prevState => {
      return {...prevState, input: e.detail.value}
    })
  }

  const tabList = state.query.data.classify;
  return (
    <View className="index">
      {/* 搜索框 */}
      <View className="rightSearchBox">
        <Image
          src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/searchimg.png'
          className="searchImg"
        />
        <Input
          type="text"
          placeholder="搜索你想要的商品"
          className="input"
          value={state.input}
          onInput={handleOnChange}
          onConfirm={clickSearch}
        />
      </View>
      <AtTabs
        current={state.current}
        className="attab"
        height="95vh;"
        tabDirection="vertical"
        tabList={tabList}
        onClick={handleClick}
      >
        {tabList.map((currentValue, index) => (
          <AtTabsPane
            tabDirection="vertical"
            current={state.current}
            index={index}
            key={currentValue.id}
          >
            {state.list.map(item => (
              <View key={item.code} className="goodBox">
                <Image src={item.imageUrl} className="img"
                       onClick={() => handleDetails(item.code)}/>
                <View className='rightBox'>
                  <View className='topText' onClick={() => handleDetails(item.code)}>
                    <Text className='goodName'>{item.name}</Text>
                  </View>
                  <Text className="goodName">{item.number}</Text>
                  <View className="item_right_box">
                    <View className="priceBox">
                      <Text className="price" onClick={() => handleDetails(item.code)}>
                        ￥{(item.price / 100).toFixed(2)}/{item.unit}
                      </Text>
                      <Text className="originalPrice">
                        ￥{(item.originalPrice / 100).toFixed(2)}
                      </Text>
                    </View>
                    <View className="shoppingCart" onClick={() => onOpenDoor(item)}>
                      <AtIcon value='shopping-cart' size='20' color='#fff'/>
                    </View>
                  </View>
                </View>
              </View>
            ))}
          </AtTabsPane>
        ))}
      </AtTabs>
    </View>
  );
}


export default Index
