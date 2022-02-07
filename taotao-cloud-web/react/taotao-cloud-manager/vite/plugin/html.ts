import html from 'vite-plugin-html'

import { ViteEnv } from '../utils'

const GLOB_CONFIG_FILE_NAME = '_app.config.js'

export function configHtmlPlugin(env: ViteEnv, isBuild: boolean) {
  const { VITE_GLOB_APP_TITLE, VITE_PUBLIC_PATH } = env

  const path = VITE_PUBLIC_PATH.endsWith('/')
    ? VITE_PUBLIC_PATH
    : `${VITE_PUBLIC_PATH}/`

  return html({
    minify: isBuild,
    inject: {
      injectData: {
        title: VITE_GLOB_APP_TITLE
      },
      tags: isBuild
        ? [
            {
              tag: 'script',
              attrs: {
                // src: `${path || '/'}${GLOB_CONFIG_FILE_NAME}?v=${pkg.version}-${new Date().getTime()}`
                src: `${path || '/'}${GLOB_CONFIG_FILE_NAME}?v=0.0.1-${new Date().getTime()}`
              }
            }
          ]
        : []
    }
  })
}
