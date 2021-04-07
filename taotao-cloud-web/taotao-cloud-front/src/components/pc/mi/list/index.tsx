import React from "react";
import Header from "../header";
import Footer from "../footer";
import {Link} from "react-router-dom";
import {Collapse} from 'antd';
import SiteHeader from "../content/siteheader";
import ListItem from "./item";
import "../style/list/list.less";
import listData from "../data/mimenu.json";

const Panel = Collapse.Panel;

const List: React.FC = (props: any) => {
  return (
      <div>
        <Header/>
        <div className="container">
          <SiteHeader/>
          <div>
            <Link className="listTitle" to="/">首页</Link>
            <span> &gt; </span>
            <Link className="listTitle" to="/list/1">所有分类</Link>
          </div>
          <div>
            <Collapse defaultActiveKey={props.match.params.defaultActiveKey}>
              {
                listData.map((item, index) => {
                  return (
                      <Panel style={{
                        fontSize: 18
                      }} key={index + 1} header={item.title}>
                        <ListItem data={item.items}/>
                      </Panel>
                  )
                })
              }
            </Collapse>
          </div>
        </div>
        <Footer/>
      </div>
  )
}

export default List
