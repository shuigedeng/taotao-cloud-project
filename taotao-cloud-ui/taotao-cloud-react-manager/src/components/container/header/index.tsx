import React, { useContext } from 'react'
import { Avatar, Badge, Dropdown, Layout, Menu } from 'antd'
import Icon from '../../icon'
import user from '../../../assets/images/user.jpg'
import screenfull from 'screenfull'
import { GlobalOutlined } from '@ant-design/icons/lib'
import { LocaleContext } from '/@/store'
import { LocaleEnum } from '/@/enums/localeEnum'
import BreadCrumb from '/@/components/breadCrumb'
import { localeToggle } from '/@/store/locale'
import { MenuInfo } from 'rc-menu/es/interface'

const { Header } = Layout

interface IProps {
  menuClick: () => void
  avatar: string | undefined
  menuToggle: boolean
  loginOut: () => void
}

interface Lang {
  [key: string]: string
}

const AppHeader: React.FC<IProps> = props => {
  const { menuClick, avatar, menuToggle, loginOut } = props

  const screenFull = () => {
    if (screenfull.isEnabled) {
      screenfull.request()
    }
  }

  const menu = (
    <Menu style={{ float: 'right' }}>
      <Menu.ItemGroup title="用户中心">
        <Menu.Item key="setting:2">
          <span onClick={loginOut}>
            <Icon type="icontouxiang-info" style={{ fontSize: '1.5rem' }} />
            个人信息
          </span>
        </Menu.Item>
        <Menu.Item key="logout">
          <span onClick={loginOut}>
            <Icon type="iconlog_out" style={{ fontSize: '1.5rem' }} />
            退出登录
          </span>
        </Menu.Item>
      </Menu.ItemGroup>
      <Menu.Divider />
      <Menu.ItemGroup title="设置中心">
        <Menu.Item key="setting:3">
          <span onClick={loginOut}>
            <Icon type="iconuser-edit" style={{ fontSize: '1.5rem' }} />
            个人设置
          </span>
        </Menu.Item>
        <Menu.Item key="setting:4">
          <span onClick={loginOut}>
            <Icon type="iconsetting" style={{ fontSize: '1.5rem' }} />
            系统设置
          </span>
        </Menu.Item>
      </Menu.ItemGroup>
    </Menu>
  )

  const locales = ['zh', 'TW', 'en']
  const languageLabels: Lang = {
    zh: '简体中文',
    TW: '繁体中文',
    en: 'English'
  }
  const languageIcons: Lang = {
    zh: '🇨🇳',
    TW: '🇭🇰',
    en: '🇺🇸'
  }
  const { localeDispatch, locale } = useContext(LocaleContext)

  const changeLang = ({ key }: MenuInfo): void => {
    const result =
      key === 'zh'
        ? LocaleEnum.zh
        : key === 'en'
        ? LocaleEnum.en
        : LocaleEnum.zh
    localeToggle(result)(localeDispatch)
  }

  const langMenu = (
    <Menu
      style={{ float: 'right' }}
      selectedKeys={[locale]}
      onClick={changeLang}
    >
      {locales.map((locale: string) => (
        <Menu.Item key={locale}>
          <span role="img" aria-label={languageLabels[locale]}>
            {languageIcons[locale]}
          </span>{' '}
          {languageLabels[locale]}
        </Menu.Item>
      ))}
    </Menu>
  )

  return (
    <Header className="header">
      <div className="left">
        <Icon
          style={{ fontSize: '1.8rem' }}
          onClick={menuClick}
          type={menuToggle ? 'iconmenu-unfold' : 'iconmenu-fold'}
        />
        <BreadCrumb />
      </div>

      <div className="right">
        <div className="mr10">
          <Icon
            type="iconfullscreen"
            style={{ cursor: 'pointer', fontSize: '1.8rem' }}
            onClick={screenFull}
          />
        </div>

        <div className="mr15">
          <Badge count={25} overflowCount={10} style={{ marginLeft: 8 }}>
            <a
              href="https://github.com/ltadpoles/react-admin"
              style={{ color: '#000' }}
            >
              <Icon
                type="iconNotificationFilled"
                style={{ fontSize: '1.8rem' }}
              />
            </a>
          </Badge>
        </div>

        <div className="mr10">
          <Dropdown overlay={menu} overlayStyle={{ width: '10rem' }}>
            <div className="ant-dropdown-link">
              <Avatar src={user} alt={avatar} style={{ cursor: 'pointer' }} />
            </div>
          </Dropdown>
        </div>

        <div>
          <Dropdown overlay={langMenu} overlayStyle={{ width: '10rem' }}>
            <span className="ant-dropdown-link" style={{ cursor: 'pointer' }}>
              <GlobalOutlined title="语言" />
            </span>
          </Dropdown>
        </div>
      </div>
    </Header>
  )
}

export default React.memo(AppHeader)
