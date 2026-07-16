package utng.gtid2.jdj.modelo;

import java.time.LocalDateTime;

import javafx.beans.property.StringProperty; 

public class Evaluacion {
    private int idEvaluacion;
    private int idAuditoria;
    private int idCriterio;
    private String cumplimiento;
    private String observaciones;
    private String evidenciaRef;        
    private LocalDateTime fechaCaptura; 

    private String descripcionCriterio; // Nueva propiedad para la descripción del criterio
    private String nombreCategoria; // Nueva propiedad para la descripción de la auditoría

    public Evaluacion() {
        // Constructor vacío
    }

    public Evaluacion( int idAuditoria, int idCriterio, String cumplimiento, String observaciones,
            String evidenciaRef) {

        this.idAuditoria = idAuditoria;
        this.idCriterio = idCriterio;
        this.cumplimiento = cumplimiento;
        this.observaciones = observaciones;
        this.evidenciaRef = evidenciaRef;
    }

    public int getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(int idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public int getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(int idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public int getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }

    public String getCumplimiento() {
        return cumplimiento;
    }

    public void setCumplimiento(String cumplimiento) {
        this.cumplimiento = cumplimiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEvidenciaRef() {
        return evidenciaRef;
    }

    public void setEvidenciaRef(String evidenciaRef) {
        this.evidenciaRef = evidenciaRef;
    }

    public LocalDateTime getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(LocalDateTime fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public String getDescripcionCriterio() {
        return descripcionCriterio;
    }

    public void setDescripcionCriterio(String descripcionCriterio) {
        this.descripcionCriterio = descripcionCriterio;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public void setObservaciones(StringProperty observacionesCapturadas) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setObservaciones'");
    }

    public void setCumplimiento(StringProperty cumplimientoSeleccionado) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCumplimiento'");
    }
    
    
}