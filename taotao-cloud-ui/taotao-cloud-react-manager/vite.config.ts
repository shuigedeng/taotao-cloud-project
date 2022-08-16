import reactRefresh from '@vitejs/plugin-react-refresh'
import { ConfigEnv, loadEnv, UserConfig } from 'vite'
import { resolve } from 'path'
import { createProxy } from './vite/proxy'
import { createVitePlugins } from './vite/plugin'
import { wrapperConf } from './vite/utils'
import legacy from '@vitejs/plugin-legacy'

require('dotenv').config()

const pkg = require('./package.json')

const root: string = process.cwd()

const pathResolve = (dir: string) => {
  return resolve(__dirname, '.', dir)
}

export default ({ command, mode }: ConfigEnv): UserConfig => {
  const config = require('./config')(mode)

  const viteEnv = wrapperConf(config)
  // const env = loadEnv(mode, root);

  const {
    VITE_PORT,
    VITE_PUBLIC_PATH,
    VITE_PROXY,
    VITE_DROP_CONSOLE,
    VITE_LEGACY
  } = viteEnv

  const isBuild = command === 'build'

  return {
    base: VITE_PUBLIC_PATH,
    root,
    alias: [
      {
        find: /^\/@\//,
        replacement: pathResolve('src') + '/'
      }
    ],
    server: {
      port: VITE_PORT,
      // proxy: createProxy(VITE_PROXY),
      hmr: {
        overlay: true
      }
    },
    build: {
      sourcemap: true,
      polyfillDynamicImport: VITE_LEGACY,
      terserOptions: {
        compress: {
          keep_infinity: true,
          drop_console: VITE_DROP_CONSOLE
        }
      },
      brotliSize: false,
      chunkSizeWarningLimit: 1200
    },
    define: {
      // ...viteEnv,
      // __VERSION__: pkg.version,
      // // setting vue-i18-next
      // // Suppress warning
      // __VUE_I18N_LEGACY_API__: false,
      // __VUE_I18N_FULL_INSTALL__: false,
      // __INTLIFY_PROD_DEVTOOLS__: false
    },
    css: {
      preprocessorOptions: {
        less: {
          modifyVars: {
            // reference:  Avoid repeated references
            // hack: `true; @import (reference) "${resolve('src/design/config.less')}";`,
            // ...generateModifyVars(),
            '@primary-color': '#34c38f',
            '@success-color': '#34c38f',
            '@warning-color': '#f1b44c',
            '@error-color': '#f46a6a',
            '@info-color': '#50a5f1',
            '@processing-color': '#6485ff',
            '@text-color': '#495057',
            '@border-radius-base': '6px',
            '@btn-border-radius-base': '5px',
            '@btn-border-radius-sm': '4px'
          },
          javascriptEnabled: true
        }
      }
    },
    plugins: [
      reactRefresh(),
      ...(VITE_LEGACY && isBuild ? [legacy()] : []),
      ...createVitePlugins(viteEnv, isBuild)
    ],
    optimizeDeps: {
      include: ['@iconify/iconify', 'lodash']
    }
  }
}
