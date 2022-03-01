package com.taotao.cloud.sys.biz.tools.security.service.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sanri.tools.modules.core.security.dtos.ThinUser;
import com.sanri.tools.modules.core.security.entitys.ToolGroup;
import com.sanri.tools.modules.core.security.entitys.ToolRole;
import com.sanri.tools.modules.core.security.entitys.ToolUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SecurityUser extends ThinUser implements UserDetails {

    public SecurityUser(ToolUser toolUser) {
        super(toolUser);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
    }

    public String getMainGroup(){
        return groups.get(0);
    }

    public String getMainRole(){
        if (roles.size() > 0){
            return roles.get(0);
        }
        return null;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return toolUser.getPassword();
    }

    @Override
    public String getUsername() {
        return toolUser.getUsername();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
