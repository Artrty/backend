package com.springboot.backend.Service;

import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.UserRepository;
import com.springboot.backend.jwt.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        // Optional<User>를 사용하여 User를 가져오고 orElseThrow 호출
        User user = userRepository.findByPhoneNumber(phoneNumber);
//                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new CustomUser(  // Long 타입으로 전달
                user.getUuid(),  // user.getUuid()가 Long 타입인지 확인
                user.getPhoneNumber(),
                user.getPassword(),
                getAuthorities(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
