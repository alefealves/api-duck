package alefe.alves.apiduck.repositories;

import alefe.alves.apiduck.models.venda.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    Optional<Venda> findVendaById(Long id);
}
