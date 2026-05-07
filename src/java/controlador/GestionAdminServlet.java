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

        String rol = (String) session.getAttribute("rol");

        int idSolicitud = Integer.parseInt(request.getParameter("id_solicitud"));
        String correoEmpleado = request.getParameter("correo_empleado");
        String tipoTabla = request.getParameter("tipo_tabla");
        String tipoSolicitud = request.getParameter("tipo_solicitud");
        String decision = request.getParameter("decision");
        boolean exito = false;

        if ("AdminArea".equals(rol)) {
            if ("turnos".equals(tipoTabla)) {
                TurnoDAO tDao = new TurnoDAO();
                exito = tDao.ActualizarEstado(idSolicitud, "PendienteRRHH");
            } else {
                SolicitudDAO sDao = new SolicitudDAO();
                exito = sDao.ActualizarEstado(idSolicitud, "PendienteRRHH");
            }

        } else if ("AdminRRHH".equals(rol)) {
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

            if (correoEmpleado != null && !correoEmpleado.trim().isEmpty()) {
                ServicioCorreo.enviarEmail(correoEmpleado, asunto, mensaje);
            }

            response.sendRedirect("Vistas/GstionSolicitudes.jsp?ok=1");
        } else {
            response.sendRedirect("Vistas/GstionSolicitudes.jsp?error=1");
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

                    psInsert = con.prepareStatement("INSERT INTO asignacion_turnos (usuario_id, fecha_inicio, fecha_fin, turno_id, estado) VALUES (?, ?, ?, ?, 'Activo')");
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