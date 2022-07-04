package com.example.woowa.security.service;

import com.example.woowa.security.repository.RoleRepository;
import com.example.woowa.security.repository.UserRepository;
import com.example.woowa.security.user.Role;
import com.example.woowa.security.user.User;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new UsernameNotFoundException("해당 아이디를 가진 유저를 찾을 수 없습니다."));
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByLoginId(username);
        user.login();
        Collection<SimpleGrantedAuthority> authroties = new ArrayList<>();
        user.getRoles().forEach(role ->
            authroties.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getLoginId(), user.getPassword(), authroties);
    }

}
