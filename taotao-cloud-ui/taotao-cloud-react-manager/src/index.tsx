import React from 'react'
import ReactDOM from 'react-dom'
import App from '/@/components/app'
import '/@/styles/tailwind.less'
import 'antd/dist/antd.less'

import { setupProdMockServer } from '../mock/_createProductionServer';

if (process.env.NODE_ENV === 'production') {
  setupProdMockServer();
}

ReactDOM.render(
  <React.StrictMode>
    <App/>
  </React.StrictMode>,
  document.getElementById('root')
)
