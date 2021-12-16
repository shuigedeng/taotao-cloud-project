import { IMenu } from '/@/config/menuConfig'
import { SettingsAction } from './SettingsAction'
import React from 'react'

export const settingsInitState: ISettingsState = {
  topNavEnable: false,
  headFixed: false,
  tageViewEnable: true,
  tagList: [],
  settingsDispatch: () => {}
}

export interface ISettingsState {
  topNavEnable: boolean
  tageViewEnable: boolean
  headFixed: boolean
  tagList: IMenu[]
  settingsDispatch: React.Dispatch<SettingsAction>
}
