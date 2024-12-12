package alessandro.backale.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import alessandro.backale.entities.Utente;
import alessandro.backale.exceptions.UnauthorizedException;
import alessandro.backale.payloads.UserLoginDTO;
import alessandro.backale.tools.JWT;

@Service
public class AuthService {
    @Autowired
    private UtenteService utenteService;

    @Autowired
    private JWT jwt;

    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(UserLoginDTO body) {
        Utente found = this.utenteService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            // 2. Se sono OK --> Genero il token
            String accessToken = jwt.createToken(found);
            // 3. Ritorno il token
            return accessToken;
        } else {

            throw new UnauthorizedException("Credenziali errate!");
        }
    }
}
