import React from "react";

import "../../../style/homehero/sub.less";
import {Row} from 'antd';
import {
  FileTextOutlined,
  GiftOutlined,
  PayCircleOutlined,
  ReloadOutlined,
  TabletOutlined,
  TrademarkOutlined
} from '@ant-design/icons';

const Sub: React.FC = () => {
  const iconStyle = {
    marginBottom: "4px",
    fontSize: "24px",
    lineHeight: "24px",
    color: "#cdcac8",
    fontWeight: 600
  }

  return (
      <div>
        <div>
          <Row className="sub-row">
            <div>
              <ul className="home-channel-list">
                <li className="exposure"><TabletOutlined style={iconStyle}/><p>选购手机</p></li>
                <li className="exposure"><GiftOutlined style={iconStyle}/><p>企业团购</p></li>
                <li className="exposure"><TrademarkOutlined style={iconStyle}/><p>F码通道</p></li>
                <li className="exposure"><FileTextOutlined style={iconStyle}/><p>米粉卡</p></li>
                <li className="exposure"><ReloadOutlined style={iconStyle}/><p>以旧换新</p></li>
                <li className="exposure"><PayCircleOutlined style={iconStyle}/><p>话费充值</p></li>
              </ul>
            </div>
            <div>
              <img style={{width: 316, height: 170}}
                   src="https://i1.mifile.cn/a4/xmad_15142931618712_eaIhT.jpg"
                   alt=""/>
            </div>
            <div>
              <img style={{width: 316, height: 170}}
                   src="https://i1.mifile.cn/a4/xmad_15142932350007_dNpbu.jpg"
                   alt=""/>
            </div>
            <div>
              <img style={{width: 316, height: 170}}
                   src="https://i1.mifile.cn/a4/xmad_15065269205274_ENtuP.jpg"
                   alt=""/>
            </div>
          </Row>
        </div>
      </div>
  )
}
export default Sub
