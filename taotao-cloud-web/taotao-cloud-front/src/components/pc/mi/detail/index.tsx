import React, {useEffect, useState} from "react";
import "../style/detail/detail.less";
import Header from "../header";
import Footer from "../footer";
import SiteHeader from "../content/siteheader/index.js";
import DetailLeft from "./detailLeft";
import DetailRight from "./detailRight";
import detailData from "../data/detail.json";
import {Col, Row} from "antd";

const DetailIndex: React.FC = (props: any) => {
  const [state, setState] = useState<{ data: Array<any> }>({
    data: []
  })

  useEffect(() => {
    let data: any[] = [];
    detailData.map((item) => {
      if (item.id == props.match.params.id) {
        data.push(item);
        setState(prevState => {
          return {...prevState, data: data}
        })
      }
    })
  }, [])


  return (
      <div>
        <Header/>
        <div className="container">
          <SiteHeader/>
          <Row>
            <Col span={9}>
              <DetailLeft data={state.data}/>
            </Col>
            <Col span={11}>
              <DetailRight data={state.data}/>
            </Col>
          </Row>
        </div>
        <Footer/>
      </div>
  )
}

export default DetailIndex
