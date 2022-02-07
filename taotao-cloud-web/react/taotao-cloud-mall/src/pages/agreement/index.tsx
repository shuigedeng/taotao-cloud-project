import Taro from "@tarojs/taro";
import {RichText, View} from "@tarojs/components";
import "./index.less";
import {free} from "./service";
import React, {useEffect, useState} from "react";

interface IState {
  about: any;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({about: '',})

  useEffect(() => {
    const init = async () => {
      const {data} = await free();
      setState({
        about: data.config.value
      });
    }
    init()
  }, [])

  const {about} = state;
  return (
    <View className="index">
      <RichText nodes={about}/>
    </View>
  );
}


export default Index
