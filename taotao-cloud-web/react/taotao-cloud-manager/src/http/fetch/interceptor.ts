export class FetchInterceptor {
  public interceptors: any[] = []

  public interceptor(
    fetch: (
      input: RequestInfo,
      init?: RequestInit | undefined
    ) => Promise<Response>,
    options: { input: RequestInfo; init?: RequestInit | undefined }
  ) {
    const reversedInterceptors = this.interceptors.reduce(
      (array, interceptor) => [...[interceptor], array]
    )
    let promise = Promise.resolve(options)
    reversedInterceptors.forEach(({ request, requestError }: any) => {
      if (request || requestError) {
        promise = promise.then(
          opt => request(opt.input, opt.init),
          requestError
        )
      }
    })
    let responsePromise = promise.then(opt => fetch(opt.input, opt.init))
    reversedInterceptors.forEach(({ response, responseError }: any) => {
      if (response || responseError) {
        responsePromise = responsePromise.then((resp: Response) => {
          return response(resp)
        })
      }
    })
    return responsePromise
  }
}

window.fetch = (fetch => {
  return (input: RequestInfo, init?: RequestInit | undefined) => {
    return fetchInterceptor.interceptor(fetch, { input, init })
  }
})(window.fetch)

export const fetchInterceptor = new FetchInterceptor()
