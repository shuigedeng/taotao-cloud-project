const getRequestUrl = (url: string) => {
  let env = process.env.NODE_ENV;
  let prefix = process.env.TAOTAO_CLOUD_HOST_PREFIX || "api"
  let version = process.env.TAOTAO_CLOUD_HOST_VERSION || "v1.0"

  let requestUrl: string;
  if (env === 'production') {
    requestUrl = `http://192.168.99.37:9527/${prefix}/${version}${url}`
  } else if (env === 'test') {
    requestUrl = `http://192.168.99.37:9527/${prefix}/${version}${url}`
  } else if (env === 'development') {
    requestUrl = `http://192.168.99.37:9527/${prefix}/${version}${url}`
  } else {
    requestUrl = `http://192.168.99.37:9527/${prefix}/${version}${url}`
  }
  return requestUrl
}

export default getRequestUrl;
