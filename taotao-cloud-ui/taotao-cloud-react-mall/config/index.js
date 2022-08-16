const path = require('path')

const config = {
  projectName: 'taotao-cloud-react-mall',
  date: '2021-4-14',
  designWidth: 750,
  deviceRatio: {
    640: 2.34 / 2,
    750: 1,
    828: 1.81 / 2
  },
  sourceRoot: 'src',
  outputRoot: `dist/${process.env.TARO_ENV}`,
  plugins: [
    [
      path.resolve(__dirname, "..", "src/plugin").concat("/taro/index.ts"),
      {
        sever: {
          url: "http://baidu.com",
          ip: "127.0.0.1",
          port: 3306,
        },
      },
    ],
    ['taro-plugin-tailwind'],
    ["@tarojs/plugin-mock", {
      host: "0.0.0.0"
    }],
    ['tarojs-plugin-generator', {
      css: 'less',  //可配置css编译器： 支持 none sass less stylus
      cssModules: 'none',      //开启页面的CssModule化
    }]
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
    patterns: [],
    options: {}
  },
  terser: {
    enable: true,
    config: {
      // 配置项同 https://github.com/terser/terser#minify-options
    }
  },
  csso: {
    enable: true,
    config: {
      // 配置项同 https://github.com/css/csso#minifysource-options
    }
  },
  framework: 'react',
  mini: {
    mediaUrlLoaderOption: {
      limit: 8192
    },
    // miniCssExtractPluginOption: {
    //   filename: '[name].css',
    //   chunkFilename: '[name].css'
    // },
    // lessLoaderOption: {
    //   lessOptions: { // 如果使用less-loader@5，请移除 lessOptions 这一级直接配置选项。
    //     strictMath: true,
    //     noIeCompat: true
    //   }
    // },
    webpackChain(chain, webpack) {
      chain.merge({
        plugin: {
          // install: {
          //   plugin: require('npm-install-webpack-plugin'),
          //   args: [{
          //     // Use --save or --save-dev
          //     dev: false,
          //     // Install missing peerDependencies
          //     peerDependencies: true,
          //     // Reduce amount of console logging
          //     quiet: false,
          //     // npm command used inside company, yarn is not supported yet
          //     npm: 'cnpm'
          //   }]
          // }
        },
        module: {
          rule: {
            myloader: {
              test: /\.md$/,
              use: [{
                loader: 'raw-loader',
                options: {}
              }]
            }
          }
        }
      })
    },
    postcss: {
      pxtransform: {
        enable: true,
        config: {}
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
    appName: "taotao-cloud-react-mall",
    // output: {
    //   ios: "./ios/main.jsbundle",
    //   iosAssetsDest: "./ios",
    //   android: "./android/app/src/main/assets/index.android.bundle",
    //   androidAssetsDest: "./android/app/src/main/res",
    // },
    // plugins: [
    //   [
    //     "module-resolver",
    //     {
    //       alias: {
    //         "@tarojs/components": "@tarojs/components-rn",
    //         "@tarojs/taro": "@tarojs/taro-rn",
    //       },
    //     },
    //   ],
    // ],
  },
}

module.exports = function (merge) {
  if (process.env.NODE_ENV === 'development') {
    return merge({}, config, require('./dev'))
  }
  return merge({}, config, require('./prod'))
}
