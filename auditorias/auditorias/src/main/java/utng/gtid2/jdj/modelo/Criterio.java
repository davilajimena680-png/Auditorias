package utng.gtid2.jdj.modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Criterio {

    private int idCriterio;
    private int idCategoria;
    private String descripcion;
    private double peso;
    private String nombreCategoria;

    private final StringProperty cumplimientoSeleccionado = new SimpleStringProperty("Cumple");
    private final StringProperty observacionesCapturadas = new SimpleStringProperty("");

    public Criterio() {
    }

    public Criterio(int idCriterio, int idCategoria, String descripcion, double peso) {
        this.idCriterio = idCriterio;
        this.idCategoria = idCategoria;
        this.descripcion = descripcion;
        this.peso = peso;
    }

    public int getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    // ===========================
    // Propiedades JavaFX
    // ===========================

    public String getCumplimientoSeleccionado() {
        return cumplimientoSeleccionado.get();
    }

    public void setCumplimientoSeleccionado(String valor) {
        cumplimientoSeleccionado.set(valor);
    }

    public StringProperty cumplimientoSeleccionadoProperty() {
        return cumplimientoSeleccionado;
    }

    public String getObservacionesCapturadas() {
        return observacionesCapturadas.get();
    }

    public void setObservacionesCapturadas(String valor) {
        observacionesCapturadas.set(valor);
    }

    public StringProperty observacionesCapturadasProperty() {
        return observacionesCapturadas;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}