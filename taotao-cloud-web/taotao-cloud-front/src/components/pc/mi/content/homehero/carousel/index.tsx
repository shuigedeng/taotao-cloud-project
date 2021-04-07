import React from "react";
import {Carousel} from "antd";
import "../../../style/homehero/banner.less";

const Banner: React.FC = () => {
  return (
      <div>
        <Carousel effect="fade" autoplay={true}>
          <div><a href="/#"><img src="https://i1.mifile.cn/a4/xmad_15151293191126_XcVZW.jpg" alt=""/></a></div>
          <div><a href="/#"><img src="https://i1.mifile.cn/a4/xmad_1513304443358_gzjfM.jpg" alt=""/></a></div>
          <div><a href="/#"><img src="https://i1.mifile.cn/a4/xmad_1515151277162_tHKyF.jpg" alt=""/></a></div>
          <div><a href="/#"><img src="https://i1.mifile.cn/a4/xmad_15151497615198_Qazlx.jpg" alt=""/></a></div>
          <div><a href="/#"><img src="https://i1.mifile.cn/a4/xmad_15039951895346_BYpul.jpg" alt=""/></a></div>
        </Carousel>
      </div>
  )
}

export default Banner
