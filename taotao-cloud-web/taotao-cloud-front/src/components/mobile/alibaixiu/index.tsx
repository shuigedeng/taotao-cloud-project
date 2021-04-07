import React from "react";
import {hot} from 'react-hot-loader';

import "./bootstrap/css/bootstrap.min.css"
import "./css/index.less"

import logo from "./images/logo.png"
import lg from "./upload/lg.png"
import p1 from "./upload/1.jpg"
import p2 from "./upload/2.jpg"
import p3 from "./upload/3.jpg"
import p4 from "./upload/4.jpg"
import zgboke from "./upload/zgboke.jpg"

const AlibaixiuMobile: React.FC = () => {
  return (
      <>
        <div className="container">
          <div className="row">
            <header className="col-md-2">
              <div className="logo">
                <a href="#">
                  <img src={logo} alt="" className="hidden-xs"/>
                  <span className="visible-xs">阿里百秀</span>
                </a>
              </div>
              <div className="nav">
                <ul>
                  <li><a href="#" className="glyphicon glyphicon-camera">生活馆</a></li>
                  <li><a href="#" className="glyphicon glyphicon-picture">自然汇</a></li>
                  <li><a href="#" className="glyphicon glyphicon-phone">科技湖</a></li>
                  <li><a href="#" className="glyphicon glyphicon-gift">奇趣事</a></li>
                  <li><a href="#" className="glyphicon glyphicon-glass">美食杰</a></li>
                </ul>
              </div>
            </header>
            <article className="col-md-7">
              <div className="news clearfix">
                <ul>
                  <li>
                    <a href="#">
                      <img src={lg} alt=""/>
                      <p>阿里百秀</p>
                    </a>
                  </li>
                  <li>
                    <a href="#">
                      <img src={p1} alt=""/>
                      <p>奇了 成都一小区护卫长得像马云 市民纷纷求合影</p>
                    </a>
                  </li>

                  <li>
                    <a href="#">
                      <img src={p2} alt=""/>
                      <p>奇了 成都一小区护卫长得像马云 市民纷纷求合影</p>
                    </a>
                  </li>

                  <li>
                    <a href="#">
                      <img src={p3} alt=""/>
                      <p>奇了 成都一小区护卫长得像马云 市民纷纷求合影</p>
                    </a>
                  </li>

                  <li>
                    <a href="#">
                      <img src={p4} alt=""/>
                      <p>奇了 成都一小区护卫长得像马云 市民纷纷求合影</p>
                    </a>
                  </li>

                </ul>
              </div>
              <div className="publish">
                <div className="row">
                  <div className="col-sm-9">
                    <h3>生活馆 关于指甲的10个健康知识 你知道几个？</h3>
                    <p className="text-muted hidden-xs">alibaixiu 发布于 2015-11-23</p>
                    <p className="hidden-xs">指甲是经常容易被人们忽略的身体部位， 事实上从指甲的健康状况可以看出一个人的身体健康状况， 快来看看10个暗藏在指甲里知识吧！</p>
                    <p className="text-muted">阅读(2417)评论(1)赞 (18) <span className="hidden-xs">标签：健康 / 感染 / 指甲 / 疾病 / 皮肤 / 营养 / 趣味生活</span>

                    </p>
                  </div>
                  <div className="col-sm-3 pic hidden-xs">
                    <img src={p3} alt=""/>
                  </div>
                </div>
                <div className="row">
                  <div className="col-sm-9">
                    <h3>生活馆 关于指甲的10个健康知识 你知道几个？</h3>
                    <p className="text-muted hidden-xs">alibaixiu 发布于 2015-11-23</p>
                    <p className="hidden-xs">指甲是经常容易被人们忽略的身体部位， 事实上从指甲的健康状况可以看出一个人的身体健康状况， 快来看看10个暗藏在指甲里知识吧！</p>
                    <p className="text-muted">阅读(2417)评论(1)赞 (18) <span className="hidden-xs">标签：健康 / 感染 / 指甲 / 疾病 / 皮肤 / 营养 / 趣味生活</span>

                    </p>
                  </div>
                  <div className="col-sm-3 pic hidden-xs">
                    <img src={p3} alt=""/>
                  </div>
                </div>
                <div className="row">
                  <div className="col-sm-9">
                    <h3>生活馆 关于指甲的10个健康知识 你知道几个？</h3>
                    <p className="text-muted hidden-xs">alibaixiu 发布于 2015-11-23</p>
                    <p className="hidden-xs">指甲是经常容易被人们忽略的身体部位， 事实上从指甲的健康状况可以看出一个人的身体健康状况， 快来看看10个暗藏在指甲里知识吧！</p>
                    <p className="text-muted">阅读(2417)评论(1)赞 (18) <span className="hidden-xs">标签：健康 / 感染 / 指甲 / 疾病 / 皮肤 / 营养 / 趣味生活</span>

                    </p>
                  </div>
                  <div className="col-sm-3 pic hidden-xs">
                    <img src={p3} alt=""/>
                  </div>
                </div>
                <div className="row">
                  <div className="col-sm-9">
                    <h3>生活馆 关于指甲的10个健康知识 你知道几个？</h3>
                    <p className="text-muted hidden-xs">alibaixiu 发布于 2015-11-23</p>
                    <p className="hidden-xs">指甲是经常容易被人们忽略的身体部位， 事实上从指甲的健康状况可以看出一个人的身体健康状况， 快来看看10个暗藏在指甲里知识吧！</p>
                    <p className="text-muted">阅读(2417)评论(1)赞 (18) <span className="hidden-xs">标签：健康 / 感染 / 指甲 / 疾病 / 皮肤 / 营养 / 趣味生活</span>

                    </p>
                  </div>
                  <div className="col-sm-3 pic hidden-xs">
                    <img src={p3} alt=""/>
                  </div>
                </div>
                <div className="row">
                  <div className="col-sm-9">
                    <h3>生活馆 关于指甲的10个健康知识 你知道几个？</h3>
                    <p className="text-muted hidden-xs">alibaixiu 发布于 2015-11-23</p>
                    <p className="hidden-xs">指甲是经常容易被人们忽略的身体部位， 事实上从指甲的健康状况可以看出一个人的身体健康状况， 快来看看10个暗藏在指甲里知识吧！</p>
                    <p className="text-muted">阅读(2417)评论(1)赞 (18) <span className="hidden-xs">标签：健康 / 感染 / 指甲 / 疾病 / 皮肤 / 营养 / 趣味生活</span>

                    </p>
                  </div>
                  <div className="col-sm-3 pic hidden-xs">
                    <img src={p3} alt=""/>
                  </div>
                </div>
              </div>
            </article>
            <aside className="col-md-3">
              <a href="#" className="banner">
                <img src={zgboke} alt=""/>
              </a>
              <a href="#" className="hot">
                <span className="btn btn-primary">热搜</span>
                <h4 className="text-primary">欢迎加入中国博客联盟</h4>
                <p>这里收录国内各个领域的优秀博客，是一个全人工编辑的开放式博客联盟交流和展示平台......</p>
              </a>
            </aside>
          </div>
        </div>
      </>
  )
}

export default (process.env.NODE_ENV === 'development' ? hot(module)(AlibaixiuMobile) : AlibaixiuMobile);
