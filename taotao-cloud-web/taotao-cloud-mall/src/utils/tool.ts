import Taro from '@tarojs/taro'

const getCurrentPages = Taro.getCurrentPages;

const mapAppKey = "FPXBZ-NMCEX-LM64A-TNISS-CD3OV-32FVH";

function Request(url, data = {}, success, fail) {
  success = success || function () {
  };
  fail = fail || function () {
  };

  let token = '';
  // 从本地缓存中异步获取指定 key 的内容
  let memberEntity = Taro.getStorageSync("memberEntity");
  if (memberEntity) {
    token = memberEntity.token;
  }
  if (!token) {
    token = getCurrentPages()[0].$component.props.common.loginInfo.token;
  }
  Taro.request({
    url: url,
    data: data,
    method: "POST",
    header: {
      'Content-Type': "application/json",
      'Wechat-Authorization': token
    },
    success: function (data) {
      let code = data.data.code;
      if (data.statusCode == 200) {
        if (code == 401) {
          setTimeout(() => {
            // 关闭所有页面，打开到登录页面
            Taro.reLaunch({url: `/pages/login/authorize/authorize`})
          }, 2000)
        } else {
          success(data)
        }
      } else if (data.statusCode == 401) {
        setTimeout(() => {
          // 关闭所有页面，打开到登录页面
          Taro.reLaunch({url: `/pages/login/authorize/authorize`})
        }, 2000)
      } else {
        fail(data);
      }
    },
    fail: function (err) {
      fail(err)
      Taro.showToast({
        title: '网络错误，稍后再试~',
        icon: 'none',
        duration: 2000,
        complete: function () {
          setTimeout(() => {
            Taro.hideLoading();
          }, 2000)
        }
      });
    }
  })
}

function createSocket(url) {
  let token = '';
  // 从本地缓存中异步获取指定 key 的内容
  let memberEntity = Taro.getStorageSync("memberEntity");
  if (memberEntity) {
    token = memberEntity.token;
  }
  if (!token) {
    token = getCurrentPages()[0].$component.props.common.loginInfo.token;
  }
  return Taro.connectSocket({
    url: url,
    header: {
      'Wechat-Authorization': token
    }
  });
}

/**
 * @author zhengqiang
 * @date 2019/6/7
 * @description 文件上传
 */
function uploadFile(id, source, filePath, success, fail) {
  let memberEntity = Taro.getStorageSync("memberEntity");
  if (!success) {
    success = function (data: any) {
      console.log(data)
    }
  }
  if (!fail) {
    fail = function (data: any) {
      console.log(data)
    }
  }
  let formData = {
    source: source,
    appUpload: "true",
    id: ""
  };
  if (id) {
    formData.id = id;
  }
  Taro.uploadFile({
    url: "http://127.0.0.1/" + "/uc/wechat/upload",
    filePath: filePath,
    name: "file",
    header: {
      "Wechat-Authorization": memberEntity.token,
      "Content-Type": "multipart/form-data"
    },
    formData: formData,
    success(data) {
      if (data.statusCode == 200) {
        success(data)
      } else {
        fail(data);
      }
    },
    fail(data) {
      fail(data);
    }
  });
}

/**
 * @author zhengqiang
 * @date 2019/6/7
 * @description 文件上传排序、删除文件
 */
function uploadFilePath(id, source, toUploadPath, success, fail) {
  let memberEntity = Taro.getStorageSync("memberEntity");
  if (!success) {
    success = function (data) {
      console.log(data)
    }
  }
  if (!fail) {
    fail = function (data) {
      console.log(data)
    }
  }
  let formData = {
    source: source,
    appUpload: "true",
    toUploadPath: toUploadPath,
    id: ""
  };
  if (id) {
    formData.id = id;
  }
  Taro.request({
    url: "127.0.0.1/" + "/uc/wechat/upload",
    data: formData,
    method: "POST",
    dataType: "json",
    header: {
      "Content-Type": "application/x-www-form-urlencoded",
      "Wechat-Authorization": memberEntity.token,
    },
    success(data) {
      if (data.statusCode == 200) {
        success(data)
      } else {
        fail(data);
      }
    },
    fail(data) {
      fail(data);
    }
  });
}

/**
 * @author zhengqiang
 * @date 2019/7/18
 * @description 获取一组图片信息
 */
function getImageInfo(srcs, callback) {
  if (!callback) {
    callback = function () {
    };
  }
  if (!srcs || srcs.length == 0) {
    callback();
  }
  let result = new Array(srcs.length);
  let complete = 0;
  srcs.forEach((item, index) => {
    if (!item) {
      item = null;
    }
    Taro.getImageInfo({
      src: item,
      complete(res) {
        complete++;
        result[index] = res;
        if (complete == result.length) {
          callback(result);
        }
      }
    })
  });
}

/**
 * @author zhengqiang
 * @date 2019/7/18
 * @description 获取用户信息
 */
function getUserInfo(callback) {
  Taro.getSetting({
    success(res) {
      if (res.authSetting['scope.userInfo']) {
        Taro.getUserInfo({
          success: function (res) {
            if (callback) {
              callback(res.userInfo);
            }
          },
          fail(res) {
            if (callback) {
              callback(res);
            }
          }
        });
      } else {
        if (callback) {
          callback();
        }
      }
    },
    fail(res) {
      if (callback) {
        callback(res);
      }
    }
  })
}

/**
 * @author zhengqiang
 * @date 2019/6/25
 * @description 获取当前位置名称
 */
function getLocationName(callback) {
  Taro.getSetting({
    success(res) {
      if (res.authSetting["scope.userLocation"] === false) {
        Taro.showModal({
          title: "请授权当前位置",
          content: "需要获取您的位置，请确认授权",
          success(res) {
            if (res.cancel) {
              Taro.showToast({
                title: '拒绝授权',
                icon: 'none',
                duration: 1000
              });
              callback();
            } else if (res.confirm) {
              Taro.openSetting({
                success(res) {
                  if (res.authSetting["scope.userLocation"] === true) {
                    Taro.showToast({
                      title: '授权成功',
                      icon: 'success',
                      duration: 1000
                    });
                    //再次授权，调用Taro.getLocation的API
                    getLocation(callback);
                  } else {
                    callback();
                    Taro.showToast({
                      title: '授权失败',
                      icon: 'none',
                      duration: 1000
                    });
                  }
                }
              });
            }
          }
        });
      } else {
        getLocation(callback);
      }
    }
  })
}

/**
 * @author zhengqiang
 * @date 2019/6/25
 * @description 获取当前位置经纬度
 */
function getLocation(callback) {
  Taro.getLocation({
    type: "wgs84",
    success: (res) => {
      let {latitude, longitude} = res;
      Taro.request({
        url: `https://apis.map.qq.com/ws/geocoder/v1/?location=${latitude},${longitude}&key=${mapAppKey}`,
        method: "GET",
        success(res) {
          if (callback) {
            callback(res.data);
          }
        }
      });
    },
    fail(res) {
      callback(res);
    }
  });
}

//转换数字， 多少万
function formatTenThousands(num) {
  if (!num) {
    return 0;
  }
  let type = 10000;
  if (num < type) {
    return num;
  }
  num = num / type;
  num = parseInt(num);
  return num + "w"
}

//转换数字， 多少千
function formatThousands(num) {
  if (!num) {
    return 0;
  }
  let type = 1000;
  if (num < type) {
    return num;
  }
  num = num / type;
  num = parseFloat(num).toFixed(2);
  return num + "k"
}


/**
 *  两数相乘
 */
function accMul(arg1, arg2) {
  // console.log("accMul")
  // console.log(arg1)
  var m = 0,
    s1 = arg1.toString(),
    s2 = arg2.toString();
  try {
    m += s1.split(".")[1].length
  } catch (e) {
  }
  try {
    m += s2.split(".")[1].length
  } catch (e) {
  }
  return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
}


/**
 *  支付订单
 */
function PayShop(orderCode, callback) {
  Request('/mall/wechat/pay/order', {"code": orderCode,}, "GET", (res) => {
    res = res.data;
    if (res.success) {
      requestPayment(res.data, orderCode, callback);
    } else {
      callback();
      Taro.showToast({
        title: res.msg || "下单接口调用失败",
        icon: "none"
      });
    }
  });
}

/**
 *  调用微信支付接口
 */
function requestPayment(data, orderCode, callback) {
  console.log(orderCode)
  Taro.requestPayment({
    timeStamp: data.timeStamp,
    nonceStr: data.nonceStr,
    package: data.packageValue,
    signType: data.signType,
    paySign: data.paySign,
    success(res) {
      callback(res);
    }, fail(res) {
      callback(res);
      Taro.showToast({
        title: '支付调用失败，稍后再试',
        icon: 'none',
      });
    }
  })
}

/**
 * @author zhengqiang
 * @date 2019/5/27
 * @description 封装商品的价格信息和最低最高利润
 */
function packPriceAndProfit(product) {
  if (product) {
    let minPrice = (product.minPrice || 0);
    let maxPrice = (product.maxPrice || 0);
    let price = '';
    if (minPrice == maxPrice) {
      price = maxPrice.toFixed(2);
    } else {
      price = minPrice.toFixed(2) + "~" + maxPrice.toFixed(2);
    }
    product.price = price;
    let profit = '0.00';
    if (product.specList && product.specList.length > 0) {
      let minProfit = 0;
      let maxProfit = 0;
      product.specList.map((item) => {
        let onceShare = item.onceShare || 0;
        let sellPrice = item.sellPrice || 0;
        let p = (onceShare * sellPrice / 100);
        if (!minProfit || minProfit > p) {
          minProfit = p;
        }
        if (!maxProfit || maxProfit < p) {
          maxProfit = p;
        }
        return item;
      });
      if (minProfit == maxProfit) {
        profit = maxProfit.toFixed(2);
      } else {
        profit = minProfit.toFixed(2) + "~" + maxProfit.toFixed(2);
      }
      product.profit = profit;
    }
  }
}

/**
 * @author zhengqiang
 * @date 2019/6/27
 * @description 检查更新
 */
function checkUpdate() {
  // 获取小程序更新机制兼容
  if (Taro.canIUse('getUpdateManager')) {
    console.log("canIUsecanIUsecanIUsecanIUse")
    const updateManager = Taro.getUpdateManager();
    updateManager.onCheckForUpdate(function (res) {
      // 请求完新版本信息的回调
      if (res.hasUpdate) {
        updateManager.onUpdateReady(function () {
          Taro.showModal({
            title: '更新提示',
            content: '新版本已经准备好，是否重启应用？',
            success: function (res) {
              if (res.confirm) {
                // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
                updateManager.applyUpdate()
              }
            }
          });
        });

        updateManager.onUpdateFailed(function () {
          // 新的版本下载失败
          Taro.showModal({
            title: '已经有新版本了哟~',
            content: '新版本已经上线啦~，请您删除当前小程序，重新搜索打开哟~',
          })
        })
      }
    });
  } else {
    // 如果希望用户在最新版本的客户端上体验您的小程序，可以这样子提示
    Taro.showModal({
      title: '提示',
      content: '当前微信版本过低，无法使用该功能，请升级到最新微信版本后重试。'
    });
  }
}

const error = (errorMsg) => {
  Taro.showToast({
    title: errorMsg,
    icon: 'none',
    duration: 2000
  });
}

//将秒数转化拆分为其他单位 level 1:分 2:时 3:日
// function formatSecond(second, level) {
//   second = parseInt(second) || 0;
//   level = parseInt(level) || 1;
//
//   function _formatStr(num) {
//     return num > 9 ? `${num}` : `0${num}`;
//   }
//
//   let result = {
//     day:  number,
//     dayStr: string
//   };
//   if (level >= 3) {
//     result.day = Math.floor(second / 86400);
//     result.dayStr = _formatStr(result.day);
//     second %= 86400;
//     level = 2;
//   }
//   if (level == 2) {
//     result.hour = Math.floor(second / 3600);
//     result.hourStr = _formatStr(result.hour);
//     second %= 3600;
//     level = 1;
//   }
//   if (level == 1) {
//     result.minute = Math.floor(second / 60);
//     result.minuteStr = _formatStr(result.minute);
//     second %= 60;
//   }
//   result.second = second;
//   result.secondStr = _formatStr(result.second);
//   return result;
// }

export {
  requestPayment,
  PayShop,
  accMul,
  Request,
  mapAppKey,
  formatTenThousands,
  formatThousands,
  packPriceAndProfit,
  uploadFile,
  uploadFilePath,
  getImageInfo,
  getUserInfo,
  getLocationName,
  checkUpdate,
  createSocket,
  error
}



