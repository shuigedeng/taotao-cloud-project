import React from "react";

import SiteHeader from "./siteheader";
import HomeHero from "./homehero";
import StarGoods from "./stargoods";
import HomeMain from "./homemain";

const Index: React.FC = () => {
  return (
      <div className="container" >
        <SiteHeader/>
        <HomeHero/>
        <StarGoods/>
        <HomeMain/>
      </div>
  )
}

export default Index
