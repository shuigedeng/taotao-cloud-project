import React, { Suspense, useReducer } from 'react'
import { ApolloProvider } from '@apollo/client'
import { ConfigProvider, Spin } from 'antd'
import { IntlProvider } from 'react-intl'
import { HelmetProvider } from 'react-helmet-async'
import {
  LocaleContext,
  MenuContext,
  SettingsContext,
  UserContext
} from '/@/store'
import { userInitState } from '/@/store/user/UserState'
import { menuInitState } from '/@/store/menu/MenuState'
import { localeInitState } from '/@/store/locale/LocaleState'
import { settingsInitState } from '/@/store/settings/SettingsState'
import { userReducer } from '/@/store/user/UserReducer'
import { menuReducer } from '/@/store/menu/MenuReducer'
import { localeReducer } from '/@/store/locale/LocaleReducer'
import { settingsReducer } from '/@/store/settings/SettingsReducer'
import Routers from '/@/components/routers'
import { client } from '/@/http/graphql'

const App: React.FC = () => {
  const [menuState, menuDispatch] = useReducer(menuReducer, menuInitState)
  const [userState, userDispatch] = useReducer(userReducer, userInitState)
  const [localeState, localeDispatch] = useReducer(
    localeReducer,
    localeInitState
  )
  const [settingsState, settingsDispatch] = useReducer(
    settingsReducer,
    settingsInitState
  )

  return (
    <SettingsContext.Provider
      value={{ ...settingsState, settingsDispatch: settingsDispatch }}
    >
      <UserContext.Provider
        value={{ ...userState, userDispatch: userDispatch }}
      >
        <MenuContext.Provider
          value={{ ...menuState, menuDispatch: menuDispatch }}
        >
          <LocaleContext.Provider
            value={{ ...localeState, localeDispatch: localeDispatch }}
          >
            <IntlProvider
              messages={localeState.messages}
              locale={localeState.locale}
              defaultLocale={localeInitState.locale}
            >
              <Suspense
                fallback={
                  // <div className="text-center bg-yellow-300">
                  //   <Spin className="" size="large" tip="页面加载中..." />
                  // </div>

                  <div className="flex w-screen h-screen justify-center items-center flex-col bg-f4f7f9">
                    <div className="absolute flex inset-y-2/4 inset-x-2/4 ">
                      <img
                        src="/resource/img/logo.png"
                        className="app-loading-logo"
                        alt="Logo"
                      />
                      <div className="app-loading-dots">
                        <span className="dot dot-spin">
                          <i></i>
                          <i></i>
                          <i></i>
                          <i></i>
                        </span>
                      </div>
                      <div className="app-loading-title"></div>
                    </div>
                  </div>
                }
              >
                <ConfigProvider locale={localeState.antdLocale}>
                  <ApolloProvider client={client}>
                    <HelmetProvider>
                      <Routers />
                    </HelmetProvider>
                  </ApolloProvider>
                </ConfigProvider>
              </Suspense>
            </IntlProvider>
          </LocaleContext.Provider>
        </MenuContext.Provider>
      </UserContext.Provider>
    </SettingsContext.Provider>
  )
}

export default App
