import React from "react";
import {PlayCircleOutlined} from '@ant-design/icons';

const VideoItem: React.FC = (props: any) => {
  return (
      <div style={{position: "relative"}} className="videoItem">
        <PlayCircleOutlined className="videoIcon"/>
        <img src={props.data.imgSrc} style={{
          marginBottom: 30
        }} alt=""/>
        <p className="videoName" style={{
          textAlign: "center",
          color: "#333"
        }}>{props.data.name}</p>
        <p style={{
          textAlign: "center",
          color: "#b0b0b0"
        }}>{props.data.desc}</p>
      </div>
  )
}

export default VideoItem
