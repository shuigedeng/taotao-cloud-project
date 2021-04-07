import React from "react";
import {InputNumber, Spin} from "antd";

type IProps = {
  data: Array<any>
}

const DetailRight: React.FC<IProps> = (props: IProps) => {
  let content = props.data.length ? <div>
    <p className="title">{props.data[0].content.title}</p>
    <p className="desc">{props.data[0].content.desc}</p>
    <div className="tm-sale-prop">
      <p className="tb-metatit">价格</p>
      <div style={{
        display: "flex",
        flexDirection: "row",
        alignItems: "center"
      }}>
        <em className="tm-yen">¥</em>
        <p className="price">{props.data[0].content.price}</p>
      </div>
    </div>
    <div className="tm-sale-prop">
      <p className="tb-metatit">运费</p>
      {/*<p>广东东莞至 厦门<Icon type="down"/>思明区 厦港街道<Icon type="down"/>快递: 0.00</p>*/}
    </div>
    <div className="tm-ind-panel">
      <div style={{
        display: "flex",
        flexDirection: "row",
        alignItems: "center",
        marginLeft: 20,
        marginRight: 20
      }}>
        <p className="tb-metatit">月销量</p>
        <span style={{
          marginLeft: 3,
          color: "#FF0036"
        }}>{props.data[0].content.sales}</span>
      </div>
      <div style={{
        display: "flex",
        flexDirection: "row",
        alignItems: "center",
        marginLeft: 20,
        marginRight: 20
      }}>
        <p className="tb-metatit">累计评价</p>
        <span style={{
          marginLeft: 3,
          color: "#FF0036"
        }}>{props.data[0].content.comment}</span>
      </div>
      <div style={{
        display: "flex",
        flexDirection: "row",
        alignItems: "center",
        marginLeft: 20,
        marginRight: 20
      }}>
        <p className="tb-metatit">送天猫积分</p>
        <span style={{
          marginLeft: 3,
          color: "#280"
        }}>{props.data[0].content.integral}</span>
      </div>
    </div>
    <div className="tm-sale-prop">
      <p className="tb-metatit">机身颜色</p>
      <ul style={{
        display: "flex",
        flexDirection: "row",
        alignItems: "center",
        flexWrap: "wrap"
      }}>
        {props.data[0].content.body_color.map((item: any, index: number) => {
          return (
              <li key={index} style={{
                height: 40,
                border: "1px solid #b8b7bd",
                boxSizing: "content-box",
                marginLeft: 5,
                marginRight: 5
              }}>
                <img src={item.img_src} alt=""/>
                <span style={{
                  paddingLeft: 9,
                  paddingRight: 9
                }}>{item.text} </span>
              </li>
          )
        })}
      </ul>
    </div>
    <div className="tm-sale-prop">
      <p className="tb-metatit">套餐类型</p>
      <ul style={{
        display: "flex",
        flexDirection: "row",
        alignItems: "center"
      }}>
        {props.data[0].content.type.map((item: any, index: number) => {
          return (
              <li key={index} style={{
                height: 32,
                paddingTop: 5,
                paddingLeft: 9,
                paddingRight: 9,
                border: "1px solid #b8b7bd",
                marginLeft: 5,
                marginRight: 5
              }}>{item}</li>
          )
        })}
      </ul>
    </div>
    <div className="tm-sale-prop">
      <p className="tb-metatit">存储容量</p>
      <ul style={{
        display: "flex",
        flexDirection: "row",
        alignItems: "center"
      }}>
        {props.data[0].content.capacity.map((item: any, index: number) => {
          console.log(item)
          return (
              <li key={index} style={{
                height: 32,
                paddingTop: 5,
                paddingLeft: 9,
                paddingRight: 9,
                border: "1px solid #b8b7bd",
                marginLeft: 5,
                marginRight: 5
              }}>64GB</li>
          )
        })}
      </ul>
    </div>
    <div className="tm-sale-prop">
      <p className="tb-metatit">版本类型</p>
      <ul style={{
        display: "flex",
        flexDirection: "row",
        alignItems: "center"
      }}>
        {props.data[0].content.version.map((item: any, index: number) => {
          return (
              <li key={index} style={{
                height: 32,
                paddingTop: 5,
                paddingLeft: 9,
                paddingRight: 9,
                border: "1px solid #b8b7bd",
                marginLeft: 5,
                marginRight: 5
              }}>{item}</li>
          )
        })}
      </ul>
    </div>
    <div className="tm-sale-prop">
      <p className="tb-metatit">数量</p>
      <InputNumber min={1} defaultValue={1}/>件
      <p style={{
        marginLeft: 20
      }}>
        库存890件
      </p>
    </div>
    <div className="tb-action">
      <div style={{
        marginLeft: 10,
        marginRight: 10
      }}>
        <a style={{
          display: "inline-block",
          width: 180,
          height: 40,
          border: "1px solid #FF0036",
          textAlign: "center",
          lineHeight: "40px",
          color: "#FF0036"
        }}>立即购买</a>
      </div>
      <div style={{
        width: 180,
        height: 40,
        textAlign: "center",
        lineHeight: "40px",
        backgroundColor: "#FF0036",
        color: "white",
        marginRight: 10,
        marginLeft: 10,
        position: "relative"
      }}>
        {/*<Icon type="shopping-cart" style={{*/}
        {/*  position: "absolute",*/}
        {/*  top: 13,*/}
        {/*  left: 40*/}
        {/*}}/>*/}
        <a style={{
          height: 40,
          width: 180,
          color: "white",
          lineHeight: "40px"
        }}>加入购物车</a>
      </div>
    </div>
  </div> : <Spin tip="Loading..."/>

  return (
      <div style={{marginTop: 10, marginLeft: 10}}>
        {content}
      </div>
  )
}

//商品详情右边模块
export default DetailRight
