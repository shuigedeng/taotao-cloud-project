import React from 'react'
import {
  SettingsAction,
  SettingsActionType
} from './SettingsAction'
import { IMenu } from '/@/config/menuConfig'

export const setHeadFixed = (headFixed: boolean) => (
  dispatch: React.Dispatch<SettingsAction>
) => {
  return dispatch({
    type: SettingsActionType.SET_HEAD_FIXED,
    payload: { headFixed: headFixed }
  })
}

export const setTopNavEnable = (topNavEnable: boolean) => (
  dispatch: React.Dispatch<SettingsAction>
) => {
  return dispatch({
    type: SettingsActionType.SET_TOP_NAV_ENABLE,
    payload: { topNavEnable: topNavEnable }
  })
}

export const setTageViewEnable = (tageViewEnable: boolean) => (
  dispatch: React.Dispatch<SettingsAction>
) => {
  return dispatch({
    type: SettingsActionType.SET_TAGE_VIEW_ENABLE,
    payload: { tageViewEnable: tageViewEnable }
  })
}

export const tagViewAddTag = (tag: IMenu) => (
  dispatch: React.Dispatch<SettingsAction>
) => {
  return dispatch({
    type: SettingsActionType.TAG_VIEW_ADD,
    payload: { tag: tag }
  })
}

export const tagViewDeleteTag = (tag: IMenu) => (
  dispatch: React.Dispatch<SettingsAction>
) => {
  return dispatch({
    type: SettingsActionType.TAG_VIEW_DELETE,
    payload: { tag: tag }
  })
}

export const tagViewEmpty = () => (
  dispatch: React.Dispatch<SettingsAction>
) => {
  return dispatch({
    type: SettingsActionType.TAG_VIEW_EMPTY,
    payload: {}
  })
}

export const tagViewCloseOther = (tag: IMenu) => (
  dispatch: React.Dispatch<SettingsAction>
) => {
  return dispatch({
    type: SettingsActionType.TAG_VIEW_CLOSE_OTHER,
    payload: { tag: tag }
  })
}
