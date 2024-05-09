package alefe.alves.apiduck.services;

import alefe.alves.apiduck.dtos.ClienteDTO;
import alefe.alves.apiduck.dtos.ClienteUpdateDTO;
import alefe.alves.apiduck.enums.TipoCliente;
import alefe.alves.apiduck.exceptions.ClienteNotFoundException;
import alefe.alves.apiduck.interfaces.ClienteInterface;
import alefe.alves.apiduck.models.cliente.Cliente;
import alefe.alves.apiduck.repositories.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService implements ClienteInterface {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ClienteDTO findClienteById(Long id) throws Exception {
        Optional<Cliente> optCliente = this.repository.findClienteById(id);
        if(optCliente.isPresent()){
            Cliente cliente = optCliente.get();
            return modelMapper.map(cliente, ClienteDTO.class);
        } else {
            throw new ClienteNotFoundException("Cliente de id "+id+" não foi encontrado.");
        }
    }

    @Override
    public List<ClienteDTO> getAllClientes() throws Exception {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Cliente> clientes = this.repository.findAll(sort);
        if (!clientes.isEmpty()) {
            List<ClienteDTO> clientesDTO = new ArrayList<>();
            for (Cliente cliente : clientes) {
                clientesDTO.add(modelMapper.map(cliente, ClienteDTO.class));
            }
            return clientesDTO;
        } else {
            throw new ClienteNotFoundException("Nenhum cliente foi encontrado.");
        }
    }

    @Override
    public ClienteDTO createCliente(ClienteDTO dto) throws Exception {
        if ((dto.getTipo() != TipoCliente.COM_DESCONTO) && (dto.getTipo() != TipoCliente.SEM_DESCONTO)){
            throw new Exception("Tipo informado inválido, tipo aceito COM_DESCONTO SEM_DESCONTO");
        }

        Cliente newCliente = new Cliente(dto);
        saveCliente(newCliente);
        return modelMapper.map(newCliente, ClienteDTO.class);
    }

    @Override
    public ClienteUpdateDTO updateCliente(ClienteUpdateDTO dto, Long id) throws Exception {
        Optional<Cliente> optCliente = this.repository.findClienteById(id);
        if(optCliente.isPresent()){
            Cliente cliente = optCliente.get();
            if (dto.getNome() != null)
                cliente.setNome(dto.getNome());
            if (dto.getTipo() != null) {
                if ((dto.getTipo() != TipoCliente.COM_DESCONTO) && (dto.getTipo() != TipoCliente.SEM_DESCONTO)){
                    throw new Exception("Tipo informado inválido, tipo aceito COM_DESCONTO SEM_DESCONTO");
                }
                cliente.setTipo(dto.getTipo());
            }
            if (dto.getAtivo() != null) {
                cliente.setAtivo(dto.getAtivo());
            }
            return modelMapper.map(cliente, ClienteUpdateDTO.class);
        } else {
            throw new ClienteNotFoundException("Cliente de id "+id+" não foi encontrado.");
        }
    }

    @Override
    public void deleteCliente(Long id) {
        Optional<Cliente> optCliente = this.repository.findClienteById(id);
        if(optCliente.isPresent()){
            this.repository.deleteById(id);
        } else {
            throw new ClienteNotFoundException("Cliente de id "+id+" não foi encontrado.");
        }
    }

    @Override
    public void saveCliente(Cliente cliente) throws Exception{
        try {
            this.repository.save(cliente);
        }catch (Exception e){
            throw new Exception("Erro ao salvar o Cliente.");
        }
    }
}
