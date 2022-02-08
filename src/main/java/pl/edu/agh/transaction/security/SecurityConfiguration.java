package pl.edu.agh.transaction.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import pl.edu.agh.transaction.security.userDetailsService.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final String JWT_KEY;
    private final String JWT_AUTH_HEADER_PREFIX;

    @Autowired
    public SecurityConfiguration(UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder passwordEncoder,
                                 @Value("${JWT_KEY}") String JWT_KEY,
                                 @Value("${JWT_AUTH_HEADER_PREFIX}") String JWT_AUTH_HEADER_PREFIX) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.passwordEncoder = passwordEncoder;

        this.JWT_KEY = JWT_KEY;
        this.JWT_AUTH_HEADER_PREFIX = JWT_AUTH_HEADER_PREFIX;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/v1/register", "/api/v1/login").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterAfter(new JwtTokenVerifierFilter(userDetailsServiceImpl, JWT_KEY, JWT_AUTH_HEADER_PREFIX),
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
