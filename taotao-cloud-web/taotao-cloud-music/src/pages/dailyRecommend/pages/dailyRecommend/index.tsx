import Taro, {useEffect} from '@tarojs/taro'
import {View} from '@tarojs/components'
import api from '../../../../services/api'
import './index.less'

const Page: Taro.FC = () => {
  const getList = () => {
    api.get('/recommend/songs').then(({data}) => {
      console.log('songs =》', data)
    })
  }

  useEffect(() => {
    getList()
  }, []);

  return (
    <View className='daily_recommend_container'>
      每日推荐
    </View>
  )
}

Page.config = {
  navigationBarTitleText: '每日推荐'
}


export default Page
