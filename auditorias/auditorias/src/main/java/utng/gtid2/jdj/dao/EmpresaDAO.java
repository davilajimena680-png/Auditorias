package utng.gtid2.jdj.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utng.gtid2.jdj.conexion.ConexionBD;
import utng.gtid2.jdj.modelo.Empresa;

public class EmpresaDAO {

    public List<Empresa> listarTodas() throws SQLException {
        List<Empresa> lista = new ArrayList<>();
        String sql = "SELECT * FROM empresas ORDER BY razon_social";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }

        return lista;
    }

    public Empresa obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM empresas WHERE id_empresa = ?";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }

        return null;
    }

    public int insertar(Empresa e) throws SQLException {

        String sql = "INSERT INTO empresas (razon_social, rfc, sector, num_empleados, contacto_email) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getRazonSocial());
            ps.setString(2, e.getRfc());
            ps.setString(3, e.getSector());
            ps.setInt(4, e.getNumEmpleados());
            ps.setString(5, e.getContactoEmail());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        return -1;
    }

    public void actualizar(Empresa e) throws SQLException {

        String sql = "UPDATE empresas "
                   + "SET razon_social=?, rfc=?, sector=?, num_empleados=?, contacto_email=? "
                   + "WHERE id_empresa=?";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {

            ps.setString(1, e.getRazonSocial());
            ps.setString(2, e.getRfc());
            ps.setString(3, e.getSector());
            ps.setInt(4, e.getNumEmpleados());
            ps.setString(5, e.getContactoEmail());
            ps.setInt(6, e.getIdEmpresa());

            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {

        String sql = "DELETE FROM empresas WHERE id_empresa = ?";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Empresa mapear(ResultSet rs) throws SQLException {

        Empresa e = new Empresa();

        e.setIdEmpresa(rs.getInt("id_empresa"));
        e.setRazonSocial(rs.getString("razon_social"));
        e.setRfc(rs.getString("rfc"));
        e.setSector(rs.getString("sector"));
        e.setNumEmpleados(rs.getInt("num_empleados"));
        e.setContactoEmail(rs.getString("contacto_email"));

        return e;
    }
}