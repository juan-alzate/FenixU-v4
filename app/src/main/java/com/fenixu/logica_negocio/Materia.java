package com.fenixu.logica_negocio;

public class Materia {

    private int id;
    private String titulo;
    private double promedio;
    private int creditos;
    private int idSemestre;

    public Materia() {
        this.id = -1;
        this.titulo = "MATERIA DEFAULT";
        this.promedio = 0.0;
        this.creditos = 0;
        this.idSemestre = -1;
    }

    public Materia(int id, String titulo, double promedio, int creditos, int idSemestre) {
        this.id = id;
        this.titulo = titulo;
        this.promedio = promedio;
        this.creditos = creditos;
        this.idSemestre = idSemestre;
    }

    @Override
    public String toString() {
        return "Materia{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", promedio=" + promedio +
                ", creditos=" + creditos +
                ", idSemestre=" + idSemestre +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public int getIdSemestre() {
        return idSemestre;
    }

    public void setIdSemestre(int idSemestre) {
        this.idSemestre = idSemestre;
    }
}
