import { MenuAction, MenuActionType } from './MenuAction'
import { IMenuState } from './MenuState'

export function menuReducer(state: IMenuState, action: MenuAction): IMenuState {
  switch (action.type) {
    case MenuActionType.MENU_TOGGLE:
      return <IMenuState>{
        ...state,
        menuToggle: action.payload.menuToggle
      }
    default:
      return state
  }
}
