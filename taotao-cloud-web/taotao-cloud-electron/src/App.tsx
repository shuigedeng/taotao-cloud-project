import React, { useState } from 'react';
import logo from './logo.svg';
import './App.css';

function App() {
  const [count, setCount] = useState(0);

  return (
    <div className='text-center'>
      <header className='App-header min-h-screen flex flex-col items-center justify-center text-white'>
        <img src={logo} className='h-40 App-logo' alt='logo' />
        <p className='mb-5'>Hello Vite + React + Typescript + Electron</p>
        <p>
          <button
            className='border border-solid border-white rounded-lg p-3 hover:bg-yellow-500 outline-none ring-0'
            onClick={() => setCount((count) => count + 1)}
          >
            Press
          </button>
        </p>
        <p className='mt-7'>count is: {count}</p>
        <p>
          <a
            className='text-gray-600'
            href='https://reactjs.org'
            target='_blank'
            rel='noopener noreferrer'
          >
            Learn React
          </a>
          {' | '}
          <a
            className='text-gray-600'
            href='https://vitejs.dev/guide/features.html'
            target='_blank'
            rel='noopener noreferrer'
          >
            Vite Docs
          </a>
        </p>
      </header>
    </div>
  );
}

export default App;
