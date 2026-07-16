package utng.gtid2.jdj.dao;

import utng.gtid2.jdj.conexion.ConexionBD;
import utng.gtid2.jdj.modelo.Criterio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CriterioDAO {

    public List<Criterio> obtenerPorCategoria(int idCategoria) throws SQLException {
    List<Criterio> lista = new ArrayList<>();
    String sql = "SELECT c.*, cat.nombre AS nombre_categoria FROM criterios c " +
    "JOIN categorias cat ON c.id_categoria = cat.id_categoria " +
    "WHERE c.id_categoria = ? ORDER BY c.id_criterio";

    try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
        ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
             while (rs.next()) lista.add(mapear(rs));
            }
    }
    return lista;
}

public List<Criterio> listarTodos() throws SQLException {
    List<Criterio> lista = new ArrayList<>();
    String sql = "SELECT c.*, cat.nombre AS nombre_categoria FROM criterios c " +
    "JOIN categorias cat ON c.id_categoria = cat.id_categoria " +
    "ORDER BY cat.nombre, c.id_criterio";

    try (PreparedStatement ps =ConexionBD.getInstancia().getConexion().prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) lista.add(mapear(rs));
    }
    return lista;
}

public void insertar(Criterio c) throws SQLException {
    String sql = "INSERT INTO criterios (id_categoria, descripcion, peso) VALUES (?, ?,?)";
    try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
    ps.setInt(1, c.getIdCategoria());
    ps.setString(2, c.getDescripcion());
    ps.setDouble(3, c.getPeso());
    ps.executeUpdate();
    }
}

private Criterio mapear(ResultSet rs) throws SQLException {
    Criterio c = new Criterio();
    c.setIdCriterio(rs.getInt("id_criterio"));
    c.setIdCategoria(rs.getInt("id_categoria"));
    c.setDescripcion(rs.getString("descripcion"));
    c.setPeso(rs.getDouble("peso"));
    c.setNombreCategoria(rs.getString("nombre_categoria"));
    return c;
    }
}