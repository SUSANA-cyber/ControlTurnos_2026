package controlador;

import ModeloDAO.AreaDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AreaServlet")
public class AreaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("id_usuario") == null) {
            res.sendRedirect("Vistas/login.jsp");
            return;
        }

        String rol = (String) session.getAttribute("rol");
        if (!"AdminRRHH".equals(rol)) {
            res.sendRedirect("Vistas/dashboard.jsp");
            return;
        }

        String nombre = req.getParameter("nombre_area");

        AreaDAO dao = new AreaDAO();
        boolean ok = dao.agregarArea(nombre);

        if (ok) {
            res.sendRedirect("Vistas/usuarios.jsp?areaok=1");
        } else {
            res.sendRedirect("Vistas/usuarios.jsp?areaerror=1");
        }
    }
}