const DEFAULT_SETTINGS = {
  /**
   * 是否显示系统设置
   *
   * 如果只想在开发环境下显示系统设置面板，生产环境下不显示，那么请打开下面这行代码
   * showSettings: process.env.NODE_ENV === "development",
   */
  showSettings: true,

  /**
   * 是否在侧边栏中显示logo
   */
  sidebarLogo: true,

  /**
   * 是否固定header
   */
  fixedHeader: false,

  /**
   * 是否开启tagsView
   */
  tagsView: true
}

export default DEFAULT_SETTINGS

// (window as any).less.modifyVars({
//   '@primary-color': color.hex,
// });
