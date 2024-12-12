package alessandro.backale.recensione;

import alessandro.backale.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecensioneRepository extends JpaRepository<Recensione, Long> {
    List<Recensione> findByUtente(Utente utente);
}
