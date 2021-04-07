import {djListInitState, DjListState} from "../../state/djState";
import {DjActionType, IDjAction} from "../../action/djAction";

// 获取电台节目列表详情
export default function index(state: DjListState = djListInitState, action: IDjAction) {
  switch (action.type) {
    case DjActionType.GETDJLISTDETAIL:
      const {djListDetailInfo} = action.payload
      return {
        ...state,
        djListDetailInfo: djListDetailInfo
      }
    default:
      return state
  }
}
