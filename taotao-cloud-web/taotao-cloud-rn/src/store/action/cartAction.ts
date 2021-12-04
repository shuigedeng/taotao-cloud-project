export interface ICartAction {
  type: CartActionType,
  payload?: any
}

export enum CartActionType {
  DELETE = 'delete',
  SAVE = 'save',
  ADD_TO_CART_BY_CODE = 'addToCartByCode',
  CART_ITEMS_APPEND = 'cartItemsAppend',
  ADD_ONE_TO_CART_ITEM_NUMBER_BY_INDEX = 'addOneToCartItemNumberByIndex',
  CHANGE_CART_ITEMS_CHECKED = 'changeCartItemsChecked',
  CHANGE_ALL_CHECKED_CART_ITEMS = 'changeAllCheckedCartItems',
  DELETE_CHECKED_CART_ITEMS = 'deleteCheckedCartItems',
  DELETE_CART_ITEMS = 'deleteCartItems',
  CHANGE_CART_ITEM_NUMBER = 'changeCartItemNumber',
  PRE_ADDRESS = 'preAddress',
}
