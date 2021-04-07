import React from "react";
import "../../../style/homemain/video.less";

type IProps = {
  itemsData?: Array<any>
  itemIndex: number
}

const RightItem: React.FC<IProps> = (props: IProps) => {
  // var itemUl ={
  // 	display:"flex",
  // 	flexDirection:"row",
  // 	flexWrap:"wrap"
  // }

  let content = <ul className="itemUl">{
    props.itemsData && props.itemsData[props.itemIndex].map((item: any, index: number) => {
      return (
          <li key={index} className="itemLi videoItem" style={{
            paddingTop: 20,
            width: 232,
            height: 300,
            marginBottom: 30,
            marginLeft: 5,
            marginRight: 5,
            float: "left"
          }}>
            <div style={{textAlign: "center"}}>
              <img style={{width: 150, height: 150}} src={item.imgSrc} alt=""/>
              <p>{item.title}{props.itemIndex} </p>
              <p style={{
                color: "#b0b0b0",
                fontSize: 12
              }}>{item.desc}</p>
              <p style={{color: "#ff6700"}}>{item.price}元
                <del className="" style={{marginLeft: 8, color: "#b0b0b0"}}>{item.del}元</del>
              </p>
            </div>
          </li>
      )
    })
  }
  </ul>

  return (<div>
    {content}
  </div>)
}

export default RightItem
