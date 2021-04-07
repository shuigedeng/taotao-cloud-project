import React, { useState } from 'react'
import img from '/@/assets/images/500.png'

const View500: React.FC = () => {
  const [state, setState] = useState({ animated: '' })
  const enter = () => {
    setState({ animated: 'hinge' })
  }
  return (
    <div
      className="center"
      style={{ height: '100%', background: '#ececec', overflow: 'hidden' }}
    >
      <img
        src={img}
        alt="404"
        className={`animated swing ${state.animated}`}
        onMouseEnter={enter}
      />
    </div>
  )
}

export default View500
