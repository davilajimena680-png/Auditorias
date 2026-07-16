package utng.gtid2.jdj.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utng.gtid2.jdj.conexion.ConexionBD;
import utng.gtid2.jdj.modelo.Auditor;

public class AuditorDAO {

    private Auditor mapear(ResultSet rs) throws SQLException {
        Auditor a = new Auditor(0, null, null, null);
        a.setIdAuditor(rs.getInt("id_auditor"));
        a.setNombreCompleto(rs.getString("nombre_completo"));
        a.setCedulaProfesional(rs.getString("cedula_profesional"));
        a.setEmail(rs.getString("email"));
        return a;
    }


    public List<Auditor> listarTodos() throws SQLException {
        List <Auditor> lista = new ArrayList<>();
        String sql = "SELECT * FROM auditores ORDER BY nombre_completo"; // Consulta SQL para obtener todos los auditores ordenados por nombre completo
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {


            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void actualizar(Auditor a) throws SQLException {
        String sql = "UPDATE auditores SET nombre_completo = ?, cedula_profesional = ?, email = ? WHERE id_auditor = ?"; // Consulta SQL para actualizar un auditor existente
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setString(1, a.getNombreCompleto());
            ps.setString(2, a.getCedulaProfesional());
            ps.setString(3, a.getEmail());
            ps.setInt(4, a.getIdAuditor());
            ps.executeUpdate(); // Retorna el número de filas afectadas
        }
    }

        public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM auditores WHERE id_auditor = ?"; // Consulta SQL para eliminar un auditor
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt (1, id);
            ps.executeUpdate(); // Retorna el número de filas afectadas
        }
    }


        public void insertar(Auditor a) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'insertar'");
        }


}