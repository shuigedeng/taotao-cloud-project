export default {

  namespace: 'shoppingCart',

  state: {},

  effects: {
    * init({ payload: code }, { call, put }) {
      const response = yield call(code);
      yield put({
        type: 'save',
        payload: {
          getCode: response,
        }
      });
    },
  },

  reducers: {
    save(state, { payload }) {
      return {
        ...state,
        ...payload
      };
    },
  },
};
