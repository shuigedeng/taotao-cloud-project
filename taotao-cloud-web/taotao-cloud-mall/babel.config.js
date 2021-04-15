// babel-preset-taro 更多选项和默认值：
// https://github.com/NervJS/taro/blob/next/packages/babel-preset-taro/README.md
module.exports = {
  presets: [
    ['taro', {
      framework: 'react',
      ts: true
      // 以下参数为 @babel/preset-env 的参数：
      // https://babeljs.io/docs/en/babel-preset-env
      // loose: true,
      // useBuiltIns: true,
      // esmodules: true,
      // modules: "commonjs",
      // targets: {
      //   ios: '9',
      //   android: '5'
      // }
    }]
  ],
  plugins: [
    // [
    //   'module-resolver',
    //   {
    //     alias: {
    //       '@tarojs/components': '@tarojs/components-rn',
    //       '@tarojs/taro': '@tarojs/taro-rn',
    //     },
    //   },
    // ]
  ]
}
