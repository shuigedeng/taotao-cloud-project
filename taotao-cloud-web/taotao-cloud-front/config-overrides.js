const {
  override,
  addLessLoader,
  addWebpackAlias,
  addBabelPlugins,
} = require('customize-cra');

const path = require('path');

module.exports = {
  webpack: override(
      addLessLoader(),
      addWebpackAlias({
        "@": path.resolve(__dirname, "src")
      }),
      // 配置babel解析器/启用ES7的修改器语法（babel 7）
      addBabelPlugins(
          ['@babel/plugin-proposal-class-properties', {loose: true}]
      )
  )
}
