package pl.edu.agh.transaction.security.userDetailsService;

public interface UserDetailsServiceFilter {
    boolean isClientLogged(String clientEmail);
}
