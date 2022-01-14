package pl.edu.agh.transaction.APILayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.transaction.Utils.Model.LoginRequest;
import pl.edu.agh.transaction.ServiceLayer.LoginService;

@RestController
@RequestMapping(path="api/v1/login")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }
}
