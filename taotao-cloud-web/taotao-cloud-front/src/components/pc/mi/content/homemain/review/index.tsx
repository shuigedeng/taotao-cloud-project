import React from "react";
import HotItem from "./item";
import hotGooData from "../../../data/hotgood.json";

const ReviewIndex: React.FC = () => {
  return (
      <div>
        <h2 style={{
          fontSize: "22px",
          fontWeight: 500,
          color: "#333"
        }}>热评产品</h2>
        <div style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-around",
          alignItems: "center"
        }}>{
          hotGooData.map((item, index) => {
            return (
                // @ts-ignore
                <HotItem key={index} data={item}/>
            )
          })
        }</div>
      </div>
  )
}

export default ReviewIndex
