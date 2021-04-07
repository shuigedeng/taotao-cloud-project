import React, { useContext, useState } from 'react'
import { Drawer, List, Switch } from 'antd'
import { SettingOutlined } from '@ant-design/icons'
import { SettingsContext } from '/@/store'
import { setHeadFixed, setTopNavEnable } from '/@/store/settings'

const Settings: React.FC = () => {
  const [visible, setVisible] = useState(false)
  const [disabledHeadFixed, setDisabledHeadFixed] = useState(true)
  const { settingsDispatch, topNavEnable, headFixed } = useContext(
    SettingsContext
  )

  const onChangeHeadFixed = (v: boolean): void => {
    setHeadFixed(v)(settingsDispatch)
  }

  const onChangeTopNavEnable = (v: boolean): void => {
    setTopNavEnable(v)(settingsDispatch)

    if (v) {
      setDisabledHeadFixed(true)
      onChangeHeadFixed(true)
    } else {
      setDisabledHeadFixed(false)
    }
  }

  return (
    <>
      <div
        onClick={() => {
          setVisible(true)
        }}
        style={{
          position: 'fixed',
          display: 'block',
          width: '45px',
          height: '45px',
          lineHeight: '48px',
          right: '0',
          top: '30%',
          backgroundColor: '#222834',
          textAlign: 'center',
          cursor: 'pointer',
          borderRadius: '5px 0 0 5px'
        }}
      >
        <SettingOutlined style={{ fontSize: '20px', color: '#fcfcfc' }} />
      </div>
      <Drawer
        title="系统布局配置"
        onClose={() => {
          setVisible(false)
        }}
        visible={visible}
        bodyStyle={{ padding: '10px' }}
      >
        <List size="small">
          <List.Item
            actions={[
              <Switch checked={topNavEnable} onChange={onChangeTopNavEnable} />
            ]}
          >
            启用顶部导航
          </List.Item>
          <List.Item
            actions={[
              <Switch
                checked={headFixed}
                disabled={disabledHeadFixed}
                onChange={onChangeHeadFixed}
              />
            ]}
          >
            固定右侧头部
          </List.Item>
        </List>
      </Drawer>
    </>
  )
}

export default Settings
