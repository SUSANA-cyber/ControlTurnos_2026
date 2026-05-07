package ModeloDAO;

import Config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AreaDAO {

    public List<String[]> listarAreas() {
        List<String[]> lista = new ArrayList<String[]>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement("SELECT id, nombre FROM areas WHERE estado='Activa' ORDER BY nombre");
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("nombre")
                });
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

    public boolean agregarArea(String nombre) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement("INSERT INTO areas(nombre, estado) VALUES(?, 'Activa')");
            ps.setString(1, nombre);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return false;
    }
}
