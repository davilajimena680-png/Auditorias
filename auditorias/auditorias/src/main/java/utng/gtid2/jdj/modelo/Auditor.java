package utng.gtid2.jdj.modelo;

public class Auditor {
    private int idAuditor;
    private String nombreCompleto;
    private String cedulaProfesional;
    private String email;

    public Auditor(int idAuditor, String nombreCompleto, String cedulaProfesional, String email) {
        this.idAuditor = idAuditor;
        this.nombreCompleto = nombreCompleto;
        this.cedulaProfesional = cedulaProfesional;
        this.email = email;
    }

    public int getIdAuditor() {
        return idAuditor;
    }

    public void setIdAuditor(int idAuditor) {
        this.idAuditor = idAuditor;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCedulaProfesional() {
        return cedulaProfesional;
    }

    public void setCedulaProfesional(String cedulaProfesional) {
        this.cedulaProfesional = cedulaProfesional;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
