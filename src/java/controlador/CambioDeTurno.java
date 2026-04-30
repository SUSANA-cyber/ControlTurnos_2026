package controlador;

import Modelo.MTurno;
import ModeloDAO.TurnoDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CambioDeTurno")
public class CambioDeTurno extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Integer idLogueado = (Integer) request.getSession().getAttribute("id_usuario");

        if (idLogueado == null) {
            response.sendRedirect("Vistas/login.jsp");
            return;
        }

        MTurno mtu = new MTurno();

        mtu.setId_usuario(idLogueado);
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
            response.sendRedirect("Vistas/ActualizarTurno.jsp?msj=error");
        }
    }
}

