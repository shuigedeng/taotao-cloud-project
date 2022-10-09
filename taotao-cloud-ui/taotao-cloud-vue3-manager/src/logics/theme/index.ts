import {generateColors, getThemeColors} from '../../../script/config/themeConfig';

import {replaceStyleVariables} from 'vite-plugin-theme/es/client';
import {mixDarken, mixLighten, tinycolor} from 'vite-plugin-theme/es/colorUtils';

export async function changeTheme(color: string) {
  const colors = generateColors({
    mixDarken,
    mixLighten,
    tinycolor,
    color,
  });

  return await replaceStyleVariables({
    colorVariables: [...getThemeColors(color), ...colors],
  });
}
