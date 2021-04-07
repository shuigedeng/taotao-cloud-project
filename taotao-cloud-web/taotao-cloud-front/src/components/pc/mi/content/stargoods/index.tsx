import React from "react";
import "../../style/stargood/stargood.less";
import starData from "../../data/Star_single.json";
import {Link} from "react-router-dom";
import {Button, Spin} from "antd";

const ButtonGroup = Button.Group;

const StarGoods: React.FC = () => {

  let starItem = starData.length ? <ul className="star-goods">{
    starData.map((item, index) => {

      let url = "detail/" + item.id;
      return (
          <li key={index} className="rainbow-list-item" style={{
            borderWidth: "1px",
            borderTopStyle: "solid",
            borderColor: item.boderColor,
            width: 234,
            height: 340,
            marginRight: "14px"
          }}>
            <Link to={url}>
              <img style={{width: 160, height: 160}} src={item.imgSrc} alt=""/>
            </Link>
            <h3>
              <a style={{color: "#212121"}} href="/#">{item.title}</a>
            </h3>
            <p style={{color: "#b0b0b0", fontSize: "12px",}}>{item.desc}</p>
            <p style={{color: "#ff6709"}}>{item.price}</p>
          </li>
      )
    })
  }</ul> : <Spin/>

  return (
      <div>
        <div style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-between",
          alignItems: "center",
          marginTop: 10
        }}>
          <h2 className="star-title">小米明星单品</h2>
          <div>
            <ButtonGroup>
              <Button type="primary">
                {/*<Icon type="left"/>*/}
              </Button>
              <Button type="primary">
                {/*<Icon type="right"/>*/}
              </Button>
            </ButtonGroup>
          </div>
        </div>
        <div style={{
          overflow: "hidden"
        }}>
          {starItem}
        </div>
      </div>
  )
}


export default StarGoods
