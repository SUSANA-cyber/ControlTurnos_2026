package ModeloDAO;

import Config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class BitacoraDAO {

    public static void registrar(Integer usuarioId, String modulo, String operacion, String descripcion) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.getConexion();
            if (con == null) {
                return;
            }

            String sql = "INSERT INTO bitacora "
                    + "(usuario_id, modulo, tipo_operacion, descripcion, fecha_hora) "
                    + "VALUES (?, ?, ?, ?, NOW())";

            ps = con.prepareStatement(sql);

            if (usuarioId != null) {
                ps.setInt(1, usuarioId);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }

            ps.setString(2, modulo);
            ps.setString(3, operacion);
            ps.setString(4, descripcion);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}


