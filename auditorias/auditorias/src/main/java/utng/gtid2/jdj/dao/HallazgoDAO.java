package utng.gtid2.jdj.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utng.gtid2.jdj.conexion.ConexionBD;
import utng.gtid2.jdj.modelo.Hallazgo;

public class HallazgoDAO {

    public int insertar(Hallazgo h) throws SQLException {
        String sql = "INSERT INTO hallazgos (id_evaluacion, tipo, nivel_riesgo, descripcion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, h.getIdEvaluacion());
            ps.setString(2, h.getTipo());
            ps.setString(3, h.getNivelRiesgo());
            ps.setString(4, h.getDescripcion());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    /** Todos los hallazgos de una auditoria (via join con evaluaciones), con la descripcion
    del criterio. */
    public List<Hallazgo> obtenerPorAuditoria(int idAuditoria) throws SQLException { 
        
        List<Hallazgo> lista = new ArrayList<>();
        String sql = "SELECT h.*, cr.descripcion AS descripcion_criterio, ev.id_auditoria " +
        "FROM hallazgos h " + "JOIN evaluaciones ev ON h.id_evaluacion = ev.id_evaluacion " +
        "JOIN criterios cr ON ev.id_criterio = cr.id_criterio " + "WHERE ev.id_auditoria = ? " +
        "ORDER BY CASE h.nivel_riesgo WHEN 'Alto' THEN 1 WHEN 'Medio' THEN 2 ELSE 3 END";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt(1, idAuditoria);
            try (ResultSet rs = ps.executeQuery()) {
             while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM hallazgos WHERE id_hallazgo = ?";
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Hallazgo mapear(ResultSet rs) throws SQLException {
        Hallazgo h = new Hallazgo();
        h.setIdHallazgo(rs.getInt("id_hallazgo"));
        h.setIdEvaluacion(rs.getInt("id_evaluacion"));
        h.setTipo(rs.getString("tipo"));
        h.setNivelRiesgo(rs.getString("nivel_riesgo"));
        h.setDescripcion(rs.getString("descripcion"));
        h.setDescripcionCriterio(rs.getString("descripcion_criterio"));
        h.setIdAuditoria(rs.getInt("id_auditoria"));
        return h;
    }
}