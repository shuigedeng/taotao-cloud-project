import { Result } from '/@/api/model/baseModel'
import { defHttp } from '/@/http/axios'
import { UploadFileParams, UploadFileVO } from '/@/api/file/model'

export function uploadApi(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void
) {
  return defHttp.uploadFile<Result<UploadFileVO>>(
    {
      url: '文件上传地址',
      onUploadProgress
    },
    params
  )
}
