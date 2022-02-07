import React from 'react'
import { Col, Divider, Layout, Row } from 'antd'
import {
  DingtalkOutlined,
  QqOutlined,
  WechatFilled,
  WeiboOutlined
} from '@ant-design/icons'
import Bar from './bar'
import Pie from './pie'
import Line from './line'
import Scatter from './scatter'
import Pictorial from './pictorial'
import './index.less'
import { useIntl } from 'react-intl'

const Dashboard: React.FC = () => {
  console.log('DashboardDashboardDashboardDashboardDashboard')
  const intl = useIntl()
  return (
    <Layout className="index animated fadeIn">
      <Row gutter={24} className="index-header">
        <Col span={6}>
          <div className="base-style wechat">
            <WechatFilled className="icon-style" />
            <div>
              <span>999</span>
              <div>微信</div>
            </div>
          </div>
        </Col>
        <Col span={6}>
          <div className="base-style qq">
            <QqOutlined className="icon-style" />
            <div>
              <span>366</span>
              <div>QQ</div>
            </div>
          </div>
        </Col>
        <Col span={6}>
          <div className="base-style dingding">
            <DingtalkOutlined className="icon-style" />
            <div>
              <span>666</span>
              <div>钉钉</div>
            </div>
          </div>
        </Col>
        <Col span={6}>
          <div className="base-style weibo">
            <WeiboOutlined className="icon-style" />
            <div>
              <span>689</span>
              <div>微博</div>
            </div>
          </div>
        </Col>
      </Row>
      <Row>
        <Col>
          <div className="base-style">
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <div>
                {intl.formatMessage({ id: 'dashboard.graphics.display' })}
              </div>
              {/*<Index type='fullscreen' style={{cursor: 'pointer'}} onClick={fullToggle}/>*/}
            </div>
            <Divider />
            <Bar />
          </div>
        </Col>
      </Row>
      <Row gutter={8}>
        <Col span={12}>
          <div className="base-style">
            <Line />
          </div>
        </Col>
        <Col span={12}>
          <div className="base-style">
            <Pie />
          </div>
        </Col>
        <Col span={12}>
          <div className="base-style">
            <Scatter />
          </div>
        </Col>
        <Col span={12}>
          <div className="base-style">
            <Pictorial />
          </div>
        </Col>
      </Row>
    </Layout>
  )
}

export default Dashboard
