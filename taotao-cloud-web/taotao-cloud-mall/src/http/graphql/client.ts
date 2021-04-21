import Taro from '@tarojs/taro';
import ApolloClient from 'apollo-boost';
import {conversionRequestEnum} from "@/enums/httpEnum";

const client = new ApolloClient({
  // 服务端地址
  uri: 'http://127.0.0.1:7001/graphql',
  fetch: (url: RequestInfo, options: RequestInit) => {
    return Taro.request({
      url: url.toString(),
      method: conversionRequestEnum(options),
      data: options.body,
      // header: options.headers,
      header: {
        ...options.headers,
        ...(Taro.getStorageSync('accessToken')
          ? {Authorization: `Bearer ${Taro.getStorageSync('accessToken')}`}
          : {}),
      },
    }).then(({data, statusCode, header}) => {
      return new Promise<Response>((resolve, reject) => {
        if (statusCode == 200) {
          resolve(new Response(
            JSON.stringify(data),
            {
              headers: header,
              status: statusCode,
              statusText: statusCode.toString()
            }
            //   ok: () => {
            //     return statusCode >= 200 && statusCode < 300;
            //   },
            //   text: () => {
            //     return Promise.resolve(JSON.stringify(data));
            //   },
          ))
        } else {
          reject(new Response(
            data,
            {
              headers: header,
              status: statusCode,
              statusText: statusCode.toString()
            }
            )
          )
        }
      })

      // return {
      //   ok: () => {
      //     return statusCode >= 200 && statusCode < 300;
      //   },
      //   text: () => {
      //     return Promise.resolve(JSON.stringify(data));
      //   },
      // };
    })
  }

});
export default client;
