import React, {useState} from "react";
import {Spin} from "antd";

type IProps = {
  data: Array<any>
}

const DetailLeft: React.FC<IProps> = (props: IProps) => {
  const [state, setState] = useState({
    index: 0,
    display: "none",
    left: 0,
    top: 0,
    zIndex: -1
  })

  const wh = 300;
  let left = state.left - wh / 2;
  left = (state.left - wh / 2 < 0) ? 0 : left;
  left = (state.left >= (450 - wh / 2)) ? (450 - wh) : left;
  let top = state.top - wh / 2;
  top = (state.top - wh / 2 < 0) ? 0 : top;
  top = (state.top >= (450 - wh / 2)) ? (450 - wh) : top;

  const change = (index: number) => {
    setState(prevState => {
      return {...prevState, index: index}
    })
  }

  const move = (e: { nativeEvent: { offsetY: any; offsetX: any; }; }) => {
    setState(prevState => {
      return {
        ...prevState,
        display: "block",
        zIndex: 10,
        top: e.nativeEvent.offsetY,
        left: e.nativeEvent.offsetX
      }
    })
  }

  const leave = () => {
    setState(prevState => {
      return {
        ...prevState,
        display: "none",
        zIndex: -1
      }
    })
  }

  let content = props.data.length ? <div style={{
    position: "relative"
  }}>
    <div style={{
      marginTop: 10,
      border: "1px solid #eeeeee",
    }}>
      <div style={{
        width: 450,
        height: 450,
        position: "absolute",
        top: 0,
        left: 0,
        zIndex: 1,
        cursor: "move"
      }}
           onMouseMove={move}
           onMouseLeave={leave}/>
      <img src={props.data[0].content.big_imgs[state.index]} alt=""/>
      <div style={{
        width: wh,
        height: wh,
        backgroundColor: "rgba(255,165,0,0.5)",
        position: "absolute",
        top: top,
        left: left,
        display: state.display
      }}/>
    </div>
    <ul style={{
      display: "flex",
      flexDirection: "row",
      justifyContent: "space-around",
      alignItems: "center",
      paddingTop: 5,
      marginTop: 20
    }}>
      <li><img src="https://static.360buyimg.com/item/default/1.0.37/components/preview/i/disabled-prev.png"/></li>
      {
        props.data[0].content.sm_imgs.map((item: any, index: number) => {
          let border = state.index == index ? "2px solid #ff6700" : "2px solid white";
          return (
              <li key={index} style={{
                padding: 2,
                border: border
              }} onMouseOver={() => change(index)}>
                <img src={item} alt=""/>
              </li>
          )
        })

      }
      <li>
        <img src="https://static.360buyimg.com/item/default/1.0.37/components/preview/i/disabled-next.png" alt=""/>
      </li>
    </ul>
    <div style={{
      position: "absolute",
      left: 460,
      top: 0,
      width: 542,
      height: 542,
      overflow: "hidden",
      zIndex: state.zIndex
    }}>
      <img src={props.data[0].content.mBig_imgs[state.index]} style={{
        position: "relative",
        display: state.display,
        left: -1.4 * left,
        top: -1.4 * top
      }} alt=""/>
    </div>
  </div> : <Spin tip="Loading..."/>


  return (
      <div>
        {content}
      </div>
  )
}
export default DetailLeft
