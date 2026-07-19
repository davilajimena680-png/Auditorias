package utng.gtid2.jdj.modelo;

import java.time.LocalDate; 

public class PlanAccion {
    
    private int idPlan;              
    private int idHallazgo;         
    private String responsable;       
    private LocalDate fechaCompromiso; 
    private String estatus;  
    //Axuliar
    private String descripcionHallazgo; // Nueva propiedad para la descripción del hallazgo        


    public PlanAccion() {
    }

   
    public PlanAccion( int idHallazgo, String responsable, LocalDate fechaCompromiso, String estatus) {
       
        this.idHallazgo = idHallazgo;
        this.responsable = responsable;
        this.fechaCompromiso = fechaCompromiso;
        this.estatus = estatus;
    }


    public int getIdPlan() {
        return idPlan;
    }


    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }


    public int getIdHallazgo() {
        return idHallazgo;
    }


    public void setIdHallazgo(int idHallazgo) {
        this.idHallazgo = idHallazgo;
    }


    public String getResponsable() {
        return responsable;
    }


    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }


    public LocalDate getFechaCompromiso() {
        return fechaCompromiso;
    }


    public void setFechaCompromiso(LocalDate fechaCompromiso) {
        this.fechaCompromiso = fechaCompromiso;
    }


    public String getEstatus() {
        return estatus;
    }


    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }


    public String getDescripcionHallazgo() {
        return descripcionHallazgo;
    }


    public void setDescripcionHallazgo(String descripcionHallazgo) {
        this.descripcionHallazgo = descripcionHallazgo;
    }

}