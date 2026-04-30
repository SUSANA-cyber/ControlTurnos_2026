package controlador;

import Modelo.Usuario;
import ModeloDAO.UsuarioDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ConsultarUsuarioServlet")
public class ConsultarUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String buscar = req.getParameter("buscar");

        if (buscar == null) {
            buscar = "";
        }

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> lista = dao.listarUsuarios(buscar);

        req.setAttribute("lista", lista);
        req.getRequestDispatcher("Vistas/consultarUsuarios.jsp").forward(req, res);
    }
}

