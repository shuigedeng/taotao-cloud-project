import React from "react";
import Header from '../header'
import Content from '../content'
import Footer from '../footer'

import "antd/dist/antd.css";

const Index: React.FC = () => {
  return (
      <div>
        <Header/>
        <Content/>
        <Footer/>
      </div>
  )
}

export default Index
