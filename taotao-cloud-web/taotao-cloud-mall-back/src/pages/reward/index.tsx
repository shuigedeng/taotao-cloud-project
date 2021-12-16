import Taro from "@tarojs/taro";
import {Button, Canvas, Image, OpenData, View} from "@tarojs/components";
import "./index.less";
import {userInfo} from "../orderDetails/service";
import drawQrcode from "weapp-qrcode";
import React, {useEffect, useState} from "react";

interface IState {
  authorization: boolean;
  query: {
    data?: any;
    loading: boolean
  };
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    authorization: false,
    query: {
      loading: true,
      data: null
    },
  })

  useEffect(() => {
    const init = async () => {
      const userResult = await userInfo();
      setState(prevState => {
        return {...prevState, query: userResult}
      })
      const id = userResult.data.userInfo.id;

      drawQrcode({
        width: 140,
        height: 140,
        canvasId: "myQrcode",
        text: `{"userId":${id}}`
      });
    }
    init()
  }, [])

  return (
    <View className="index">
      <View className="pickUp">
        <View className="userInformation">
          <View className="head">
            <OpenData type="userAvatarUrl"/>
          </View>
          <View className="nameAndPhone">
            <OpenData type="userNickName"/>
          </View>
        </View>

        <View className="ercode">
          <Canvas style="width: 140px; height: 140px;" canvas-id="myQrcode"/>
        </View>
        <View>
        </View>
      </View>
      <Button open-type="share" className="shareButton">
        <Image
          src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/fengxing.png'
          className="share"
        />
        微信分享
      </Button>
    </View>
  );
}


export default Index
