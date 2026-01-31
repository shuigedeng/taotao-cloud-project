package com.taotao.cloud.sys.biz.utils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

//组装生成复杂父子树形结构， Stream + Lambda优雅搞定！
//一般来说完成这样的需求大多数人会想到递归，但递归的方式弊端过于明显：方法多次自调用效率很低、数据量大容易导致堆栈溢出、随着树深度的增加其时间复杂度会呈指数级增加等。

//一般来说完成这样的需求大多数人会想到递归，但递归的方式弊端过于明显：方法多次自调用效率很低、数据量大容易导致堆栈溢出、随着树深度的增加其时间复杂度会呈指数级增加等。

//核心思路如下：
//
// 一次数据库查询全部数据（几万条），其它全是内存操作、性能高；
// 同时熟练使用 stream 流操作、Lambda 表达式、Java 地址引用，完成组装；
// 使用缓存注解（底层Redis分布式缓存实现），过期后自动更新缓存，再次调用接口则先命中缓存，没有的话再查数据库
// 使用RocketMQ来做异步通知更新，即当数据有更改时，可以异步将数据先更新，再写入缓存，使业务更合理，考虑更全面
/**
 * TreeOtherUtils
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class TreeOtherUtils {

    public List<DeptTreeNodeVO> assembleTree() {
        // 租户信息列表，这里是两个租户
        List<PmTenant> tenantList = new ArrayList<>();
        // List<PmTenant> tenantList = this.pmTenantService.list();
        // step1：最外层根据租户去组装，有两个租户那么 Stream 就会遍历组装两次；换句话说，如果只有一个租户，就不需要最外层的 Stream
        List<DeptTreeNodeVO> resultList = tenantList
                .stream()
                .map(tenant -> {
                    // 注：这里 map 只是简单转换了返回的对象属性（返回需要的类型），本质还是该租户下的所有部门数据
                    List<DeptTreeNodeVO> deptTreeNodeVOList = this.selectAllDeptByTenantCode(tenant.getTenantCode())
                            .stream()
                            .map(val -> val.convertExt(DeptTreeNodeVO.class))
                            .collect(Collectors.toList());
                    // step2：利用父节点分组，即按照该租户下的所有部门的父Id进行分组，把所有的子节点List集合都找出来并一层层分好组
                    Map<Integer, List<DeptTreeNodeVO>> listMap = deptTreeNodeVOList
                            .parallelStream()
                            .collect(Collectors.groupingBy(DeptTreeNodeVO::getParentDeptId));
                    // step3：关键一步，关联上子部门，将子部门的List集合经过遍历一层层地放置好，最终会得到完整的部门父子关系List集合
                    deptTreeNodeVOList.forEach(val -> val.setChildrenNodeList(listMap.get(val.getDeptId())));
                    // step4：过滤出顶级部门，即所有的子部门数据都归属于一个顶级父Id
                    List<DeptTreeNodeVO> allChildrenList = deptTreeNodeVOList
                            .stream()
                            .filter(val -> val.getParentDeptId().equals(NumberUtils.INTEGER_ZERO))
                            .collect(Collectors.toList());
                    // 组装最外层关于租户需要的数据，实质已经不是处理部门数据了
                    DeptTreeNodeVO node = new DeptTreeNodeVO();
                    node.setChildrenNodeList(allChildrenList);
                    node.setDeptName(tenant.getTenantName());
                    return node;
                }).collect(Collectors.toList());
        return Optional.of(resultList).orElse(null);
    }

    /**
     * 获取某个租户下的所有部门信息
     */
    public List<PmDept> selectAllDeptByTenantCode( String tenantCode ) {
        // return pmDeptMapper.selectList(new LambdaQueryWrapper<PmDept>()
        // 	.eq(PmDept::getTenantCode, tenantCode)
        // 	.eq(PmDept::getStatus, PmDeptStatus.DISABLE.getStatus()));
        return new ArrayList<>();
    }

    public List<RegionCascadeVO> quickAllTree() {
        // 第一步，从数据库中查出所有数据，按照排序条件进行排序，本质上还是这个所有数据的 List 集合
        // List<RegionCascadeVO> regionCascadeVOList = this.regionRepository.findAll().stream()
        List<RegionCascadeVO> regionCascadeVOListData = new ArrayList<>();
        List<RegionCascadeVO> regionCascadeVOList = regionCascadeVOListData
                .stream()
                // 注：这里使用 map 映射了需要返回的VO，即相同的属性字段就会转换
                .map(val -> val.convertExt(RegionCascadeVO.class))
                // 业务需要的排序规则，使用工具来处理
                // .sorted((s1, s2) -> RegionSortUtil.citySort(s1.getName(), s2.getName()))
                // .sorted((s1, s2) -> RegionSortUtil.countySort(s1.getName(), s2.getName()))
                .collect(Collectors.toList());
        // 第二步，根据父Id 字段进行分组，即所有数据都会按照第一层至最后一层都按照父子关系进行分组；注意，是对所有数据分组
        Map<Long, List<RegionCascadeVO>> listMap = regionCascadeVOList
                .parallelStream()
                .collect(Collectors.groupingBy(RegionCascadeVO::getParentId));
        // 第三步，也是最关键的一步，将父Id下面的所有子数据List集合，经过遍历后都一层层地放置好，最终会得到一个包含父子关系的完整List
        regionCascadeVOList.forEach(val -> val.setChildrenRegionList(listMap.get(val.getId())));
        // 第四步，过滤出符合顶层父Id的所有数据，即所有数据都归属于一个顶层父Id
        return regionCascadeVOList
                .stream()
                //.filter(val -> RegionConstant.CHINA_ID.equals(val.getParentId()))
                .collect(Collectors.toList());
    }


    @Data
    public class RegionCascadeVO {

        /**
         * 子节点 list 集合
         */
        private List<RegionCascadeVO> childrenRegionList;
        /**
         * 区域id
         */
        public Long id;
        /**
         * 地区名称
         */
        public String name;
        /**
         * 所处层级
         */
        public Integer depth;
        /**
         * 省
         */
        public String province;
        /**
         * 城市
         */
        public String city;
        /**
         * 地区全称
         */
        public String district;
        /**
         * 父Id
         */
        public Long parentId;
        /**
         * 所属省Id
         */
        public Long provinceId;
        /**
         * 所属地级市Id
         */
        public Long cityId;

        public RegionCascadeVO convertExt( Class clazz ) {
            return null;
        }
    }


    @Data
    public class PmTenant {

        /**
         * 主键Id
         */
        @TableId(type = IdType.ASSIGN_ID)
        private Long id;
        /**
         * 租户名称
         */
        private String tenantName;
        /**
         * 租户唯一编码，对外暴露
         */
        private String tenantCode;
        /**
         * 租户Id
         */
        private String tenantId;
        /**
         * 租户状态，0可用，1禁用
         */
        private Integer status;
    }

    @Data
    public class PmDept {

        /**
         * 主键id
         */
        @TableId(type = IdType.ASSIGN_ID)
        private Integer id;
        /**
         * 父部门Id
         */
        private Integer parentDeptId;
        /**
         * 部门id，全局唯一，所有系统用
         */
        private Integer deptId;
        /**
         * 部门名称
         */
        private String deptName;
        /**
         * 部门所处的排序
         */
        private Integer orderNum;
        /**
         * 部门所处的层级
         */
        private Integer depth;
        /**
         * 部门状态，0可用，1删除
         */
        private Integer status;
        /**
         * 租户id
         */
        private String tenantId;
        /**
         * 租户编码
         */
        private String tenantCode;

        public DeptTreeNodeVO convertExt( Class clazz ) {
            return null;
        }
    }

    @Data
    public class DeptTreeNodeVO implements Serializable {

        /**
         * 子节点 list 集合，封装自己
         */
        private List<DeptTreeNodeVO> childrenNodeList;
        /**
         * 部门Id
         */
        protected Integer deptId;
        /**
         * 父部门Id
         */
        protected Integer parentDeptId;
        /**
         * 部门名称
         */
        protected String deptName;

        public DeptTreeNodeVO convertExt( Class clazz ) {
            return null;
        }
    }
}
