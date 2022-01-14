package pl.edu.agh.transaction.ServiceLayer.security.userDetailsService;

public interface UserDetailsServiceFilter {
    boolean isClientLogged(String clientEmail);
}
