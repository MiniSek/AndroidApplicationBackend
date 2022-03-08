package pl.edu.agh.transaction.security.filters;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.edu.agh.transaction.exception.AuthenticationException;
import pl.edu.agh.transaction.exception.IllegalDatabaseState;
import pl.edu.agh.transaction.exception.ObjectNotFoundException;
import pl.edu.agh.transaction.security.userDetailsService.UserDetailsServiceFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifierFilter extends OncePerRequestFilter {
    private final UserDetailsServiceFilter userDetailsServiceFilter;

    private final String JWT_KEY;
    private final String JWT_AUTH_HEADER_PREFIX;

    public JwtTokenVerifierFilter(UserDetailsServiceFilter userDetailsServiceFilter,
                                  String JWT_KEY, String JWT_AUTH_HEADER_PREFIX) {
        this.userDetailsServiceFilter = userDetailsServiceFilter;

        this.JWT_KEY = JWT_KEY;
        this.JWT_AUTH_HEADER_PREFIX = JWT_AUTH_HEADER_PREFIX;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, AuthenticationException {
        if(!(request.getServletPath().equals("/api/v1/login") || request.getServletPath().equals("/api/v1/register"))) {
            String authorizationHeader = request.getHeader("Authorization");

            if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(JWT_AUTH_HEADER_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwtToken = null;
            try {
                jwtToken = authorizationHeader.replace(JWT_AUTH_HEADER_PREFIX, "");
                Jws<Claims> jwsClaims = Jwts.parserBuilder().
                        setSigningKey(Keys.hmacShaKeyFor(JWT_KEY.getBytes()))
                        .build()
                        .parseClaimsJws(jwtToken);

                Claims body = jwsClaims.getBody();

                String email = body.getSubject();

                var roles = (List<Map<String, String>>)body.get("authorities");

                Set<SimpleGrantedAuthority> simpleGrantedAuthorities = roles.stream()
                        .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                        .collect(Collectors.toSet());

                Authentication auth = new UsernamePasswordAuthenticationToken(email, null, simpleGrantedAuthorities);
                SecurityContextHolder.getContext().setAuthentication(auth);

                if(!userDetailsServiceFilter.isClientLogged(email, jwtToken))
                    throw new AuthenticationException(String.format("Token %s was logged out", jwtToken));
            } catch(JwtException | IllegalDatabaseState | ObjectNotFoundException | AuthenticationException e) {
                throw new IllegalStateException(String.format("Token %s cannot be trusted", jwtToken));
            }
        }

        filterChain.doFilter(request, response);
    }
}