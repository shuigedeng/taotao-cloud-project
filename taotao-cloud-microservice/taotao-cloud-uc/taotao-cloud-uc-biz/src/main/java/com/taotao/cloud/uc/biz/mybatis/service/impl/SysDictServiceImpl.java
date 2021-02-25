// package com.taotao.cloud.uc.biz.service.impl;
//
// import cn.hutool.core.bean.BeanUtil;
// import cn.hutool.core.util.ObjectUtil;
// import com.baomidou.mybatisplus.core.toolkit.Wrappers;
// import com.taotao.cloud.common.exception.BaseException;
// import com.taotao.cloud.data.mybatis.plus.service.impl.SuperServiceImpl;
// import com.taotao.cloud.uc.api.dto.DictDTO;
// import com.taotao.cloud.uc.biz.entity.SysDict;
// import com.taotao.cloud.uc.biz.mapper.SysDictMapper;
// import com.taotao.cloud.uc.biz.service.ISysDictItemService;
// import com.taotao.cloud.uc.biz.service.ISysDictService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
//
// import java.io.Serializable;
// import java.util.List;
// import java.util.stream.Collectors;
//
// /**
//  * 字典表 服务实现类
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:19
//  */
// @Service
// public class SysDictServiceImpl extends SuperServiceImpl<SysDictMapper, SysDict> implements ISysDictService {
//     @Autowired
//     private ISysDictItemService iSysDictItemService;
//
//     @Override
//     public boolean save(SysDict entity) {
//         return super.save(entity);
//     }
//
//     @Override
//     public boolean updateDict(DictDTO dictDto) {
//         if (ObjectUtil.isNull(dictDto.getValue())) {
//             // 先查询所有的含有的主键 然后批量修改
//             List<SysDict> sysDicts = baseMapper.selectList(Wrappers.<SysDict>lambdaQuery().select(SysDict::getId)
//                     .eq(SysDict::getDictName, baseMapper.selectById(dictDto.getId()).getDictName()));
//             List<SysDict> collect = sysDicts.stream().map(sysDict1 -> {
//                 SysDict sysDict = new SysDict();
//                 sysDict.setId(sysDict1.getId());
//                 sysDict.setDictName(dictDto.getDictName());
//                 return sysDict;
//             }).collect(Collectors.toList());
//             return updateBatchById(collect);
//         }
//         SysDict sysDict = new SysDict();
//         BeanUtil.copyProperties(dictDto, sysDict);
//         return baseMapper.updateById(sysDict) > 0;
//     }
//
//     @Override
//     public boolean removeById(Serializable id) {
// //        int count = iSysDictItemService.count(Wrappers.<SysDictItem>lambdaQuery().eq(SysDictItem::getDictId, id));
//         int count = 8;
//         if (count > 0) {
//             throw new BaseException("该字典详细有数据，无法删除");
//         }
//         return super.update(Wrappers.<SysDict>lambdaUpdate().set(SysDict::getDelFlag, "1").eq(SysDict::getId, id));
//     }
// }
