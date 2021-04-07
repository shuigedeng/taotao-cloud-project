import React, {useEffect, useState} from "react";
import Swiper from 'swiper';
import 'swiper/swiper.min.css';
import "../../../style/homemain/contentItem.less";
import "../../../style/homemain/video.less";

const ContentItem: React.FC = (props: any) => {
  let [state, setState] = useState({
    bgColor: "white",
    color: ""
  })

  useEffect(() => {
    setState(prevState => {
      return {...prevState, color: props.data.color}
    })
    new Swiper('.swiper-container', {
      autoplay: false,//可选选项，自动滑动
      pagination: {
        el: '.swiper-pagination',
        clickable: true,
      },
      navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
      },
      allowTouchMove: false
    })
  }, [])

  const change = () => {
    setState(prevState => {
      return {...prevState, color: "white", bgColor: props.data.color}
    })
  }

  const leave = () => {
    setState(prevState => {
      return {...prevState, bgColor: "white", color: props.data.color}
    })
  }

  let priceStyle = {
    color: "#333",
    fontSize: 14
  }

  let bgColor = state.bgColor;
  let color = state.color;
  let titleColor = props.data.color;
  let content = props.data.content.map((item: any, index: number) => {
    return (
        props.data.content.length - index == 1 ?
            <div key={index} className="swiper-slide flex" style={{width: 296, height: 340}}>
              <p style={{
                width: 200,
                height: 40
              }}>{item.name}</p>
              <a style={{
                display: "block",
                width: 120,
                height: 30,
                color: color,
                border: "1px solid",
                borderColor: color,
                backgroundColor: bgColor,
                textAlign: "center",
                fontSize: 12,
                paddingTop: 5,
                boxSizing: "border-box"
              }}
                 onMouseOver={change}
                 onMouseLeave={leave}>{item.desc}</a>
              <img style={{
                margin: "30px 40px 0 40px"
              }} src={item.imgSrc} alt=""/>
            </div> : <div key={index} className="swiper-slide flex" style={{width: 296, height: 340}}>
              <p style={{
                color: "#333",
                fontSize: 20,
                fontWeight: 400,
                textAlign: "center"
              }}>{item.name}</p>
              <p style={{
                width: 200,
                height: 40,
                color: "#b0b0b0",
                fontSize: 12,
                textAlign: "center"
              }}>{item.desc}</p>
              <p style={priceStyle}>{item.price}</p>
              <img src={item.imgSrc} alt=""/>
            </div>
    )
  })

  return (
      <div style={{paddingTop: 45, borderTop: "1px solid", borderColor: titleColor}} className="videoItem">
        <div>
          <h2 style={{
            color: titleColor,
            textAlign: "center",
            fontSize: 16,
            fontWeight: 400,
            margin: "0 10px 18px"
          }}>{props.data.title}</h2>
        </div>
        <div className="swiper-container" style={{width: 296}}>
          <div className="swiper-wrapper">
            {content}
          </div>
          <div className="swiper-pagination" />
          <div className="swiper-button-prev swiper-navigation">
            {/*<Icon type="left"/>*/}
          </div>
          <div className="swiper-button-next swiper-navigation">
            {/*<Icon type="right"/>*/}
          </div>
        </div>

      </div>
  )
}
export default ContentItem
