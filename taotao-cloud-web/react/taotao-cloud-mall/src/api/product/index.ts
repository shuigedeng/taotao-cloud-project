import {BasicPageParams, Page, Result} from "@/api/model/baseModel";
import request from "@/http/request";
import {Classify, Item} from "@/api/product/model";

export default {
  getClassify(): Promise<Result<Classify[]>> {
    return request.get<Result<Classify[]>, {}>("/classify", {});
  },
  getItems(data: BasicPageParams): Promise<Result<Page<Item>>> {
    return request.get<Result<Page<Item>>, BasicPageParams>("/items", data);
  },
  getProjectItems({projectId, pageSize}): Promise<Result<Item[]>> {
    return request.get<Result<Item[]>, {}>("/items/project", {});
  },
  getItemsByClassId(data: { currentPage: number, pageSize: number }, classId: number): Promise<Result<Page<Item>>> {
    return request.get<Result<Page<Item>>, {}>("/items/" + classId, data);
  },
}
