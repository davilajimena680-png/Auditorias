package utng.gtid2.jdj.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.util.List;
import java.util.Map;

import utng.gtid2.jdj.conexion.ConexionBD;
import utng.gtid2.jdj.modelo.Evaluacion;

/**
* Inserta la evaluacion o, si ya existe una evaluacion previa para el mismo
* criterio dentro de la misma auditoria, actualiza sus valores (upsert).
* Devuelve el id_evaluacion resultante.
*/
public class EvaluacionDAO {

    public int guardarOActualizar(Evaluacion e) throws SQLException { 

        String sql = "INSERT INTO evaluaciones (id_auditoria, id_criterio, cumplimiento, observaciones, evidencia_ref) " +
        "VALUES (?, ?, ?, ?, ?) " +
        "ON CONFLICT (id_auditoria, id_criterio) DO UPDATE SET " +
        "cumplimiento = EXCLUDED.cumplimiento, " +
        "observaciones = EXCLUDED.observaciones, " +
        "evidencia_ref = EXCLUDED.evidencia_ref, " +
        "fecha_captura = NOW() " +
        "RETURNING id_evaluacion";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt(1, e.getIdAuditoria());
            ps.setInt(2, e.getIdCriterio());
            ps.setString(3, e.getCumplimiento());
            ps.setString(4, e.getObservaciones());
            ps.setString(5, e.getEvidenciaRef());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt(1);
                }
        }
        return -1;
    }

    public List<Evaluacion> obtenerPorAuditoria(int idAuditoria) throws SQLException {

        List<Evaluacion> lista = new ArrayList<>();
        String sql = "SELECT ev.*, cr.descripcion AS descripcion_criterio, cat.nombre AS nombre_categoria " + "FROM evaluaciones ev " +
        "JOIN criterios cr ON ev.id_criterio = cr.id_criterio " +
        "JOIN categorias cat ON cr.id_categoria = cat.id_categoria " +
        "WHERE ev.id_auditoria = ? ORDER BY cat.nombre, cr.id_criterio";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
        ps.setInt(1, idAuditoria);
            try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /** Cuenta cuántos criterios caen en cada estado de cumplimiento (para la grafica de
    pastel). */
    public Map<String, Integer> resumenCumplimiento(int idAuditoria) throws SQLException {

        Map<String, Integer> resumen = new LinkedHashMap<>();
        String sql = "SELECT cumplimiento, COUNT(*) AS total FROM evaluaciones " +
        "WHERE id_auditoria = ? GROUP BY cumplimiento";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
        ps.setInt(1, idAuditoria);
            try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) resumen.put(rs.getString("cumplimiento"),
                rs.getInt("total"));
            }
        }
        return resumen;
    }

    /** Porcentaje de criterios que "Cumple" agrupados por categoria (para la grafica de
    barras). */

    public Map<String, Double> porcentajePorCategoria(int idAuditoria) throws SQLException {

        Map<String, Double> resultado = new LinkedHashMap<>();
        String sql = "SELECT cat.nombre AS categoria, " +
        "ROUND(100.0 * SUM(CASE WHEN ev.cumplimiento = 'Cumple' THEN 1 ELSE 0 END) " +
        " / COUNT(*), 2) AS porcentaje " +
        "FROM evaluaciones ev " +
        "JOIN criterios cr ON ev.id_criterio = cr.id_criterio " +
        "JOIN categorias cat ON cr.id_categoria = cat.id_categoria " +
        "WHERE ev.id_auditoria = ? " +
        "GROUP BY cat.nombre ORDER BY cat.nombre";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
        ps.setInt(1, idAuditoria);
            try (ResultSet rs = ps.executeQuery()) {
             while (rs.next()) resultado.put(rs.getString("categoria"),
                rs.getDouble("porcentaje"));
            }
        }
        return resultado;
    }

    /** Porcentaje global de cumplimiento ponderado por el peso de cada criterio. */
    public double porcentajeGlobalPonderado(int idAuditoria) throws SQLException {

        String sql = "SELECT SUM(CASE WHEN ev.cumplimiento = 'Cumple' THEN cr.peso " +
        " WHEN ev.cumplimiento = 'Parcial' THEN cr.peso * 0.5 " + " ELSE 0 END) AS logrado, " +
        "SUM(cr.peso) AS total_peso " +
        "FROM evaluaciones ev JOIN criterios cr ON ev.id_criterio = cr.id_criterio " +
        "WHERE ev.id_auditoria = ?";

        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt(1, idAuditoria);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getDouble("total_peso") > 0) {
                        return Math.round((rs.getDouble("logrado") / rs.getDouble("total_peso")) * 10000.0) / 100.0;
                    }
                }
        }
        return 0.0;
    }
    private Evaluacion mapear(ResultSet rs) throws SQLException {
        Evaluacion e = new Evaluacion();
        e.setIdEvaluacion(rs.getInt("id_evaluacion"));
        e.setIdAuditoria(rs.getInt("id_auditoria"));
        e.setIdCriterio(rs.getInt("id_criterio"));
        e.setCumplimiento(rs.getString("cumplimiento"));
        e.setObservaciones(rs.getString("observaciones"));
        e.setEvidenciaRef(rs.getString("evidencia_ref"));
        Timestamp ts = rs.getTimestamp("fecha_captura");
        e.setFechaCaptura(ts != null ? ts.toLocalDateTime() : null);
        e.setDescripcionCriterio(rs.getString("descripcion_criterio"));
        e.setNombreCategoria(rs.getString("nombre_categoria"));
        return e;
    }
}