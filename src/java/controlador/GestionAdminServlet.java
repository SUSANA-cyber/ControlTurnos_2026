package controlador;

import ModeloDAO.BitacoraDAO;
import ModeloDAO.SolicitudDAO;
import ModeloDAO.TurnoDAO;
import Config.ServicioCorreo;
import java.io.IOException;
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

        int idSolicitud = Integer.parseInt(request.getParameter("id_solicitud"));
        String correoEmpleado = request.getParameter("correo_empleado");
        String tipoTabla = request.getParameter("tipo_tabla");
        String tipoSolicitud = request.getParameter("tipo_solicitud");
        String decision = request.getParameter("decision");
        boolean exito;

        if ("turnos".equals(tipoTabla)) {
            TurnoDAO tDao = new TurnoDAO();
            exito = tDao.ActualizarEstado(idSolicitud, decision);
        } else {
            SolicitudDAO sDao = new SolicitudDAO();
            exito = sDao.ActualizarEstado(idSolicitud, decision);
        }

        if (exito) {
            String asunto = "Respuesta a tu solicitud de " + tipoSolicitud;
            String mensaje = "Hola,\n\nTu solicitud de " + tipoSolicitud
                    + " fue marcada como: " + decision.toUpperCase()
                    + ".\n\nSaludos,\nSistema ControlTurnos2026";

            if (correoEmpleado != null && !correoEmpleado.trim().isEmpty()) {
                ServicioCorreo.enviarEmail(correoEmpleado, asunto, mensaje);
            }

            BitacoraDAO.registrar((Integer) session.getAttribute("id_usuario"),
                    "Gestion de Solicitudes", decision,
                    "Solicitud " + tipoSolicitud + " ID " + idSolicitud);

            response.sendRedirect("Vistas/GstionSolicitudes.jsp?ok=1");
        } else {
            response.sendRedirect("Vistas/GstionSolicitudes.jsp?error=1");
        }
    }
}


