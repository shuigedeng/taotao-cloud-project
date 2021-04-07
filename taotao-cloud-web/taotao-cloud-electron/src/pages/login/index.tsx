import React, { useContext, useEffect, useState } from 'react'
import {
  Button,
  Checkbox,
  Col,
  Dropdown,
  Form,
  Input,
  Menu,
  message,
  Row,
  Spin,
  Tabs
} from 'antd'
import {
  AlipayOutlined,
  DingtalkOutlined,
  GithubFilled,
  MobileOutlined,
  QqOutlined,
  UserOutlined,
  WechatFilled,
  WeiboCircleFilled
} from '@ant-design/icons'
import { Rule } from 'antd/lib/form'
import url from 'url'
import { useIntl } from 'react-intl'

import { useHistory, useLocation } from 'react-router-dom'
import { LocaleContext } from '/@/store'
// import sensors from '/@/http/sa'
import { setLoginUserIsRemember, setLoginUserToken } from '/@/utils/lsUtil'
import './index.less'
import logo from '/@/assets/logo.png'
import api from '/@/api'
import { RuleObject, Store, StoreValue } from 'rc-field-form/lib/interface'
import { LoginParam, LoginVO } from '/@/api/auth/model'
import { Result } from '/@/api/model/baseModel'
import { GlobalOutlined } from '@ant-design/icons/lib'
import { LocaleEnum } from '/@/enums/localeEnum'
import { localeToggle } from '/@/store/locale'
import Loading from '/@/components/loading'
import { MenuInfo } from 'rc-menu/es/interface'

interface Lang {
  [key: string]: string
}

interface IBasicState {
  isShow: boolean
  customActiveKey: string
  loginBtn: boolean
  loginType: number
  codeSrc: string
  imgCodeLoading: boolean
}

interface IState {
  time: number
  loginBtn: boolean
  loginType: number
  smsSendBtn: boolean
  isRemember: boolean
}

interface ILoginForm {
  username: string
  password: string
  code: string
  token: string
  t: number
}

interface ISocialForm {
  code?: string
  state?: string
}

const initBasicState = () => {
  return {
    isShow: true,
    customActiveKey: 'usernameTab',
    loginBtn: false,
    loginType: 0,
    codeSrc: '',
    imgCodeLoading: true
  }
}
const initState = () => {
  return {
    time: 60,
    loginBtn: false,
    loginType: 0,
    smsSendBtn: false,
    isRemember: false
  }
}
const initLoginForm = () => {
  return {
    username: '',
    password: '',
    code: '',
    token: '',
    t: 0
  }
}

const Login: React.FC = () => {
  // const {error, data} = useQuery(LIST_USERS, {errorPolicy: 'all'});
  // if (error) console.log(error.message)
  // console.log(data)

  // const [getDog, { loading, data1 }] = useLazyQuery(LIST_USERS);
  // console.log(getDog())
  // console.log(loading1)
  // console.log(data1)
  const [form] = Form.useForm()
  const { TabPane } = Tabs

  const [basicState, setBasicState] = useState<IBasicState>(initBasicState)
  const [state, setState] = useState<IState>(initState)
  const [loginForm, setLoginForm] = useState<ILoginForm>(initLoginForm)
  const [loading, setLoading] = useState(false)

  const history = useHistory()
  const location = useLocation()
  const { formatMessage } = useIntl()

  const { localeDispatch, locale } = useContext(LocaleContext)

  useEffect(() => {
    let isUnmount = true
    if (isUnmount) {
      // refreshCaptcha()
      // socialLogin()
    }
    return () => {
      isUnmount = false
    }
  }, [])

  const refreshCaptcha = async () => {
    setBasicState(pr => {
      return { ...pr, imgCodeLoading: true }
    })
    const time = new Date().getTime()
    const data = await api.auth.getCaptcha({ t: time })
    if (data) {
      setLoginForm(prevState => {
        return { ...prevState, t: time }
      })

      setBasicState(pr => {
        return { ...pr, codeSrc: data.data, imgCodeLoading: false }
      })
    }
  }

  const socialLogin = () => {
    const params: ISocialForm = url.parse(location.search, true).query
    if (!params.state && !params.code) {
      return
    }

    setBasicState(prevState => {
      return { ...prevState, isShow: false }
    })

    api.auth
      .loginBySocial(params)
      .then((res: any) => res && loginSuccess(res))
      .catch(() => {
        setBasicState(prevState => {
          return { ...prevState, loginBtn: false }
        })
      })
  }

  const loginSuccess = (res: Result<LoginVO>) => {
    // sensors.login(res.data.access_token)
    if (state.isRemember) {
      setLoginUserIsRemember(state.isRemember)
    }
    setLoginUserToken(res.data.access_token)
    setLoading(false)
    // setTimeout(() => {
    //   history.push('/')
    // }, 300)
  }

  const submit = (event: React.MouseEvent) => {
    event.preventDefault()
    setLoading(true)
    setBasicState(prevState => {
      return { ...prevState, loginBtn: true }
    })

    // sensors.track('submitLogin', {
    //   ProductName: 'submitLogin',
    //   ProductPrice: 123.45,
    //   IsAddedToFav: false
    // })

    const validateFieldsKey =
      basicState.customActiveKey === 'usernameTab'
        ? ['username', 'password', 'imgCode']
        : ['mobile', 'smsCode']
    form
      .validateFields(validateFieldsKey)
      .then((values: Store) => {
        const loginParams = { ...values }
        if (values.username) {
          const param: LoginParam = {
            grant_type: 'password',
            client_id: 'backend',
            client_secret: '123456',
            password: values.password,
            t: loginForm.t,
            code: values.imgCode,
            scope: 'all'
          }
          param[!state.loginType ? 'email' : 'username'] = values.username

          api.auth
            .login(param)
            .then((res: any) => res && loginSuccess(res))
            .catch(() => {
              refreshCaptcha()
              setBasicState(prevState => {
                return { ...prevState, loginBtn: false }
              })
              setLoading(false)
            })
            .finally(() => {
              setBasicState(prevState => {
                return { ...prevState, loginBtn: false }
              })
            })
        } else {
          api.auth
            .loginByMobile(loginParams)
            .then((res: any) => res && loginSuccess(res))
            .catch(() => {
              refreshCaptcha()
              setBasicState(prevState => {
                return { ...prevState, loginBtn: false }
              })
              setLoading(false)
            })
            .finally(() => {
              setBasicState(prevState => {
                return { ...prevState, loginBtn: false }
              })
            })
        }
      })
      .catch(error => {
        message.error(error)
        setTimeout(() => {
          setBasicState(prevState => {
            return { ...prevState, loginBtn: false }
          })
        }, 500)
        setLoading(false)
      })
  }

  const validateUsernameOrEmail = (_rule: RuleObject, value: StoreValue) => {
    if (value) {
      const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
      if (regex.test(value)) {
        setBasicState(prevState => {
          return { ...prevState, loginType: 0 }
        })
      } else {
        setBasicState(prevState => {
          return { ...prevState, loginType: 1 }
        })
      }
      return Promise.resolve()
    } else {
      return Promise.reject()
    }
  }

  const validatePhone = (_rule: Rule, value: any) => {
    if (!value) {
      return Promise.reject()
    } else {
      if (new RegExp(/^1[3|4|5|7|8][0-9]\d{8}$/).test(value)) {
        // let params = {
        //   fieldVal: value,
        //   dataId: ''
        // }
        // return api.auth.repeatCheck(params).then((res: any) => {
        //   if (res) {
        //     if (res.code === 200 && res.data) {
        //       return Promise.resolve()
        //     } else {
        //       setState(prevState => {
        //         return {...prevState, smsSendBtn: true}
        //       })
        //       return Promise.reject("该手机号未注册")
        //     }
        //   }
        // }).catch(error => {
        //   return Promise.reject(error)
        // })
      } else {
        return Promise.reject('请输入正确格式的手机号码')
      }
    }
  }

  const getSmsCode = (e: any) => {
    e.preventDefault()
    form.validateFields(['mobile']).then(values => {
      setState(prevState => {
        return { ...prevState, smsSendBtn: true }
      })

      const interval = window.setInterval(() => {
        const second = --state.time
        if (second <= 0) {
          setState(prevState => {
            return { ...prevState, smsSendBtn: false, time: 60 }
          })
          window.clearInterval(interval)
        }
        setState(prevState => {
          return { ...prevState, time: second }
        })
      }, 1000)

      const hide = message.loading('验证码发送中..', 2)
      const params = { mobile: values.mobile }
      api.auth
        .getSmsCode(params)
        .then((res: any) => {
          if (res.code === 200) {
            setTimeout(hide)
            message.success('验证码发送成功', 5)
          } else {
            setTimeout(hide)
            message.error('验证码发送失败', 5)
          }
        })
        .catch((err: any) => {
          setTimeout(hide, 1)
          clearInterval(interval)
          setState(prevState => {
            return { ...prevState, smsSendBtn: false, time: 60 }
          })
          requestFailed(err)
        })
    })
  }

  const requestFailed = (err: any) => {
    setState(prevState => {
      return { ...prevState, loginBtn: false }
    })
    refreshCaptcha()
    message.error(
      ((err.response || {}).data || {}).message || '请求出现错误，请稍后再试',
      4
    )
  }

  const handleTabClick = (key: string) => {
    setBasicState(prevState => {
      return { ...prevState, customActiveKey: key }
    })
  }

  const getSocialUrl = (type: string) => {
    api.auth.getSocialUrl({ loginType: type }).then((res: any) => {
      if (res) {
        window.location.href = res.data
      }
    })
  }

  const locales = ['zh', 'TW', 'en']
  const languageLabels: Lang = {
    zh: '简体中文',
    TW: '繁体中文',
    en: 'English'
  }
  const languageIcons: Lang = {
    zh: '🇨🇳',
    TW: '🇭🇰',
    en: '🇺🇸'
  }

  const changeLang = ({ key }: MenuInfo): void => {
    const result =
      key === 'zh'
        ? LocaleEnum.zh
        : key === 'en'
        ? LocaleEnum.en
        : LocaleEnum.zh
    localeToggle(result)(localeDispatch)
  }

  const langMenu = (
    <Menu
      style={{ float: 'right' }}
      selectedKeys={[locale]}
      onClick={changeLang}
    >
      {locales.map((locale: string) => (
        <Menu.Item key={locale}>
          <span role="img" aria-label={languageLabels[locale]}>
            {languageIcons[locale]}
          </span>{' '}
          {languageLabels[locale]}
        </Menu.Item>
      ))}
    </Menu>
  )

  return basicState.isShow ? (
    <div id="userLayout" className="user-layout-wrapper">
      <div className="login-container">
        <div className="login-top">
          <div className="login-header">
            <a href="/">
              <img src={logo} className="login-logo" alt="logo" />
              <span className="login-title">
                {formatMessage({ id: 'app.login.title' })}
              </span>
            </a>
          </div>
          <div className="desc">{formatMessage({ id: 'app.login.desc' })}</div>
        </div>

        <div className="login-main">
          <Form id="formLogin" className="user-layout-login" form={form}>
            <Spin
              spinning={loading}
              tip={formatMessage({ id: 'app.login.loading' })}
            >
              <Tabs
                activeKey={basicState.customActiveKey}
                tabBarStyle={{ textAlign: 'center', borderBottom: 'unset' }}
                onChange={handleTabClick}
              >
                <TabPane
                  tab={
                    <span>
                      <UserOutlined />
                      {formatMessage({ id: 'app.login.account.login' })}
                    </span>
                  }
                  key="usernameTab"
                >
                  <Form.Item
                    name="username"
                    rules={[
                      {
                        required: true,
                        message: formatMessage({
                          id: 'app.login.require.username'
                        })
                      },
                      {
                        validator: (rules, value) =>
                          validateUsernameOrEmail(rules, value)
                      }
                    ]}
                  >
                    <Input
                      size="large"
                      placeholder={formatMessage({
                        id: 'app.login.require.username'
                      })}
                      style={{ borderRadius: '7px' }}
                      autoComplete="false"
                    />
                  </Form.Item>
                  <Form.Item
                    name="password"
                    rules={[
                      {
                        required: true,
                        message: formatMessage({
                          id: 'app.login.require.password'
                        })
                      }
                    ]}
                  >
                    <Input.Password
                      size="large"
                      placeholder={formatMessage({
                        id: 'app.login.require.password'
                      })}
                      style={{ borderRadius: '7px' }}
                    />
                  </Form.Item>
                  <Form.Item
                    name="imgCode"
                    rules={[
                      {
                        required: true,
                        message: formatMessage({
                          id: 'app.login.require.imgCode'
                        })
                      }
                    ]}
                  >
                    <Row>
                      <Col span="14">
                        <Input
                          size="large"
                          placeholder={formatMessage({
                            id: 'app.login.require.imgCode'
                          })}
                          style={{ borderRadius: '7px' }}
                        />
                      </Col>
                      <Col span="1" className="line" />
                      <Col span="9">
                        <div style={{ position: 'relative' }}>
                          {basicState.imgCodeLoading ? (
                            <Loading />
                          ) : (
                            <img
                              src={basicState.codeSrc}
                              onClick={refreshCaptcha}
                              alt={formatMessage({
                                id: 'app.login.imgCode.error'
                              })}
                              style={{
                                width: '130px',
                                cursor: 'pointer',
                                borderRadius: '7px',
                                border: 'solid 1px red'
                              }}
                            />
                          )}
                        </div>
                      </Col>
                    </Row>
                  </Form.Item>
                </TabPane>

                <TabPane
                  tab={
                    <span>
                      <MobileOutlined />
                      {formatMessage({ id: 'app.login.mobile.login' })}
                    </span>
                  }
                  key="mobileTab"
                >
                  <Form.Item
                    name="mobile"
                    rules={[
                      {
                        required: true,
                        message: formatMessage({
                          id: 'app.login.require.mobile'
                        })
                      },
                      {
                        validator: (rules, value) => validatePhone(rules, value)
                      }
                    ]}
                  >
                    <Input
                      size="large"
                      autoComplete="false"
                      placeholder={formatMessage({
                        id: 'app.login.require.mobile'
                      })}
                      style={{ borderRadius: '7px' }}
                    />
                  </Form.Item>
                  <Row gutter={16}>
                    <Col className="gutter-row" span={14}>
                      <Form.Item name="smsCode">
                        <Input
                          size="large"
                          placeholder={formatMessage({
                            id: 'app.login.require.smsCode'
                          })}
                          style={{ borderRadius: '7px' }}
                        />
                      </Form.Item>
                    </Col>
                    <Col className="gutter-row" span={10}>
                      <Button
                        type="primary"
                        tabIndex={-1}
                        disabled={state.smsSendBtn}
                        className="getCaptcha"
                        style={{ borderRadius: '5px' }}
                        onClick={getSmsCode}
                      >
                        {(!state.smsSendBtn &&
                          formatMessage({ id: 'app.login.get.smsCode' })) ||
                          state.time +
                            formatMessage({ id: 'app.login.resend.smsCode' })}
                      </Button>
                    </Col>
                  </Row>
                </TabPane>
              </Tabs>

              <Form.Item>
                <Checkbox
                  onClick={() => {
                    setState(prevState => {
                      return { ...prevState, isRemember: !prevState.isRemember }
                    })
                  }}
                  checked={state.isRemember}
                >
                  {formatMessage({ id: 'app.login.auto' })}
                </Checkbox>
                <a className="forge-password" style={{ float: 'right' }}>
                  {formatMessage({ id: 'app.login.forget.password' })}
                </a>
              </Form.Item>

              <Form.Item>
                <Button
                  size="large"
                  type="primary"
                  disabled={state.loginBtn}
                  onClick={submit}
                  className="login-button"
                  style={{ borderRadius: '5px' }}
                >
                  {formatMessage({ id: 'app.login.submit' })}
                </Button>
              </Form.Item>

              <div className="user-login-other">
                <span>{formatMessage({ id: 'app.login.other.login' })}</span>
                <a
                  href="#"
                  onClick={() => {
                    getSocialUrl('github')
                  }}
                >
                  <GithubFilled className="item-icon" />
                </a>
                <a
                  href="#!"
                  onClick={() => {
                    getSocialUrl('wechat')
                  }}
                >
                  <WechatFilled className="item-icon" />
                </a>
                <a
                  href="#!"
                  onClick={() => {
                    getSocialUrl('qq')
                  }}
                >
                  <QqOutlined className="item-icon" />
                </a>
                <a
                  href="#!"
                  onClick={() => {
                    getSocialUrl('alipay')
                  }}
                >
                  <AlipayOutlined className="item-icon" />
                </a>
                <a
                  href="#!"
                  onClick={() => {
                    getSocialUrl('dingtalk')
                  }}
                >
                  <DingtalkOutlined className="item-icon" />
                </a>
                <a
                  href="#!"
                  onClick={() => {
                    getSocialUrl('gitee')
                  }}
                >
                  <WeiboCircleFilled className="item-icon" />
                </a>
                <a className="register">
                  {formatMessage({ id: 'app.login.register' })}
                </a>
              </div>
            </Spin>
          </Form>
        </div>

        <div className="login-footer">
          <div className="links">
            <a href="_self">{formatMessage({ id: 'app.login.help' })}</a>
            <a href="_self">{formatMessage({ id: 'app.login.privacy' })}</a>
            <a href="_self">{formatMessage({ id: 'app.login.terms' })}</a>
          </div>
          <div className="copyright">
            {formatMessage({ id: 'app.login.copyright' })}
          </div>
        </div>

        <div
          style={{
            position: 'fixed',
            right: '20px',
            top: '20px',
            fontSize: '20px'
          }}
        >
          <Dropdown overlay={langMenu} overlayStyle={{ width: '10rem' }}>
            <span className="ant-dropdown-link" style={{ cursor: 'pointer' }}>
              <GlobalOutlined title="语言" />
            </span>
          </Dropdown>
        </div>
      </div>
    </div>
  ) : (
    <div />
  )
}

export default Login
