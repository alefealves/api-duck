package alefe.alves.apiduck.models.venda;

import alefe.alves.apiduck.dtos.VendaDTO;
import alefe.alves.apiduck.enums.TipoCliente;
import alefe.alves.apiduck.models.cliente.Cliente;
import alefe.alves.apiduck.models.pato.Pato;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "venda")
@Table(name = "venda")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = "id")
@JsonIdentityInfo(
generator = ObjectIdGenerators.PropertyGenerator.class,
property = "id")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "venda")
    @JsonManagedReference
    private List<Pato> patos = new ArrayList<>();

    @NotNull
    private BigDecimal valor;

    @Column(nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private TipoCliente tipo_cliente;

    private LocalDateTime data;

    @NotNull
    private Boolean ativo;

    public Venda(VendaDTO dto) {
        this.patos = dto.getPatos();
        this.cliente = dto.getCliente();
        this.valor = dto.getValor();
        this.tipo_cliente = dto.getTipo_cliente();
        this.ativo = true;
    }
}
