import React from "react";
import {hot} from 'react-hot-loader';

import "./css/index.less"
import "./css/normalize.less"

import focus from "./upload/focus.jpg"
import pic1 from "./upload/pic1.jpg"
import pic2 from "./upload/pic2.jpg"
import pic3 from "./upload/pic3.jpg"
import pic4 from "./upload/pic4.jpg"
import pic5 from "./upload/pic5.jpg"
import pic6 from "./upload/pic6.jpg"

const CtripMobile: React.FC = () => {
  return (
      <>
        <div className="search-index">
          <div className="search">搜索:目的地/酒店/景点/航班号</div>
          <a href="#" className="user">我 的</a>
        </div>

        <div className="focus">
          <img src={focus} alt=""/>
        </div>

        <ul className="local-nav">
          <li>
            <a href="#" title="景点·玩乐">
              <span className="local-nav-icon-icon1"/>
              <span>景点·玩乐</span>
            </a>
          </li>
          <li>
            <a href="#" title="景点·玩乐">
              <span className="local-nav-icon-icon2"/>
              <span>景点·玩乐</span>
            </a>
          </li>
          <li>
            <a href="#" title="景点·玩乐">
              <span className="local-nav-icon-icon3"/>
              <span>景点·玩乐</span>
            </a>
          </li>
          <li>
            <a href="#" title="景点·玩乐">
              <span className="local-nav-icon-icon4"/>
              <span>景点·玩乐</span>
            </a>
          </li>
          <li>
            <a href="#" title="景点·玩乐">
              <span className="local-nav-icon-icon5"/>
              <span>景点·玩乐</span>
            </a>
          </li>
        </ul>

        <nav>
          <div className="nav-common">
            <div className="nav-items">
              <a href="#">海外酒店</a>
            </div>
            <div className="nav-items">
              <a href="#">海外酒店</a>
              <a href="#">特价酒店</a>
            </div>
            <div className="nav-items">
              <a href="#">海外酒店</a>
              <a href="#">特价酒店</a>
            </div>
          </div>
          <div className="nav-common">
            <div className="nav-items">
              <a href="#">海外酒店</a>
            </div>
            <div className="nav-items">
              <a href="#">海外酒店</a>
              <a href="#">特价酒店</a>
            </div>
            <div className="nav-items">
              <a href="#">海外酒店</a>
              <a href="#">特价酒店</a>
            </div>
          </div>
          <div className="nav-common">
            <div className="nav-items">
              <a href="#">海外酒店</a>
            </div>
            <div className="nav-items">
              <a href="#">海外酒店</a>
              <a href="#">特价酒店</a>
            </div>
            <div className="nav-items">
              <a href="#">海外酒店</a>
              <a href="#">特价酒店</a>
            </div>
          </div>
        </nav>

        <ul className="subnav-entry">
          <li>
            <a href="#">
              <span className="subnav-entry-icon"/>
              <span>电话费</span>
            </a>
          </li>
          <li>
            <a href="#">
              <span className="subnav-entry-icon"/>
              <span>电话费</span>
            </a>
          </li>
          <li>
            <a href="#">
              <span className="subnav-entry-icon"/>
              <span>电话费</span>
            </a>
          </li>
          <li>
            <a href="#">
              <span className="subnav-entry-icon"/>
              <span>电话费</span>
            </a>
          </li>
          <li>
            <a href="#">
              <span className="subnav-entry-icon"/>
              <span>电话费</span>
            </a>
          </li>
          <li>
            <a href="#">
              <span className="subnav-entry-icon"/>
              <span>电话费</span>
            </a>
          </li>
          <li>
            <a href="#">
              <span className="subnav-entry-icon"/>
              <span>电话费</span>
            </a>
          </li>
          <li>
            <a href="#">
              <span className="subnav-entry-icon"/>
              <span>电话费</span>
            </a>
          </li>
          <li>
            <a href="#">
              <span className="subnav-entry-icon"/>
              <span>电话费</span>
            </a>
          </li>
          <li>
            <a href="#">
              <span className="subnav-entry-icon"/>
              <span>电话费</span>
            </a>
          </li>
        </ul>

        <div className="sales-box">
          <div className="sales-hd">
            <h2>热门活动</h2>
            <a href="#" className="more">获取更多福利</a>
          </div>
          <div className="sales-bd">
            <div className="row">
              <a href="#">
                <img src={pic1} alt=""/>
              </a>
              <a href="#">
                <img src={pic2} alt=""/>
              </a>
            </div>
            <div className="row">
              <a href="#">
                <img src={pic3} alt=""/>
              </a>
              <a href="#">
                <img src={pic4} alt=""/>
              </a>
            </div>
            <div className="row">
              <a href="#">
                <img src={pic5} alt=""/>
              </a>
              <a href="#">
                <img src={pic6} alt=""/>
              </a>
            </div>
          </div>
        </div>
      </>
  )
}

export default (process.env.NODE_ENV === 'development' ? hot(module)(CtripMobile) : CtripMobile);
