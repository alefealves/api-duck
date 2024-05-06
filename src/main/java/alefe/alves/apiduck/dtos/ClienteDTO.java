package alefe.alves.apiduck.dtos;

import alefe.alves.apiduck.enums.TipoCliente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClienteDTO {
    private String nome;
    private String tipo;
    private TipoCliente tipoCliente;
}
