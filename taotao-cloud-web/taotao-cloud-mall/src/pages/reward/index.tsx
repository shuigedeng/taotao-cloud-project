import Taro, { Component, Config } from "@tarojs/taro";
import { View, Image, Button, Canvas, OpenData } from "@tarojs/components";
import "./index.less";
import { userInfo } from "../orderDetails/service";
import drawQrcode from "weapp-qrcode";

interface IState {
  authorization: boolean;
  query: {
    data?: any;
  };
}
export default class certificates extends Component<null, IState>  {
  config: Config = {
    navigationBarTitleText: "邀请好友"
  };
  state = {
    authorization: false,
    query: {
      loading: true,
      data: null
    },
  };

  async componentWillMount() {
    const userResult = await userInfo();
    this.setState({
      query: userResult
    });
    const id = userResult.data.userInfo.id;

    drawQrcode({
      width: 140,
      height: 140,
      canvasId: "myQrcode",
      text: `{"userId":${id}}`
    });
  }

  render() {
    return (
      <View className="index">
        <View className="pickUp">
          <View className="userInformation">
            <View className="head">
              <OpenData type="userAvatarUrl" />
            </View>
            <View className="nameAndPhone">
              <OpenData type="userNickName" />
            </View>
          </View>

          <View className="ercode">
            <Canvas style="width: 140px; height: 140px;" canvas-id="myQrcode" />
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
}
