import {
  SettingsAction,
  SettingsActionType as type
} from './SettingsAction'
import { ISettingsState } from './SettingsState'

export function settingsReducer(
  state: ISettingsState,
  action: SettingsAction
): ISettingsState {
  switch (action.type) {
    case type.SET_HEAD_FIXED:
      return <ISettingsState>{
        ...state,
        headFixed: action.payload.headFixed
      }
    case type.SET_TOP_NAV_ENABLE:
      return <ISettingsState>{
        ...state,
        topNavEnable: action.payload.topNavEnable
      }
    case type.SET_TAGE_VIEW_ENABLE:
      return <ISettingsState>{
        ...state,
        tageViewEnable: action.payload.tageViewEnable
      }
    case type.TAG_VIEW_ADD:
      const tag = action.payload.tag
      if (tag) {
        if (state.tagList.includes(tag)) {
          return state
        } else {
          return <ISettingsState>{
            ...state,
            tagList: [...state.tagList, tag]
          }
        }
      }
      return state
    case type.TAG_VIEW_DELETE:
      return <ISettingsState>{
        ...state,
        tagList: [...state.tagList.filter(item => item !== action.payload.tag)]
      }
    case type.TAG_VIEW_EMPTY:
      return <ISettingsState>{
        ...state,
        tagList: [...state.tagList.filter(item => item.path === '/dashboard')]
      }
    case type.TAG_VIEW_CLOSE_OTHER:
      return <ISettingsState>{
        ...state,
        tagList: [
          ...state.tagList.filter(
            item => item.path === '/dashboard' || item === action.payload.tag
          )
        ]
      }
    default:
      return state
  }
}
