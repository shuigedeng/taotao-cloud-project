import { LocaleAction, LocaleActionType as type } from '../locale/LocaleAction'
import { ILocaleState } from '../locale/LocaleState'

export function localeReducer(
  state: ILocaleState,
  action: LocaleAction
): ILocaleState {
  switch (action.type) {
    case type.LOCALE_TOGGLE:
      return <ILocaleState>{
        ...state,
        locale: action.payload.locale,
        messages: action.payload.messages,
        antdLocale: action.payload.antdLocale
      }
    default:
      return state
  }
}
