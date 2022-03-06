package com.esm.epam.jwt.config;

import com.esm.epam.jwt.filter.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.esm.epam.util.ParameterAttribute.ADMIN_ROLE;
import static com.esm.epam.util.ParameterAttribute.USER_ROLE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
   private JwtFilter filterBean;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()

                .antMatchers(GET, "/certificates/*").permitAll()
                .antMatchers(POST, "/auth/*").permitAll()

                .antMatchers(PATCH, "/users/*").hasAnyRole(USER_ROLE, ADMIN_ROLE)
                .antMatchers(GET, "/users/*", "/tags/*", "/certificates/*", "/tags", "/certificates").hasAnyRole(USER_ROLE, ADMIN_ROLE)

                .anyRequest().hasRole(ADMIN_ROLE)
                .and()
                .addFilterBefore(filterBean, UsernamePasswordAuthenticationFilter.class)
        ;
 }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
