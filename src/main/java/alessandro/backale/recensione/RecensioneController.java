package alessandro.backale.recensione;

import alessandro.backale.entities.Utente;
import alessandro.backale.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recensione")
public class RecensioneController {

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @Autowired
    RecensioneService recensioneService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Page<Recensione> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        try {
            return this.recensioneService.findAll(page, size, sortBy);
        } catch (Exception e) {
            throw new BadRequestException("Errore durante il recupero del corso: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public List<Recensione> getMyPrenotazioni(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        return recensioneService.findByUtente(currentAuthenticatedUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Recensione getArgomentoById(@PathVariable long id) {
        return recensioneService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public Recensione save(@RequestBody @Validated RecensioneDTO body, BindingResult validationResult, @AuthenticationPrincipal Utente currentAuthenticatedUser) {
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        try {
            return this.recensioneService.save(body, currentAuthenticatedUser);
        } catch (Exception e) {
            throw new BadRequestException("Errore durante il salvataggio del corso: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")  // Il permesso per aggiornare il corso
    @ResponseStatus(HttpStatus.OK)
    public Recensione updateCorso(@PathVariable long id,
                             @RequestBody @Validated RecensioneDTO body,
                             BindingResult validationResult,
                             @AuthenticationPrincipal Utente currentAuthenticatedUser) {
        // Verifica se ci sono errori di validazione nel body della richiesta
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        try {
            return this.recensioneService.findByIdAndUpdate(id, body, currentAuthenticatedUser);
        } catch (Exception e) {
            throw new BadRequestException("Errore durante l'aggiornamento del corso: " + e.getMessage());
        }
    }


    //con lo stesso id utente di chi lo ha creato
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long id, @AuthenticationPrincipal Utente currentAuthenticatedUser) {
        try {
            this.recensioneService.findByIdAndDelete(id, currentAuthenticatedUser);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Corso con id " + id + " non trovata.");
        } catch (Exception e) {
            throw new BadRequestException("Errore durante l'eliminazione della fattura: " + e.getMessage());
        }
    }




}
