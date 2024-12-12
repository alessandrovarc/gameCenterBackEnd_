package alessandro.backale.gioco;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiocoRepository extends JpaRepository<Gioco, Long> {
    Page<Gioco> findByNomeStartingWithIgnoreCase(String nome, Pageable pageable);
    Page<Gioco> findByGenereContainingIgnoreCase(String genere, Pageable pageable);
}
