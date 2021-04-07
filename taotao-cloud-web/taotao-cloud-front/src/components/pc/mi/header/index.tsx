import React from "react";

import "../style/header/header.less";

const Index: React.FC = () => {
  let navList = ["小米商城", "MIUI", "IoT", "云服务", "水滴", "金融", "有品", "Select Region"]
  let sep = <span className='sep'>|</span>

  return (
      <div>
        <div className="site-topbar">
          <div className="container">
            {/*左边菜单*/}
            <div className="bar">
              <ul className="leftBar">{
                navList.map((item, index) => {
                  let sep = index < navList.length - 1 ? <span className='sep'>|</span> : ""
                  return (
                      <li key={index} className="leftBar_item">{item}{sep}</li>
                  )
                })
              }
              </ul>
            </div>
            <div className="bar_right">
              {/*右边菜单*/}
              <div className="topbar-info">
                <a className="nofollow" href="/#">登录{sep}</a>
                <a className="nofollow" href="/#">注册{sep}</a>
                <a className="nofollow" href="/#">消息通知{sep}</a>
              </div>
              {/*购物车*/}
              <div className="cart">
                <a className="nofollow" href="/#">
                  {/*<Icon className="nofollow" type="shopping-cart" style={{fontSize: "18px"}}/>购物车*/}
                  <span className="nofollow">(0)</span>
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
  )
}
export default Index
