import React from 'react'
import api from '/@/api'
import {
  UserAction,
  UserActionPayload,
  UserActionType
} from './UserAction'

export const setUserLoginSuccess = (userInfo: UserActionPayload) => (
  dispatch: React.Dispatch<UserAction>
) => {
  return dispatch({
    type: UserActionType.USER_SET_LOGIN_SUCCESS,
    payload: { ...userInfo }
  })
}

export const setUserLoginToken = (token: string) => (
  dispatch: React.Dispatch<UserAction>
) => {
  return dispatch({
    type: UserActionType.USER_SET_LOGIN_TOKEN,
    payload: { token: token }
  })
}

export const resetUser = () => (dispatch: React.Dispatch<UserAction>) => {
  return dispatch({
    type: UserActionType.USER_SET_LOGIN_TOKEN,
    payload: {}
  })
}

export const getUserInfo = () => (dispatch: React.Dispatch<UserAction>) => {
  return new Promise((resolve, reject) => {
    api.uc
      .getLoginUserInfo()
      .then(response => {
        const userinfo = response.data
        setUserLoginSuccess(userinfo)(dispatch)
        resolve(response.data)
      })
      .catch(error => {
        reject(error)
      })
  })
}
