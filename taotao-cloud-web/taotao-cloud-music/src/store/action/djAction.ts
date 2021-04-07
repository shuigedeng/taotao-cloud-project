export interface IDjAction {
  type: DjActionType,
  payload?: any
}

export enum DjActionType {
  GETDJLISTDETAIL = 'GETDJLISTDETAIL'
}
