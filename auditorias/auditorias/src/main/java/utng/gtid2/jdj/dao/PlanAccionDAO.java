package utng.gtid2.jdj.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utng.gtid2.jdj.conexion.ConexionBD;
import utng.gtid2.jdj.modelo.PlanAccion;

public class PlanAccionDAO {

    public int insertar(PlanAccion p) throws SQLException {

        String sql = "INSERT INTO planes_accion (id_hallazgo, responsable, fecha_compromiso,estatus) " + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getIdHallazgo());
            ps.setString(2, p.getResponsable());
            ps.setDate(3, p.getFechaCompromiso() != null ?
            Date.valueOf(p.getFechaCompromiso()) : null);
            ps.setString(4, p.getEstatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public void actualizarEstatus(int idPlan, String estatus) throws SQLException {

        String sql = "UPDATE planes_accion SET estatus = ? WHERE id_plan = ?";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setString(1, estatus);
            ps.setInt(2, idPlan);
            ps.executeUpdate();
        }
    }

    public List<PlanAccion> obtenerPorAuditoria(int idAuditoria) throws SQLException {

        List<PlanAccion> lista = new ArrayList<>();
        String sql = "SELECT pa.*, h.descripcion AS descripcion_hallazgo " +
        "FROM planes_accion pa " + "JOIN hallazgos h ON pa.id_hallazgo = h.id_hallazgo " +
        "JOIN evaluaciones ev ON h.id_evaluacion = ev.id_evaluacion " +
        "WHERE ev.id_auditoria = ? ORDER BY pa.fecha_compromiso NULLS LAST";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt(1, idAuditoria);
            try (ResultSet rs = ps.executeQuery()) {
             while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<PlanAccion> obtenerPorHallazgo(int idHallazgo) throws SQLException {

        List<PlanAccion> lista = new ArrayList<>();
        String sql = "SELECT * FROM planes_accion WHERE id_hallazgo = ?";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt(1, idHallazgo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PlanAccion p = new PlanAccion();
                    p.setIdPlan(rs.getInt("id_plan"));
                    p.setIdHallazgo(rs.getInt("id_hallazgo"));
                    p.setResponsable(rs.getString("responsable"));
                    Date fc = rs.getDate("fecha_compromiso");
                    p.setFechaCompromiso(fc != null ? fc.toLocalDate() : null);
                    p.setEstatus(rs.getString("estatus"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }

    private PlanAccion mapear(ResultSet rs) throws SQLException {

        PlanAccion p = new PlanAccion();
        p.setIdPlan(rs.getInt("id_plan"));
        p.setIdHallazgo(rs.getInt("id_hallazgo"));
        p.setResponsable(rs.getString("responsable"));
        Date fc = rs.getDate("fecha_compromiso");
        p.setFechaCompromiso(fc != null ? fc.toLocalDate() : null);
        p.setEstatus(rs.getString("estatus"));
        p.setDescripcionHallazgo(rs.getString("descripcion_hallazgo"));
        return p;
    }
}