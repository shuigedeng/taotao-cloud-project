/**
 * 时间格式
 */
export const timeFix = (): string => {
  const time = new Date()
  const hour = time.getHours()
  return hour < 9
    ? '早上好呀'
    : hour <= 11
    ? '上午好呀'
    : hour <= 13
    ? '中午好呀'
    : hour < 20
    ? '下午好'
    : '晚上好呀'
}

/**
 * 欢迎
 */
export const welcome = (): string => {
  const arr = [
    '休息一会儿吧',
    '准备吃什么呢?',
    '要不要打一把 DOTA',
    '我猜你可能累了'
  ]
  const index = Math.floor(Math.random() * arr.length)
  return arr[index]
}

/**
 * 触发 window.resize
 */
export function triggerWindowResizeEvent() {
  const event: Event = document.createEvent('HTMLEvents')
  event.initEvent('resize', true, true)
  window.dispatchEvent(event)
}

export function handleScrollHeader(callback: Function) {
  let timer: NodeJS.Timeout

  let beforeScrollTop = window.pageYOffset
  callback = callback || function () {}

  window.addEventListener(
    'scroll',
    event => {
      console.log(event)
      clearTimeout(timer)
      timer = setTimeout(() => {
        let direction = 'up'
        const afterScrollTop = window.pageYOffset
        const delta = afterScrollTop - beforeScrollTop
        if (delta === 0) {
          return false
        }
        direction = delta > 0 ? 'down' : 'up'
        callback(direction)
        beforeScrollTop = afterScrollTop
      }, 50)
    },
    false
  )
}

/**
 * Remove loading animate
 * @param id parent element id or class
 * @param timeout
 */
export function removeLoadingAnimate(id = '', timeout = 1500) {
  if (id === '') {
    return
  }
  setTimeout(() => {
    const element = document.getElementById(id)
    if (null != element) {
      document.body.removeChild(element)
    }
  }, timeout)
}

/**
 * 过滤对象中为空的属性
 * @param obj
 * @returns {*}
 */
export function filterObj(obj: Object | Array<string>) {
  if (!(typeof obj == 'object')) {
    return
  }

  for (const key in obj) {
    if (
      obj.hasOwnProperty(key) &&
      // @ts-ignore
      (obj[key] == null || obj[key] == undefined || obj[key] === '')
    ) {
      // @ts-ignore
      delete obj[key]
    }
  }
  return obj
}

// 表单序列化
export const serialize = (data: {}) => {
  const list: Array<string> = []
  Object.keys(data).forEach(ele => {
    // @ts-ignore
    list.push(`${ele}=${data[ele]}`)
  })
  return list.join('&')
}

/**
 * 判断是否为空
 */
export function validatenull(val: Object | undefined) {
  if (val instanceof Array) {
    if (val.length == 0) {
      return true
    }
  } else {
    if (JSON.stringify(val) === '{}') {
      return true
    }
  }
  return false
}

/**
 * 打开小窗口
 */
export const openWindow = (
  url: string,
  title: string,
  w: number,
  h: number
) => {
  // Fixes dual-screen position                            Most browsers       Firefox
  // eslint-disable-next-line no-restricted-globals
  const dualScreenLeft =
    window.screenLeft !== undefined ? window.screenLeft : screen.height
  // eslint-disable-next-line no-restricted-globals
  const dualScreenTop =
    window.screenTop !== undefined ? window.screenTop : screen.width
  // eslint-disable-next-line no-restricted-globals
  const width = window.innerWidth
    ? window.innerWidth
    : document.documentElement.clientWidth
    ? document.documentElement.clientWidth
    : screen.width
  // eslint-disable-next-line no-restricted-globals
  const height = window.innerHeight
    ? window.innerHeight
    : document.documentElement.clientHeight
    ? document.documentElement.clientHeight
    : screen.height

  const left = width / 2 - w / 2 + dualScreenLeft
  const top = height / 2 - h / 2 + dualScreenTop
  const newWindow: Window | null = window.open(
    url,
    title,
    'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=yes, copyhistory=no, width=' +
      w +
      ', height=' +
      h +
      ', top=' +
      top +
      ', left=' +
      left
  )

  // Puts focus on the newWindow
  if (window['focus']) {
    if (newWindow) {
      newWindow.focus()
    }
  }
}
