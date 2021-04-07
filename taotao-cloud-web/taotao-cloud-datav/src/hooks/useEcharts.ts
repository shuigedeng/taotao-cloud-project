import {useEffect, useState} from 'react'
// @ts-ignore
import debounce from 'lodash/debounce'
import * as echarts from 'echarts'
import {EChartOption, ECharts} from 'echarts'

const useEcharts = (
    ref: HTMLElement | null,
    initOption: EChartOption,
    theme: string | object = 'macarons',
): ECharts | undefined => {
  const [chart, setChart] = useState<ECharts>()

  useEffect(() => {
    console.log(ref)
    // @ts-ignore
    const newChart = echarts.init(ref.current, theme)
    newChart.setOption(initOption)
    setChart(newChart)

    const resizeHandler = debounce(() => {
      newChart.resize()
    }, 100)

    window.addEventListener('resize', resizeHandler)

    return () => {
      newChart.dispose()
      window.removeEventListener('resize', resizeHandler)
    }
  }, [1])

  return chart
}

export default useEcharts
