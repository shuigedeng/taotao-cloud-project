import {Item} from "@/api/product/model";

export interface ICartState {
  cartChecked: boolean;
  cartItems: Item[];
  newState: any;
  data: string;
  address: any;
  distance: any;
  receiverInfo: any;
}

export const initCartState: ICartState = {
  cartChecked: true,
  cartItems: [],
  newState: {},
  data: '',
  address: null,
  distance: null,
  receiverInfo: null,
}
