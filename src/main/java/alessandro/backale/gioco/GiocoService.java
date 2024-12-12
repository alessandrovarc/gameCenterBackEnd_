package alessandro.backale.gioco;

import alessandro.backale.entities.Utente;
import alessandro.backale.exceptions.BadRequestException;
import alessandro.backale.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiocoService {

    @Autowired
    GiocoRepository giocoRepository;

    public Page<Gioco> findAll(int page, int size, String sortBy) {
        try {
            if (size > 100) size = 100;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            return this.giocoRepository.findAll(pageable);
        } catch (Exception e) {
            throw new BadRequestException("Errore durante il recupero del gioco: " + e.getMessage());
        }
    }

    public Gioco findById(long id) {
        try {
            return this.giocoRepository.findById(id).orElseThrow(() -> new NotFoundException("Gioco con id " + id + " non trovato"));
        } catch (NotFoundException e) {
            throw e; // Rilancia l'eccezione NotFoundException
        } catch (Exception e) {
            throw new BadRequestException("Errore durante il recupero del gioco: " + e.getMessage());
        }
    }

    //POST --------------------------------------------
    public Gioco save(GiocoDTO body) {
        try {
            Gioco newGioco = new Gioco(body.nome(), body.genere(), body.piattaforma(), body.descrizione(), body.urlImmagine());
            return giocoRepository.save(newGioco);

        } catch (Exception e) {
            throw new BadRequestException("Errore durante il salvataggio del gioco: " + e.getMessage());
        }
    }


    //PUT --------------------------------------------
    public Gioco findByIdAndUpdate(long id, GiocoDTO body) {
        Gioco found = findById(id);

        found.setNome(body.nome());
        found.setDescrizione(body.descrizione());
        found.setGenere(body.genere());
        found.setPiattaforma(body.piattaforma());
        found.setUrlImmagine(body.urlImmagine());
        return this.giocoRepository.save(found);
    }

    //DELETE --------------------------------------------
    public void findByIdAndDelete(long id) {
        try {
            Gioco found = this.findById(id);

            this.giocoRepository.delete(found);
        } catch (BadRequestException e) {
            throw e; // Rilancia l'eccezione BadRequestException
        } catch (Exception e) {
            throw new BadRequestException("Errore durante l'eliminazione del gioco: " + e.getMessage());
        }
    }

    //devo aggiungere la ricerca
    // Metodo per paginare la ricerca dei giochi per nome
    public Page<Gioco> findByNome(String nome, int page, int size, String sortBy) {
        try {
            if (size > 100) size = 100; // Limita la dimensione massima della pagina a 100
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            return giocoRepository.findByNomeStartingWithIgnoreCase(nome, pageable);
        } catch (Exception e) {
            throw new BadRequestException("Errore durante il recupero dei giochi: " + e.getMessage());
        }
    }

    //anche vedere i giochi attraverso la categoria
    public Page<Gioco> findByCategoria(String categoria, int page, int size, String sortBy) {
        try {
            if (size > 100) size = 100; // Limita la dimensione massima della pagina a 100
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            return giocoRepository.findByGenereContainingIgnoreCase(categoria, pageable);
        } catch (Exception e) {
            throw new BadRequestException("Errore durante il recupero dei giochi per categoria: " + e.getMessage());
        }
    }

}
