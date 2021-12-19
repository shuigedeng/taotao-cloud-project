import { withRouter } from 'react-router'
import { Route, Redirect } from 'react-router-dom'
import React from 'react'

const Auth = ({ component: Component, ...rest }) => {
  const isLogged = localStorage.getItem('isLogin') === '1'
  return (
    <Route
      {...rest}
      render={props =>
        isLogged ? <Component {...props} /> : <Redirect to={'/login'} />
      }
    />
  )
}

export default withRouter(Auth)
