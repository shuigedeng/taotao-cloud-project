import {Image, View} from '@tarojs/components';
import Taro from '@tarojs/taro';
import React, {useEffect, useState} from 'react';
import './index.less';

interface IState {
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({})

  useEffect(() => {

  }, [])

  return (
    <View className="index">
      <Image src="https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/business.jpg"
             mode="widthFix" className='img'/>
    </View>
  );
}


export default Index
