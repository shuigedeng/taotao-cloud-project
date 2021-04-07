import React, {useState} from "react";
import Title from "../homeelec/title";
import VideoItem from "./videoItem";
import {RightCircleOutlined} from '@ant-design/icons';
import "../../../style/homemain/video.less";
import videoData from "../../../data/video.json";

const VideoIndex: React.FC = () => {
  let [state, setState] = useState({
    moreColor: "#424242",
    iColor: "#b0b0b0"
  })
  const leave = () => {
    setState({
      moreColor: "#424242",
      iColor: "#b0b0b0"
    })
  }
  const over = () => {
    setState({
      moreColor: "#f87300",
      iColor: "#f87300"
    })
  }

  return (
      <div>
        <div style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-between",
          alignItems: "center"
        }}>
          // @ts-ignore
          <Title data={{"title": "视频", "tab": []}}/>
          <div onMouseOver={over} onMouseLeave={leave} style={{
            fontSize: 16,
            cursor: "pointer",
            color: state.moreColor
          }}>查看更多
            <RightCircleOutlined style={{
              width: 20,
              height: 20,
              color: state.iColor
            }}/>
          </div>
        </div>
        <div style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-around",
          alignItems: "center"
        }}>{
          videoData.map((item, index) => {
            return (
                // @ts-ignore
                <VideoItem key={index} data={item}/>
            )
          })
        }</div>
      </div>
  )
}

export default VideoIndex
