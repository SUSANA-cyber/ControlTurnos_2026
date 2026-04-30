package ModeloDAO;

import Modelo.MTurno;
import Config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class TurnoDAO {

    private Connection con;
    private PreparedStatement ps;

    public boolean CambiarTurno(MTurno mt) {
        String sql = "INSERT INTO turnos "
                + "(id_usuario, fecha_inicio, TurnoInicial, NuevoTurno, Nuevafecha, Motivo, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, 'Pendiente')";

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mt.getId_usuario());
            ps.setString(2, mt.getFecha_inicio());
            ps.setString(3, mt.getTurnoInicial());
            ps.setString(4, mt.getNuevoTurno());
            ps.setString(5, mt.getNuevaFecha());
            ps.setString(6, mt.getMotivo());
            ps.executeUpdate();

            BitacoraDAO.registrar(mt.getId_usuario(), "Cambio de Turno", "Crear",
                    "Solicitud de cambio de turno enviada");
            return true;

        } catch (Exception e) {
            System.out.println("Error en solicitud: " + e);
            return false;

        } finally {
            cerrar();
        }
    }

    public boolean ActualizarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE turnos SET estado=? WHERE id=?";

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al actualizar estado: " + e);
            return false;

        } finally {
            cerrar();
        }
    }

    public boolean eliminarTurno(int id) {
        String sql = "DELETE FROM asignacion_turnos WHERE id=?";

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al eliminar turno: " + e);
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


