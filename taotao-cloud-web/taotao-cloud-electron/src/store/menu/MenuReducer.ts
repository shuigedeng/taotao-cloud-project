import { MenuAction, MenuActionType as type } from './MenuAction'
import { IMenuState } from './MenuState'

export function menuReducer(state: IMenuState, action: MenuAction): IMenuState {
  switch (action.type) {
    case type.MENU_TOGGLE:
      return <IMenuState>{
        ...state,
        menuToggle: action.payload.menuToggle
      }
    default:
      return state
  }
}
