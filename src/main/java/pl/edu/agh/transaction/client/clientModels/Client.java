package pl.edu.agh.transaction.client.clientModels;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Data
@Document
public class Client {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<GrantedAuthority> roles;
    private DateTime subscriptionStartDate;
    private DateTime subscriptionEndDate;
    private String jwtToken;

    public Client(String firstName, String lastName, String email, String password, Set<GrantedAuthority> roles,
                  DateTime subscriptionStartDate, DateTime subscriptionEndDate, String jwtToken) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.subscriptionStartDate = subscriptionStartDate;
        this.subscriptionEndDate = subscriptionEndDate;
        this.jwtToken = jwtToken;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", subscriptionStartDate=" + subscriptionStartDate +
                ", subscriptionEndDate=" + subscriptionEndDate +
                ", jwtToken='" + jwtToken + '\'' +
                '}';
    }
}
