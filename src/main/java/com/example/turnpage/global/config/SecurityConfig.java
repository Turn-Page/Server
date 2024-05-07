package com.example.turnpage.global.config;

import com.example.turnpage.domain.member.repository.MemberRepository;
import com.example.turnpage.domain.member.service.redis.RefreshTokenService;
import com.example.turnpage.global.config.security.filter.JwtAuthenticationFilter;
import com.example.turnpage.global.config.security.handler.CustomAccessDeniedHandler;
import com.example.turnpage.global.config.security.handler.OAuth2LoginSuccessHandler;
import com.example.turnpage.global.config.security.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.example.turnpage.global.config.security.service.CustomOAuth2UserService;
import com.example.turnpage.global.config.security.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtils jwtUtils;
    private final MemberRepository memberRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final RefreshTokenService refreshTokenService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
        return new OAuth2LoginSuccessHandler(jwtUtils, refreshTokenService, memberRepository, oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/public").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**").permitAll()
                        .requestMatchers("/private/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler))
                .oauth2Login(oauth2 -> oauth2
                                .loginPage("/auth/login")
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler())
                )
                .addFilterAfter(new JwtAuthenticationFilter(jwtUtils), OAuth2LoginAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    static RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return hierarchy;
    }
}
