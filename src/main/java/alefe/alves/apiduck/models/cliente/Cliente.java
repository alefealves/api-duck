package alefe.alves.apiduck.models.cliente;

import alefe.alves.apiduck.dtos.ClienteDTO;
import alefe.alves.apiduck.enums.TipoCliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity(name = "cliente")
@Table(name = "cliente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length=100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private TipoCliente tipoCliente;

    @NotNull
    private Boolean ativo;

    public Cliente(ClienteDTO dto) {
        this.nome = dto.getNome();
        this.tipoCliente = dto.getTipoCliente();
        this.ativo = true;
    }
}
