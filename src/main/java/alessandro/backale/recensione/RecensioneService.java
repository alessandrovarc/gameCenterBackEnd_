package alessandro.backale.recensione;

import alessandro.backale.entities.Utente;
import alessandro.backale.exceptions.BadRequestException;
import alessandro.backale.exceptions.NotFoundException;
import alessandro.backale.gioco.Gioco;
import alessandro.backale.gioco.GiocoDTO;
import alessandro.backale.gioco.GiocoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecensioneService {

    @Autowired
    RecensioneRepository recensioneRepository;

    @Autowired
    GiocoService giocoService;

    public Page<Recensione> findAll(int page, int size, String sortBy) {
        try {
            if (size > 100) size = 100;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            return this.recensioneRepository.findAll(pageable);
        } catch (Exception e) {
            throw new BadRequestException("Errore durante il recupero delle fatture: " + e.getMessage());
        }
    }

    public Recensione findById(long id) {
        try {
            return this.recensioneRepository.findById(id).orElseThrow(() -> new NotFoundException("Recensione con id " + id + " non trovato"));
        } catch (NotFoundException e) {
            throw e; // Rilancia l'eccezione NotFoundException
        } catch (Exception e) {
            throw new BadRequestException("Errore durante il recupero della fattura: " + e.getMessage());
        }
    }

    //POST --------------------------------------------
    public Recensione save(RecensioneDTO body, Utente utente) {
        try {
            Gioco gioco = giocoService.findById(body.id_gioco());

            Recensione newCorso = new Recensione(body.valoreRecensione(), body.recensione(), utente, gioco);
            return recensioneRepository.save(newCorso);

        } catch (Exception e) {
            throw new BadRequestException("Errore durante il salvataggio della recensione: " + e.getMessage());
        }
    }

    //PUT --------------------------------------------
    public Recensione findByIdAndUpdate(long id, RecensioneDTO body, Utente utente) {
        Recensione found = findById(id);
        if (found.getUtente().getId() != utente.getId()) {
            throw new BadRequestException("Non hai i permessi per modificare questa recensione");
        }
        found.setRecensione(body.recensione());
        found.setValoreRecensione(body.valoreRecensione());
        return this.recensioneRepository.save(found);
    }

    //DELETE --------------------------------------------
    public void findByIdAndDelete(long id, Utente utente) {
        try {
            Recensione found = this.findById(id);
            if (found.getUtente().getId() != utente.getId()) {
                throw new BadRequestException("Non hai i permessi per eliminare appartiene ad un altro utente");
            }
            this.recensioneRepository.delete(found);
        } catch (BadRequestException e) {
            throw e; // Rilancia l'eccezione BadRequestException
        } catch (Exception e) {
            throw new BadRequestException("Errore durante l'eliminazione della recensione: " + e.getMessage());
        }
    }


    public List<Recensione> findByUtente(Utente utente) {
        return recensioneRepository.findByUtente(utente);
    }

}
