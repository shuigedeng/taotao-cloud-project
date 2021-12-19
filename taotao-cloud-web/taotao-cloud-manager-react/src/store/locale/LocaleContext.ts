import { Context, createContext } from 'react'
import { ILocaleState, localeInitState } from '../locale/LocaleState'

export const LocaleContext: Context<ILocaleState> = createContext(
  localeInitState
)
