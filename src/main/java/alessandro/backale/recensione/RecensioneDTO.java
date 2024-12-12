package alessandro.backale.recensione;

import jakarta.validation.constraints.NotNull;

public record RecensioneDTO(
        @NotNull(message = "il valore della recensione è obbligatorio")
        int valoreRecensione,
        String recensione,
        @NotNull(message = "L'id del gioco è obbligatorio")
        long id_gioco
) {
}
