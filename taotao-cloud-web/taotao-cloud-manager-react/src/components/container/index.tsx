import React, { useContext, useEffect, useState } from 'react'
import { Route, Switch, useHistory, useLocation } from 'react-router-dom'
import { BackTop, Layout, message } from 'antd'
import DocumentTitle from 'react-document-title'
import menuConfig, { IMenu } from '/@/config/menuConfig'
import AppHeader from './header'
import AppSider from './sider'
import { MenuContext, SettingsContext, UserContext } from '/@/store'
import Settings from '/@/components/settings'
import { getMenuItemInMenuListByProperty, getPermissionMenuData } from '/@/utils'
// import useEcharts from '/@/hooks/useEcharts'
import api from '/@/api'

import './index.less'
import TagsView from '/@/components/container/TagsView'
import { CSSTransition, TransitionGroup } from 'react-transition-group'
import { setMenuToggle } from '/@/store/menu'
import { removeLoginUserIsRemember, removeLoginUserToken } from '/@/utils/lsUtil'
import { filterRouterMap } from '/@/config/routeConfig'

const { Content } = Layout

interface IState {
  avatar?: string
  show: boolean
  menu: IMenu[]
  routers: {
    path: string
    exact: boolean
    name: string
    component: string
    roles: string[]
  }[]
}

const Container: React.FC = () => {
  const initState = () => {
    return {
      show: false,
      menu: [],
      routers: []
    }
  }

  const [state, setState] = useState<IState>(initState)
  const history = useHistory()
  const location = useLocation()
  const { menuToggle, menuDispatch } = useContext(MenuContext)
  const { tageViewEnable } = useContext(SettingsContext)
  const { roles } = useContext(UserContext)

  // useEcharts(document.getElementById('bar'), {})
  // useEcharts(document.getElementById('line'), {})
  // useEcharts(document.getElementById('pie'), {})
  // useEcharts(document.getElementById('pictorialBar'), {})
  // useEcharts(document.getElementById('scatter'), {})

  useEffect(() => {
    let flag = true
    if (flag) {
      const isLoginAndGetRoles = async () => {
        const menus = await api.uc.getLoginUserMenuInfo()

        setState(prevState => {
          return {
            ...prevState,
            menu: getPermissionMenuData(roles, menus.data)
          }
        })

        const routers = await api.uc.getLoginUserRouters()
        setState(prevState => {
          return {
            ...prevState,
            routers: routers.data
          }
        })
      }

      isLoginAndGetRoles()
    }
    return () => {
      flag = false
    }
  }, [roles])

  const loginOut = () => {
    api.auth.logout().then(res => {
      const result = res.data
      if (result) {
        removeLoginUserToken()
        removeLoginUserIsRemember()

        history.push('/login')
        message.success('退出成功!')
      }
    })
  }

  const getPageTitle = (menuList: IMenu[], pathname: string) => {
    let title = '滔滔cloud管理后台'
    const item = getMenuItemInMenuListByProperty(menuList, pathname)
    if (item) {
      title = `${item.title} - 滔滔cloud管理后台`
    }
    return title
  }

  const handleFilter = (route: {
    path: string
    exact: boolean
    name: string
    component: string
    roles: string[]
  }) => {
    for (const role of roles) {
      // 过滤没有权限的页面
      return role === 'admin' || !route.roles || route.roles.includes(role)
    }
  }

  const renderRouter = () => {
    return (
      <DocumentTitle title={getPageTitle(menuConfig, location.pathname)}>
        <Content style={{ height: 'calc(100% - 100px)' }}>
          <TransitionGroup>
            <CSSTransition
              key={location.pathname}
              timeout={500}
              classNames="fade"
              exit={false}
            >
              <Switch location={location}>
                {state.routers.map(route => {
                  const component = filterRouterMap(route.component)

                  return (
                    handleFilter(route) && (
                      <Route
                        key={route.path}
                        path={route.path}
                        exact={route.exact}
                        component={component}
                      />
                    )
                  )
                })}
              </Switch>
            </CSSTransition>
          </TransitionGroup>
        </Content>
      </DocumentTitle>
    )
  }

  return (
    <Layout className="app">
      <BackTop />
      <AppSider menuToggle={menuToggle} menu={state.menu} />

      <Layout
        style={{
          marginLeft: menuToggle ? '80px' : '200px',
          minHeight: '100vh'
        }}
      >
        <AppHeader
          menuToggle={menuToggle}
          menuClick={() => {
            setMenuToggle(!menuToggle)(menuDispatch)
          }}
          avatar=""
          loginOut={loginOut}
        />
        {tageViewEnable ? <TagsView /> : null}

        {state.routers.length > 0 && renderRouter()}

        {/*<AppFooter/>*/}
      </Layout>

      <Settings />
    </Layout>
  )
}
export default Container
