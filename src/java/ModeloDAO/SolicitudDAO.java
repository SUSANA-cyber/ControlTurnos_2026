package ModeloDAO;

import Modelo.MSolicitudes;
import Config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SolicitudDAO {

    private Connection con;
    private PreparedStatement ps;

    public boolean AgregarSolicitud(MSolicitudes sol) {
        String sql = "INSERT INTO solicitudes "
                + "(usuario_id, tipo, fecha_inicio, fecha_fin, motivo, estado) "
                + "VALUES (?, ?, ?, ?, ?, 'Pendiente')";

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, sol.getUsuario_id());
            ps.setString(2, sol.getTipo());
            ps.setString(3, sol.getFecha_inicio());
            ps.setString(4, sol.getFecha_fin());
            ps.setString(5, sol.getMotivo());
            ps.executeUpdate();

            BitacoraDAO.registrar(sol.getUsuario_id(), "Gestiones del Empleado", "Crear",
                    "Solicitud enviada: " + sol.getTipo());
            return true;

        } catch (Exception e) {
            System.out.println("Error en la solicitud: " + e);
            return false;

        } finally {
            cerrar();
        }
    }

    public boolean ActualizarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE solicitudes SET estado=? WHERE id=?";

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al actualizar estado en Solicitudes: " + e);
            return false;
        } finally {
            cerrar();
        }
    }

    private void cerrar() {
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (con != null) con.close(); } catch (Exception e) {}
    }
}


