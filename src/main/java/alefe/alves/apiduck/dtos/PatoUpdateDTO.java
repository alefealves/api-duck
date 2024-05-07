package alefe.alves.apiduck.dtos;

import alefe.alves.apiduck.enums.StatusPato;
import alefe.alves.apiduck.enums.TipoPato;
import alefe.alves.apiduck.models.pato.Pato;
import alefe.alves.apiduck.models.venda.Venda;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatoUpdateDTO {
    private Long id;
    private String nome;
    private TipoPato tipo;
    private BigDecimal valor;
    private StatusPato status;
    private Long mae_id;
    private Pato mae;
    private Long venda_id;
    private Venda venda;
}
