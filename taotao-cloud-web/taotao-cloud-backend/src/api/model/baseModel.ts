export interface BasicPageParams {
  currentPage: number
  pageSize: number
}

export type Result<D> = {
  code: number
  message: string
  data: D
  requestId: string
  timestamp: string
}

export type PageResult<D> = {
  code: number
  message: string
  total: number
  pageSize: number
  currentPage: number
  data: Array<D>
  requestId: string
  timestamp: string
}
