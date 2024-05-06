package alefe.alves.apiduck.controllers;

import alefe.alves.apiduck.dtos.ClienteDTO;
import alefe.alves.apiduck.models.cliente.Cliente;
import alefe.alves.apiduck.services.ClienteService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAllClientes() throws Exception {
        List<ClienteDTO> clientes = this.clienteService.getAllClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Cliente> createCliente(@Valid @RequestBody ClienteDTO dto) throws Exception {
        Cliente newCliente = clienteService.createCliente(dto);
        return new ResponseEntity<>(newCliente, HttpStatus.CREATED);
    }

}
