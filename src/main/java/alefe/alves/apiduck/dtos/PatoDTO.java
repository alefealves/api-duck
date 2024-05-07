package alefe.alves.apiduck.dtos;

import alefe.alves.apiduck.enums.StatusPato;
import alefe.alves.apiduck.enums.TipoPato;
import alefe.alves.apiduck.models.pato.Pato;
import alefe.alves.apiduck.models.venda.Venda;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatoDTO {
    private Long id;

    @NotNull
    private String nome;
    @NotNull
    private TipoPato tipo;
    @NotNull
    private BigDecimal valor;

    private StatusPato status;
    private Long mae_id;
    private Pato mae;
    private Long venda_id;
    private Venda venda;
}
