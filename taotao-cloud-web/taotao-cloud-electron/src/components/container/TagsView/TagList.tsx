import React, { useContext, useEffect, useRef, useState } from 'react'
import { Scrollbars } from 'react-custom-scrollbars'
import { useHistory } from 'react-router'
import { SettingsContext } from '/@/store'
import { IMenu } from '/@/config/menuConfig'
import { Tag } from 'antd'
import {
  tagViewCloseOther,
  tagViewDeleteTag,
  tagViewEmpty
} from '/@/store/settings'

const TagList: React.FC = () => {
  const [state, setState] = useState<{
    left: number
    top: number
    menuVisible: boolean
    currentTag: IMenu
  }>({
    left: 0,
    top: 0,
    menuVisible: false,
    currentTag: {
      title: '',
      path: '',
      icon: '',
      roles: ['']
    }
  })

  const tagListContainer = useRef<HTMLUListElement>(null)
  const contextMenuContainer = useRef<HTMLUListElement>(null)

  const history = useHistory()
  const { tagList, settingsDispatch } = useContext(SettingsContext)

  const handleClickOutside = (event: MouseEvent) => {
    const { menuVisible } = state
    const isOutside = !(
      contextMenuContainer.current &&
      // @ts-ignore
      contextMenuContainer.current.contains(event.target)
    )
    if (isOutside && menuVisible) {
      closeContextMenu()
    }
  }

  useEffect(() => {
    document.body.addEventListener('click', handleClickOutside)

    return () => {
      document.body.removeEventListener('click', handleClickOutside)
    }
  }, [])

  const handleClose = (tag: IMenu) => {
    const path = tag.path
    const currentPath = history.location.pathname
    const length = tagList.length

    // 如果关闭的是当前页，跳转到最后一个tag
    if (path === currentPath) {
      history.push(tagList[length - 1].path)
    }

    // 如果关闭的是最后的tag ,且当前显示的也是最后的tag对应的页面，才做路由跳转
    if (
      path === tagList[length - 1].path &&
      currentPath === tagList[length - 1].path
    ) {
      // 因为cutTaglist在最后执行，所以跳转到上一个tags的对应的路由，应该-2
      if (length - 2 > 0) {
        history.push(tagList[length - 2].path)
      } else if (length === 2) {
        history.push(tagList[0].path)
      }
    }
    // 先跳转路由，再修改state树的taglist
    tagViewDeleteTag(tag)(settingsDispatch)
  }

  const handleClick = (path: string) => {
    history.push(path)
  }

  const openContextMenu = (tag: IMenu, event: React.MouseEvent) => {
    event.preventDefault()
    const menuMinWidth = 105
    const clickX = event.clientX
    //事件发生时鼠标的Y坐标
    const clickY = event.clientY
    // container width
    const clientWidth = tagListContainer.current!.clientWidth
    // left boundary
    const maxLeft = clientWidth - menuMinWidth

    // 当鼠标点击位置大于左侧边界时，说明鼠标点击的位置偏右，将菜单放在左边
    if (clickX > maxLeft) {
      setState(prevState => {
        return {
          ...prevState,
          left: clickX - menuMinWidth + 15,
          top: clickY,
          menuVisible: true,
          currentTag: tag
        }
      })
    } else {
      // 反之，当鼠标点击的位置偏左，将菜单放在右边
      setState(prevState => {
        return {
          ...prevState,
          left: clickX,
          top: clickY,
          menuVisible: true,
          currentTag: tag
        }
      })
    }
  }

  const closeContextMenu = () => {
    setState(prevState => {
      return { ...prevState, menuVisible: false }
    })
  }

  const handleCloseAllTags = () => {
    tagViewEmpty()(settingsDispatch)
    history.push('/dashboard')
    closeContextMenu()
  }

  const handleCloseOtherTags = () => {
    const currentTag = state.currentTag
    const { path } = currentTag
    tagViewCloseOther(currentTag)(settingsDispatch)
    history.push(path)
    closeContextMenu()
  }

  const { left, top, menuVisible } = state
  const currentPath = history.location.pathname
  return (
    <>
      <Scrollbars
        autoHide
        autoHideTimeout={1000}
        autoHideDuration={200}
        hideTracksWhenNotNeeded={true}
        renderView={(props: JSX.IntrinsicAttributes & React.ClassAttributes<HTMLDivElement> & React.HTMLAttributes<HTMLDivElement>) => (
          <div
            {...props}
            className="scrollbar-container"
            style={{ position: 'static' }}
          />
        )}
        renderTrackVertical={(props: JSX.IntrinsicAttributes & React.ClassAttributes<HTMLDivElement> & React.HTMLAttributes<HTMLDivElement>) => (
          <div {...props} className="scrollbar-track-vertical" />
        )}
      >
        <ul className="tags-wrap" ref={tagListContainer}>
          {tagList &&
            tagList.map(tag => (
              <li key={tag.path}>
                <Tag
                  onClose={() => {
                    handleClose(tag)
                  }}
                  closable={tag.path !== '/dashboard'}
                  color={currentPath === tag.path ? 'geekblue' : 'gold'}
                  onClick={() => {
                    handleClick(tag.path)
                  }}
                  onContextMenu={e => {
                    openContextMenu(tag, e)
                  }}
                >
                  {tag.title}
                </Tag>
              </li>
            ))}
        </ul>
      </Scrollbars>

      {menuVisible && (
        <ul
          className="contextmenu"
          style={{ left: `${left}px`, top: `${top}px` }}
          ref={contextMenuContainer}
        >
          <li onClick={handleCloseOtherTags}>关闭其他</li>
          <li onClick={handleCloseAllTags}>关闭所有</li>
        </ul>
      )}
    </>
  )
}

export default TagList
