package alessandro.backale.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record EditUtenteDTO (
        @NotEmpty(message = "Il nome è obbligatoria")
        String nome,
        @NotEmpty(message = "Il cognome è obbligatoria")
        String cognome,
        @NotEmpty(message = "L'email è obbligatoria")
        @Email(message = "L'email deve essere valida")
        String email
){

}
