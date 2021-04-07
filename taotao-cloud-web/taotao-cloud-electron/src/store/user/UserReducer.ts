import { UserAction, UserActionType as type } from '../user/UserAction'
import { IUserState, userInitState } from './UserState'

export function userReducer(state: IUserState, action: UserAction): IUserState {
  switch (action.type) {
    case type.USER_SET_LOGIN_SUCCESS:
      return <IUserState>{
        ...state,
        ...action.payload
      }
    case type.USER_SET_LOGIN_TOKEN:
      return <IUserState>{
        ...state,
        token: action.payload.token
      }
    case type.USER_RESET_USER:
      return userInitState
    default:
      return state
  }
}
