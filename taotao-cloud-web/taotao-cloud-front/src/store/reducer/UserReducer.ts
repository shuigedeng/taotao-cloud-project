import {UserAction, UserActionType as type} from "../action/UserAction"
import {IUserState} from "../state/UserState"

export function loginReducer(state: IUserState, action: UserAction) {
  switch (action.type) {
    case type.SUCCESS:
      return {
        ...state,
        token: action.payload,
      }
    default:
      return state
  }
}
