package utng.gtid2.jdj.modelo;

public class Empresa {
    private int idEmpresa;
    private String razonSocial;
    private String rfc;
    private int sector;
    private int numEmpleados;
    private String contactoEmail;

    public Empresa(){}

    public Empresa(String razonSocial, String rfc, int sector, int numEmpleados, String contactoEmail) {
        this.razonSocial = razonSocial;
        this.rfc = rfc;
        this.sector = sector;
        this.numEmpleados = numEmpleados;
        this.contactoEmail = contactoEmail;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public int getSector() {
        return sector;
    }

    public void setSector(int sector) {
        this.sector = sector;
    }

    public int getNumEmpleados() {
        return numEmpleados;
    }

    public void setNumEmpleados(int numEmpleados) {
        this.numEmpleados = numEmpleados;
    }

    public String getContactoEmail() {
        return contactoEmail;
    }

    public void setContactoEmail(String contactoEmail) {
        this.contactoEmail = contactoEmail;
    }

    @Override
    public String toString() {
        return razonSocial;
    }

  

   
    
}
