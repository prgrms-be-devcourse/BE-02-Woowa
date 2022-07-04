package com.example.woowa.security.configuration;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.example.woowa.security.filter.CustomerAuthenticationFilter;
import com.example.woowa.security.filter.CustomerAuthorizationFilter;
import com.example.woowa.security.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomerAuthenticationFilter customerAuthenticationFilter = new CustomerAuthenticationFilter(
            authenticationManagerBean());

        customerAuthenticationFilter.setFilterProcessesUrl("/baemin/v1/login");
        http.
            csrf().disable().
            sessionManagement().sessionCreationPolicy(STATELESS).
            and().
            authorizeRequests().antMatchers(HttpMethod.GET, "/baemin/v1/login/**").permitAll().
            and().
            authorizeRequests().antMatchers(HttpMethod.POST, "/baemin/v1/owners").permitAll().
            and().
            authorizeRequests().antMatchers("/baemin/v1/owners/**").hasAnyAuthority(UserRole.ROLE_OWNER.toString()).
            and().
            authorizeRequests().anyRequest().authenticated().
            and().
            addFilter(customerAuthenticationFilter);

        http.addFilterBefore(new CustomerAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }

}
