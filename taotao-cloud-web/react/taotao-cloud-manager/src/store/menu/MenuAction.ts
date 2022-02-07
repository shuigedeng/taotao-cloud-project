export interface MenuAction {
  type: MenuActionType
  payload: MenuActionPayload
}

export interface MenuActionPayload {
  menuToggle?: boolean
}

export enum MenuActionType {
  MENU_TOGGLE = 'menuToggle'
}
