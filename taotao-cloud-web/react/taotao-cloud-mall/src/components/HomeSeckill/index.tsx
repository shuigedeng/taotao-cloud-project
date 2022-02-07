import {Image, ScrollView, View} from '@tarojs/components';
import ListItem from '../ListItem';
import ProductCard from '../ProductCard';
import './index.less';
import JsonData from '../../Json';

import seckillImg from "../../assets/images/temp/secskill-img.jpg"
import React, {useState} from "react";
import {FC} from "@tarojs/taro";

type IProps = {
  className: string
}

const Seckill: FC<IProps> = () => {
  let [state] = useState({
    list: JsonData.goodsList
  })

  let scrollTop = 0;
  const scrollStyle = {
    height: '235px',
    width: '100%',
  };

  return (
    <View className='home-seckill bgfff pb30'>
      <ListItem className='mt20'>
        <Image
          style={{width: '70px', height: '18px'}}
          mode='aspectFit'
          src={seckillImg}
        />
        <View className='font30 pl30 color999'>8点场</View>
      </ListItem>

      <ScrollView
        style={scrollStyle}
        className='scrollview'
        scrollX
        scrollWithAnimation
        scrollLeft={scrollTop}
      >
        <View className='list-container'>
          {state.list.map((e, index) => (
            <ProductCard
              className='ml30'
              width='100px'
              key={'key' + index}
              // @ts-ignore
              taroKey={index}
              title={e.title}
              src={e.image}
              price={e.price}
              // @ts-ignore
              originalPrice={e.originalPrice}
            />
          ))}
        </View>
      </ScrollView>
    </View>
  );
}

export default Seckill
