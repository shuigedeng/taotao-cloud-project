import React from "react";
import Title from "../homeelec/title";
import ContentItem from "./contentItem";
import contentData from "../../../data/content.json";

const Content: React.FC = () => {
  return (
      <div>
        // @ts-ignore
        <Title data={{"title": "内容", "tab": []}}/>
        <div>
          <div style={{
            display: "flex",
            flexDirection: "row",
            justifyContent: "space-around",
            alignItems: "center"
          }}>{
            contentData.map((item, index) => {
              return (
              // @ts-ignore
                  <ContentItem key={index} data={item}/>
              )
            })
          }</div>
        </div>
      </div>
  )
}

export default Content
