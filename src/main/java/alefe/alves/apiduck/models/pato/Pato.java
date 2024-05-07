package alefe.alves.apiduck.models.pato;

import alefe.alves.apiduck.dtos.PatoDTO;
import alefe.alves.apiduck.enums.StatusPato;
import alefe.alves.apiduck.enums.TipoPato;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "pato")
@Table(name = "pato")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nome;

    @OneToOne
    @JoinColumn(name = "mae_id")
    private Pato mae;

    /*@OneToOne
    @JoinColumn(name = "venda_id")
    private Venda venda;*/

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private TipoPato tipo;

    @NotNull
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private StatusPato status;

    public Pato(PatoDTO dto) {
        this.nome = dto.getNome();
        this.mae = dto.getMae();
        this.tipo = dto.getTipo();
        this.valor = dto.getValor();
        this.status = StatusPato.DISPONIVEL;
    }
}
