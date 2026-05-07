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

    public boolean registrarUsuario(Usuario u) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion();

            if (u.getDpi() == null || !u.getDpi().matches("[0-9]+")) {
                return false;
            }

            if (u.getRol_id() == 2) {
                u.setAdmin_responsable_id(null);
            }

            if (u.getAdmin_responsable_id() != null) {
                if (u.getArea_id() == null) {
                    return false;
                }

                ps = con.prepareStatement(
                    "SELECT id FROM usuarios " +
                    "WHERE id=? AND rol_id=2 AND estado='Activo' AND area_id=?"
                );
                ps.setInt(1, u.getAdmin_responsable_id());
                ps.setInt(2, u.getArea_id());
                rs = ps.executeQuery();

                if (!rs.next()) {
                    return false;
                }

                rs.close();
                ps.close();
                rs = null;
                ps = null;
            }

            ps = con.prepareStatement("SELECT id FROM usuarios WHERE usuario=? OR dpi=?");
            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getDpi());
            rs = ps.executeQuery();

            if (rs.next()) {
                return false;
            }

            rs.close();
            ps.close();
            rs = null;
            ps = null;

            String sql = "INSERT INTO usuarios "
                    + "(dpi, nombre, usuario, area, area_id, puesto, turno_actual_id, correo, password, estado, motivo_inactivo_id, rol_id, admin_responsable_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql);
            ps.setString(1, u.getDpi());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getUsuario());
            ps.setString(4, u.getArea() == null ? "" : u.getArea());

            if (u.getArea_id() != null) {
                ps.setInt(5, u.getArea_id());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            ps.setString(6, u.getPuesto());
            ps.setInt(7, u.getTurno_actual_id());
            ps.setString(8, u.getCorreo());
            ps.setString(9, u.getPassword());
            ps.setString(10, u.getEstado());

            if (u.getMotivo_inactivo_id() != null) {
                ps.setInt(11, u.getMotivo_inactivo_id());
            } else {
                ps.setNull(11, java.sql.Types.INTEGER);
            }

            ps.setInt(12, u.getRol_id());

            if (u.getAdmin_responsable_id() != null) {
                ps.setInt(13, u.getAdmin_responsable_id());
            } else {
                ps.setNull(13, java.sql.Types.INTEGER);
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
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

            String sql = "SELECT u.*, r.nombre AS rol_nombre, t.nombre AS turno_nombre, "
                    + "a.nombre AS area_nombre, adm.nombre AS admin_nombre "
                    + "FROM usuarios u "
                    + "LEFT JOIN roles r ON u.rol_id = r.id "
                    + "LEFT JOIN turnos_catalogo t ON u.turno_actual_id = t.id "
                    + "LEFT JOIN areas a ON u.area_id = a.id "
                    + "LEFT JOIN usuarios adm ON u.admin_responsable_id = adm.id "
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
                u.setArea(rs.getString("area_nombre"));
                u.setArea_id((Integer) rs.getObject("area_id"));
                u.setPuesto(rs.getString("puesto"));
                u.setCorreo(rs.getString("correo"));
                u.setEstado(rs.getString("estado"));
                u.setRol(rs.getString("rol_nombre"));
                u.setTurno(rs.getString("turno_nombre"));
                u.setAdminResponsableNombre(rs.getString("admin_nombre"));
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