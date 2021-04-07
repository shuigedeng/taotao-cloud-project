export interface UserAction {
  type: UserActionType,
  payload?: any
}

export enum UserActionType {
  SUCCESS = 'success',
}
