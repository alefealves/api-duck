package alefe.alves.apiduck.services;

import alefe.alves.apiduck.dtos.ClienteDTO;
import alefe.alves.apiduck.dtos.ExceptionDTO;
import alefe.alves.apiduck.enums.TipoCliente;
import alefe.alves.apiduck.exceptions.ClienteNotFoundException;
import alefe.alves.apiduck.interfaces.ClienteInterface;
import alefe.alves.apiduck.models.cliente.Cliente;
import alefe.alves.apiduck.repositories.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<ClienteDTO> getAllClientes() throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Cliente> clientes = this.repository.findAll(sort);
        if (!clientes.isEmpty()) {
            List<ClienteDTO> clientesDTO = clientes.stream()
                    .map(entity -> modelMapper.map(clientes, ClienteDTO.class))
                    .collect(Collectors.toList());
            return clientesDTO;
        } else {
            throw new ClienteNotFoundException("Nenhum cliente foi encontrado.");
        }
    }

    @Override
    public Cliente createCliente(ClienteDTO dto) throws Exception {
        if ((dto.getTipo() != TipoCliente.COM_DESCONTO) && (dto.getTipo() != TipoCliente.SEM_DESCONTO)){
            throw new Exception("Tipo informado inválido, tipo aceito COM_DESCONTO SEM_DESCONTO");
        }

        Cliente newCliente = new Cliente(dto);
        this.repository.save(newCliente);
        return newCliente;
    }

    @Override
    public Cliente updateCliente(ClienteDTO dto, Long id) {
        return null;
    }

    @Override
    public void deleteCliente(Long id) {

    }
}
