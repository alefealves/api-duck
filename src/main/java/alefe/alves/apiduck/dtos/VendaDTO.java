package alefe.alves.apiduck.dtos;

import alefe.alves.apiduck.enums.TipoCliente;
import alefe.alves.apiduck.models.cliente.Cliente;
import alefe.alves.apiduck.models.pato.Pato;
import alefe.alves.apiduck.models.venda.LongPato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendaDTO{
    private Long clienteId;
    private Cliente cliente;
    private TipoCliente tipo_cliente;
    private BigDecimal valor;
    private LocalDateTime data;
    private List<LongPato> patosIds;
    private List<Pato> patos;
}
