import React, {lazy, useContext} from 'react'
import {BrowserRouter, Redirect, Route, Switch} from 'react-router-dom'
import {UserContext} from '/@/store'
import {getLoginUserToken} from '/@/utils/lsUtil'
import {getUserInfo} from '/@/store/user'

const Container = lazy(() => import('/@/components/container'))
const View404 = lazy(() => import('../../pages/404'));
const View500 = lazy(() => import('/@/pages/500'));
const Login = lazy(() => import('/@/pages/login'));

const Routers: React.FC = () => {
  const {roles, userDispatch} = useContext(UserContext)
  const token = getLoginUserToken()

  return (
    <BrowserRouter>
      <Switch>
        {/*<Route path='/' exact render={() => <Redirect to='/dashboard'/>}/>*/}
        <Route path="/500" component={View500}/>
        <Route path="/login" component={Login}/>
        <Route path="/404" component={View404}/>
        <Route
          path="/"
          render={() => {
            if (!token) {
              return <Redirect to="/login"/>
            } else {
              if (roles && roles.length > 0) {
                return <Container/>
              } else {
                getUserInfo()(userDispatch).then(() => <Container/>)
              }
            }
          }}
        />
        <Route render={() => <Redirect to="/404"/>}/>
      </Switch>
    </BrowserRouter>
  )
}

export default Routers
