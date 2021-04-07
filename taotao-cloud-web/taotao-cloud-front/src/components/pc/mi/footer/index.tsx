import React from "react";
import {Button} from "antd";
import "../style/footer/footer.less";

const links = [{"title": "帮助中心", "content": ["账户管理", "购物指南", "订单操作"]},
  {"title": "服务支持", "content": ["售后政策", "自助服务", "相关下载"]},
  {"title": "线下门店", "content": ["小米之家", "服务网点", "零售网点"]},
  {"title": "关于小米", "content": ["了解小米", "加入小米", "联系我们"]},
  {"title": "关注我们", "content": ["新浪微博", "小米部落", "官方微信"]},
  {"title": "特色服务", "content": ["F 码通道", "礼物码", "防伪查询"]}];
const sites = ["小米商城", "MIUI", "米家", "米聊", "多看", "路由器", "米粉卡", "小米天猫店", "隐私政策", "问题反馈", "Select Region"];
const infoLinks = ["1.png", "truste.png", "v-logo-1.png", "v-logo-2.png", "v-logo-3.png"];

const Footer: React.FC = () => {
  let linkItem = links.map((item, index) => {
    let p = item.content.map((it, i) => {
      return (
          <p key={i} className="footer-links-txt">{it}</p>
      )
    })
    return (
        <div key={index} style={{width: 160}}>
          <h3 className="footer-links-title">{item.title}</h3>
          {p}
        </div>
    )
  })

  return (
      <div className="site-footer">
        <div className="container">
          <div className="footer-service">
            <ul className="list-service">
              <li>预约维修服务</li>
              <li>7天无理由退货</li>
              <li>15天免费换货</li>
              <li>满150元包邮</li>
              <li>520余家售后网点</li>
            </ul>
          </div>
          <div className="footer-links">
            {linkItem}
            <div className="col-contact">
              <p className="phone">400-100-5678</p>
              <p className="">周一至周日 8:00-18:00</p>
              <p className="">（仅收市话费）</p>
              <p><Button className="btn-line-primary">24小时在线客服</Button></p>
            </div>
          </div>
        </div>
        <div className="site-info">
          <div className="container footer-site">
            <div className="info-left">
              <div className="logo ir">小米官网</div>
              <div className="info-text">
                <p>
                  {
                    sites.map((item, index) => {
                      var sep = index < sites.length - 1 ? <span className='sep'>|</span> : ""
                      return (
                          <a key={index} className="info-text-a" href="/#">{item}{sep}</a>
                      )
                    })
                  }
                </p>
                <p style={{color: "#b0b0b0"}}>
                  ©mi.com 京ICP证110507号 京ICP备10046444号 京公网安备11010802020134号
                  京网文[2014]0059-0009号违法和不良信息举报电话：185-0130-1238，本网站所列数据，除特殊说明，所有数据均出自我司实验室测试
                </p>
              </div>
            </div>
            <div className="info-links">
              {
                infoLinks.map((item, index) => {
                  let src = "./src/images/" + item
                  return (
                      <a key={index} href="/#">
                        <img style={{width: 85}} src={src} alt=""/>
                      </a>
                  )
                })
              }
            </div>
          </div>
          <div className="slogan ir">探索黑科技，小米为发烧而生</div>
        </div>
      </div>
  )
}

export default Footer
