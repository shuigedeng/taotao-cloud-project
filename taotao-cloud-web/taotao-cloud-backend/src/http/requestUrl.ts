const getRequestUrl = (url: string) => {
  const env = process.env.NODE_ENV
  const prefix = process.env.TAOTAO_CLOUD_HOST_PREFIX || 'api'
  const version = process.env.TAOTAO_CLOUD_HOST_VERSION || 'v1.0'

  let requestUrl: string
  if (env === 'production') {
    requestUrl = `https://api.taotaocloud.top/${prefix}/${version}${url}`
  } else if (process.env.NODE_ENV === 'test') {
    requestUrl = `https://api.taotaocloud.top/${prefix}/${version}${url}`
  } else {
    requestUrl = `https://api.taotaocloud.top/${prefix}/${version}${url}`
  }
  return requestUrl
}

export default getRequestUrl
