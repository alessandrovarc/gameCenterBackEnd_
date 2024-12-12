package alessandro.backale.gioco;

import jakarta.validation.constraints.NotEmpty;

public record GiocoDTO(
        @NotEmpty(message = "Inserisi il nome del gioco")
        String nome,
        @NotEmpty(message = "Inserisi il genere")
        String genere,
        @NotEmpty(message = "Inserisi la piattaforma")
        String piattaforma,
        @NotEmpty(message = "Inserisi la descrizione del gioco")
        String descrizione,
        @NotEmpty(message = "Inserisi la descrizione del gioco")
        String urlImmagine
) {
}
