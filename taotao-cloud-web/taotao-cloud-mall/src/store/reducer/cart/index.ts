import {CartActionType, ICartAction} from "@/store/action/cartAction";
import {ICartState, initCartState} from "@/store/state/cartState";

export default function index(state: ICartState = initCartState, action: ICartAction): ICartState {
  let {cartItems} = state

  switch (action.type) {
    case CartActionType.DELETE:
      console.log('商品删除', cartItems);
      return deleteCheckedCartItems(state)
    case CartActionType.SAVE:
      return {...state, ...action.payload};
    case CartActionType.ADD_TO_CART_BY_CODE:
      console.log('加入购物车cartItems', cartItems);
      if (cartItems) {
        console.log('cartItems.length > 0');
        Taro.showTabBarRedDot({
          index: 2
        })
      }

      let index;
      if (action.payload.itemId) {
        index = cartItems.findIndex(currentValue => currentValue.itemId === action.payload.itemId);
      } else {
        index = cartItems.findIndex(currentValue => currentValue.itemId === action.payload);
      }
      console.log('index', index);

      if (index === -1) {
        try {
          let result = cartItemsAppend(state, action.payload.item);
          setTimeout(function () {
            Taro.showToast({
              title: '成功加入购物车',
              icon: 'success',
              mask: true,
            })
          }, 100)
          return result
        } catch (e) {
          Taro.showToast({
            title: '加入失败请重试',
            duration: 1500,
            mask: true,
            image: 'https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/failure.png',
          });
          break
        }
      } else {
        let result = addOneToCartItemNumerByIndex(state, action.payload.index);
        setTimeout(function () {
          Taro.showToast({
            title: '成功加入购物车',
            icon: 'success',
            mask: true,
          })
        }, 10)
        return result;
      }
    case CartActionType.CART_ITEMS_APPEND:
      return cartItemsAppend(state, action.payload.item)
    case CartActionType.ADD_ONE_TO_CART_ITEM_NUMBER_BY_INDEX:
      return addOneToCartItemNumerByIndex(state, action.payload.index)
    case CartActionType.CHANGE_CART_ITEMS_CHECKED:
      const currentCartChecked = action.payload.checkeds.findIndex(currentValue => currentValue === 'all') !== -1;
      const cartItemCheckedCodes = action.payload.checkeds.filter(currentValue => currentValue !== 'all');
      for (let i = 0; i < cartItems.length; i++) {
        const cartItem = cartItems[i];
        cartItem.checked = cartItemCheckedCodes.findIndex(currentValue => currentValue === cartItem.itemId) !== -1;
      }
      return {
        ...state,
        cartChecked: currentCartChecked,
        cartItems: cartItems.slice(),
      };
    case CartActionType.CHANGE_ALL_CHECKED_CART_ITEMS:
      for (let i = 0; i < cartItems.length; i++) {
        cartItems[i].checked = !action.payload.check;
      }
      return state;
    case CartActionType.DELETE_CHECKED_CART_ITEMS:
      return deleteCheckedCartItems(state)
    case CartActionType.DELETE_CART_ITEMS:
      console.log('deleteCartItems');

      cartItems.findIndex((currentValue, index) => {
        if (currentValue.checked !== false) {
          cartItems.splice(index, 1)
        }
      });
      return {
        ...state,
        cartItems: cartItems.slice(),
      }
    case CartActionType.CHANGE_CART_ITEM_NUMBER:
      console.log('获取商品数量');
      const indexData = cartItems.findIndex(currentValue => currentValue.itemId === action
        .payload.itemId);
      cartItems[indexData].number = action.payload.number;
      return {
        ...state,
        cartItems: cartItems.slice(),
      };
    case CartActionType.PRE_ADDRESS:
      return {
        ...state,
        receiverInfo: action.payload.receiverInfo,
        distance: action.payload.distance,
      };
    default:
      return state
  }
}

const deleteCheckedCartItems = (state: ICartState) => {
  let {cartItems} = state
  console.log('deleteCheckedCartItems', cartItems);
  const uncheckedCartItems = cartItems.filter(currentValue => !currentValue.checked);
  console.log('deleteCheckedCartItems uncheckedCartItems', uncheckedCartItems);
  return {
    ...state,
    cartItems: uncheckedCartItems,
  };
}

const cartItemsAppend = (state: ICartState, item) => {
  const {cartItems} = state;
  console.log('存入加入购物车的值', cartItems);
  cartItems.push({...item, checked: true});
  return {
    ...state,
    cartItems: cartItems.slice(),
  };
}

const addOneToCartItemNumerByIndex = (state: ICartState, index) => {
  const {cartItems} = state;
  cartItems[index].number += 1;
  return {
    ...state,
    cartItems: cartItems.slice(),
  };
}
