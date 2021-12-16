import React, { useContext, useEffect, useState } from 'react'
import { Menu } from 'antd'
import { Link, useLocation } from 'react-router-dom'
import menuList, { IMenu } from '/@/config/menuConfig'
import Icon from '../icon'
import { SettingsContext } from '/@/store'
import { getMenuItemInMenuListByProperty } from '/@/utils'
import { tagViewAddTag } from '/@/store/settings'

interface IProps {
  menu: IMenu[]
  location?: string
}

interface IState {
  openKeys: string[]
  selectedKeys: string[]
}

const AppMenu: React.FC<IProps> = props => {
  const initState = () => {
    return {
      openKeys: [],
      selectedKeys: []
    }
  }
  const [state, setState] = useState<IState>(initState)
  const location = useLocation()
  const { settingsDispatch } = useContext(SettingsContext)

  const getOpenKeys = (str: string) => {
    let newStr = ''
    const newArr = []
    const arr = str.split('/').map(i => '/' + i)

    for (let i = 1; i < arr.length - 1; i++) {
      newStr += arr[i]
      newArr.push(newStr)
    }
    return newArr
  }

  const handleMenuSelect = (key = '/dashboard') => {
    const menuItem = getMenuItemInMenuListByProperty(menuList, key)
    tagViewAddTag(menuItem)(settingsDispatch)
  }

  useEffect(() => {
    const { pathname } = location

    setState(prevState => {
      if (props.location !== pathname) {
        handleMenuSelect(pathname)
        return {
          ...prevState,
          selectedKeys: [pathname],
          openKeys: getOpenKeys(pathname)
        }
      } else {
        return { ...prevState }
      }
    })
  }, [location])

  // 只展开一个 SubMenu
  const onOpenChange = (openKeys: string[]) => {
    if (openKeys.length === 0 || openKeys.length === 1) {
      setState(prevState => {
        return {
          ...prevState,
          openKeys: openKeys
        }
      })
      return
    }

    // 最新展开的 SubMenu
    const latestOpenKey = openKeys[openKeys.length - 1]

    // 这里与定义的路由规则有关
    if (latestOpenKey.includes(openKeys[0])) {
      setState(prevState => {
        return {
          ...prevState,
          openKeys: openKeys
        }
      })
    } else {
      setState(prevState => {
        return {
          ...prevState,
          openKeys: [latestOpenKey]
        }
      })
    }
  }

  const renderMenuItem = ({ path, icon, title }: IMenu) => (
    <Menu.Item key={path}>
      <Link to={path}>
        {icon && <Icon type={icon} style={{ fontSize: '1.5rem' }} />}
        <span>{title}</span>
      </Link>
    </Menu.Item>
  )

  // 循环遍历数组中的子项 subs ，生成子级 menu
  const renderSubMenu = ({ path, icon, title, children }: IMenu) => {
    return (
      <Menu.SubMenu
        key={path}
        title={
          <span>
            {icon && <Icon type={icon} style={{ fontSize: '1.5rem' }} />}
            <span>{title}</span>
          </span>
        }
      >
        {children &&
          children.map(item => {
            return item.children && item.children.length > 0
              ? renderSubMenu(item)
              : renderMenuItem(item)
          })}
      </Menu.SubMenu>
    )
  }

  return (
    <Menu
      mode="inline"
      theme="dark"
      openKeys={state.openKeys}
      selectedKeys={state.selectedKeys}
      style={{ marginTop: '20px' }}
      // onClick={({key}) => setState(prevState => {
      //   return {
      //     ...prevState,
      //     selectedKeys: [key]
      //   }
      // })}
      // onSelect={(e) => handleMenuSelect(e.key)}
      // onOpenChange={onOpenChange}
    >
      {props.menu &&
        props.menu.map(item => {
          return item.children && item.children.length > 0
            ? renderSubMenu(item)
            : renderMenuItem(item)
        })}
    </Menu>
  )
}

export default AppMenu
