import React from 'react'
import { Layout } from 'antd'
import AppMenu from '../../menu'
import { IMenu } from '/@/config/menuConfig'
import { GithubFilled } from '@ant-design/icons'

const { Sider } = Layout

interface IProps {
  menuToggle: boolean
  menu: IMenu[]
}

const AppSider: React.FC<IProps> = props => {
  const { menuToggle, menu } = props

  return (
    <Sider className="aside" collapsed={menuToggle}>
      <div className="logo">
        <a
          rel="noopener noreferrer"
          href="https://github.com/ltadpoles"
          target="_blank"
        >
          <GithubFilled style={{ color: '#fff' }} />
        </a>
      </div>

      <div style={{ backgroundColor: 'red' }}>
        <AppMenu menu={menu} />
      </div>
    </Sider>
  )
}

export default AppSider
