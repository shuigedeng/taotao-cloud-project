const config = mode => {
  switch (mode) {
    case 'production':
      return require('./prod')
    case 'development':
      return require('./dev')
    default:
      return {}
  }
}

const defaultConfig = {
  VITE_PORT: 3100,
  VITE_GLOB_APP_TITLE: 'Vbe Admin',
  VITE_GLOB_APP_SHORT_NAME: 'vue_vben_admin_2x'
}

module.exports = mode => {
  return Object.assign(defaultConfig, config(mode))
}
