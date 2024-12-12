package alessandro.backale.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import alessandro.backale.entities.Utente;
import alessandro.backale.exceptions.BadRequestException;
import alessandro.backale.exceptions.NotFoundException;
import alessandro.backale.payloads.EditUtenteDTO;
import alessandro.backale.payloads.NewUtenteDTO;
import alessandro.backale.repositories.UtenteRepository;

import java.io.IOException;
import java.util.List;

@Service
public class UtenteService {
    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private Cloudinary cloudinaryUploader;

    public Utente findById(long id) {
        return this.utenteRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Utente findByEmail(String email) {
        return this.utenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con email " + email + " non è stato trovato"));
    }

    public List<Utente> findAll() {
        return this.utenteRepository.findAll();
    }

    public Utente save(NewUtenteDTO body) {
        this.utenteRepository.findByEmail(body.email()).ifPresent(
                user -> {
                    throw new BadRequestException("Email " + body.email() + " già in uso!");
                }
        );
        this.utenteRepository.findByUsername(body.username()).ifPresent(
                user -> {
                    throw new BadRequestException("Username " + body.username() + " è già in uso!");
                }
        );
        Utente newUtente = new Utente(body.username(), body.nome(), body.cognome(), body.email(), bcrypt.encode(body.password()));
        return this.utenteRepository.save(newUtente);
    }

    public Utente save(Utente utente) {
        return this.utenteRepository.save(utente);
    }


    public Utente findByIdAndUpdate(long id, EditUtenteDTO body) {
        Utente found = this.findById(id);

        if (!found.getEmail().equals(body.email())) {
            this.utenteRepository.findByEmail(body.email()).ifPresent(
                    user -> {
                        throw new BadRequestException("Email " + body.email() + " già in uso!");
                    }
            );
        }

        found.setNome(body.nome());
        found.setCognome(body.cognome());
        found.setEmail(body.email());

        return this.utenteRepository.save(found);
    }



    public void findByIdAndDelete(long id) {
        Utente found = this.findById(id);
        this.utenteRepository.delete(found);
    }

    public String uploadAvatar(MultipartFile file, long userId) {
        String url = null;
        try {
            url = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        } catch (IOException e) {
            throw new BadRequestException("Ci sono stati problemi con l'upload del file!");
        }
        return url;
    }
}
