package ${package}.${moduleName}.service;

import ${package}.framework.common.utils.PageResult;
import ${package}.framework.mybatis.service.BaseService;
import ${package}.${moduleName}.vo.${ClassName}VO;
import ${package}.${moduleName}.query.${ClassName}Query;
import ${package}.${moduleName}.entity.${ClassName}Entity;

import java.util.List;

/**
 * ${tableComment}
 *
 * @author ${author} ${email}
 * @since ${version} ${date}
 */
public interface ${ClassName}Service extends BaseService<${ClassName}Entity> {

    PageResult<${ClassName}VO> page(${ClassName}Query query);

    void save(${ClassName}VO vo);

    void update(${ClassName}VO vo);

    void delete(List<Long> idList);
}