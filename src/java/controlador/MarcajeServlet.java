package controlador;

import ModeloDAO.MarcajeDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/MarcajeServlet")
public class MarcajeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        try {

            HttpSession sesion = req.getSession(false);

            if (sesion == null || sesion.getAttribute("id_usuario") == null) {
                res.sendRedirect("Vistas/login.jsp");
                return;
            }

            int idUsuario = (int) sesion.getAttribute("id_usuario");
            String accion = req.getParameter("accion");

            MarcajeDAO dao = new MarcajeDAO();

            String mensaje = dao.procesarMarcaje(idUsuario, accion);

            res.sendRedirect("Vistas/marcaje.jsp?msg="
                    + java.net.URLEncoder.encode(mensaje, "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("Vistas/marcaje.jsp?msg=Error en el sistema");
        }
    }
}

