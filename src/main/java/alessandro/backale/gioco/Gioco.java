package alessandro.backale.gioco;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "giochi")
public class Gioco {
    @Id
    @GeneratedValue
    private long id;
    private String nome;
    private String genere;
    private String piattaforma;
    private String descrizione;
    private String urlImmagine;

    public Gioco() {}

    public Gioco(String nome, String genere, String piattaforma, String descrizione, String urlImmagine) {
        this.nome = nome;
        this.genere = genere;
        this.piattaforma = piattaforma;
        this.descrizione = descrizione;
        this.urlImmagine = urlImmagine;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getPiattaforma() {
        return piattaforma;
    }

    public void setPiattaforma(String piattaforma) {
        this.piattaforma = piattaforma;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getUrlImmagine() {
        return urlImmagine;
    }

    public void setUrlImmagine(String urlImmagine) {
        this.urlImmagine = urlImmagine;
    }

    @Override
    public String toString() {
        return "Gioco{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", genere='" + genere + '\'' +
                ", piattaforma='" + piattaforma + '\'' +
                ", descrizione='" + descrizione + '\'' +
                '}';
    }
}
