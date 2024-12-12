package alessandro.backale.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import alessandro.backale.entities.Utente;
import alessandro.backale.exceptions.BadRequestException;
import alessandro.backale.payloads.EditUtenteDTO;
import alessandro.backale.payloads.NewUtenteDTO;
import alessandro.backale.services.FileUploadService;
import alessandro.backale.services.UtenteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/utente")
public class UtenteController {

    @Autowired
    UtenteService utenteService;

    @Autowired
    FileUploadService fileUploadService;

    //questo dovrebbe essere visto solamente dall'admin, ma non lo ho aggiunto perché non è richiesto
    @GetMapping
    public List<Utente> findAll() {
        return this.utenteService.findAll();
    }

    // /Me endpoints----------------------------------------------------------------
    @GetMapping("/me")
    public Utente getProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public Utente updateProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser, @RequestBody @Validated EditUtenteDTO body) {
        return this.utenteService.findByIdAndUpdate(currentAuthenticatedUser.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        this.utenteService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }

    @PatchMapping("/me/avatar")
    public String uploadAvatar(@AuthenticationPrincipal Utente currentAuthenticatedUser,
                               @RequestParam("avatar") MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il file è vuoto");
        }

        try {
            String avatarUrl = utenteService.uploadAvatar(file, currentAuthenticatedUser.getId());
            currentAuthenticatedUser.setAvatar(avatarUrl);
            utenteService.save(currentAuthenticatedUser);
            return avatarUrl;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore durante l'upload dell'avatar: " + e.getMessage(), e);
        }
    }

    // il resto dei metodi----------------------------------------------------------------
    //questi sono tutti metodi da parte del admin
    //questi in realtà non servono, ma li ho messi per testare
    @GetMapping("/{id}")
    public Utente findById(@PathVariable long id) {
        return this.utenteService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utente save(@RequestBody @Validated NewUtenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.utenteService.save(body);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Utente findByIdAndUpdate(@PathVariable long id, @RequestBody @Validated EditUtenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        // Ovunque ci sia un body bisognerebbe validarlo!
        return this.utenteService.findByIdAndUpdate(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long id) {
        this.utenteService.findByIdAndDelete(id);
    }
}
