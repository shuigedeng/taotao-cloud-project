import zhCN from 'antd/lib/locale-provider/zh_CN'
import zh_CN from '../../locales/zh-CN'
import { Locale } from 'antd/lib/locale-provider'
import React from 'react'
import { LocaleAction } from '/@/store/locale/LocaleAction'
import { MessageFormatElement } from 'intl-messageformat-parser'

export const localeInitState: ILocaleState = {
  locale: 'zh',
  messages: zh_CN,
  antdLocale: zhCN,
  localeDispatch: () => {}
}

export interface ILocaleState {
  locale: string
  messages: Record<string, string> | Record<string, MessageFormatElement[]>
  antdLocale: Locale
  localeDispatch: React.Dispatch<LocaleAction>
}
