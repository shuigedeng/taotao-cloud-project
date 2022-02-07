declare module "*.png";
declare module "*.gif";
declare module "*.jpg";
declare module "*.jpeg";
declare module "*.svg";
declare module "*.css";
declare module "*.less";
declare module "*.scss";
declare module "*.sass";
declare module "*.styl";
declare module "*.mp4";

declare const IS_H5: boolean;
declare const IS_WEAPP: boolean;
declare const IS_RN: boolean;

declare interface GlobalData {
  //是否为开发环境
  debug: boolean;
  console: boolean;
}
