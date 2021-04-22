import {Image, Text, View} from "@tarojs/components";
import React from "react";
import Taro from "@tarojs/taro";
import {AtIcon} from "taro-ui";
import {useDispatch} from "react-redux";
import {CartActionType} from "@/store/action/cartAction";
import {Item} from "@/api/product/model";

interface IProps {
  itemList: Item[];
}

const OptimizationBox: Taro.FC<IProps> = (props) => {
  const dispatch = useDispatch();

  const onOpenDoor = (code, name, number, price, unit, imageUrl, pointDiscountPrice, originalPrice, memberPrice) => {
    const data: any = {};
    data.itemId = code;
    data.name = name;
    data.number = number;
    data.price = price;
    data.unit = unit;
    data.imageUrl = imageUrl;
    data.pointDiscountPrice = pointDiscountPrice;
    data.originalPrice = originalPrice;
    data.memberPrice = memberPrice;
    console.log('data', data);

    dispatch({
      type: CartActionType.ADD_TO_CART_BY_CODE,
      payload: data
    });
  }

  // 跳转商品详情
  const handleDetails = (code) => {
    Taro.navigateTo({
      url: `../details/index?code=${code}`
    });
  }

  return (
    <View className="optimizationBox">
      <View className="optimizationLine">
        <View className="line"/>
        <Text className="optimizationText">为你推荐</Text>
        <View className="line"/>
      </View>
      <View className="items_box">
        {props.itemList && props.itemList.map(item => (
          item &&
          <View
            className="item_box"
            key={item.code}
          >
            <Image src={item.imageUrl} className="image"
                   onClick={handleDetails.bind(this, item.code)}/>
            <View className="item_bottom_box">
              <Text className="title"
                    onClick={handleDetails.bind(this, item.code)}>{item.name}</Text>
              <View className="item_right_box">
                <View className="priceBox">
                  <Text className="price">
                    ￥{(item.price / 100).toFixed(2)}/{item.unit}
                  </Text>
                  <Text className="originalPrice">
                    ￥{(item.originalPrice / 100).toFixed(2)}
                  </Text>
                </View>
                <View
                  className="shoppingCart"
                  onClick={onOpenDoor.bind(this,
                    item.code,
                    item.name,
                    1,
                    item.price,
                    item.unit,
                    item.imageUrl,
                    item.pointDiscountPrice,
                    item.originalPrice,
                    item.memberPrice,
                  )}
                >
                  <AtIcon value='shopping-cart' size='20' color='#fff'/>
                </View>
              </View>
            </View>
          </View>
        ))}
      </View>
    </View>
  )
}

export default OptimizationBox
