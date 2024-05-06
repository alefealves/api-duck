package alefe.alves.apiduck.dtos;

import alefe.alves.apiduck.enums.TipoCliente;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {

    @NotNull
    private String nome;

    @NotNull
    private TipoCliente tipo;

    private Boolean ativo;
    //private TipoCliente tipoCliente;
}
