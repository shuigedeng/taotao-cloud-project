const globalData: GlobalData = {
  console: undefined,
  debug: false
}

export function setGlobalData(key, val) {
  globalData[key] = val
}

export function getGlobalData(key) {
  return globalData[key]
}
