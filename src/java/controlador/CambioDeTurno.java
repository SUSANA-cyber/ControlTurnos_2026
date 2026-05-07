package controlador;

import Modelo.MTurno;
import ModeloDAO.TurnoDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/CambioDeTurno")
public class CambioDeTurno extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("id_usuario") == null) {
            response.sendRedirect("Vistas/login.jsp");
            return;
        }

        Integer idLogueado = (Integer) sesion.getAttribute("id_usuario");

        MTurno mtu = new MTurno();
        mtu.setId_usuario(idLogueado.intValue());
        mtu.setFecha_inicio(request.getParameter("FechaInicio"));
        mtu.setTurnoInicial(request.getParameter("InicialTurno"));
        mtu.setNuevoTurno(request.getParameter("NuevoTurno"));
        mtu.setNuevaFecha(request.getParameter("Nuevafecha"));
        mtu.setMotivo(request.getParameter("motivo"));

        TurnoDAO dao = new TurnoDAO();
        boolean exito = dao.CambiarTurno(mtu);

        if (exito) {
            response.sendRedirect("Vistas/ActualizarTurno.jsp?msj=exito");
        } else {
            response.sendRedirect("Vistas/ActualizarTurno.jsp?msj=asignado");
        }
    }
}
