package ${package}.${moduleName}.convert;

import ${package}.${moduleName}.entity.${ClassName}Entity;
import ${package}.${moduleName}.vo.${ClassName}VO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* ${tableComment}
*
* @author ${author} ${email}
* @since ${version} ${date}
*/
@Mapper
public interface ${ClassName}Convert {
    ${ClassName}Convert INSTANCE = Mappers.getMapper(${ClassName}Convert.class);

    ${ClassName}Entity convert(${ClassName}VO vo);

    ${ClassName}VO convert(${ClassName}Entity entity);

    List<${ClassName}VO> convertList(List<${ClassName}Entity> list);

}