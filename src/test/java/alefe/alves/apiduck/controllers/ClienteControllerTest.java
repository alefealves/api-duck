package alefe.alves.apiduck.controllers;

import alefe.alves.apiduck.dtos.ClienteDTO;
import alefe.alves.apiduck.dtos.ClienteUpdateDTO;
import alefe.alves.apiduck.enums.TipoCliente;
import alefe.alves.apiduck.exceptions.ClienteNotFoundException;
import alefe.alves.apiduck.models.cliente.Cliente;
import alefe.alves.apiduck.services.ClienteService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class ClienteControllerTest {

    public static Integer INDEX = 0;
    public static Long ID = 1L;
    public static String NOME = "Joao";
    public static TipoCliente TIPO = TipoCliente.COM_DESCONTO;

    private Cliente cliente = new Cliente();
    private ClienteDTO clienteDTO = new ClienteDTO();
    private ClienteUpdateDTO clienteUpdateDTO = new ClienteUpdateDTO();
    ResponseEntity<ClienteDTO> response;

    @InjectMocks
    private ClienteController controller;

    @Mock
    private ClienteService service;

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
    void getClienteById() throws Exception {
        when(service.findClienteById(ID)).thenReturn(clienteDTO);
        when(mapper.map(any(), any())).thenReturn(clienteDTO);

        ResponseEntity<ClienteDTO> response = controller.getClienteById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ClienteDTO.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getId());
        assertEquals(NOME, response.getBody().getNome());
        assertEquals(TIPO, response.getBody().getTipo());
        assertEquals(true, response.getBody().getAtivo());
    }

    @Test
    @DisplayName("Não deve retornar um cliente com ID inexistnte")
    void getClienteByIdInexistente() throws Exception {
        Long clienteId = 99L;
        when(service.findClienteById(clienteId)).thenThrow(
                new ClienteNotFoundException("Cliente de id "+clienteId+" não foi encontrado."));
        Throwable e = Assertions.catchThrowable(() -> controller.getClienteById(clienteId));
        assertThat(e).isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("Cliente de id "+clienteId+" não foi encontrado.");
    }

    @Test
    @DisplayName("Deve retornar uma lista de clientes")
    void getAllClientes() throws Exception {
        when(service.getAllClientes()).thenReturn(List.of(clienteDTO));
        when(mapper.map(any(), any())).thenReturn(clienteDTO);

        ResponseEntity<List<ClienteDTO>> response = controller.getAllClientes();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ClienteDTO.class, response.getBody().get(INDEX).getClass());

        assertEquals(ID, response.getBody().get(INDEX).getId());
        assertEquals(NOME, response.getBody().get(INDEX).getNome());
        assertEquals(TIPO, response.getBody().get(INDEX).getTipo());
        assertEquals(true, response.getBody().get(INDEX).getAtivo());
    }

    @Test
    @DisplayName("Não deve retornar um clientes com base vazia ")
    void getAllClientesReturnNull() throws Exception {
        when(service.getAllClientes()).thenThrow(
                new ClienteNotFoundException("Nenhum cliente foi encontrado."));
        Throwable e = Assertions.catchThrowable(() -> controller.getAllClientes());
        assertThat(e).isInstanceOf(Exception.class)
                .hasMessageContaining("Nenhum cliente foi encontrado.");
    }

    @Test
    @DisplayName("Deve atualizar um cliente por ID")
    void createCliente() throws Exception {
        when(service.createCliente(any())).thenReturn(clienteDTO);
        when(mapper.map(any(), any())).thenReturn(clienteDTO);

        ResponseEntity<ClienteDTO> response = controller.createCliente(clienteDTO);

        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve atualizar um cliente por ID")
    void updateCliente() throws Exception {
        when(service.updateCliente(clienteUpdateDTO, ID)).thenReturn(clienteUpdateDTO);
        when(mapper.map(any(), any())).thenReturn(clienteUpdateDTO);

        ResponseEntity<ClienteUpdateDTO> response = controller.updateCliente(clienteUpdateDTO, ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ClienteUpdateDTO.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getId());
        assertEquals(NOME, response.getBody().getNome());
        assertEquals(TIPO, response.getBody().getTipo());
        assertEquals(true, response.getBody().getAtivo());
    }

    @Test
    @DisplayName("Não deve atualizar cliente com ID inexistente")
    void updateClienteIdInexistente() throws Exception {
        Long clienteID = 99L;
        when(service.updateCliente(clienteUpdateDTO, clienteID)).thenThrow(
                new ClienteNotFoundException("Cliente de id "+clienteID+" não foi encontrado."));
        Throwable e = Assertions.catchThrowable(() -> controller.updateCliente(clienteUpdateDTO, clienteID));
        assertThat(e).isInstanceOf(ClienteNotFoundException.class)
                     .hasMessageContaining("Cliente de id "+clienteID+" não foi encontrado.");
    }

    @Test
    @DisplayName("Deve deletar um cliente por ID")
    void deleteCliente() throws Exception {
        doNothing().when(service).deleteCliente(anyLong());

        ResponseEntity response = controller.deleteCliente(ID);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteCliente(anyLong());
    }

    @Test
    @DisplayName("Não deve deletar cliente com ID inexistente")
    void deleteClienteIdInexistente() throws Exception {
        Long clienteID = 99L;
        doNothing().when(service).deleteCliente(clienteID);
        when(controller.deleteCliente(clienteID)).thenThrow(
                new ClienteNotFoundException("Cliente de id "+clienteID+" não foi encontrado."));
        Throwable e = Assertions.catchThrowable(() -> controller.deleteCliente(clienteID));
        assertThat(e).isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("Cliente de id "+clienteID+" não foi encontrado.");
    }

    private void startCliente() {
        cliente = new Cliente(ID, NOME, TIPO,Boolean.TRUE);
        clienteDTO = new ClienteDTO(ID, NOME, TIPO,Boolean.TRUE);
        clienteUpdateDTO = new ClienteUpdateDTO(ID, NOME, TIPO,Boolean.TRUE);
        response = new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    }
}