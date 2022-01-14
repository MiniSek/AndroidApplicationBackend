package pl.edu.agh.transaction.APILayer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.transaction.Utils.Model.RegisterRequest;
import pl.edu.agh.transaction.ServiceLayer.RegisterService;

@RestController
@RequestMapping(path="api/v1/register")
public class RegisterController {
    private final RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping
    public ResponseEntity<String> registerClient(@RequestBody RegisterRequest registerRequest) {
        return registerService.register(registerRequest);
    }
}
