package controlador;

import ModeloDAO.TurnoDAO;
import ModeloDAO.BitacoraDAO;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/EliminarTurnoServlet")
public class EliminarTurnoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        try {

            int id = Integer.parseInt(req.getParameter("id"));

            TurnoDAO dao = new TurnoDAO();
            dao.eliminarTurno(id);

            BitacoraDAO.registrar((Integer) req.getSession().getAttribute("id_usuario"),
                    "Asignacion de Turnos", "Eliminar",
                    "Eliminacion de asignacion de turno ID " + id);

            res.sendRedirect(req.getContextPath() + "/Vistas/turnos.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect(req.getContextPath() + "/Vistas/turnos.jsp");
        }
    }
}


