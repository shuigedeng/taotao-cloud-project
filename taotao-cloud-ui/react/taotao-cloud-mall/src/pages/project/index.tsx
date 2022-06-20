import Taro from "@tarojs/taro";
import {Image, Text, View} from "@tarojs/components";
import "./index.less";
import {handleQuery} from "./service";
import {projects} from "../home/service";
import React, {useEffect, useState} from "react";
import useReachBottom = Taro.useReachBottom;
import useRouter = Taro.useRouter;

interface IState {
  list?: any;
  currentPage: number;
  pageSize: number;
  projectImg?: string;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    list: [],
    currentPage: 1,
    pageSize: 10,
    projectImg: '',
  })
  const router = useRouter();

  useEffect(() => {
    const init = async () => {
      const project = await projects();
      const myProject = project.data.projects;
      const {currentPage, pageSize} = state;
      const {id} = router.params;
      switch (id) {
        case '1':
          Taro.setNavigationBarTitle({
            title: "每周新品"
          });
          setState(prevState => {
            return {...prevState, projectImg: myProject[0].imageUrl}
          })
          break;
        case '2':
          Taro.setNavigationBarTitle({
            title: "品牌主打"
          });
          setState(prevState => {
            return {...prevState, projectImg: myProject[1].imageUrl}
          })
          break;
        case '3':
          Taro.setNavigationBarTitle({
            title: "能量必备"
          });
          setState(prevState => {
            return {...prevState, projectImg: myProject[2].imageUrl}
          })
          break;
        default:
          break;
      }
      const result = await handleQuery(currentPage, pageSize, id);
      const list = result.data.items.list;
      setState(prevState => {
        return {...prevState, list}
      })
    }
    init()
  }, [])

  useReachBottom(async () => {
    const {currentPage, pageSize, list} = state;
    const {id} = router.params;
    const currentPageAdd = currentPage + 1;
    const result = await handleQuery(currentPageAdd, pageSize, id);
    const {list: itemList} = result.data.items;
    if (itemList.length !== 0) {
      //上拉加载
      Taro.showLoading({
        title: '正在加载',
      })
      setState(prevState => {
        return {...prevState, currentPage: currentPageAdd}
      })
      if (itemList) {
        for (const iterator of itemList) {
          list.push(iterator);
        }
        setState(prevState => {
          return {...prevState, list}
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

  const handleDetails = code => {
    Taro.navigateTo({
      url: `../details/index?code=${code}`
    });
  }

  const getCoupons = () => {
    Taro.navigateTo({
      url: `../gifts/index`
    });
  }

  const {list, projectImg} = state;
  console.log('projectImg', projectImg);

  return (
    <View>
      <View
        className="topImg"
        onClick={getCoupons}
      >
        <Image
          src={projectImg}
        />
      </View>
      <View>
        {list.length > 0 ? (
          <View className="index">
            <View>
              {list.map(item => (
                <View className="item_box"
                      onClick={() => handleDetails(item.code)}
                >
                  <Image src={item.imageUrl} className="img"/>
                  <View className="item_box_right">
                    <Text className="item_box_right_title">{item.name}</Text>
                    <View>
                      <Text className="item_box_bottom_view_price">
                        ￥{item.price / 100}
                      </Text>
                      <Text className="item_box_bottom_view_originalPrice">
                        ￥{item.originalPrice / 100}
                      </Text>
                    </View>
                  </View>
                </View>
              ))}
            </View>
          </View>
        ) : (
          <View className="nullBox">
            <Image src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/takenGoods.png'
                   className="empty"/>
            <Text className="orederNullText">暂无商品！</Text>
          </View>
        )}
      </View>
    </View>
  );
}


export default Index
