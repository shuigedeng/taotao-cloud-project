import React, {useState} from "react";
import "../../../style/homemain/title.less";

interface TitleProps {
  title: string,
  tab: Array<any>
}

type IProps = {
  data?: TitleProps,
  change?: (index: number) => void
}

const Title: React.FC<IProps> = (props: IProps) => {
  let [state, setState] = useState({
    index: 0
  })

  const over = (index: number) => {
    setState(prevState => {
      return {...prevState, index: index}
    })
    props.change && props.change(index);
  }

  return (
      <div>
        <div className="bx-hd">
          <h2>{props.data && props.data.title}</h2>
          <div>
            <ul className="tab-list">
              {
                props.data && props.data.tab.map((item: any, index: number) => {
                  let borderBottom = state.index == index ? "2px solid #ff6700" : "2px solid white";
                  return (
                      <li style={{borderBottom: borderBottom, cursor: "pointer"}} key={index}
                          onMouseOver={() => over(index)}>{item}</li>
                  )
                })
              }
            </ul>
          </div>
        </div>
      </div>
  )
}

export default Title

