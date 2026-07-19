package utng.gtid2.jdj.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utng.gtid2.jdj.conexion.ConexionBD;
import utng.gtid2.jdj.modelo.Auditoria;

public class AuditoriaDAO {

    private static final String SELECT_BASE =
            "SELECT a.*, e.razon_social, au.nombre_completo " +
            "FROM auditorias a " +
            "JOIN empresas e ON a.id_empresa = e.id_empresa " +
            "JOIN auditores au ON a.id_auditor = au.id_auditor ";

    public List<Auditoria> listarTodas() throws SQLException {

        List<Auditoria> lista = new ArrayList<>();

        String sql = SELECT_BASE + "ORDER BY a.fecha_inicio DESC";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }

        return lista;
    }

    public Auditoria obtenerPorId(int id) throws SQLException {

        String sql = SELECT_BASE + "WHERE a.id_auditoria = ?";

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

    public int insertar(Auditoria a) throws SQLException {

        String sql = "INSERT INTO auditorias (id_empresa, id_auditor, fecha_inicio, fecha_fin, estatus) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, a.getIdEmpresa());
            ps.setInt(2, a.getIdAuditor());

            ps.setDate(3,
                    a.getFehcaInicio() != null
                            ? Date.valueOf(a.getFehcaInicio())
                            : null);

            ps.setDate(4,
                    a.getFechaFin() != null
                            ? Date.valueOf(a.getFechaFin())
                            : null);

            ps.setString(5, a.getEstatus());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        return -1;
    }

    public void actualizarEstatus(int idAuditoria, String estatus, java.time.LocalDate fechaFin)
            throws SQLException {

        String sql = "UPDATE auditorias SET estatus = ?, fecha_fin = ? WHERE id_auditoria = ?";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {

            ps.setString(1, estatus);
            ps.setDate(2, fechaFin != null ? Date.valueOf(fechaFin) : null);
            ps.setInt(3, idAuditoria);

            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {

        String sql = "DELETE FROM auditorias WHERE id_auditoria = ?";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Auditoria mapear(ResultSet rs) throws SQLException {

        Auditoria a = new Auditoria();

        a.setIdAuditoria(rs.getInt("id_auditoria"));
        a.setIdEmpresa(rs.getInt("id_empresa"));
        a.setIdAuditor(rs.getInt("id_auditor"));

        Date fi = rs.getDate("fecha_inicio");
        a.setFehcaInicio(fi != null ? fi.toLocalDate() : null);

        Date ff = rs.getDate("fecha_fin");
        a.setFechaFin(ff != null ? ff.toLocalDate() : null);

        a.setEstatus(rs.getString("estatus"));

        a.setNombreEmpresa(rs.getString("razon_social"));
        a.setNombreAuditor(rs.getString("nombre_completo"));

        return a;
    }
}