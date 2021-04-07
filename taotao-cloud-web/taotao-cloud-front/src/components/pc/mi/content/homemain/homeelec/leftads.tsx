import React from "react";
import "../../../style/homemain/video.less";

type IProps = {
  ads: Array<any>
}

const LeftADS: React.FC<IProps> = (props: IProps) => {
  // const ads = {
  //   display: "flex",
  //   flexDirection: "column",
  //   flexWrap: "wrap",
  //   width: "300px"
  // }
  let w = props.ads.length ? 234 : 0;

  return (
      <div style={{width: w}}>{
        props.ads.map((item: any, index: number) => {
          return (
              <img className="videoItem"
                   key={index}
                   style={{
                     width: w,
                     marginBottom: "10px"
                   }}
                   src={item}
                   alt=""/>
          )
        })
      }</div>
  )
}

export default LeftADS
