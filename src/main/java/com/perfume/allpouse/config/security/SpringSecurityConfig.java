package com.perfume.allpouse.config.security;

import com.perfume.allpouse.filter.CustomAccessDeniedHandler;
import com.perfume.allpouse.filter.CustomAuthenticationEntryPoint;
import com.perfume.allpouse.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 2022/2/21부터 스프링 시큐리티에서 해당 옵션이 사라짐으로 securityfilterchain을 @Bean으로 구현
 */

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final TokenProvider tokenProvider;


    protected SpringSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * 인증과 인과가 적용되지 않는 리소스들의 경로를 명시하는 설정 시프링 시큐리티를 타지 않는다.
     * @return WebSecurityCustomizer
     */

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers(
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger/**",
                "**Exception**"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/sign-api/sign-in","/sign-api/sign-up","/sign-api/sign-exception","/swagger-ui/**" ).permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("**exception**").permitAll()
                .anyRequest().hasRole("USER")
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

                return http.build();
    }

}
