import React from 'react'
import { MenuAction } from './MenuAction'

export const menuInitState: IMenuState = {
  menuToggle: false,
  menuDispatch: () => {}
}

export interface IMenuState {
  menuToggle: boolean
  menuDispatch: React.Dispatch<MenuAction>
}
