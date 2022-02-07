import request, {Method, commonUrl} from '@/plugins/request.js';

/**
 * 获取拼图验证
 */
export function getVerifyImg (verificationEnums) {
  return request({
    url: `${commonUrl}/common/slider/${verificationEnums}`,
    method: Method.GET,
    needToken: false
  });
}
/**
 * 验证码校验
 */
export function postVerifyImg (params) {
  return request({
    url: `${commonUrl}/common/slider/${params.verificationEnums}`,
    method: Method.POST,
    needToken: false,
    params
  });
}
/**
 * 发送短信验证码
 */
export function sendSms (params) {
  return request({
    url: `${commonUrl}/common/sms/${params.verificationEnums}/${params.mobile}`,
    method: Method.GET,
    needToken: false,
    params
  });
}
/**
 * 获取logo图标
 */
export function getLogo () {
  return request({
    url: `${commonUrl}/common/logo`,
    method: Method.GET,
    needToken: false
  });
}
// 地区数据，用于三级联动
export function getRegion (id) {
  return request({
    url: `${commonUrl}/common/region/item/${id}`,
    needToken: true,
    method: Method.GET
  });
}

/**
 * 分页获取文章列表
 * @param cateId 文章分类id
 */
export function articleList (params) {
  return request({
    url: `/buyer/article`,
    method: Method.GET,
    params
  });
}

/**
 * 获取帮助中心文章分类列表
 * @param cateId 文章分类id
 */
export function articleCateList () {
  return request({
    url: `/buyer/article/articleCategory/list`,
    method: Method.GET
  });
}

// 通过id获取文章
export function articleDetail (id) {
  return request({
    url: `/buyer/article/get/${id}`,
    method: Method.GET
  });
}
