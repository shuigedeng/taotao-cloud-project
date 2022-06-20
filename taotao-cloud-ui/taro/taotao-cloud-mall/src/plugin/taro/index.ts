import {IPluginContext} from '@tarojs/service'

export default (ctx: IPluginContext, pluginOpts) => {
  console.log(pluginOpts)

  ctx.onBuildStart(() => {
    console.log('编译开始！')
  })

  ctx.onBuildFinish(() => {
    console.log('编译结束！')
  })

  // ctx.addPluginOptsSchema((joi) => {
  //   console.log(joi.object())
  //   return joi.object().keys({
  //     server: joi.object().keys({
  //         url: joi.string(),
  //         port: joi.number(),
  //         ip: joi.string(),
  //       }
  //     ),
  //   })
  // })

  ctx.registerCommand({
    name: 'upload',
    optionsMap: {
      '--remote': 'server url',
    },
    synopsisList: [
      'taro upload --remote xxx.xxx.xxx.xxx'
    ],
    async fn() {
      const {options: {remote}} = ctx.runOpts
      await uploadDist(remote)
    }
  })

  const uploadDist = async (url) => {
    return Promise.resolve(url)
  }
}
