package ModeloDAO;

import Modelo.Usuario;
import Config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public boolean registrarUsuario(String dpi, String nombre, String usuario,
            String area, String puesto, int turnoActualId,
            String correo, String password, String estado,
            Integer motivoInactivoId, int rolId) {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.getConexion();

            String sql = "INSERT INTO usuarios "
                    + "(dpi, nombre, usuario, area, puesto, turno_actual_id, correo, password, estado, motivo_inactivo_id, rol_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql);
            ps.setString(1, dpi);
            ps.setString(2, nombre);
            ps.setString(3, usuario);
            ps.setString(4, area);
            ps.setString(5, puesto);
            ps.setInt(6, turnoActualId);
            ps.setString(7, correo);
            ps.setString(8, password);
            ps.setString(9, estado);

            if (motivoInactivoId != null) {
                ps.setInt(10, motivoInactivoId);
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }

            ps.setInt(11, rolId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return false;
    }

    public List<Usuario> listarUsuarios(String buscar) {
        List<Usuario> lista = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion();

            String sql = "SELECT u.*, r.nombre AS rol_nombre, t.nombre AS turno_nombre "
                    + "FROM usuarios u "
                    + "LEFT JOIN roles r ON u.rol_id = r.id "
                    + "LEFT JOIN turnos_catalogo t ON u.turno_actual_id = t.id "
                    + "WHERE u.nombre LIKE ? OR u.usuario LIKE ? OR u.dpi LIKE ? "
                    + "ORDER BY u.nombre";

            ps = con.prepareStatement(sql);
            String filtro = "%" + buscar + "%";
            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);

            rs = ps.executeQuery();

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId_usuario(rs.getInt("id"));
                u.setDpi(rs.getString("dpi"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setArea(rs.getString("area"));
                u.setPuesto(rs.getString("puesto"));
                u.setCorreo(rs.getString("correo"));
                u.setEstado(rs.getString("estado"));
                u.setRol(rs.getString("rol_nombre"));
                u.setTurno(rs.getString("turno_nombre"));
                lista.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return lista;
    }

    public boolean asignarRol(String usuario, int rolId) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement("UPDATE usuarios SET rol_id=? WHERE usuario=?");
            ps.setInt(1, rolId);
            ps.setString(2, usuario);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return false;
    }

    public boolean quitarRol(String usuario) {
        return asignarRol(usuario, 3);
    }

    public List<String[]> listarRoles() {
        List<String[]> roles = new ArrayList<>();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion();
            st = con.createStatement();
            rs = st.executeQuery("SELECT id, nombre FROM roles ORDER BY id");

            while (rs.next()) {
                roles.add(new String[]{String.valueOf(rs.getInt("id")), rs.getString("nombre")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (st != null) st.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return roles;
    }
}


