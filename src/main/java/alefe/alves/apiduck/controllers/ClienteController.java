package alefe.alves.apiduck.controllers;

import alefe.alves.apiduck.dtos.ClienteDTO;
import alefe.alves.apiduck.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) throws Exception {
        ClienteDTO clienteDTO = this.clienteService.findClienteById(id);
        return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    }

}
