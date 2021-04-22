const HOST = '"http://192.168.99.37:9527"';
module.exports = {
  env: {
    NODE_ENV: '"development"'
  },
  defineConstants: {
  },
  mini: {},
  h5: {
    esnextModules: ['taro-ui'],
    devServer: {
      proxy: {
        '/': {
          target: JSON.parse(HOST),
          pathRewrite: {
            '^/api': '/api'
          },
          changeOrigin: true
        },
      }
    },
  }
}
