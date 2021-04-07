import React from "react";
import pageData from "../../data/pagemain.json";
import leftAds from "../../data/leftads.json";
import HomeEelec from "./homeelec";
import ReviewIndex from "./review";
import Content from "./content";
import VideoIndex from "./video";

const HomeMain: React.FC = () => {
  return (
      <div>
        {
          pageData.map((_item: any, index) => {
            return (
                <HomeEelec key={index}
                           ads={leftAds[index].leftAds}
                           itemsData={pageData[index][1].gooData}
                           title={pageData[index][0].titleData}/>
            )
          })
        }
        <ReviewIndex/>
        <Content/>
        <VideoIndex/>
      </div>
  )
}

export default HomeMain
