package com.fenixu.logica_negocio;

public class Nota {

    private int idNota;
    private double nota;
    private double porcentaje;
    private int idMateria;

    public Nota() {
        this.idNota = -1;
        this.nota = 0.0;
        this.porcentaje = 0.0;
        this.idMateria = -1;
    }

    public Nota(int idNota, double nota, double porcentaje, int idMateria) {
        this.idNota = idNota;
        this.nota = nota;
        this.porcentaje = porcentaje;
        this.idMateria = idMateria;
    }

    public int getIdNota() {
        return idNota;
    }

    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "idNota=" + idNota +
                ", nota=" + nota +
                ", porcentaje=" + porcentaje +
                ", idMateria=" + idMateria +
                '}';
    }
}
