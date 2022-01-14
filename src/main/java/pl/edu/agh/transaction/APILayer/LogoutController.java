package pl.edu.agh.transaction.APILayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.transaction.ServiceLayer.LogoutService;


@RestController
@RequestMapping(path="api/v1/logout")
public class LogoutController {
    private final LogoutService logoutService;

    @Autowired
    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @PutMapping
    public ResponseEntity<String> logout() {
        return logoutService.logout();
    }
}
