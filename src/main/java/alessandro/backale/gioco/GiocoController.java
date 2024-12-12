package alessandro.backale.gioco;

import alessandro.backale.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/gioco")
public class GiocoController {

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }


    @Autowired
    GiocoService giocoService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Page<Gioco> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        try {
            return this.giocoService.findAll(page, size, sortBy);
        } catch (Exception e) {
            throw new BadRequestException("Errore durante il recupero del corso: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Gioco getArgomentoById(@PathVariable long id) {
        return giocoService.findById(id);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Gioco save(@RequestBody @Validated GiocoDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        try {
            return this.giocoService.save(body);
        } catch (Exception e) {
            throw new BadRequestException("Errore durante il salvataggio del corso: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")  // Il permesso per aggiornare il corso
    @ResponseStatus(HttpStatus.OK)
    public Gioco updateCorso(@PathVariable long id,
                             @RequestBody @Validated GiocoDTO body,
                             BindingResult validationResult) {
        // Verifica se ci sono errori di validazione nel body della richiesta
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        try {
            return this.giocoService.findByIdAndUpdate(id, body);
        } catch (Exception e) {
            throw new BadRequestException("Errore durante l'aggiornamento del corso: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long id) {
        try {
            this.giocoService.findByIdAndDelete(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Corso con id " + id + " non trovata.");
        } catch (Exception e) {
            throw new BadRequestException("Errore durante l'eliminazione della fattura: " + e.getMessage());
        }
    }

    @GetMapping("/search/{nome}")
    public Page<Gioco> searchByNome(
            @PathVariable String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return giocoService.findByNome(nome, page, size, sortBy);
    }


    @GetMapping("/search/genere/{categoria}")
    public Page<Gioco> searchByCategoria(
            @PathVariable String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return giocoService.findByCategoria(categoria, page, size, sortBy);
    }

}
