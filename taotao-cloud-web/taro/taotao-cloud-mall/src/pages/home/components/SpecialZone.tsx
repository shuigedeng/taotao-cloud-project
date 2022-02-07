import {Image, Text, View} from "@tarojs/components";
import React from "react";
import Taro from "@tarojs/taro";
import {Item} from "@/api/product/model";

interface IProps {
  imageUrl: string;
  projectItems2: Item[];
  projectItems3: Item[];
}

const SpecialZone: Taro.FC<IProps> = (props) => {
  // 专题
  const handleProject = (id) => {
    Taro.navigateTo({
      url: `../project/index?id=${id}`
    });
  }

  return (
    <View className="SpecialZone">
      <View
        className="WithinSpecialZone"
        onClick={handleProject.bind(this, 1)}
      >
        <View className="TodaySpecial">
          <View className="TodaySpecialCode">
            <Text className="TodaySpecialTitle">每周新品</Text>
            <Text className="TodaySpecialLittle">低至一元起</Text>
          </View>
          <Text className="TodaySpecialCon">精选低价 持续热销</Text>
        </View>
        <View className="specialLeftBox">
          <Image src={props.imageUrl} className="leftSpecial"/>
        </View>
      </View>
      <View className="WithinSpecialZone2">
        <View
          className="specialLeftBox1"
          onClick={handleProject.bind(this, 2)}
        >
          <View className="TodaySpecial">
            <View className="TodaySpecialCode">
              <Text className="TodaySpecialTitle">品牌主打</Text>
              <Text className="TodaySpecialLittle SpecialCoupon">领券超优惠</Text>
            </View>
            <Text className="TodaySpecialBrand">品牌特色 味蕾释放</Text>
          </View>
          <View className="SpecialZoneImg">
            <Image src={props.projectItems2[0] && props.projectItems2[0].imageUrl}
                   className="leftSpecial1"/>
            <Image src={props.projectItems2[1] && props.projectItems2[1].imageUrl}
                   className="leftSpecial1"/>
          </View>
        </View>
        <View className='divider'/>
        <View
          className="specialLeftBox2"
          onClick={handleProject.bind(this, 3)}
        >
          <View className="TodaySpecial">
            <View className="TodaySpecialCode">
              <Text className="TodaySpecialTitle">能量必备</Text>
            </View>
            <Text className="TodaySpecialBrand">能量加油站 精神整天</Text>
          </View>
          <View className="SpecialZoneImg">
            <Image src={props.projectItems3[0] && props.projectItems3[0].imageUrl}
                   className="leftSpecial1"/>
            <Image src={props.projectItems3[1] && props.projectItems3[1].imageUrl}
                   className="leftSpecial1"/>
          </View>
        </View>
      </View>
    </View>
  )
}

export default SpecialZone
