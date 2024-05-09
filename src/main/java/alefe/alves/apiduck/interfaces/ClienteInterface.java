package alefe.alves.apiduck.interfaces;

import alefe.alves.apiduck.dtos.ClienteDTO;
import alefe.alves.apiduck.dtos.ClienteUpdateDTO;
import alefe.alves.apiduck.models.cliente.Cliente;

import java.util.List;

public interface ClienteInterface {
    public ClienteDTO findClienteById(Long id) throws Exception;
    public List<ClienteDTO> getAllClientes() throws Exception;
    public ClienteDTO createCliente(ClienteDTO dto) throws Exception;
    public ClienteUpdateDTO updateCliente(ClienteUpdateDTO dto, Long id) throws Exception;
    public void deleteCliente (Long id) throws Exception;
    public void saveCliente(Cliente cliente) throws Exception;
}
