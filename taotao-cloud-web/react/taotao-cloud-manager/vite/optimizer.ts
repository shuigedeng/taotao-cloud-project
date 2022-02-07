import { GetManualChunk, GetManualChunkApi } from 'rollup'

const vendorLibs: { match: string[]; output: string }[] = [
  // {
  //   match: ['xlsx'],
  //   output: 'xlsx',
  // },
]

export const configManualChunk: GetManualChunk = (
  id: string,
  api: GetManualChunkApi
) => {
  if (/[\\/]node_modules[\\/]/.test(id)) {
    const matchItem = vendorLibs.find(item => {
      const reg = new RegExp(
        `[\\/]node_modules[\\/]_?(${item.match.join('|')})(.*)`,
        'ig'
      )
      return reg.test(id)
    })
    return matchItem ? matchItem.output : null
  }
}
