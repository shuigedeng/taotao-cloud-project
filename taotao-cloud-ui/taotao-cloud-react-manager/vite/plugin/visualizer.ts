import { isReportMode } from '../utils'
// @ts-ignore
import visualizer from 'rollup-plugin-visualizer'

export function configVisualizerConfig() {
  if (isReportMode()) {
    return visualizer({
      filename: './visualizer/stats.html',
      open: false
    }) as Plugin
  }
  return []
}
