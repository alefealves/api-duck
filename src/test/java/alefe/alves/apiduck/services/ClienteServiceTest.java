package alefe.alves.apiduck.services;

import alefe.alves.apiduck.dtos.ClienteDTO;
import alefe.alves.apiduck.dtos.ClienteUpdateDTO;
import alefe.alves.apiduck.enums.TipoCliente;
import alefe.alves.apiduck.exceptions.ClienteNotFoundException;
import alefe.alves.apiduck.models.cliente.Cliente;
import alefe.alves.apiduck.repositories.ClienteRepository;

import org.assertj.core.api.Assertions;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

    private Cliente cliente = new Cliente();
    private ClienteDTO clienteDTO = new ClienteDTO();
    private ClienteUpdateDTO clienteUpdateDTO = new ClienteUpdateDTO();
    private Optional<Cliente> optionalCliente;

    @InjectMocks
    private ClienteService service;
    @Mock
    private ClienteRepository repository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private RuntimeException runtimeException;
    @Mock
    private ClienteNotFoundException clienteNotFoundException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCliente();
    }

    @Test
    @DisplayName("Deve retornar um cliente por ID")
    void whenFindByIdThenReturnSuccess() throws Exception {
        when(this.repository.findClienteById(ID)).thenReturn(optionalCliente);
        when(mapper.map(any(), any())).thenReturn(clienteDTO);

        ClienteDTO response = this.service.findClienteById(ID);

        assertNotNull(response);
        assertEquals(ClienteDTO.class, response.getClass());

        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(TIPO, response.getTipo());
        assertEquals(true, response.getAtivo());
    }

    @Test
    @DisplayName("Não deve retornar um cliente inexistente")
    void whenFindByIdThenDontReturn() throws Exception {
        Long clienteId = 99L;
        when(this.repository.findClienteById(ID)).thenReturn(optionalCliente);
        Throwable e = Assertions.catchThrowable(() -> service.findClienteById(clienteId));
        assertThat(e).isInstanceOf(ClienteNotFoundException.class);
    }

    @Test
    @DisplayName("Deve retornar uma lista de clientes")
    void whenFindAllThenReturnAListOfClienteDTO() throws Exception {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(this.repository.findAll(sort)).thenReturn(List.of(cliente));
        when(mapper.map(any(), any())).thenReturn(clienteDTO);

        List<ClienteDTO> response = this.service.getAllClientes();

        assertNotNull(response);
        assertEquals(ClienteDTO.class, response.get(INDEX).getClass());
        assertEquals(ID, response.get(INDEX).getId());
        assertEquals(NOME, response.get(INDEX).getNome());
        assertEquals(TIPO, response.get(INDEX).getTipo());
        assertEquals(true, response.get(INDEX).getAtivo());
    }

    @Test
    @DisplayName("Não deve retornar uma lista clientes inexistente")
    void whenFindAllThenDontReturnAListOfClienteDTO() throws Exception {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(this.repository.findAll(sort)).thenReturn(List.of());
        Throwable e = Assertions.catchThrowable(() -> service.getAllClientes());
        assertThat(e).isInstanceOf(ClienteNotFoundException.class);
    }

    @Test
    @DisplayName("Deve criar um cliente")
    void createCliente() throws Exception {
        when(this.repository.save(any())).thenReturn(cliente);
        when(mapper.map(any(), any())).thenReturn(clienteDTO);

        ClienteDTO response = this.service.createCliente(clienteDTO);

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
        when(mapper.map(any(), any())).thenReturn(clienteUpdateDTO);

        ClienteUpdateDTO response = this.service.updateCliente(clienteUpdateDTO, ID);

        assertNotNull(response);
        assertEquals(ClienteUpdateDTO.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(TIPO, response.getTipo());
        assertEquals(true, response.getAtivo());
    }

    @Test
    @DisplayName("Não deve atualizar cliente com ID inexistente")
    void updateClienteErroIdInexistente() throws Exception {
        Long ID_ERRO = 99L;
        when(this.repository.save(cliente)).thenReturn(cliente);
        when(this.repository.findClienteById(ID)).thenReturn(optionalCliente);
        when(mapper.map(any(), any())).thenReturn(clienteUpdateDTO);

        Throwable e = Assertions.catchThrowable(() -> this.service.updateCliente(clienteUpdateDTO,ID_ERRO));
        assertThat(e).isInstanceOf(ClienteNotFoundException.class);
    }

    @Test
    @DisplayName("Deve deletar um cliente por ID")
    void deleteCliente() {
        when(this.repository.findClienteById(anyLong())).thenReturn(optionalCliente);
        doNothing().when(this.repository).deleteById(anyLong());
        this.service.deleteCliente(ID);
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Não deve deletar cliente com ID inexistente")
    void deleteClienteIdInexistente() {
        Long ID_ERRO = 99L;
        when(this.repository.save(cliente)).thenReturn(cliente);
        when(this.repository.findClienteById(ID)).thenReturn(optionalCliente);

        Throwable e = Assertions.catchThrowable(() -> this.service.deleteCliente(ID_ERRO));
        assertThat(e).isInstanceOf(ClienteNotFoundException.class);
    }

    @Test
    @DisplayName("Deve salvar um cliente")
    void saveCliente() throws Exception{
        when(this.repository.save(any())).thenReturn(cliente);
        this.service.saveCliente(cliente);
        verify(repository, times(1)).save(cliente);
    }

    private void startCliente() {
        cliente = new Cliente(ID, NOME, TIPO,Boolean.TRUE);
        clienteDTO = new ClienteDTO(ID, NOME, TIPO,Boolean.TRUE);
        clienteUpdateDTO = new ClienteUpdateDTO(ID, NOME, TIPO,Boolean.TRUE);
        optionalCliente = Optional.of(new Cliente(ID, NOME, TIPO,Boolean.TRUE));
    }
}