package br.com.roberto.sistemaponto.persistence.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
public class BatidaPonto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private long idUsuario;

    @Column(updatable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dataBatida = new DateTime();

    @Column(nullable = false)
    private boolean entrada;

    public long getId() {
        return id;
    }

    public DateTime getDataBatida() {
        return dataBatida;
    }

    public void setDataBatida(DateTime dataBatida) {
        this.dataBatida = dataBatida;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public boolean isEntrada() {
        return entrada;
    }

    public void setEntrada(boolean entrada) {
        this.entrada = entrada;
    }
}
