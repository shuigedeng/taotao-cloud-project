import React from 'react'
import { MenuAction, MenuActionType } from './MenuAction'

export const setMenuToggle = (data: boolean) => (
  dispatch: React.Dispatch<MenuAction>
) => {
  return dispatch({
    type: MenuActionType.MENU_TOGGLE,
    payload: { menuToggle: data }
  })
}
