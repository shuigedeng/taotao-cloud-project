import {ADD, MINUS} from '../../constants/counter'

export const add = () => (dispatch) => {
  return dispatch({
    type: ADD
  })
}

export const minus = () => (dispatch) => {
  return dispatch({
    type: MINUS
  })
}

export const asyncAdd = () => (dispatch) => {
  return setTimeout(() => {
    dispatch({
      type: ADD
    })
  }, 2000)
}

