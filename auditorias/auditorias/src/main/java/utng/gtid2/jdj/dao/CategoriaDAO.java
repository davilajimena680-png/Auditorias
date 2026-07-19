package utng.gtid2.jdj.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utng.gtid2.jdj.conexion.ConexionBD;
import utng.gtid2.jdj.modelo.Categoria;

public class CategoriaDAO {

    public List<Categoria> listarTodas() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias ORDER BY nombre";
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Categoria obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM categorias WHERE id_categoria = ?";
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public void insertar(Categoria c) throws SQLException {
        String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.executeUpdate();
        }
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
    Categoria c = new Categoria();
    c.setIdCategoria(rs.getInt("id_categoria"));
    c.setNombre(rs.getString("nombre"));
    c.setDescripcion(rs.getString("descripcion"));
    return c;
    }
}