import {formatTime} from './common'

/**
 *
 * @param {string} name 错误名字
 * @param {string} action 错误动作描述
 * @param {string} info 错误信息，通常是 fail 返回的
 */
export const logError = (name: string, action?: string, info?: string | object) => {
  if (!info) {
    info = 'empty'
  }

  let device;
  try {
    let deviceInfo = Taro.getSystemInfoSync()
    device = JSON.stringify(deviceInfo)
  } catch (e) {
    console.error('not support getSystemInfoSync api', e.message)
  }

  let time = formatTime(new Date())
  if (typeof info === 'object') {
    info = JSON.stringify(info)
  }

  console.error(time, name, action, info, device)
}



