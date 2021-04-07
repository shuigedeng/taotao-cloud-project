import { IMenu } from '/@/config/menuConfig'

export interface SettingsAction {
  type: SettingsActionType
  payload: SettingsActionPayload
}

export interface SettingsActionPayload {
  topNavEnable?: boolean
  tageViewEnable?: boolean
  headFixed?: boolean
  tag?: IMenu
}

export enum SettingsActionType {
  SET_HEAD_FIXED = 'setHeadFixed',
  SET_TOP_NAV_ENABLE = 'setTopNavEnable',
  SET_TAGE_VIEW_ENABLE = 'setTageViewEnable',

  TAG_VIEW_ADD = 'tagViewAdd',
  TAG_VIEW_DELETE = 'tagViewDelete',
  TAG_VIEW_EMPTY = 'tagViewEmpty',
  TAG_VIEW_CLOSE_OTHER = 'tagViewCloseOther'
}
