import {GetCaptchaParam} from "./model";
import {Result} from "@/api/model/baseModel";
import request from "@/http/request";

export default {
  getCaptcha(data: GetCaptchaParam): Promise<Result<string>> {
    return request.get<Result<string>, GetCaptchaParam>("/code", data);
  },
}
