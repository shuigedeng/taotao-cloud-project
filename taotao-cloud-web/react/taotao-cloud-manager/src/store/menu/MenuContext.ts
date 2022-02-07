import { Context, createContext } from 'react'
import { IMenuState, menuInitState } from './MenuState'

export const MenuContext: Context<IMenuState> = createContext(menuInitState)
