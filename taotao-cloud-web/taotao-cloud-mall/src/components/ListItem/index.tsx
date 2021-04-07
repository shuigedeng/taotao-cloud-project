import React from "react";
import {View} from '@tarojs/components';
import './index.less';
import {FC} from "@tarojs/taro";

type IProps = {
  className: string
}

const ListItem: FC<IProps> = (props) => {
  return (
    <View className={props.className + ' list-item'}>
      <View className='flex row middle'>
        <View className='flex-1 flex row middle'>{props.children}</View>
        <View className='iconfont icon-angle-right font30 iconmore'/>
      </View>
    </View>
  );
}

export default ListItem
