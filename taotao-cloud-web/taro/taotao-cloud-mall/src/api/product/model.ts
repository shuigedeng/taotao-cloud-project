export type Classify = {
  id
  title
  imageUrl
}

export type Item = {
  // # id
  code: number

  // # 名称
  name: string

  // # 主图
  imageUrl: string

  // # 简介
  content: string

  // # 原价
  originalPrice: number

  // # 佣金
  commission: number

  // # 价格
  price: number

  // # 会员价
  memberPrice: number

  // # 积分最大抵扣金额
  pointDiscountPrice: number

  // # 单位
  unit: string

  // # 库存
  stock: number

  // # 类型
  type: ItemType

  // # 专享/普通
  kind: ItemType

  // # 状态
  status: string

  // # 关注情况
  followed: boolean
  checked? : boolean
  number?: string
  itemId?: number
}

export enum ItemType {
// # 可退款类型（普通）
  ordinary,

// # 不可退款类型（专享）
  special
}
