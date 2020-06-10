package br.com.roberto.sistemaponto.dto;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class BatidaPontoDto {
    private long id;
    private long idUsuario;
    private String dataBatida;
    private boolean entrada;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDataBatida(String dataBatida) {
        this.dataBatida = dataBatida;
    }

    public String getDataBatida() {
        return dataBatida;
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

    public void setConvertedDate(DateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        this.dataBatida = dateTime.toString(formatter);
    }

    public DateTime getConvertedDate(String date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        return formatter.parseDateTime(date);
    }
}
