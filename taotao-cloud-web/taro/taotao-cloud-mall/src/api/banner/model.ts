export type Banner = {
  //  id
  id: number;

  //  标题
  title: string;

  // 位置
  position: string;

  //  图片地址
  imageUrl: string
}


export type QueryBannerParam = {
  position?: string
}
