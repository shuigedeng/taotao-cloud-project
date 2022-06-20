const path = require("path");
const config = {
  projectName: 'taotao-cloud-rn',
  date: '2021-12-3',
  designWidth: 750,
  deviceRatio: {
    640: 2.34 / 2,
    750: 1,
    828: 1.81 / 2
  },
  sourceRoot: 'src',
  outputRoot: 'dist',
  plugins: [
    ['taro-plugin-tailwind'],
    // ["@tarojs/plugin-mock", {
    //   host: "0.0.0.0"
    // }],
  ],
  defineConstants: {
    IS_H5: process.env.TARO_ENV === "h5",
    IS_RN: process.env.TARO_ENV === "rn",
    IS_WEAPP: process.env.TARO_ENV === "weapp",
  },
  alias: {
    "@/api": path.resolve(__dirname, "..", "src/api"),
    "@/assets": path.resolve(__dirname, "..", "src/assets"),
    "@/components": path.resolve(__dirname, "..", "src/components"),
    "@/constants": path.resolve(__dirname, "..", "src/constants"),
    "@/enums": path.resolve(__dirname, "..", "src/enums"),
    "@/http": path.resolve(__dirname, "..", "src/http"),
    "@/pages": path.resolve(__dirname, "..", "src/pages"),
    "@/store": path.resolve(__dirname, "..", "src/store"),
    "@/utils": path.resolve(__dirname, "..", "src/utils"),
    "@/styles": path.resolve(__dirname, "..", "src/styles"),
    "@/package": path.resolve(__dirname, "..", "package.json"),
    "@/project": path.resolve(__dirname, "..", "project.config.json"),
  },
  copy: {
    patterns: [
    ],
    options: {
    }
  },
  framework: 'react',
  mini: {
    postcss: {
      pxtransform: {
        enable: true,
        config: {

        }
      },
      url: {
        enable: true,
        config: {
          limit: 1024 // 设定转换尺寸上限
        }
      },
      cssModules: {
        enable: false, // 默认为 false，如需使用 css modules 功能，则设为 true
        config: {
          namingPattern: 'module', // 转换模式，取值为 global/module
          generateScopedName: '[name]__[local]___[hash:base64:5]'
        }
      }
    }
  },
  h5: {
    publicPath: '/',
    staticDirectory: 'static',
    output: {
      filename: 'js/[name].[hash:8].js',
      chunkFilename: 'js/[name].[chunkhash:8].js'
    },
    router: {
      mode: 'hash', // 或者是 'browser' 有问题
      customRoutes: {
        '/pages/home/index': '/home',
        '/pages/home_bak/index': '/home_bak',
        '/pages/cart/index': '/cart',
        '/pages/classify/index': '/classify',
        '/pages/lifeCircle/index': '/lifeCircle',
        '/pages/login/index': '/login',
        '/pages/ucenter/index': '/ucenter',
      }
    },
    esnextModules: ['taro-ui'],
    lessLoaderOption: {
      lessOptions: { // 如果使用less-loader@5，请移除 lessOptions 这一级直接配置选项。
        strictMath: true,
        noIeCompat: true
      }
    },
    miniCssExtractPluginOption: {
      filename: 'css/[name].css',
      chunkFilename: 'css/[id].css'
    },
    mediaUrlLoaderOption: {
      limit: 8192
    },
    postcss: {
      autoprefixer: {
        enable: true,
        config: {}
      },
      cssModules: {
        enable: false, // 默认为 false，如需使用 css modules 功能，则设为 true
        config: {
          namingPattern: 'module', // 转换模式，取值为 global/module
          generateScopedName: '[name]__[local]___[hash:base64:5]'
        }
      }
    }
  },
  rn: {
    appName: 'taroDemo',
    output: {
      ios: './ios/main.jsbundle',
      iosAssetsDest: './ios',
      android: './android/app/src/main/assets/index.android.bundle',
      androidAssetsDest: './android/app/src/main/res',
      // iosSourceMapUrl: '',
      iosSourcemapOutput: './ios/main.map',
      // iosSourcemapSourcesRoot: '',
      // androidSourceMapUrl: '',
      androidSourcemapOutput: './android/app/src/main/assets/index.android.map',
      // androidSourcemapSourcesRoot: '',
    },
    postcss: {
      cssModules: {
        enable: false, // 默认为 false，如需使用 css modules 功能，则设为 true
      }
    }
  }
}

module.exports = function (merge) {
  if (process.env.NODE_ENV === 'development') {
    return merge({}, config, require('./dev'))
  }
  return merge({}, config, require('./prod'))
}
