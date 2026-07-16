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

    public Criterio(){}

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

    public StringProperty getCumplimientoSeleccionado() {
        return cumplimientoSeleccionado;
    }

    public StringProperty getObservacionesCapturadas() {
        return observacionesCapturadas;
    }

    @Override
    public String toString() {
        return descripcion;
    }

    public Object setCumplimientoSeleccionado(String newValue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCumplimientoSeleccionado'");
    }

    public Object setObservacionesCapturadas(String newValue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setObservacionesCapturadas'");
    }
    
}
