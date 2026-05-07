package controlador;

import Modelo.Usuario;
import ModeloDAO.LoginDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String usuario = req.getParameter("usuario");
        String password = req.getParameter("password");

        if (usuario != null) usuario = usuario.trim();
        if (password != null) password = password.trim();

        try {
            LoginDAO dao = new LoginDAO();
            Usuario u = dao.validarUsuario(usuario);

            if (u != null && u.getPassword() != null && u.getPassword().trim().equals(password)) {
                HttpSession sesion = req.getSession();
                sesion.setAttribute("id_usuario", Integer.valueOf(u.getId_usuario()));
                sesion.setAttribute("usuario", u.getUsuario());
                sesion.setAttribute("rol", u.getRol());

                res.sendRedirect("Vistas/dashboard.jsp");
                return;
            }

            res.sendRedirect("Vistas/login.jsp?error=1");

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("Vistas/login.jsp?error=2");
        }
    }
}
