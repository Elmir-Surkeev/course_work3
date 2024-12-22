package com.example.carsharing.config;

import com.example.carsharing.model.User;
import com.example.carsharing.security.jwt.JwtConfigurer;
import com.example.carsharing.security.jwt.JwtTokenFilter;
import com.example.carsharing.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/register", "/inject", "/login", "/health", "/v2/api-docs",
                        "/swagger-resources/**", "/swagger-ui.html",
                        "/webjars/**", "/payments/success",
                        "/payments/cancel", "/cars").permitAll()
                .antMatchers(HttpMethod.POST, "/cars").hasRole(User.Role.MANAGER.name())
                .antMatchers("/cars/*").hasRole(User.Role.MANAGER.name())
                .antMatchers(HttpMethod.DELETE, "/cars/*", "/users/*")
                .hasRole(User.Role.MANAGER.name())
                .antMatchers(HttpMethod.GET, "/payments").hasRole(User.Role.MANAGER.name())
                .antMatchers(HttpMethod.GET, "/payments/my").hasRole(User.Role.CUSTOMER.name())
                .antMatchers(HttpMethod.PUT, "/users/*/role").hasRole(User.Role.MANAGER.name())
                .antMatchers(HttpMethod.PATCH, "/cars/*").hasRole(User.Role.MANAGER.name())
                .antMatchers(HttpMethod.GET, "/rentals/users/*", "rentals/*"
        ).hasRole(User.Role.MANAGER.name())
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
        http.addFilterBefore(new JwtTokenFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtTokenProvider);
    }
}
