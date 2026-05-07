package controlador;

import ModeloDAO.TurnoDAO;
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

            res.sendRedirect(req.getContextPath() + "/Vistas/turnos.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect(req.getContextPath() + "/Vistas/turnos.jsp");
        }
    }
}