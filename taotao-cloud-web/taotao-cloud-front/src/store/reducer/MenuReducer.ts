import {MenuAction, MenuActionType as type} from "../action/MenuAction"
import {IMenuState} from "../state/MenuState"

export function menuToggleReducer(state: IMenuState, action: MenuAction) {
  switch (action.type) {
    case type.MENU_TOGGLE:
      return {
        menuToggle: action.payload
      }
    default:
      return state
  }
}
