import React from "react";
import "./style/base.less"
import "./style/common.less"
import "./style/index.less"

import focus from "./upload/focus.jpg"
import bargain from  "./upload/bargain.jpg"
import pic from  "./upload/pic.jpg"
import floor11 from  "./upload/floor-1-1.png"
import floor12 from  "./upload/floor-1-2.png"
import floor13 from  "./upload/floor-1-3.png"
import floor14 from  "./upload/floor-1-4.png"
import floor15 from  "./upload/floor-1-5.png"
import floor16 from  "./upload/floor-1-6.png"
import pic1 from  "./upload/pic1.jpg"
import erweima from  "./upload/erweima.png"
import clock from  "./img/clock.png"
import {hot} from "react-hot-loader";

const PinYouGouPC: React.FC = () => {
  return (
      <>
        <div className="shortcut">
          <div className="w">
            <div className="fl">
              <ul>
                <li>品优购欢迎您！</li>
                <li>
                  <a href="/#">请登录</a>
                  <a href="/#" className="style-red">免费注册</a>
                </li>
              </ul>
            </div>
            <div className="fr">
              <ul>
                <li><a href="/#">我的订单</a></li>
                <li className="spacer"/>
                <li>
                  <a href="/#">我的品优购</a>
                  <i className="icomoon"></i>
                </li>
                <li className="spacer"/>
                <li><a href="/#">品优购会员</a></li>
                <li className="spacer"/>
                <li><a href="/#">企业采购</a></li>
                <li className="spacer"/>
                <li><a href="/#">关注品优购</a> <i className="icomoon"></i></li>
                <li className="spacer"/>
                <li><a href="/#">客户服务</a> <i className="icomoon"></i></li>
                <li className="spacer"/>
                <li><a href="/#">网站导航</a> <i className="icomoon"></i></li>
              </ul>
            </div>
          </div>
        </div>

        <div className="header w">
          <div className="logo">
            <h1>
              <a href="index.html" title="品优购">品优购</a>
            </h1>
          </div>
          <div className="search">
            <input type="text" className="text" value="请搜索内容..."/>
            <button className="btn">搜索</button>
          </div>
          <div className="hotwrods">
            <a href="/#" className="style-red">优惠购首发</a>
            <a href="/#">亿元优惠</a>
            <a href="/#">9.9元团购</a>
            <a href="/#">美满99减30</a>
            <a href="/#">办公用品</a>
            <a href="/#">电脑</a>
            <a href="/#">通信</a>
          </div>
          <div className="shopcar">
            <i className="car"> </i>我的购物车 <i className="arrow">  </i>
            <i className="count">80</i>
          </div>
        </div>

        <div className="nav">
          <div className="w">
            <div className="dropdown fl">
              <div className="dt"> 全部商品分类</div>
              <div className="dd">
                <ul>
                  <li className="menu_item"><a href="/#">家用电器</a> <i>  </i></li>
                  <li className="menu_item">
                    <a href="list.html">手机</a> 、
                    <a href="/#">数码</a> 、
                    <a href="/#">通信</a>
                    <i>  </i>
                  </li>
                  <li className="menu_item"><a href="/#">电脑、办公</a> <i>  </i></li>
                  <li className="menu_item"><a href="/#">家居、家具、家装、厨具</a> <i>  </i></li>
                  <li className="menu_item"><a href="/#">男装、女装、童装、内衣</a> <i>  </i></li>
                  <li className="menu_item"><a href="/#">个户化妆、清洁用品、宠物</a> <i>  </i></li>
                  <li className="menu_item"><a href="/#">鞋靴、箱包、珠宝、奢侈品</a> <i>  </i></li>
                  <li className="menu_item"><a href="/#">运动户外、钟表</a> <i>  </i></li>
                  <li className="menu_item"><a href="/#">汽车、汽车用品</a> <i>  </i></li>
                  <li className="menu_item"><a href="/#">母婴、玩具乐器</a> <i>  </i></li>
                  <li className="menu_item"><a href="/#">食品、酒类、生鲜、特产</a> <i>  </i></li>
                  <li className="menu_item"><a href="/#">医药保健</a> <i>  </i></li>
                  <li className="menu_item"><a href="/#">图书、音像、电子书</a> <i> </i></li>
                  <li className="menu_item"><a href="/#">彩票、旅行、充值、票务</a> <i> </i></li>
                  <li className="menu_item"><a href="/#">理财、众筹、白条、保险</a> <i>  </i></li>
                </ul>
              </div>
            </div>
            <div className="navitems fl">
              <ul>
                <li><a href="/#">服装城</a></li>
                <li><a href="/#">美妆馆</a></li>
                <li><a href="/#">传智超市</a></li>
                <li><a href="/#">全球购</a></li>
                <li><a href="/#">闪购</a></li>
                <li><a href="/#">团购</a></li>
                <li><a href="/#">拍卖</a></li>
                <li><a href="/#">有趣</a></li>
              </ul>
            </div>
          </div>
        </div>

        <div className="w">
          <div className="main">
            <div className="focus fl">
              <a href="/#" className="arrow-l">
                &nbsp;
              </a>
              <a href="/#" className="arrow-r">  </a>
              <ul>
                <li>
                  <a href="/#"><img src={focus} alt=""/></a>
                </li>
              </ul>
              <ol className="circle">
                <li/>
                <li className="current"/>
                <li/>
                <li/>
                <li/>
                <li/>
                <li/>
                <li/>
              </ol>
            </div>
            <div className="newsflash fr">
              <div className="news">
                <div className="news-hd">
                  品优购快报
                  <a href="/#">更多</a>
                </div>
                <div className="news-bd">
                  <ul>
                    <li><a href="/#">【特惠】爆款耳机5折秒！</a></li>
                    <li><a href="/#">【特惠】母亲节，健康好礼低至5折！</a></li>
                    <li><a href="/#">【特惠】爆款耳机5折秒！</a></li>
                    <li><a href="/#">【特惠】9.9元洗100张照片！</a></li>
                    <li><a href="/#">【特惠】长虹智能空调立省1000</a></li>
                  </ul>
                </div>
              </div>
              <div className="lifeservice">
                <ul>
                  <li>
                    <a href="/#">
                      <i className="service_ico service_ico_huafei"/>
                      <p>话费</p>
                    </a>
                  </li>
                  <li>
                    <a href="/#">
                      <i className="service_ico service_ico_feiji"/>
                      <p>机票</p>
                    </a>
                    <span className="hot"/>
                  </li>
                  <li>
                    <a href="/#">
                      <i className="service_ico service_ico_feiji"/>
                      <p>机票</p>
                    </a>
                  </li>
                  <li>
                    <a href="/#">
                      <i className="service_ico service_ico_feiji"/>
                      <p>机票</p>
                    </a>
                  </li>
                  <li>
                    <a href="/#">
                      <i className="service_ico service_ico_feiji"/>
                      <p>机票</p>
                    </a>
                  </li>
                  <li>
                    <a href="/#">
                      <i className="service_ico service_ico_feiji"/>
                      <p>机票</p>
                    </a>
                  </li>
                  <li>
                    <a href="/#">
                      <i className="service_ico service_ico_feiji"/>
                      <p>机票</p>
                    </a>
                  </li>
                  <li>
                    <a href="/#">
                      <i className="service_ico service_ico_feiji"/>
                      <p>机票</p>
                    </a>
                  </li>
                  <li>
                    <a href="/#">
                      <i className="service_ico service_ico_feiji"/>
                      <p>机票</p>
                    </a>
                  </li>
                  <li>
                    <a href="/#">
                      <i className="service_ico service_ico_feiji"/>
                      <p>机票</p>
                    </a>
                  </li>
                  <li>
                    <a href="/#">
                      <i className="service_ico service_ico_feiji"/>
                      <p>机票</p>
                    </a>
                  </li>
                  <li>
                    <a href="/#">
                      <i className="service_ico service_ico_feiji"/>
                      <p>机票</p>
                    </a>
                  </li>
                </ul>
              </div>
              <div className="bargain">
                <img src={bargain} alt=""/>
              </div>
            </div>
          </div>
        </div>


        <div className="recommend w">
          <div className="recom-hd fl">
            <img src={clock} alt=""/>
            <h3>今日推荐</h3>
          </div>
          <div className="recom-bd fl">
            <ul>
              <li>
                <a href="/#">
                  <img src={pic} alt=""/>
                </a>
              </li>
              <li>
                <a href="/#">
                  <img src={pic} alt=""/>
                </a>
              </li>
              <li>
                <a href="/#">
                  <img src={pic} alt=""/>
                </a>
              </li>
              <li className="last">
                <a href="/#">
                  <img src={pic} alt=""/>
                </a>
              </li>
            </ul>
          </div>
        </div>

        <div className="floor">
          <div className="jiadian w">
            <div className="box-hd">
              <h3>家用电器</h3>
              <div className="tab-list">
                <ul>
                  <li><a href="/#" className="style-red">热门</a>|</li>
                  <li><a href="/#">大家电</a>|</li>
                  <li><a href="/#">生活电器</a>|</li>
                  <li><a href="/#">厨房电器</a>|</li>
                  <li><a href="/#">个护健康</a>|</li>
                  <li><a href="/#">应季电器</a>|</li>
                  <li><a href="/#">空气/净水</a>|</li>
                  <li><a href="/#">新奇特</a>|</li>
                  <li><a href="/#">高端电器</a></li>
                </ul>
              </div>
            </div>
            <div className="box-bd">
              <ul className="tab-con">
                <li className="w209">
                  <ul className="tab-con-list">
                    <li>
                      <a href="/#">节能补贴</a>
                    </li>
                    <li>
                      <a href="/#">4K电视</a>
                    </li>
                    <li>
                      <a href="/#">空气净化器</a>
                    </li>
                    <li>
                      <a href="/#">IH电饭煲</a>
                    </li>
                    <li>
                      <a href="/#">滚筒洗衣机</a>
                    </li>
                    <li>
                      <a href="/#">电热水器</a>
                    </li>
                  </ul>
                  <img src={floor11} alt=""/>
                </li>
                <li className="w329">
                  <img src={pic1} alt=""/>
                </li>
                <li className="w219">
                  <div className="tab-con-item">
                    <a href="/#">
                      <img src={floor12} alt=""/>
                    </a>
                  </div>
                  <div className="tab-con-item">
                    <a href="/#">
                      <img src={floor13} alt=""/>
                    </a>
                  </div>
                </li>
                <li className="w220">
                  <div className="tab-con-item">
                    <a href="/#">
                      <img src={floor14} alt=""/>
                    </a>
                  </div>
                </li>
                <li className="w220">
                  <div className="tab-con-item">
                    <a href="/#">
                      <img src={floor15} alt=""/>
                    </a>
                  </div>
                  <div className="tab-con-item">
                    <a href="/#">
                      <img src={floor16} alt=""/>
                    </a>
                  </div>
                </li>
              </ul>
              {/*<ul class="tab-con">*/}
              {/*    <li>1</li>*/}
              {/*    <li>2</li>*/}
              {/*    <li>3</li>*/}
              {/*    <li>4</li>*/}
              {/*    <li>5</li>*/}
              {/*</ul>*/}
            </div>
          </div>
          <div className="shouji w">
            <div className="box-hd">
              <h3>手机通讯</h3>
              <div className="tab-list">
                <ul>
                  <li><a href="/#" className="style-red">热门</a>|</li>
                  <li><a href="/#">大家电</a>|</li>
                  <li><a href="/#">生活电器</a>|</li>
                  <li><a href="/#">厨房电器</a>|</li>
                  <li><a href="/#">个护健康</a>|</li>
                  <li><a href="/#">应季电器</a>|</li>
                  <li><a href="/#">空气/净水</a>|</li>
                  <li><a href="/#">新奇特</a>|</li>
                  <li><a href="/#">高端电器</a></li>
                </ul>
              </div>
            </div>
            <div className="box-bd">
              <ul className="tab-con">
                <li className="w209">
                  <ul className="tab-con-list">
                    <li>
                      <a href="/#">节能补贴</a>
                    </li>
                    <li>
                      <a href="/#">4K电视</a>
                    </li>
                    <li>
                      <a href="/#">空气净化器</a>
                    </li>
                    <li>
                      <a href="/#">IH电饭煲</a>
                    </li>
                    <li>
                      <a href="/#">滚筒洗衣机</a>
                    </li>
                    <li>
                      <a href="/#">电热水器</a>
                    </li>
                  </ul>
                  <img src={floor11} alt=""/>
                </li>
                <li className="w329">
                  <img src={pic1} alt=""/>
                </li>
                <li className="w219">
                  <div className="tab-con-item">
                    <a href="/#">
                      <img src={floor12} alt=""/>
                    </a>
                  </div>
                  <div className="tab-con-item">
                    <a href="/#">
                      <img src={floor13} alt=""/>
                    </a>
                  </div>
                </li>
                <li className="w220">
                  <div className="tab-con-item">
                    <a href="/#">
                      <img src={floor14} alt=""/>
                    </a>
                  </div>
                </li>
                <li className="w220">
                  <div className="tab-con-item">
                    <a href="/#">
                      <img src={floor15} alt=""/>
                    </a>
                  </div>
                  <div className="tab-con-item">
                    <a href="/#">
                      <img src={floor16} alt=""/>
                    </a>
                  </div>
                </li>
              </ul>
              {/*<ul class="tab-con">*/}
              {/*    <li>1</li>*/}
              {/*    <li>2</li>*/}
              {/*    <li>3</li>*/}
              {/*    <li>4</li>*/}
              {/*    <li>5</li>*/}
              {/*</ul>*/}
            </div>
          </div>
        </div>

        <div className="fixedtool">
          <ul>
            <li className="current">家用电器</li>
            <li>手机通讯</li>
            <li>家用电器</li>
            <li>家用电器</li>
            <li>家用电器</li>
            <li>家用电器</li>
          </ul>
        </div>

        <div className="footer">
          <div className="w">
            <div className="mod_service">
              <ul>
                <li>
                  <i className="mod-service-icon mod_service_zheng"/>
                  <div className="mod_service_tit">
                    <h5>正品保障</h5>
                    <p>正品保障，提供发票</p>
                  </div>
                </li>
                <li>
                  <i className="mod-service-icon mod_service_kuai"/>
                  <div className="mod_service_tit">
                    <h5>正品保障</h5>
                    <p>正品保障，提供发票</p>
                  </div>
                </li>
                <li>
                  <i className="mod-service-icon mod_service_bao"/>
                  <div className="mod_service_tit">
                    <h5>正品保障</h5>
                    <p>正品保障，提供发票</p>
                  </div>
                </li>
                <li>
                  <i className="mod-service-icon mod_service_bao"/>
                  <div className="mod_service_tit">
                    <h5>正品保障</h5>
                    <p>正品保障，提供发票</p>
                  </div>
                </li>
                <li>
                  <i className="mod-service-icon mod_service_bao"/>
                  <div className="mod_service_tit">
                    <h5>正品保障</h5>
                    <p>正品保障，提供发票</p>
                  </div>
                </li>
              </ul>
            </div>
            <div className="mod_help">
              <dl className="mod_help_item">
                <dt>购物指南</dt>
                <dd><a href="/#">购物流程 </a></dd>
                <dd><a href="/#">会员介绍 </a></dd>
                <dd><a href="/#">生活旅行/团购 </a></dd>
                <dd><a href="/#">常见问题 </a></dd>
                <dd><a href="/#">大家电 </a></dd>
                <dd><a href="/#">联系客服 </a></dd>
              </dl>
              <dl className="mod_help_item">
                <dt>购物指南</dt>
                <dd><a href="/#">购物流程 </a></dd>
                <dd><a href="/#">会员介绍 </a></dd>
                <dd><a href="/#">生活旅行/团购 </a></dd>
                <dd><a href="/#">常见问题 </a></dd>
                <dd><a href="/#">大家电 </a></dd>
                <dd><a href="/#">联系客服 </a></dd>
              </dl>
              <dl className="mod_help_item">
                <dt>购物指南</dt>
                <dd><a href="/#">购物流程 </a></dd>
                <dd><a href="/#">会员介绍 </a></dd>
                <dd><a href="/#">生活旅行/团购 </a></dd>
                <dd><a href="/#">常见问题 </a></dd>
                <dd><a href="/#">大家电 </a></dd>
                <dd><a href="/#">联系客服 </a></dd>
              </dl>
              <dl className="mod_help_item">
                <dt>购物指南</dt>
                <dd><a href="/#">购物流程 </a></dd>
                <dd><a href="/#">会员介绍 </a></dd>
                <dd><a href="/#">生活旅行/团购 </a></dd>
                <dd><a href="/#">常见问题 </a></dd>
                <dd><a href="/#">大家电 </a></dd>
                <dd><a href="/#">联系客服 </a></dd>
              </dl>
              <dl className="mod_help_item">
                <dt>购物指南</dt>
                <dd><a href="/#">购物流程 </a></dd>
                <dd><a href="/#">会员介绍 </a></dd>
                <dd><a href="/#">生活旅行/团购 </a></dd>
                <dd><a href="/#">常见问题 </a></dd>
                <dd><a href="/#">大家电 </a></dd>
                <dd><a href="/#">联系客服 </a></dd>
              </dl>
              <dl className="mod_help_item mod_help_app">
                <dt>帮助中心</dt>
                <dd>
                  <img src={erweima} alt=""/>
                  <p>品优购客户端</p>
                </dd>
              </dl>
            </div>

            <div className="mod_copyright">
              <p className="mod_copyright_links">
                关于我们 | 联系我们 | 联系客服 | 商家入驻 | 营销中心 | 手机品优购 | 友情链接 | 销售联盟 | 品优购社区 | 品优购公益 | English Site | Contact U
              </p>
              <p className="mod_copyright_info">
                地址：北京市昌平区建材城西路金燕龙办公楼一层 邮编：100096 电话：400-618-4000 传真：010-82935100 邮箱: zhanghj+itcast.cn <br/>
                京ICP备08001421号京公网安备110108007702
              </p>
            </div>
          </div>
        </div>
      </>
  )
}

export default (process.env.NODE_ENV === 'development' ? hot(module)(PinYouGouPC) : PinYouGouPC);

