package alefe.alves.apiduck.dtos;

import alefe.alves.apiduck.enums.TipoCliente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteUpdateDTO {
    private Long id;
    private String nome;
    private TipoCliente tipo;
    private Boolean ativo;
}
