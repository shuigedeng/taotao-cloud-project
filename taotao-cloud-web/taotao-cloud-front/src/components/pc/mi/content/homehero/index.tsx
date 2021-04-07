import React from "react";
import Banner from "./carousel";
import Menu from "./menu";
import Sub from "./sub";

const HomeHero: React.FC = () => {
  return (
      <div style={{position: "relative"}}>
        <Banner/>
        <Menu/>
        <Sub/>
      </div>
  )
}

export default HomeHero
