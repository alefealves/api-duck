package alefe.alves.apiduck.interfaces;

import alefe.alves.apiduck.dtos.ClienteDTO;
import alefe.alves.apiduck.models.cliente.Cliente;

import java.util.List;

public interface ClienteInterface {
    public ClienteDTO findClienteById(Long id) throws Exception;
    public List<ClienteDTO> getAllClientes() throws Exception;
    public Cliente createCliente(ClienteDTO dto) throws Exception;
    public Cliente updateCliente(ClienteDTO dto, Long id) throws Exception;
    public void deleteCliente (Long id) throws Exception;
}
