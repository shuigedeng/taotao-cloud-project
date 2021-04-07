import React from "react";
import Routers from "./router";
import {hot} from 'react-hot-loader';

const MiPC: React.FC = () => {
  return (
      <Routers/>
  )
}

export default (process.env.NODE_ENV === 'development' ? hot(module)(MiPC) : MiPC);
