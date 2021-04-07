import React from "react";
import {hot} from 'react-hot-loader';

import "./css/index.less"

import ad1 from "./upload/ad1.gif"
import ad2 from "./upload/ad2.gif"
import ad3 from "./upload/ad3.gif"

import nav1 from "./upload/nav1.png"

const SuningMobile: React.FC = () => {
  return (
      <>
        <div className="search-content">
          <a href="/#" className="classify"/>
          <div className="search">
            <form action="">
              <input type="search" value="厨卫保暖季 哒哒哒"/>
            </form>
          </div>
          <a href="/#" className="login">登录</a>
        </div>

        <div className="banner">
          <img src="upload/banner.gif" alt=""/>
        </div>

        <div className="ad">
          <a href="/#"><img src={ad1} alt=""/></a>
          <a href="/#"><img src={ad2} alt=""/></a>
          <a href="/#"><img src={ad3} alt=""/></a>
        </div>

        <nav>
          <a href="/#">
            <img src={nav1} alt=""/>
            <span>爆款手机</span>
          </a>
          <a href="/#">
            <img src={nav1} alt=""/>
            <span>爆款手机</span>
          </a>
          <a href="/#">
            <img src={nav1} alt=""/>
            <span>爆款手机</span>
          </a>
          <a href="/#">
            <img src={nav1} alt=""/>
            <span>爆款手机</span>
          </a>
          <a href="/#">
            <img src={nav1} alt=""/>
            <span>爆款手机</span>
          </a>
          <a href="/#">
            <img src={nav1} alt=""/>
            <span>爆款手机</span>
          </a>
          <a href="/#">
            <img src={nav1} alt=""/>
            <span>爆款手机</span>
          </a>
          <a href="/#">
            <img src={nav1} alt=""/>
            <span>爆款手机</span>
          </a>
          <a href="/#">
            <img src={nav1} alt=""/>
            <span>爆款手机</span>
          </a>
          <a href="/#">
            <img src={nav1} alt=""/>
            <span>爆款手机</span>
          </a>
        </nav>
      </>
  )
}

export default (process.env.NODE_ENV === 'development' ? hot(module)(SuningMobile) : SuningMobile)
