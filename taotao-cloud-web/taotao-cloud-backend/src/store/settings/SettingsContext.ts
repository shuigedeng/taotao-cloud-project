import { Context, createContext } from 'react'
import { ISettingsState, settingsInitState } from './SettingsState'

export const SettingsContext: Context<ISettingsState> = createContext(
  settingsInitState
)
