import React from "react";
import "../../../style/homemain/video.less";

const HotItem: React.FC = (props: any) => {
  return (
      <div style={{
        width: 296
      }} className="videoItem">
        <img style={{
          width: 296,
          height: 220,
          marginBottom: 28
        }} src={props.data.imgSrc} alt=""/>
        <p style={{
          width: 240,
          height: 72,
          margin: "0 28px 22px 28px",
          color: "#333",
          fontSize: "14px",
          overflow: "hidden"
        }}>
          {props.data.comment}
        </p>
        <p style={{
          color: "#b0b0b0",
          fontSize: "12px",
          margin: "0 28px 8px"
        }}>来自 {props.data.commUser} 的评价</p>
        <div style={{
          display: "flex",
          marginLeft: 28,
          marginRight: 28
        }}>
          <p style={{
            fontSize: 14,
            fontWeight: 400,
            color: "#333"
          }}>{props.data.goodName}</p>
          <span style={{
            color: "#e0e0e0",
            marginLeft: 5,
            marginRight: 5
          }}>|</span>
          <p style={{
            color: "#ff6700"
          }}>{props.data.price}元</p>
        </div>
      </div>
  )
}
export default HotItem
