package utng.gtid2.jdj.modelo;

import java.time.LocalDate;

public class Auditoria {
    
    private int idAuditoria;
    private int idEmpresa;
    private int idAuditor;
    private LocalDate fehcaInicio;
    private LocalDate fechaFin;
    private String estatus;
    
    //Campos auxiliares para mostrar tablas sin hacer JOINS
    private String nombreEmpresa;
    private String nombreAuditor;

    public Auditoria(){

    }

    public Auditoria(int idEmpresa, int idAuditor, LocalDate fehcaInicio) {
        this.idEmpresa = idEmpresa;
        this.idAuditor = idAuditor;
        this.fehcaInicio = fehcaInicio;
        this.estatus = "En proceso";
    }

    public int getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(int idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdAuditor() {
        return idAuditor;
    }

    public void setIdAuditor(int idAuditor) {
        this.idAuditor = idAuditor;
    }

    public LocalDate getFehcaInicio() {
        return fehcaInicio;
    }

    public void setFehcaInicio(LocalDate fehcaInicio) {
        this.fehcaInicio = fehcaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombreAuditor() {
        return nombreAuditor;
    }

    public void setNombreAuditor(String nombreAuditor) {
        this.nombreAuditor = nombreAuditor;
    }

    @Override
    public String toString() {
        return "Auditoria #" + idAuditoria + 
               " - Empresa: " + nombreEmpresa + 
               " - Auditor: " + nombreAuditor + 
               " - Fecha Inicio: " + fehcaInicio + 
               " - Fecha Fin: " + fechaFin + 
               " - Estatus: " + estatus;
    }

}
