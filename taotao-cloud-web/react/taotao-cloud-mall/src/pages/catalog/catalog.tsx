import Taro, {usePullDownRefresh} from '@tarojs/taro';
import {Image, Navigator, ScrollView, Text, View} from '@tarojs/components';

import './index.less';
import {useDispatch, useSelector} from "react-redux";
import React, {useEffect} from "react";


const Index: Taro.FC<any> = (props) => {
  const goodsCount = useSelector(state => state.count.number);
  let dispatch = useDispatch();

  const getData = (cbk?) => {
    dispatch({type: 'catalog/getCatalogList'}).then(() => {
      cbk && cbk()
    })
    dispatch({type: 'goods/getGoodsCount'})
  }

  useEffect(() => {
    getData()
  }, [])

  usePullDownRefresh(() => {
    Taro.showNavigationBarLoading() //在标题栏中显示加载
    getData(() => {
      Taro.hideNavigationBarLoading() //完成停止加载
      Taro.stopPullDownRefresh() //停止下拉刷新
    });
  })

  const switchCate = (data) => {
    const {currentCategory} = props;
    if (currentCategory.id == data.id) {
      return false;
    }
    dispatch({type: 'catalog/getCurrentCategory', payload: data.id})

    // this.getCurrentCategory(event.currentTarget.dataset.id);
  }

  const {categoryList, currentCategory, currentSubCategory} = props;

  return (
    <View className='container'>
      <View className='search'>
        <Navigator url='/pages/search/search' className='input'>
          {/*<van-icon name='search' />*/}
          <Text className='txt'>商品搜索, 共{goodsCount}款好物</Text>
        </Navigator>
      </View>
      <View className='catalog'>
        <ScrollView className='nav' scrollY>
          {
            Array.isArray(categoryList) && categoryList.map(item => {
              return <View
                className={`item ${currentCategory.id == item.id ? 'active' : ''}`}
                key='id'
                onClick={() => switchCate(item)}
              >
                {item.name}
              </View>
            })
          }
        </ScrollView>
        <ScrollView className='cate' scrollY>
          <Navigator url='url' className='banner'>
            <Image className='image' src={currentCategory.picUrl}/>
            <View className='txt'>{currentCategory.frontName}</View>
          </Navigator>
          <View className='hd'>
            <Text className='line'/>
            <Text className='txt'>{currentCategory.name}分类</Text>
            <Text className='line'/>
          </View>
          <View className='bd'>
            {
              Array.isArray(currentSubCategory) && currentSubCategory.map((item, index) => {
                return <Navigator url={`/pages/category/category?id=${item.id}`}
                                  className={`item ${(index + 1) % 3 == 0 ? 'last' : ''}`}
                                  key={item.id}>
                  <Image className='icon' src={item.picUrl}/>
                  <Text className='txt'>{item.name}</Text>
                </Navigator>
              })
            }
          </View>
        </ScrollView>
      </View>
    </View>
  )
}

export default Index;
