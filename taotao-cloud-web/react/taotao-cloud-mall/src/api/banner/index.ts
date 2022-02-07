import {Result} from "@/api/model/baseModel";
import request from "@/http/request";
import {Banner, QueryBannerParam} from "@/api/banner/model";

export default {
  getBanners(data?: QueryBannerParam): Promise<Result<Banner[]>> {
    return request.get<Result<Banner[]>, QueryBannerParam>("/banners", data);
  },
}
