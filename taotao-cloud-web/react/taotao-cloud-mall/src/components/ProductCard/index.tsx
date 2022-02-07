import React from "react";
import {Image, Text, View} from '@tarojs/components';
import './index.less';

type IProps = {
  width?: string
  className: string,
  src: string,
  price: number,
  title: string,
  originalPrice: number,
}

const ProductCard: Taro.FC<IProps> = (props) => {
  return (
    <View
      className={props.className + ' product-card'}
      style={{width: props.width, flex: `0 0 ${props.width}`}}
    >
      <Image
        style={{width: props.width, height: props.width}}
        className='img'
        src={props.src}
        mode='aspectFill'
      />
      <View className='title'>{props.title}</View>
      <View className='font20 lh1'>
        {props.price && <Text className='price'>{props.price}</Text>}
        {props.originalPrice && (
          <Text className='originalPrice'>{props.originalPrice}</Text>
        )}
      </View>
    </View>
  );
}

ProductCard.defaultProps = {
  width: '200px'
}

export default ProductCard
