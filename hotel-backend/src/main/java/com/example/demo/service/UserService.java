package com.example.demo.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final PasswordEncoder encoder;

    @Getter
    public static class DemoUser {
        private final Long id = 1L;
        private final String loginId = "demo";
        private final String nickname = "데모";
        private final String encodedPassword;
        public DemoUser(String encodedPassword) { this.encodedPassword = encodedPassword; }
    }

    private DemoUser demoUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (demoUser == null) demoUser = new DemoUser(encoder.encode("pass1234"));
        if (!"demo".equals(username)) throw new UsernameNotFoundException("user not found");
        return User.withUsername("demo")
                .password(demoUser.getEncodedPassword())
                .authorities("ROLE_USER")
                .build();
    }

    public DemoUser getDemoUser() {
        if (demoUser == null) demoUser = new DemoUser(encoder.encode("pass1234"));
        return demoUser;
    }
}
