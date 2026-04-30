package controlador;

import ModeloDAO.BitacoraDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession sesion = req.getSession(false);

        if (sesion != null) {
            Integer idUsuario = (Integer) sesion.getAttribute("id_usuario");

            if (idUsuario != null) {
                BitacoraDAO.registrar(idUsuario, "Login", "Logout", "Cierre de sesion");
            }

            sesion.invalidate();
        }

        res.sendRedirect("Vistas/login.jsp");
    }
}
