package ModeloDAO;

import Modelo.MTurno;
import Config.Conexion;
import Modelo.Bitacora;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TurnoDAO {

    private Connection con;
    private PreparedStatement ps;

    public boolean empleadoYaTieneTurno(int usuarioId, String fecha) {
        ResultSet rs = null;
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(
                "SELECT id FROM asignacion_turnos WHERE usuario_id=? AND estado='Activo' AND ? BETWEEN fecha_inicio AND fecha_fin"
            );
            ps.setInt(1, usuarioId);
            ps.setString(2, fecha);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            cerrar();
        }
    }

    public boolean CambiarTurno(MTurno mt) {
<<<<<<< HEAD
        String sql = "INSERT INTO turnos (id_usuario, fecha_inicio, TurnoInicial, NuevoTurno, Nuevafecha, Motivo, estado) VALUES (?, ?, ?, ?, ?, ?, 'Pendiente')";
=======
        String sql = "INSERT INTO turnos "
            + "(id_usuario, fecha_inicio, TurnoInicial, NuevoTurno, Nuevafecha, Motivo, estado) "
            + "VALUES (?, ?, ?, ?, ?, ?, 'Pendiente_Area')";
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)

        try {
            if (empleadoYaTieneTurno(mt.getId_usuario(), mt.getNuevaFecha())) {
                return false;
            }

            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mt.getId_usuario());
            ps.setString(2, mt.getFecha_inicio());
            ps.setString(3, mt.getTurnoInicial());
            ps.setString(4, mt.getNuevoTurno());
            ps.setString(5, mt.getNuevaFecha());
            ps.setString(6, mt.getMotivo());
            ps.executeUpdate();

            Bitacora.solicitudes(mt.getId_usuario(), "Solicitud de cambio de turno enviada");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            cerrar();
        }
    }

    public boolean ActualizarEstado(int id, String nuevoEstado) {
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement("UPDATE turnos SET estado=? WHERE id=?");
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }

    public boolean eliminarTurno(int id) {
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement("DELETE FROM asignacion_turnos WHERE id=?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
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