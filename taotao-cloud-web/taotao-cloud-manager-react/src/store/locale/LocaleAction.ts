import { Locale } from 'antd/lib/locale-provider'
import { MessageFormatElement } from 'intl-messageformat-parser'

export interface LocaleAction {
  type: LocaleActionType
  payload: LocaleActionPayload
}

export interface LocaleActionPayload {
  locale?: string
  messages?: Record<string, string> | Record<string, MessageFormatElement[]>
  antdLocale?: Locale
}

export enum LocaleActionType {
  LOCALE_TOGGLE = 'localeToggle'
}
