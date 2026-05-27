package com.pacifico.claims.infrastructure.config;

import com.pacifico.claims.infrastructure.adapters.out.persistence.repository.RoleR2dbcRepository;
import com.pacifico.claims.infrastructure.adapters.out.persistence.repository.UserR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements ReactiveUserDetailsService {

    private final UserR2dbcRepository userR2dbcRepository;
    private final RoleR2dbcRepository roleR2dbcRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userR2dbcRepository.findByUsername(username)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User not found: " + username))))
            .flatMap(user -> roleR2dbcRepository.findRolesByUserId(user.getId())
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList())
                .map(authorities -> User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .disabled(!user.isEnabled())
                    .authorities(authorities)
                    .build()
                )
            );
    }
}
