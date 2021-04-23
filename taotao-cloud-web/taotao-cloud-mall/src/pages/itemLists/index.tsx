import Taro from "@tarojs/taro";
import {Image, Text, View} from "@tarojs/components";
import {handleQuery} from "./service";
import "./index.less";
import React, {useEffect, useState} from "react";
import usePullDownRefresh = Taro.usePullDownRefresh;
import useReachBottom = Taro.useReachBottom;
import useRouter = Taro.useRouter;

interface IState {
  choose: number;
  img?: any;
  items?: any;
  list?: any;
  currentPage: number;
  pageSize: number;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    choose: 0,
    img: 'https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/sorting.png',
    list: [],
    currentPage: 1,
    pageSize: 10,
  })
  const router = useRouter();

  useEffect(() => {
    const init = async () => {
      const {id, title} = router.params;
      Taro.setNavigationBarTitle({
        title: title
      });
      const {currentPage, pageSize} = state;
      const result = await handleQuery(id, currentPage, pageSize);
      console.log('result', result);

      const list = result.data.items.list;
      setState(prevState => {
        return {...prevState, list: list}
      })
    }
    init()
  }, [])

  usePullDownRefresh(async () => {
    const {id} = router.params;
    const {pageSize} = state;
    const result = await handleQuery(id, 1, pageSize);
    const list = result.data.items.list;
    setState(prevState => {
      return {...prevState, list: list, currentPage: 1}
    })

    setTimeout(() => {
      Taro.stopPullDownRefresh(); //停止下拉刷新
    }, 1000);
  })


  useReachBottom(async () => {

    const {currentPage, pageSize, list} = state;
    const {id} = router.params;
    const currentPageAdd = currentPage + 1;
    const resultItems = await handleQuery(id, currentPageAdd, pageSize);
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


  const handleDetails = (code) => {
    Taro.navigateTo({
      url: `../details/index?code=${code}`
    });
  }
  // 价格排序
  const priceSorted = () => {
    const {choose, list} = state;
    if (choose === 0 || choose === 2) {
      function ascending(property) {
        return function (a, b) {
          var value1 = a[property];
          var value2 = b[property];
          return value1 - value2;
        };
      }

      const ascendingArr = list.sort(ascending("scope"));
      setState(prevState => {
        return {
          ...prevState,
          choose: 1,
          img: 'https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/top_sorting.png',
          list: ascendingArr
        }
      })
    } else if (choose === 1) {
      function descending(property) {
        return function (a, b) {
          var value1 = a[property];
          var value2 = b[property];
          return value2 - value1;
        };
      }

      const descendingArr = list.sort(descending("scope"));
      setState(prevState => {
        return {
          ...prevState,
          choose: 2,
          img: 'https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/bottom_sorting.png',
          list: descendingArr
        }
      })
    }
  }

  const {img, list} = state;
  return (
    <View className="index">
      {/* 排序选项 */}
      <View className="sortingOptions">
        {/* 价格排序 */}
        <View onClick={priceSorted} className="price_stort">
          <Text className="sorted_text">价格</Text>
          <Image src={img} className="arrow"/>
        </View>
        {/* 占位排序（无用）  */}
        <View className="price_stort">
          <Text className="sorted_text">筛选</Text>
          <Image
            src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/screening.png'
            className="ortherIcon"
          />
        </View>
        <Image
          src="https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/search_classification.png"
          className="ortherIcon"/>
      </View>
      {/* 底部商品 */}
      <View className="items_box">
        {list.map(item => (
          <View
            className="item_box"
            key={item.code}
            onClick={handleDetails.bind(this, item.code)}
          >
            <Image src={item.imageUrl} className="image"/>
            <View className="item_bottom_box">
              <Text className="title">{item.name}</Text>
              <View className="priceLine">
                <Text className="price">
                  ￥{(item.price / 100).toFixed(2)}
                </Text>
                <Text className="originalPrice">
                  ￥{(item.originalPrice / 100).toFixed(2)}
                </Text>
              </View>
              <Text className="memberPrice">
                会员价:￥{(item.memberPrice / 100).toFixed(2)}
              </Text>
            </View>
          </View>
        ))}
      </View>
    </View>
  );
}


export default Index
