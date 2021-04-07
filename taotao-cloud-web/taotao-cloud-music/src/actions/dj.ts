import api from '../services/api'
import {DjActionType} from "../store/action/djAction";

// 获取电台节目列表详情
const getDjListDetail = (payload: any) => (dispatch: any) => {
  api.get('/dj/program/detail', {
    id: payload.id
  }).then((res) => {
    dispatch({
      type: DjActionType.GETDJLISTDETAIL,
      payload: {
        djListDetailInfo: res.data.ids || []
      }
    })
  })
}

export default getDjListDetail


