import { viteMockServe } from 'vite-plugin-mock'
import { ViteEnv } from '../utils'
import path = require("path");

export function configMockPlugin(env: ViteEnv, isBuild: boolean) {
  const { VITE_USE_MOCK } = env

  const useLocalMock = !isBuild && VITE_USE_MOCK
  const useProdMock = isBuild && VITE_USE_MOCK

  if (useLocalMock || useProdMock) {
    return viteMockServe({
      ignore: /^_/,
      mockPath: 'mock',
      showTime: true,
      localEnabled: true,
      prodEnabled: false,
      supportTs: true,
      injectFile: path.resolve(process.cwd(), 'src/index.tsx'),
      injectCode: `
      import { setupProdMockServer } from '../mock/_createProductionServer';

      setupProdMockServer();
      `
    })
  }
  return []
}
