import Taro from "@tarojs/taro";
import {Item} from "@/api/product/model";

export function cartTabBarBadge(cartItems: Item[]) {
// 购物车右上角图标
  if (cartItems.length === 0) {
    Taro.removeTabBarBadge({
      index: 2
    })
  } else {
    let sum = 0;
    let i;
    for (i in cartItems) {
      if (cartItems[i].checked) {
        // @ts-ignore
        sum += parseInt(cartItems[i].number);
      }
    }
    Taro.setTabBarBadge({
      index: 2,
      text: "" + sum + "",
    })
  }
}

