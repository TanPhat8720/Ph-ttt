package com.hutech.tests3.Config;

import com.hutech.tests3.Services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private SuccessHandle successHandle;
    @Autowired
    private FailureHandle failureHandle;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                request->request
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/users").hasAuthority("USER")
                        .requestMatchers("/users/**").hasAuthority("MODIFIER")
                        .requestMatchers("/roles").hasAuthority("ADMIN")
                        .requestMatchers("/restapi").permitAll()
                        .anyRequest().permitAll()
        ).formLogin(AbstractConfiguredSecurityBuilder
                ->AbstractConfiguredSecurityBuilder.loginPage("/login")
                .successHandler(successHandle)
                        .failureHandler(failureHandle)
                .permitAll()
        ).rememberMe(remember->remember.tokenRepository(persistentTokenRepository())
                        .tokenValiditySeconds(1000*10*60))
                .logout(logout->logout.deleteCookies("JSESSIONID")).build();
    }

    public UserDetailsService UserDetailsService() {

        return customUserDetailService;
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(UserDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepo = new JdbcTokenRepositoryImpl();
        tokenRepo.setDataSource(dataSource);
        return tokenRepo;
    }

}
