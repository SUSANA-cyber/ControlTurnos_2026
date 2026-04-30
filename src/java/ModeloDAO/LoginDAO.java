package ModeloDAO;

import Modelo.Usuario;
import Config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {

    public Usuario validarUsuario(String usuario) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            con = Conexion.getConexion();

            String sql = "SELECT u.id, u.usuario, u.password, r.nombre AS rol " +
                         "FROM usuarios u " +
                         "INNER JOIN roles r ON u.rol_id = r.id " +
                         "WHERE u.usuario=? AND u.estado='Activo'";

            ps = con.prepareStatement(sql);
            ps.setString(1, usuario);

            rs = ps.executeQuery();

            if (rs.next()) {

                Usuario u = new Usuario();

                u.setId_usuario(rs.getInt("id"));
                u.setUsuario(rs.getString("usuario"));
                u.setPassword(rs.getString("password"));
                u.setRol(rs.getString("rol"));

                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return null;
    }
}

