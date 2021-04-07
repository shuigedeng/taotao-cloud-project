import {AtIcon, AtSearchBar} from 'taro-ui'
import Taro, {useDidShow, useEffect, useState} from '@tarojs/taro'
import CLoading from '../../../../components/CLoading'
import classnames from 'classnames'
import {Image, ScrollView, Text, View} from '@tarojs/components'
import {getKeywordInHistory, setKeywordInHistory} from '../../../../utils/common'
import api from '../../../../services/api'
import './index.less'


type PageState = {
  searchValue: string,
  hotList: Array<{
    searchWord: string,
    score: number,
    iconUrl: string,
    content: string,
    iconType: number
  }>,
  historyList: Array<string>
}


const Page: Taro.FC = () => {
  const [state, setState] = useState<PageState>({
    searchValue: '',
    hotList: [],
    historyList: []
  });

  useEffect(() => {
    getHotSearch()
  }, []);

  const getHotSearch = () => {
    api.get('/search/hot/detail', {})
      .then((res) => {
        if (res.data && res.data.data) {
          setState(prevState => {
            return {...prevState, hotList: res.data.data}
          })
        }
      })
  }

  useDidShow(() => {
    setState(prevState => {
      return {...prevState, historyList: getKeywordInHistory()}
    })
  })

  const searchTextChange = (val) => {
    setState(prevState => {
      return {...prevState, searchValue: val}
    })
  }

  const searchResult = () => {
    goResult(state.searchValue)
  }

  const goResult = (keywords) => {
    setKeywordInHistory(keywords)
    Taro.navigateTo({
      url: `/pages/search/pages/searchResult/index?keywords=${keywords}`
    })
  }

  const clearKeywordInHistory = () => {
    setState(prevState => {
      return {...prevState, historyList: []}
    })
    clearKeywordInHistory()
  }

  const {searchValue, hotList, historyList} = state

  return (
    <View className='search_container'>
      <AtSearchBar
        actionName='搜一下'
        value={searchValue}
        onChange={searchTextChange}
        onActionClick={searchResult}
        onConfirm={searchResult}
        focus={true}
        className='search__input'
        fixed={true}
      />
      <ScrollView className='search_content' scrollY>
        {
          historyList.length ? <View className='search__history'>
            <View className='search__history__title'>
              <Text className='search__history__title__label'>
                搜索历史
              </Text>
              <AtIcon prefixClass='fa' value='trash-o' size='20' color='#cccccc'
                      className='search__history__title__icon' onClick={clearKeywordInHistory}/>
            </View>
            <ScrollView className='search__history__list' scrollX>
              {
                historyList.map((keyword) =>
                  <Text className='search__history__list__item' key={keyword}
                        onClick={() => {
                          goResult(keyword)
                        }}>{keyword}</Text>)
              }
            </ScrollView>
          </View> : ''
        }
        <View className='search__hot'>
          <View className='search__history__title'>
            <Text className='search__history__title__label'>
              热搜榜
            </Text>
          </View>
          {
            hotList.length === 0 ? <CLoading/> : ''
          }
          <View className='search__hot__list'>
            {
              hotList.map((item, index) =>
                <View className='search__hot__list__item flex flex-align-center'
                      key={item.searchWord} onClick={() => {
                  goResult(item.searchWord)
                }}>
                  <View className={
                    classnames({
                      search__hot__list__item__index: true,
                      spec: index <= 2
                    })
                  }>
                    {index + 1}
                  </View>
                  <View className='search__hot__list__item__info'>
                    <View className="flex flex-align-center">
                      <Text className={
                        classnames({
                          search__hot__list__item__info__title: true,
                          spec: index <= 2
                        })
                      }>
                        {item.searchWord}
                      </Text>
                      <Text className='search__hot__list__item__info__score'>
                        {item.score}
                      </Text>
                      {
                        item.iconUrl ? <Image src={item.iconUrl} mode="widthFix" className={
                          classnames({
                            search__hot__list__item__info__icon: true,
                            spec: item.iconType === 5
                          })
                        }/> : ''
                      }
                    </View>
                    <View className='search__hot__list__item__info__desc'>
                      {item.content}
                    </View>
                  </View>
                </View>)
            }
          </View>
        </View>
      </ScrollView>
    </View>
  )
}

Page.config = {
  navigationBarTitleText: '搜索'
}

export default Page
