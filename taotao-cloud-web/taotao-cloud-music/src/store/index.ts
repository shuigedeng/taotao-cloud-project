import {applyMiddleware, compose, createStore} from 'redux'
import thunkMiddleware from 'redux-thunk'
import {logger as loggerMiddleware} from 'redux-logger'
import combineReducers from './reducers'

const composeEnhancers =
  typeof window === 'object' &&
  (window as any).__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ ?
    (window as any).__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({
      // Specify extension’s options like name, actionsBlacklist, actionsCreators, serialize...
    }) : compose

const middlewares = [
  thunkMiddleware
]

if (process.env.NODE_ENV === 'development') {
  middlewares.push(loggerMiddleware)
}

const enhancer = composeEnhancers(
  applyMiddleware(...middlewares),
)

export default function configStore() {
  return createStore(combineReducers, enhancer)
}
