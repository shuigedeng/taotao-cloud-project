import gzipPlugin from 'rollup-plugin-gzip'
import { Plugin } from 'vite'
import { isBuildGzip } from '../utils'

export function configGzipPlugin(isBuild: boolean): Plugin | Plugin[] {
  const useGzip = isBuild && isBuildGzip()

  if (useGzip) {
    return gzipPlugin()
  }

  return []
}
