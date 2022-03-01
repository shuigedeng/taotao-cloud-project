package com.taotao.cloud.sys.biz.tools.security.service;

import com.sanri.tools.modules.security.service.dtos.SecurityUser;
import com.sanri.tools.modules.security.service.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class FileUserDetailServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    public FileUserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final SecurityUser securityUser = userRepository.getUser(username);
        if (securityUser == null){
            throw new UsernameNotFoundException("未找到用户:"+username);
        }
        return securityUser;
    }
}
