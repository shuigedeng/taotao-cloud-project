import Taro, {request} from "@tarojs/taro"
import {Result} from "@/api/model/baseModel";
import {HttpStatusEnum} from "@/enums/httpEnum";

let REQUEST_NUM = 0;
let TIME: NodeJS.Timeout | null;

const taotaoCloudInterceptor = (chain) => {
  increaseRequest();
  const requestParams = chain.requestParams;
  return chain.proceed(requestParams).then((res: request.SuccessCallbackResult<Result<any>>) => {
    reduceRequest();
    if(res){
      if (res.statusCode === HttpStatusEnum.NOT_FOUND) {
        errShowToast('请求资源不存在');
      } else if (res.statusCode === HttpStatusEnum.BAD_GATEWAY) {
        errShowToast('网关错误');
      } else if (res.statusCode === HttpStatusEnum.FORBIDDEN) {
        errShowToast('没有权限访问接口')
      } else if (res.statusCode === HttpStatusEnum.AUTHENTICATE) {
        errShowToast('用户未认证')
      } else if (res.statusCode === HttpStatusEnum.SUCCESS) {
        if (res.data.code === 200) {
          return res;
        }
        errShowToast(res.data.message);
      } else {
        errShowToast('服务器错误,请稍后重试')
      }
    }
  }).catch((err: Error) => {
    console.log(err)
    reduceRequest();
    errShowToast('请求错误,请检查网络')
  })
}

const errShowToast = (message: any) => {
  isHideLoading();
  if (TIME) {
    REQUEST_NUM -= 1;
    clearTimeout(TIME);
    TIME = null;
  }
  Taro.showToast({
    title: message,
    icon: 'none',
    duration: 5000
  })
  setTimeout(() => {
    isShowLoading();
  }, 1500);
}

const isHideLoading = () => {
  if (REQUEST_NUM > 0) {
    Taro.hideLoading();
  }
}

const isShowLoading = () => {
  if (REQUEST_NUM > 0) {
    Taro.showLoading({
      title: "正在加载,请稍后",
      mask: true
    })
  }
}

const increaseRequest = () => {
  if (REQUEST_NUM === 0) {
    Taro.showLoading({
      title: "正在加载,请稍后",
      mask: true
    })
  }
  REQUEST_NUM += 1;
}

const reduceRequest = () => {
  TIME = setTimeout(() => {
    REQUEST_NUM -= 1;
    if (REQUEST_NUM === 0) {
      Taro.hideLoading();
    }
  }, 300);
}

// Taro 提供了两个内置拦截器
// logInterceptor - 用于打印请求的相关信息
// timeoutInterceptor - 在请求超时时抛出错误。
const interceptors = [
  taotaoCloudInterceptor,
  Taro.interceptors.logInterceptor,
  Taro.interceptors.timeoutInterceptor
]

export default interceptors
