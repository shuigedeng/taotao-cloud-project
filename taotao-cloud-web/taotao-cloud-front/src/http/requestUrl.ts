const getRequestUrl = (url: string) => {
  let env = process.env.NODE_ENV;
  let prefix = process.env.TAOTAO_CLOUD_HOST_PREFIX || "api"
  let version = process.env.TAOTAO_CLOUD_HOST_VERSION || "v1.0"

  let requestUrl: string;
  if (env === 'production') {
    requestUrl = `http://127.0.0.1:9900/${prefix}/${version}${url}`
  } else if (process.env.NODE_ENV === 'test') {
    requestUrl = `http://127.0.0.1:9900/${prefix}/${version}${url}`
  } else {
    requestUrl = `http://127.0.0.1:3721/${prefix}/${version}${url}`
  }
  return requestUrl
}

export default getRequestUrl;
