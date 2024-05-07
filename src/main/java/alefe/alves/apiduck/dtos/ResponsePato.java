package alefe.alves.apiduck.dtos;

import alefe.alves.apiduck.enums.StatusPato;
import alefe.alves.apiduck.enums.TipoPato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePato {
    private Long id;
    private String nome;
    private TipoPato tipoPato;
    private BigDecimal valor;
    private Long mae_id;
    //private Long venda_id;
    private StatusPato status;
}
