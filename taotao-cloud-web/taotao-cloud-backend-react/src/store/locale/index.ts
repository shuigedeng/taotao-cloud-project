import React from 'react'
import { LocaleAction, LocaleActionType } from '/@/store/locale/LocaleAction'
import { LocaleEnum } from '/@/enums/localeEnum'
import zh_CN from '/@/locales/zh-CN'
import en_US from '/@/locales/en-US'
import zhCN from 'antd/lib/locale-provider/zh_CN'
import enUS from 'antd/lib/locale-provider/en_US'

export const localeToggle = (localeEnum: LocaleEnum) => (
  dispatch: React.Dispatch<LocaleAction>
) => {
  dispatch({
    type: LocaleActionType.LOCALE_TOGGLE,
    payload: {
      locale: localeEnum,
      messages: getMessage(localeEnum),
      antdLocale: getAntdLocale(localeEnum)
    }
  })
}

export const getMessage = (localeEnum: LocaleEnum) => {
  let returnMsg
  switch (localeEnum) {
    case LocaleEnum.zh:
      returnMsg = zh_CN
      break
    case LocaleEnum.en:
      returnMsg = en_US
      break
  }
  return returnMsg
}

export const getAntdLocale = (localeEnum: LocaleEnum) => {
  let returnMsg
  switch (localeEnum) {
    case LocaleEnum.zh:
      returnMsg = zhCN
      break
    case LocaleEnum.en:
      returnMsg = enUS
      break
  }
  return returnMsg
}
