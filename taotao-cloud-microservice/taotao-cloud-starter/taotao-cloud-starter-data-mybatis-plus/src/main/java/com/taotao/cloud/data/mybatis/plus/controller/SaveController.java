//package com.taotao.cloud.data.mybatis.plus.controller;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.tangyh.basic.annotation.log.SysLog;
//import com.tangyh.basic.annotation.security.PreAuth;
//import com.tangyh.basic.base.R;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
///**
// * 新增
// *
// * @param <Entity>  实体
// * @param <SaveDTO> 保存参数
// * @author zuihou
// * @date 2020年03月07日22:07:31
// */
//public interface SaveController<Entity, SaveDTO> extends BaseController<Entity> {
//
//    /**
//     * 新增
//     *
//     * @param saveDTO 保存参数
//     * @return 实体
//     */
//    @ApiOperation(value = "新增")
//    @PostMapping
//    @SysLog(value = "新增", request = false)
//    @PreAuth("hasAnyPermission('{}add')")
//    default R<Entity> save(@RequestBody @Validated SaveDTO saveDTO) {
//        R<Entity> result = handlerSave(saveDTO);
//        if (result.getDefExec()) {
//            Entity model = BeanUtil.toBean(saveDTO, getEntityClass());
//            getBaseService().save(model);
//            result.setData(model);
//        }
//        return result;
//    }
//
//    /**
//     * 自定义新增
//     *
//     * @param model 保存对象
//     * @return 返回SUCCESS_RESPONSE, 调用默认更新, 返回其他不调用默认更新
//     */
//    default R<Entity> handlerSave(SaveDTO model) {
//        return R.successDef();
//    }
//
//}
