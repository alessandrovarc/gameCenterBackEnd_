package alessandro.backale.recensione;

import alessandro.backale.entities.Utente;
import alessandro.backale.gioco.Gioco;
import jakarta.persistence.*;

@Entity
@Table(name = "recensioni")
public class Recensione {
    @Id
    @GeneratedValue
    private long id;
    private int valoreRecensione;
    private String recensione;
    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;
    @ManyToOne
    @JoinColumn(name = "id_gioco")
    private Gioco gioco;

    public  Recensione() {}

    public Recensione(int valoreRecensione, String recensione, Utente utente, Gioco gioco) {
        this.valoreRecensione = valoreRecensione;
        this.recensione = recensione;
        this.utente = utente;
        this.gioco = gioco;
    }

    public long getId() {
        return id;
    }

    public int getValoreRecensione() {
        return valoreRecensione;
    }

    public void setValoreRecensione(int valoreRecensione) {
        this.valoreRecensione = valoreRecensione;
    }

    public String getRecensione() {
        return recensione;
    }

    public void setRecensione(String recensione) {
        this.recensione = recensione;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Gioco getGioco() {
        return gioco;
    }

    public void setGioco(Gioco gioco) {
        this.gioco = gioco;
    }

    @Override
    public String toString() {
        return "Recensione{" +
                "id=" + id +
                ", valoreRecensione=" + valoreRecensione +
                ", recensione='" + recensione + '\'' +
                ", utente=" + utente +
                ", gioco=" + gioco +
                '}';
    }
}
