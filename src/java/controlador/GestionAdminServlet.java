package controlador;

import Config.Conexion;
import Config.ServicioCorreo;
import Modelo.Bitacora;
import ModeloDAO.SolicitudDAO;
import ModeloDAO.TurnoDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "GestionAdminServlet", urlPatterns = {"/GestionAdminServlet"})
public class GestionAdminServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("id_usuario") == null) {
            response.sendRedirect("Vistas/login.jsp");
            return;
        }
<<<<<<< HEAD

        String rol = (String) session.getAttribute("rol");
        Integer adminId = (Integer) session.getAttribute("id_usuario");

=======
        
         
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)
        int idSolicitud = Integer.parseInt(request.getParameter("id_solicitud"));
        String correoEmpleado = request.getParameter("correo_empleado");
        String tipoTabla = request.getParameter("tipo_tabla");
        String tipo = request.getParameter("tipo_solicitud");
        String decision = request.getParameter("decision");
<<<<<<< HEAD
        boolean exito = false;

        if ("AdminArea".equals(rol)) {
            if (!solicitudPerteneceAlAdmin(idSolicitud, tipoTabla, adminId)) {
                response.sendRedirect("Vistas/GstionSolicitudes.jsp?error=1");
                return;
            }

            String nuevoEstado = "Aprobado".equalsIgnoreCase(decision) ? "PendienteRRHH" : "Rechazado";

            if ("turnos".equals(tipoTabla)) {
                TurnoDAO tDao = new TurnoDAO();
                exito = tDao.ActualizarEstado(idSolicitud, nuevoEstado);
            } else {
                SolicitudDAO sDao = new SolicitudDAO();
                exito = sDao.ActualizarEstado(idSolicitud, nuevoEstado);
            }

        } else if ("AdminRRHH".equals(rol)) {
            if (!solicitudPendienteRRHH(idSolicitud, tipoTabla)) {
                response.sendRedirect("Vistas/GstionSolicitudes.jsp?error=1");
                return;
            }

            if ("turnos".equals(tipoTabla)) {
                TurnoDAO tDao = new TurnoDAO();
                exito = tDao.ActualizarEstado(idSolicitud, decision);

                if (exito && "Aprobado".equalsIgnoreCase(decision)) {
                    aplicarCambioTurno(idSolicitud);
                }
            } else {
                SolicitudDAO sDao = new SolicitudDAO();
                exito = sDao.ActualizarEstado(idSolicitud, decision);
            }
        }

        if (exito) {
            String asunto;
            String mensaje;

            if ("Aprobado".equalsIgnoreCase(decision)) {
                asunto = "Solicitud aprobada: " + tipoSolicitud;
                mensaje = "Hola,\n\nTu solicitud de " + tipoSolicitud + " ha sido APROBADA.\n\nSaludos,\nSistema ControlTurnos2026";
                Bitacora.solicitudesAprobadas((Integer) session.getAttribute("id_usuario"), "Solicitud " + tipoSolicitud + " ID " + idSolicitud);
            } else {
                asunto = "Solicitud rechazada: " + tipoSolicitud;
                mensaje = "Hola,\n\nTu solicitud de " + tipoSolicitud + " ha sido RECHAZADA.\n\nSaludos,\nSistema ControlTurnos2026";
                Bitacora.solicitudesRechazadas((Integer) session.getAttribute("id_usuario"), "Solicitud " + tipoSolicitud + " ID " + idSolicitud);
            }
=======
        String rol = (String) session.getAttribute("rol");
        
        String nuevoEstado = "";
        boolean esFinal = false;

        
        if ("Aprobado".equals(decision)) {
            if ("AdminArea".equals(rol)) {
                nuevoEstado = "Pendiente_RRHH"; 
            } else if ("AdminRRHH".equals(rol)) {
                nuevoEstado = "Aprobado"; 
                esFinal = true;
            }
        } else {
          
            nuevoEstado = "Rechazado"; 
            esFinal = true;
        }

        boolean exito;
        if ("turnos".equals(tipoTabla)) {
            TurnoDAO tDao = new TurnoDAO();
            exito = tDao.ActualizarEstado(idSolicitud, nuevoEstado);
        } else {
            SolicitudDAO sDao = new SolicitudDAO();
            exito = sDao.ActualizarEstado(idSolicitud, nuevoEstado);
        }

        if (exito) {
           
            if (esFinal) {
                String asunto = "Respuesta final a tu solicitud de " + tipo;
                String mensaje = "Hola,\n\nTu solicitud de " + tipo
                        + " ha sido " + nuevoEstado.toUpperCase()
                        + ".\n\nSaludos,\nSistema ControlTurnos2026";
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)

                if (correoEmpleado != null && !correoEmpleado.trim().isEmpty()) {
                    ServicioCorreo.enviarEmail(correoEmpleado, asunto, mensaje);
                }
            }

<<<<<<< HEAD
=======
            BitacoraDAO.registrar((Integer) session.getAttribute("id_usuario"),
                    "Gestion de Solicitudes", nuevoEstado,
                    "Solicitud " + tipo + " ID " + idSolicitud + " procesada por " + rol);

>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)
            response.sendRedirect("Vistas/GstionSolicitudes.jsp?ok=1");
        } else {
            response.sendRedirect("Vistas/GstionSolicitudes.jsp?error=1");
        }
    }

    private boolean solicitudPerteneceAlAdmin(int idSolicitud, String tipoTabla, Integer adminId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion();

            if ("turnos".equals(tipoTabla)) {
                ps = con.prepareStatement(
                    "SELECT t.id FROM turnos t " +
                    "JOIN usuarios u ON t.id_usuario = u.id " +
                    "WHERE t.id=? " +
                    "AND t.estado IN ('Pendiente', 'PendienteArea') " +
                    "AND u.admin_responsable_id=?"
                );
            } else {
                ps = con.prepareStatement(
                    "SELECT s.id FROM solicitudes s " +
                    "JOIN usuarios u ON s.usuario_id = u.id " +
                    "WHERE s.id=? " +
                    "AND s.estado IN ('Pendiente', 'PendienteArea') " +
                    "AND u.admin_responsable_id=?"
                );
            }

            ps.setInt(1, idSolicitud);
            ps.setInt(2, adminId.intValue());
            rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    private boolean solicitudPendienteRRHH(int idSolicitud, String tipoTabla) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion();

            if ("turnos".equals(tipoTabla)) {
                ps = con.prepareStatement("SELECT id FROM turnos WHERE id=? AND estado='PendienteRRHH'");
            } else {
                ps = con.prepareStatement("SELECT id FROM solicitudes WHERE id=? AND estado='PendienteRRHH'");
            }

            ps.setInt(1, idSolicitud);
            rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    private void aplicarCambioTurno(int idSolicitudTurno) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement psTurnoId = null;
        PreparedStatement psInsert = null;
        ResultSet rs = null;
        ResultSet rsTurnoId = null;

        try {
            con = Conexion.getConexion();

            ps = con.prepareStatement("SELECT id_usuario, Nuevafecha, NuevoTurno FROM turnos WHERE id=?");
            ps.setInt(1, idSolicitudTurno);
            rs = ps.executeQuery();

            if (rs.next()) {
                int usuarioId = rs.getInt("id_usuario");
                String nuevaFecha = rs.getString("Nuevafecha");
                String nuevoTurno = rs.getString("NuevoTurno");

                psTurnoId = con.prepareStatement("SELECT id FROM turnos_catalogo WHERE nombre=?");
                psTurnoId.setString(1, nuevoTurno);
                rsTurnoId = psTurnoId.executeQuery();

                if (rsTurnoId.next()) {
                    int turnoId = rsTurnoId.getInt("id");

                    psInsert = con.prepareStatement(
                        "INSERT INTO asignacion_turnos " +
                        "(usuario_id, fecha_inicio, fecha_fin, turno_id, estado) " +
                        "VALUES (?, ?, ?, ?, 'Activo')"
                    );
                    psInsert.setInt(1, usuarioId);
                    psInsert.setString(2, nuevaFecha);
                    psInsert.setString(3, nuevaFecha);
                    psInsert.setInt(4, turnoId);
                    psInsert.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (rsTurnoId != null) rsTurnoId.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (psTurnoId != null) psTurnoId.close(); } catch (Exception e) {}
            try { if (psInsert != null) psInsert.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}