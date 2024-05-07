package alefe.alves.apiduck.repositories;

import alefe.alves.apiduck.models.pato.Pato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatoRepository extends JpaRepository<Pato, Long> {
    Optional<Pato> findPatoById(Long id);
    Optional<Pato> findPatoByMae(Pato mae);
    Optional<List<Pato>> findPatoFilhosByMae(Pato mae);
}
