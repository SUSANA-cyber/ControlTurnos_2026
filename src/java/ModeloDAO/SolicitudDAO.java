package ModeloDAO;

import Modelo.MSolicitudes;
import Config.Conexion;
import Modelo.Bitacora;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class SolicitudDAO {

    private Connection con;
    private PreparedStatement ps;

    public boolean AgregarSolicitud(MSolicitudes sol) {
<<<<<<< HEAD
        String sql = "INSERT INTO solicitudes (usuario_id, tipo, fecha_inicio, fecha_fin, motivo, estado) VALUES (?, ?, ?, ?, ?, 'PendienteArea')";
=======
       String sql = "INSERT INTO solicitudes "
            + "(usuario_id, tipo, fecha_inicio, fecha_fin, motivo, estado) "
            + "VALUES (?, ?, ?, ?, ?, 'Pendiente_Area')";
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, sol.getUsuario_id());
            ps.setString(2, sol.getTipo());
            ps.setString(3, sol.getFecha_inicio());
            ps.setString(4, sol.getFecha_fin());
            ps.setString(5, sol.getMotivo());
            ps.executeUpdate();

            Bitacora.solicitudes(sol.getUsuario_id(), "Solicitud enviada: " + sol.getTipo());
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
<<<<<<< HEAD
}
=======



public List<MSolicitudes> ListarPendientesPorAdmin(int adminId) {
    List<MSolicitudes> lista = new ArrayList<>();
    
    String sql = "SELECT s.*, u.nombre as nombre_empleado "
               + "FROM solicitudes s "
               + "JOIN usuarios u ON s.usuario_id = u.id "
               + "WHERE u.admin_responsable_id = ? AND s.estado = 'Pendiente'";

    try {
        con = Conexion.getConexion();
        ps = con.prepareStatement(sql);
        ps.setInt(1, adminId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            MSolicitudes s = new MSolicitudes();
            s.setId(rs.getInt("id"));
            s.setUsuario_id(rs.getInt("usuario_id"));
            s.setNombre_empleado(rs.getString("nombre_empleado")); 
            s.setTipo(rs.getString("tipo"));         
            s.setFecha_inicio(rs.getString("fecha_inicio")); 
            s.setFecha_fin(rs.getString("fecha_fin"));
            
            s.setMotivo(rs.getString("motivo"));
            s.setEstado(rs.getString("estado"));
            lista.add(s);
        }
    } catch (Exception e) {
        System.out.println("Error al listar solicitudes: " + e);
    } finally {
        cerrar();
    }
    return lista;
}
}


>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)
