package alefe.alves.apiduck.services;

import alefe.alves.apiduck.dtos.ClienteDTO;
import alefe.alves.apiduck.dtos.ClienteUpdateDTO;
import alefe.alves.apiduck.enums.TipoCliente;
import alefe.alves.apiduck.models.cliente.Cliente;
import alefe.alves.apiduck.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class ClienteServiceTest {

    public static Integer INDEX = 0;
    public static Long ID = 1L;
    public static String NOME = "Joao";
    public static TipoCliente TIPO = TipoCliente.COM_DESCONTO;

    @InjectMocks
    private ClienteService clienteService;
    @Mock
    private ClienteRepository repository;
    @Mock
    private ModelMapper modelMapper;

    private Cliente cliente = new Cliente();
    private ClienteDTO clienteDTO = new ClienteDTO();
    private ClienteUpdateDTO clienteUpdateDTO = new ClienteUpdateDTO();
    private Optional<Cliente> optionalCliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCliente();
    }

    @Test
    @DisplayName("Deve retornar um cliente por ID")
    void whenFindByIdThenReturnSuccess() throws Exception {
        when(this.repository.findClienteById(anyLong())).thenReturn(optionalCliente);
        when(modelMapper.map(any(), any())).thenReturn(clienteDTO);

        ClienteDTO response = this.clienteService.findClienteById(ID);

        assertNotNull(response);
        assertEquals(ClienteDTO.class, response.getClass());

        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(TIPO, response.getTipo());
        assertEquals(true, response.getAtivo());
    }

    @Test
    @DisplayName("Deve retornar uma lista de clientes")
    void whenFindAllThenReturnAListOfClienteDTO() throws Exception {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(this.repository.findAll(sort)).thenReturn(List.of(cliente));
        when(modelMapper.map(any(), any())).thenReturn(clienteDTO);

        List<ClienteDTO> response = this.clienteService.getAllClientes();

        assertNotNull(response);
        assertEquals(ClienteDTO.class, response.get(INDEX).getClass());
        assertEquals(ID, response.get(INDEX).getId());
        assertEquals(NOME, response.get(INDEX).getNome());
        assertEquals(TIPO, response.get(INDEX).getTipo());
        assertEquals(true, response.get(INDEX).getAtivo());
    }

    @Test
    @DisplayName("Deve criar um cliente")
    void createCliente() throws Exception {
        when(this.repository.save(any())).thenReturn(cliente);
        when(modelMapper.map(any(), any())).thenReturn(clienteDTO);

        ClienteDTO response = this.clienteService.createCliente(clienteDTO);

        assertNotNull(response);
        assertEquals(ClienteDTO.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(TIPO, response.getTipo());
        assertEquals(true, response.getAtivo());
    }

    @Test
    @DisplayName("Deve atualizar um cliente por ID")
    void updateCliente() throws Exception {
        when(this.repository.save(any())).thenReturn(cliente);
        when(this.repository.findClienteById(anyLong())).thenReturn(optionalCliente);
        when(modelMapper.map(any(), any())).thenReturn(clienteUpdateDTO);

        ClienteUpdateDTO response = this.clienteService.updateCliente(clienteUpdateDTO, ID);

        assertNotNull(response);
        assertEquals(ClienteUpdateDTO.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(TIPO, response.getTipo());
        assertEquals(true, response.getAtivo());
    }

    @Test
    @DisplayName("Deve deletar um cliente por ID")
    void deleteCliente() {
        when(this.repository.findClienteById(anyLong())).thenReturn(optionalCliente);
        doNothing().when(this.repository).deleteById(anyLong());
        this.clienteService.deleteCliente(ID);
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve salvar um cliente")
    void saveCliente() throws Exception{
        when(this.repository.save(any())).thenReturn(cliente);
        this.clienteService.saveCliente(cliente);
        verify(repository, times(1)).save(cliente);
    }

    private void startCliente() {
        cliente = new Cliente(ID, NOME, TIPO,Boolean.TRUE);
        clienteDTO = new ClienteDTO(ID, NOME, TIPO,Boolean.TRUE);
        clienteUpdateDTO = new ClienteUpdateDTO(ID, NOME, TIPO,Boolean.TRUE);
        optionalCliente = Optional.of(new Cliente(ID, NOME, TIPO,Boolean.TRUE));
    }
}