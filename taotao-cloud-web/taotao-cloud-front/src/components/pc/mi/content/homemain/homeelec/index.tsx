import React, {useState} from "react";
import Title from "./title";
import LeftADS from "./leftads";
import RightItem from "./rightitem";

type IProps = {
  key: number,
  ads: Array<string>,
  itemsData?: Array<any>,
  title?: {
    title: string
    tab: Array<string>
  }
}

const HomeEelec: React.FC<IProps> = (props: IProps) => {
  const [state, setState] = useState({
    index: 0
  })

  const change = (index: number) => {
    setState(prevState => {
      return {...prevState, index: index}
    })
  }
  return (
      <div>
        <Title data={props.title} change={change}/>
        <div style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-around"
        }}>
          <LeftADS ads={props.ads}/>
          <RightItem itemsData={props.itemsData} itemIndex={state.index}/>
        </div>
      </div>
  )
}

export default HomeEelec
