import React from "react";
import {hot} from 'react-hot-loader';

import "./css/index.less"

import close from "./images/close.png"
import logo from "./images/logo.png"
import banner from "./upload/banner.png"
import pic11 from "./upload/pic11.png"
import pic22 from "./upload/pic22.png"
import pic33 from "./upload/pic33.png"
import nav1 from "./upload/nav1.png"
import nav2 from "./upload/nav2.png"
import nav3 from "./upload/nav3.png"
import new1 from "./upload/new1.png"
import new2 from "./upload/new2.png"
import new3 from "./upload/new3.png"

const JDMobile: React.FC = () => {
  return (
      <>
        <header className="app">
          <ul>
            <li>
              <img src={close} alt=""/>
            </li>
            <li>
              <img src={logo} alt=""/>
            </li>
            <li>打开京东App，购物更轻松</li>
            <li>立即打开</li>
          </ul>
        </header>

        <div className="search-wrap">
          <div className="search-btn"/>
          <div className="search">
            <div className="jd-icon"/>
            <div className="sou"/>
          </div>
          <div className="search-login">登陆</div>
        </div>

        <div className="main-content">
          <div className="slider">
            <img src={banner} alt=""/>
          </div>

          <div className="brand">
            <div>
              <a href="/#">
                <img src={pic11} alt=""/>
              </a>
            </div>
            <div>
              <a href="/#">
                <img src={pic22} alt=""/>
              </a>

            </div>
            <div>
              <a href="/#">
                <img src={pic33} alt=""/>
              </a>

            </div>
          </div>
          <nav className="clearfix">
            <a href="">
              <img src={nav1} alt=""/>
              <span>京东超市</span>
            </a>

            <a href="">
              <img src={nav2} alt=""/>
              <span>京东超市</span>
            </a>

            <a href="">
              <img src={nav1} alt=""/>
              <span>京东超市</span>
            </a>

            <a href="">
              <img src={nav1} alt=""/>
              <span>京东超市</span>
            </a>

            <a href="">
              <img src={nav1} alt=""/>
              <span>京东超市</span>
            </a>

            <a href="">
              <img src={nav3} alt=""/>
              <span>京东超市</span>
            </a>

            <a href="">
              <img src={nav1} alt=""/>
              <span>京东超市</span>
            </a>

            <a href="">
              <img src={nav1} alt=""/>
              <span>京东超市</span>
            </a>

            <a href="">
              <img src={nav1} alt=""/>
              <span>京东超市</span>
            </a>

            <a href="">
              <img src={nav1} alt=""/>
              <span>京东超市</span>
            </a>

          </nav>
          <div className="news">
            <a href="/#">
              <img src={new1} alt=""/>
            </a>
            <a href="/#">
              <img src={new2} alt=""/>

            </a>
            <a href="/#">
              <img src={new3} alt=""/>
            </a>
          </div>
        </div>
      </>
  )
}

export default (process.env.NODE_ENV === 'development' ? hot(module)(JDMobile) : JDMobile)
