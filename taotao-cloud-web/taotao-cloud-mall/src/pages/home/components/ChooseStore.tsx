import {Image, Input, Text, View} from "@tarojs/components";
import React, {useState} from "react";
import scan from "@/assets/img/scan.png";
import Taro from "@tarojs/taro";
import {Banner} from "@/api/banner/model";

interface IProps {
  tabbarFix: boolean;
  statusBarHeight: number;
  storeName: string;
  bannerDataList?: Banner[];
  lookForward: () => {}
}

const ChooseStore: Taro.FC<IProps> = (props) => {
  let [state, setState] = useState<{ input: string; }>({
    input: ""
  })

  // 跳转附件商家列表
  const nearshore = () => {
    Taro.navigateTo({
      url: "../nearbystores/index"
    })
  };

  // 搜索框内容修改
  const handleOnChange = (e) => {
    setState(prevState => {
      return {...prevState, input: e.detail.value}
    })
  }

  // 搜索按钮
  const clickSearch = () => {
    Taro.navigateTo({
      url: `../searchPage/index?nameLike=${state.input}`
    });
  }

  return (
    <View
      className={props.tabbarFix ? 'absorption' : 'firstViewBox'}
      style={
        props.tabbarFix ?
          `height:${props.statusBarHeight - 20 + 115}Px;top:0;`
          :
          `top:${props.statusBarHeight}PX;`
      }
      id="topTabBar"
    >

      <View
        className='youDiDaoTitle'
        style={
          props.tabbarFix ?
            `display: flex;justify-content: space-between;margin-top: ${props.statusBarHeight - 20 + 33}PX;`
            : `display: flex;justify-content: space-between;margin: 9px;margin-top: 0;margin-left:15px;`
        }
      >
        <Text className="youDiDaoName">
          有地道门店平台
        </Text>
      </View>

      {/* 第一行地址扫码及取货 */}
      <View
        className={props.tabbarFix ? "firstAbsorption" : "firstLineView"}
        style={
          props.tabbarFix ?
            `display: flex;justify-content: space-between;margin: 9px;margin-left:15px;margin-top: ${props.statusBarHeight - 20 + 16}PX;`
            : "display: flex;justify-content: space-between;margin: 9px;margin-top: 0;margin-left:15px;"
        }
      >
        <View
          className="firstLineLeft"
          onClick={nearshore}
        >
          <Image
            src="https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/bottomIcon.png"
            className="storeIcon"/>
          <Text/>
          <Text className="chooseStore">
            {props.storeName ? props.storeName : '请选择店铺！'}
          </Text>
        </View>
        <View className="rightSearchBox">
          <Image
            src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/searchimg.png'
            className="searchImg"
          />
          <Input
            type="text"
            placeholder="搜索你想要的商品"
            className="input"
            value={state.input}
            onInput={handleOnChange}
            // disabled
            onConfirm={clickSearch}
          />
          <View className="firstLineLeft" onClick={props.lookForward}>
            <Image src={scan} className="scanIcon"/>
          </View>
        </View>
      </View>
      {props.tabbarFix ? null : (
        <View className='searchAscan'>
          <View className="rightSearchBox">
            <Image
              src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/searchimg.png'
              className="searchImg"
            />
            <Input
              type="text"
              placeholder="搜索你想要的商品"
              className="input"
              value={state.input}
              onInput={handleOnChange}
              // disabled
              onConfirm={clickSearch}
            />
          </View>
          <View className="firstLineLeft">
            <View className="firstLineLeft" onClick={props.lookForward}>
              <Image src={scan} className="scanIcon"/>
              <Text className="sweep">扫码</Text>
            </View>
          </View>
        </View>
      )}
    </View>
  )
}

export default ChooseStore
