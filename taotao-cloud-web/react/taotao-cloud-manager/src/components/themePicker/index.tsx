import React, { useState } from 'react'
import { SketchPicker } from 'react-color'
import classNames from 'classnames'
import Index from '../icon'
// import "../../style/utils-color.less"
// import "../../style/utils-text.less"
// import "../../style/menu.less"

interface IState {
  switcherOn: boolean
  background: string
}

const ThemePicker: React.FC = () => {
  const initState = () => {
    return {
      switcherOn: false,
      background: localStorage.getItem('@primary-color') || '#313653'
    }
  }

  const [state, setState] = useState<IState>(initState)

  const switcherOn = () => {
    setState(prevState => {
      return { ...prevState, switcherOn: !prevState.switcherOn }
    })
  }

  const handleChangeComplete = (color: any) => {
    console.log(color)
    setState(prevState => {
      return { ...prevState, background: color.hex }
    })
    localStorage.setItem('@primary-color', color.hex)
    ;(window as any).less.modifyVars({
      '@primary-color': color.hex
    })
  }

  return (
    <div className={classNames('switcher dark-white', { active: switcherOn })}>
      <span className="sw-btn dark-white" onClick={switcherOn}>
        <Index type="iconsetting" className="text-dark" />
      </span>
      <div style={{ padding: 10 }} className="clear">
        <SketchPicker
          color={state.background}
          onChangeComplete={handleChangeComplete}
        />
      </div>
    </div>
  )
}

export default ThemePicker
