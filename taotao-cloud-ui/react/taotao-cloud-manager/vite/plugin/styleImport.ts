import styleImport from 'vite-plugin-style-import'

export function configStyleImportPlugin() {
  return styleImport({
    libs: [
      {
        libraryName: 'antd',
        esModule: true,
        resolveStyle: name => {
          return `antd/es/${name}/style/index`
        }
      }
    ]
  })
}
