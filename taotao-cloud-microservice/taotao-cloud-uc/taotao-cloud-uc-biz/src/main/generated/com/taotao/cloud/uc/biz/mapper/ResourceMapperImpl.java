package com.taotao.cloud.uc.biz.mapper;

import com.taotao.cloud.uc.api.dto.resource.ResourceDTO;
import com.taotao.cloud.uc.api.vo.resource.ResourceVO;
import com.taotao.cloud.uc.biz.entity.SysResource;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-01-20T16:59:50+0800",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class ResourceMapperImpl implements ResourceMapper {

    @Override
    public SysResource resourceDtoToSysResource(ResourceDTO resourceDTO) {
        if ( resourceDTO == null ) {
            return null;
        }

        SysResource sysResource = new SysResource();

        sysResource.setName( resourceDTO.getName() );
        sysResource.setPerms( resourceDTO.getPerms() );
        sysResource.setPath( resourceDTO.getPath() );
        sysResource.setComponent( resourceDTO.getComponent() );
        sysResource.setParentId( resourceDTO.getParentId() );
        sysResource.setIcon( resourceDTO.getIcon() );
        sysResource.setKeepAlive( resourceDTO.getKeepAlive() );
        sysResource.setHidden( resourceDTO.getHidden() );
        sysResource.setAlwaysShow( resourceDTO.getAlwaysShow() );
        sysResource.setRedirect( resourceDTO.getRedirect() );
        sysResource.setIsFrame( resourceDTO.getIsFrame() );
        sysResource.setSortNum( resourceDTO.getSortNum() );
        if ( resourceDTO.getType() != null ) {
            sysResource.setType( resourceDTO.getType() );
        }

        return sysResource;
    }

    @Override
    public ResourceVO sysResourceDtoResourceVo(SysResource sysResource) {
        if ( sysResource == null ) {
            return null;
        }

        ResourceVO resourceVO = new ResourceVO();

        resourceVO.setId( sysResource.getId() );
        resourceVO.setName( sysResource.getName() );
        resourceVO.setType( sysResource.getType() );
        resourceVO.setPerms( sysResource.getPerms() );
        resourceVO.setPath( sysResource.getPath() );
        resourceVO.setComponent( sysResource.getComponent() );
        resourceVO.setParentId( sysResource.getParentId() );
        resourceVO.setIcon( sysResource.getIcon() );
        resourceVO.setKeepAlive( sysResource.getKeepAlive() );
        resourceVO.setHidden( sysResource.getHidden() );
        resourceVO.setAlwaysShow( sysResource.getAlwaysShow() );
        resourceVO.setRedirect( sysResource.getRedirect() );
        resourceVO.setIsFrame( sysResource.getIsFrame() );
        resourceVO.setSortNum( sysResource.getSortNum() );
        resourceVO.setCreateTime( sysResource.getCreateTime() );
        resourceVO.setLastModifiedTime( sysResource.getLastModifiedTime() );

        return resourceVO;
    }

    @Override
    public List<ResourceVO> sysResourceToResourceVo(List<SysResource> resourceList) {
        if ( resourceList == null ) {
            return null;
        }

        List<ResourceVO> list = new ArrayList<ResourceVO>( resourceList.size() );
        for ( SysResource sysResource : resourceList ) {
            list.add( sysResourceDtoResourceVo( sysResource ) );
        }

        return list;
    }

    @Override
    public void copyResourceDtoToSysResource(ResourceDTO resourceDTO, SysResource sysResource) {
        if ( resourceDTO == null ) {
            return;
        }

        sysResource.setName( resourceDTO.getName() );
        sysResource.setPerms( resourceDTO.getPerms() );
        sysResource.setPath( resourceDTO.getPath() );
        sysResource.setComponent( resourceDTO.getComponent() );
        sysResource.setParentId( resourceDTO.getParentId() );
        sysResource.setIcon( resourceDTO.getIcon() );
        sysResource.setKeepAlive( resourceDTO.getKeepAlive() );
        sysResource.setHidden( resourceDTO.getHidden() );
        sysResource.setAlwaysShow( resourceDTO.getAlwaysShow() );
        sysResource.setRedirect( resourceDTO.getRedirect() );
        sysResource.setIsFrame( resourceDTO.getIsFrame() );
        sysResource.setSortNum( resourceDTO.getSortNum() );
        if ( resourceDTO.getType() != null ) {
            sysResource.setType( resourceDTO.getType() );
        }
    }
}
