package pl.edu.agh.transaction.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import pl.edu.agh.transaction.security.filters.JwtTokenVerifierFilter;
import pl.edu.agh.transaction.security.userDetailsService.UserDetailsServiceDecorated;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceDecorated userDetailsServiceDecorated;

    @Autowired
    public SecurityConfiguration(UserDetailsServiceDecorated userDetailsServiceDecorated, PasswordEncoder passwordEncoder) {
        this.userDetailsServiceDecorated = userDetailsServiceDecorated;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceDecorated);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/v1/register", "/api/v1/login").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterAfter(new JwtTokenVerifierFilter(userDetailsServiceDecorated),
                SecurityContextHolderAwareRequestFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}