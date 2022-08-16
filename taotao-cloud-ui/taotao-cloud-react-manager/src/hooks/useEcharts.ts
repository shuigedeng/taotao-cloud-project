// import { useEffect, useState } from 'react'
// import debounce from 'lodash/debounce'
// import echarts, { ECharts } from 'echarts'
// import 'echarts/theme/macarons'
//
// const useEcharts = (
//   ref: HTMLElement | null,
//   initOption: echarts.EChartOption,
//   theme: string | object = 'macarons'
// ): echarts.ECharts | undefined => {
//   const [chart, setChart] = useState<ECharts>()
//
//   useEffect(() => {
//     // @ts-ignore
//     const newChart = echarts.init(ref.current, theme)
//     newChart.setOption(initOption)
//     setChart(newChart)
//
//     const resizeHandler = debounce(() => {
//       newChart.resize()
//     }, 100)
//
//     window.addEventListener('resize', resizeHandler)
//
//     return () => {
//       newChart.dispose()
//       window.removeEventListener('resize', resizeHandler)
//     }
//   }, [1])
//
//   return chart
// }
//
// export default useEcharts
export default {}