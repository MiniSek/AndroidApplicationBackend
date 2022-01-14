package pl.edu.agh.transaction.APILayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.transaction.Utils.Model.Client;
import pl.edu.agh.transaction.ServiceLayer.ClientService;

@RestController
@RequestMapping(path="api/v1/client")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<Client> getClient() {
        return clientService.getClient();
    }
}