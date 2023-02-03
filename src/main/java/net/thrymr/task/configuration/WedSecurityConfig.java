package net.thrymr.task.configuration;

import net.thrymr.task.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WedSecurityConfig {


    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @Autowired
    AppUserRepository appUserRepository;

    private String[] PUBLIC_RESOURCE_AND_URL = {"/",
            "/api/signup",
            "/api/sign-in",
    };

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and().addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenUtils, appUserRepository), BasicAuthenticationFilter.class).
                addFilterBefore(new CustomCORSFilter(), ChannelProcessingFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(false)
                .ignoring()
                .requestMatchers(PUBLIC_RESOURCE_AND_URL);
    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
