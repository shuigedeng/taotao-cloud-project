import SecureLS from 'secure-ls'

export const ls = new SecureLS({
  encodingType: 'des',
  isCompression: false,
  encryptionSecret: 'dGFvdGFvLWNsb3VkLWJhY2tlbmQ='
})

export const setLoginUserToken = (token: string) => {
  ls.set('TAOTAO_CLOUD_LOGIN_TOKEN', token)
}

export const getLoginUserToken = (): string => {
  return ls.get('TAOTAO_CLOUD_LOGIN_TOKEN')
}

export const removeLoginUserToken = () => {
  ls.remove('TAOTAO_CLOUD_LOGIN_TOKEN')
}

export const setLoginUserIsRemember = (data: boolean) => {
  ls.set('TAOTAO_CLOUD_LOGIN_IS_REMEMBER', data)
}

export const getLoginUserIsRemember = (): boolean => {
  return ls.get('TAOTAO_CLOUD_LOGIN_IS_REMEMBER')
}

export const removeLoginUserIsRemember = () => {
  ls.remove('TAOTAO_CLOUD_LOGIN_IS_REMEMBER')
}
