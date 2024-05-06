package alefe.alves.apiduck.services;

import alefe.alves.apiduck.dtos.ClienteDTO;
import alefe.alves.apiduck.exceptions.ClienteNotFoundException;
import alefe.alves.apiduck.interfaces.ClienteInterface;
import alefe.alves.apiduck.models.cliente.Cliente;
import alefe.alves.apiduck.repositories.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService implements ClienteInterface {

    @Autowired
    private ClienteRepository repository;

    @Override
    public ClienteDTO findClienteById(Long id) throws Exception {
        Optional<Cliente> optionalCliente = this.repository.findClienteById(id);
        if(optionalCliente.isPresent()){
            Cliente cliente = optionalCliente.get();
            ModelMapper modelMapper = new ModelMapper();
            ClienteDTO clienteDTO = modelMapper.map(cliente, ClienteDTO.class);
            return clienteDTO;
        } else {
            throw new ClienteNotFoundException("Cliente de id "+id+" não foi encontrado.");
        }
    }

    @Override
    public List<ClienteDTO> getAllClientes() {
        return List.of();
    }

    @Override
    public Cliente createCliente(ClienteDTO dto) {
        return null;
    }

    @Override
    public Cliente updateCliente(ClienteDTO dto, Long id) {
        return null;
    }

    @Override
    public void deleteCliente(Long id) {

    }
}
