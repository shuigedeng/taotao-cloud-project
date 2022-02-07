import React from 'react'
import ReactDOM from 'react-dom'
import './index.css'
import App from './components/App'

import process from 'process';

window.process = process;
(window as any).global = window;

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
)
