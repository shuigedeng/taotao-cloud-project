import React from "react";

type IProps = {
  data: Array<any>
}

const ListItem: React.FC<IProps> = (props: IProps) => {
  return (
      <div>
        <ul className="list-items">{
          props.data.map((it: any, i: number) => {
            var borderT = "1px solid #e0e0e0";
            var borderL = ((i + 1) % 5) == 0 ? "1px solid white" : "1px solid #e0e0e0"
            return (
                <li key={i} className="list-item" style={{
                  borderTop: borderT,
                  borderRight: borderL
                }}>
                  <a style={{
                    margin: "0 auto 24px"
                  }}>
                    <img src={it.imgSrc} alt=""/>
                  </a>
                  <p className="list-good-name">{it.name}</p>
                </li>
            )
          })
        }</ul>
      </div>
  )
}

export default ListItem
