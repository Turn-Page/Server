package com.example.turnpage.global.config;

import com.example.turnpage.domain.member.converter.MemberConverter;
import com.example.turnpage.domain.member.repository.MemberRepository;
import com.example.turnpage.domain.member.service.redis.RefreshTokenService;
import com.example.turnpage.global.config.security.filter.CustomAuthenticationEntryPoint;
import com.example.turnpage.global.config.security.filter.CustomOAuth2LoginAuthenticationFilter;
import com.example.turnpage.global.config.security.filter.JwtAuthenticationFilter;
import com.example.turnpage.global.config.security.filter.JwtExceptionFilter;
import com.example.turnpage.global.config.security.handler.OAuth2LoginSuccessHandler;
import com.example.turnpage.global.config.security.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.example.turnpage.global.config.security.service.CustomOAuth2UserService;
import com.example.turnpage.global.config.security.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtils jwtUtils;
    private final MemberRepository memberRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final RefreshTokenService refreshTokenService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;
    private final MemberConverter memberConverter;

    public CustomOAuth2LoginAuthenticationFilter customOAuth2LoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        CustomOAuth2LoginAuthenticationFilter customOAuth2LoginAuthenticationFilter = new CustomOAuth2LoginAuthenticationFilter(
                clientRegistrationRepository, oAuth2AuthorizedClientRepository, authenticationManager);

        customOAuth2LoginAuthenticationFilter.setAuthenticationSuccessHandler(oAuth2LoginSuccessHandler());
        return customOAuth2LoginAuthenticationFilter;
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
        return new OAuth2LoginSuccessHandler(jwtUtils, refreshTokenService, memberRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(), memberConverter);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder amBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        AuthenticationManager authenticationManager = amBuilder.build();

        http.authenticationManager(authenticationManager);

        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .disable()
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/callback/oauth2/code/**").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/books/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/salePosts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/salePosts/my").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/comments").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**").permitAll()
                        .requestMatchers("/members/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint))
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler())
                        .loginPage("/auth/login")
                )
                .addFilterAt(customOAuth2LoginAuthenticationFilter(authenticationManager), OAuth2LoginAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthenticationFilter(jwtUtils), OAuth2LoginAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
        ;


        return http.build();
    }

    @Bean
    static RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return hierarchy;
    }
}
