package alessandro.backale.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record NewUtenteDTO(
        @NotEmpty(message = "Lo username è obbligatorio")
        String username,
        @NotEmpty(message = "Il nome è obbligatoria")
        String nome,
        @NotEmpty(message = "Il cognome è obbligatoria")
        String cognome,
        @NotEmpty(message = "L'email è obbligatoria")
        @Email(message = "L'email deve essere valida")
        String email,
        @NotEmpty(message = "La password è obbligatoria")
        String password,
        String avatar
) {
}
