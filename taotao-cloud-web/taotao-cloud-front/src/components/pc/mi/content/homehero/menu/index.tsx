import React, {useState} from "react";
import "../../../style/homehero/menu.less";
import menuData from "../../../data/mimenu.json";
import {Link} from "react-router-dom";
import {Spin} from "antd";
import {RightOutlined} from '@ant-design/icons';

const Menu: React.FC = () => {
  let [state, setState] = useState({
    index: 0,
    isShow: false
  })

  let menu = menuData.length ? <ul style={{
    backgroundColor: "rgba(0,0,0,0.6)",
    paddingTop: 20,
    paddingBottom: 20
  }}>{menuData.map((item, index) => {
    var defaultActiveKey = "/list/" + (index + 1);
    return (
        <li className="menu-item" key={index} onMouseOver={() => over(index)}>
          <Link style={{
            color: "white"
          }} to={defaultActiveKey}>{item.title}
            <RightOutlined/>
          </Link>
        </li>
    )
  })}</ul> : <Spin size="large" tip="Loading..."/>;

  let isW = parseInt(String(menuData[state.index].items.length / 6)) * 250;

  let w = isW < 900 ? isW + 250 : isW;

  let menuRight = state.isShow ? (menuData[state.index].items.length ? <ul style={{
    display: "flex",
    flexDirection: "column",
    flexWrap: "wrap",
    height: 462,
    width: w
  }}>{
    menuData[state.index].items.map((item, index) => {
      return (
          <li key={index} className="menu-right-item">
            <img style={{
              width: 40,
              height: 40,
              marginRight: "12px"
            }} src={item.imgSrc} alt=""/>
            <p style={{margin: 0}}>{item.name}</p>
          </li>
      )
    })
  }</ul> : <Spin size="large" tip="Loading..."/>) : "";

  const leave = () => {
    setState(prevState => {
      return {...prevState, isShow: false}
    })
  }

  const over = (index: number) => {
    setState(prevState => {
      return {...prevState, index: index, isShow: true}
    })
  }

  return (
      <div className="menu">
        <div className="site-category" onMouseLeave={leave}>
          {menu}
          <div className="menu-right">
            {menuRight}
          </div>
        </div>
      </div>
  )
}
export default Menu
