export default {
    namespace: 'classification',
  
    state: {
        cartChecked:true,
        cartItems: [],
        newState: {},
        data:''
    },
    effects: {
      },
    reducers: {
        save(state, { payload }) {
            return { ...state, ...payload };
          },
    },
  };