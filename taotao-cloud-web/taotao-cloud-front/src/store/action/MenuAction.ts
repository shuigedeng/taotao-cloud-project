export interface MenuAction {
  type: MenuActionType,
  payload?: any
}

export enum MenuActionType {
  MENU_TOGGLE = 'menuToggle',
}
