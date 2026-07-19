package utng.gtid2.jdj.modelo;

public class Hallazgo {

    private int idHallazgo;
    private int idEvaluacion;
    private int idAuditoria;
    private String tipo;
    private String nivelRiesgo;
    private String descripcion;
    private String descripcionCriterio;

    public Hallazgo() {
    }

    public Hallazgo(int idEvaluacion, String tipo, String nivelRiesgo, String descripcion) {
        this.idEvaluacion = idEvaluacion;
        this.tipo = tipo;
        this.nivelRiesgo = nivelRiesgo;
        this.descripcion = descripcion;
    }

    public int getIdHallazgo() {
        return idHallazgo;
    }

    public void setIdHallazgo(int idHallazgo) {
        this.idHallazgo = idHallazgo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(String nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcionCriterio() {
        return descripcionCriterio;
    }

    public void setDescripcionCriterio(String descripcionCriterio) {
        this.descripcionCriterio = descripcionCriterio;
    }
}