import {
  mixDarken,
  mixLighten,
  tinycolor,
  viteThemePlugin
} from 'vite-plugin-theme'
import { generateColors, getThemeColors } from '../themeConfig'

export function configThemePlugin() {
  const colors = generateColors({
    mixDarken,
    mixLighten,
    tinycolor
  })

  return viteThemePlugin({
    colorVariables: [...getThemeColors(), ...colors]
  })
}
