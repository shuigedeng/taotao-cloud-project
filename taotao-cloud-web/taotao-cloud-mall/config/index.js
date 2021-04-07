const path = require("path");
const src = path.resolve("src");

const config = {
  projectName: "taotao-cloud-mall-react",
  date: "2020-12-9",
  designWidth: 750,
  deviceRatio: {
    640: 2.34 / 2,
    750: 1,
    828: 1.81 / 2,
  },
  sourceRoot: "src",
  outputRoot: `dist/${process.env.TARO_ENV}`,
  plugins: [
    [
      src.concat("/plugin/taotao/index.ts"),
      {
        sever: {
          url: "http://baidu.com",
          ip: "127.0.0.1",
          port: 3306,
        },
      },
    ],
    [
      src.concat("/plugin/tailwind/index.ts"),
      {
        test: "/\\/src\\/styles\\/tailwind\\.less$/",
      },
    ],
    ["@tarojs/plugin-mock"],
  ],
  defineConstants: {
    IS_H5: process.env.TARO_ENV === "h5",
    IS_RN: process.env.TARO_ENV === "rn",
    IS_WEAPP: process.env.TARO_ENV === "weapp",
  },
  alias: {
    "@/components": path.resolve(__dirname, "..", "src/components"),
    "@/utils": path.resolve(__dirname, "..", "src/utils"),
    "@/package": path.resolve(__dirname, "..", "package.json"),
    "@/project": path.resolve(__dirname, "..", "project.config.json"),
  },
  terser: {
    enable: true,
    config: {
      // 配置项同 https://github.com/terser/terser#minify-options
    },
  },
  csso: {
    enable: true,
    config: {
      // 配置项同 https://github.com/css/csso#minifysource-options
    },
  },
  copy: {
    patterns: [],
    options: {},
  },
  framework: "react",
  mini: {
    postcss: {
      pxtransform: {
        enable: true,
        config: {},
      },
      url: {
        enable: true,
        config: {
          limit: 1024, // 设定转换尺寸上限
        },
      },
      cssModules: {
        enable: false, // 默认为 false，如需使用 css modules 功能，则设为 true
        config: {
          namingPattern: "module", // 转换模式，取值为 global/module
          generateScopedName: "[name]__[local]___[hash:base64:5]",
        },
      },
    },
  },
  h5: {
    publicPath: "/",
    staticDirectory: "static",
    router: {
      mode: "browser", // 或者是 'hash会有#号' history
    },
    postcss: {
      autoprefixer: {
        enable: true,
        config: {},
      },
      cssModules: {
        enable: false, // 默认为 false，如需使用 css modules 功能，则设为 true
        config: {
          namingPattern: "module", // 转换模式，取值为 global/module
          generateScopedName: "[name]__[local]___[hash:base64:5]",
        },
      },
    },
  },
  rn: {
    appName: "taotao-cloud-mall",
    output: {
      ios: "./ios/main.jsbundle",
      iosAssetsDest: "./ios",
      android: "./android/app/src/main/assets/index.android.bundle",
      androidAssetsDest: "./android/app/src/main/res",
    },
    plugins: [
      [
        "module-resolver",
        {
          alias: {
            "@tarojs/components": "@tarojs/components-rn",
            "@tarojs/taro": "@tarojs/taro-rn",
          },
        },
      ],
    ],
  },
};

module.exports = function (merge) {
  if (process.env.NODE_ENV === "development") {
    return merge({}, config, require("./dev"));
  }
  // if (process.env.NODE_ENV === 'mock') {
  //   return merge({}, config, require('./mock'))
  // }
  return merge({}, config, require("./prod"));
};
