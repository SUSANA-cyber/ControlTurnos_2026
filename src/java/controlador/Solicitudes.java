package controlador;

import Modelo.MSolicitudes;
import ModeloDAO.SolicitudDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;




@WebServlet(name = "Solicitudes", urlPatterns = {"/Solicitudes"})
public class Solicitudes extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("id_usuario") == null) {
            response.sendRedirect("Vistas/login.jsp");
            return;
        }

        Integer idLogueado = (Integer) session.getAttribute("id_usuario");

        MSolicitudes solicitud = new MSolicitudes();
        solicitud.setUsuario_id(idLogueado);
        solicitud.setTipo(request.getParameter("tipo_solicitud"));
        solicitud.setFecha_inicio(request.getParameter("FechaInicio"));
        solicitud.setFecha_fin(request.getParameter("FechaFin"));
        solicitud.setMotivo(request.getParameter("motivo"));

        SolicitudDAO dao = new SolicitudDAO();
        boolean exito = dao.AgregarSolicitud(solicitud);

        if (exito) {
            response.sendRedirect("Vistas/Solicitudes.jsp?msj=exito");
        } else {
            response.sendRedirect("Vistas/Solicitudes.jsp?msj=error");
        }
    }
    
    
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    HttpSession session = request.getSession(false);
    
    if (session != null && session.getAttribute("id_usuario") != null) {
        int adminId = (int) session.getAttribute("id_usuario");
        
        SolicitudDAO dao = new SolicitudDAO();
        
        List<MSolicitudes> lista = dao.ListarPendientesPorAdmin(adminId);
        
        
        request.setAttribute("listaSolicitudes", lista);
        request.getRequestDispatcher("Vistas/GestionSolicitudes.jsp").forward(request, response);
    } else {
        response.sendRedirect("Vistas/login.jsp");
    }
}
}


