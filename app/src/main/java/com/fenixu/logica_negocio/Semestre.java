package com.fenixu.logica_negocio;

public class Semestre {

    private int id;
    private String titulo;
    private double promedio;
    private int creditos;

    public Semestre(){
        this.id = -1;
        this.titulo = "Semestre Default";
        this.promedio = 0.0;
        this.creditos = -1;
    }

    public Semestre(int id, String titulo, double promedio, int creditos) {
        this.id = id;
        this.titulo = titulo;
        this.promedio = promedio;
        this.creditos = creditos;
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

    @Override
    public String toString() {
        return "Semestre{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", promedio=" + promedio +
                ", creditos=" + creditos +
                '}';
    }

    public boolean isDeafault(){
        if(this.id == -1 &&
        this.titulo.equals("Semestre Default") &&
        this.promedio == 0.0 &&
        this.creditos == -1) return true;
        return false;
    }
}
