package controlador;

import ModeloDAO.BitacoraDAO;
import ModeloDAO.UsuarioDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/RolServlet")
public class RolServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("id_usuario") == null) {
            res.sendRedirect("Vistas/login.jsp");
            return;
        }

        String rolSesion = (String) session.getAttribute("rol");
        if (rolSesion == null || !rolSesion.equals("AdminRRHH")) {
            res.sendRedirect("Vistas/dashboard.jsp");
            return;
        }

        String accion = req.getParameter("accion");
        String usuario = req.getParameter("usuario");
        UsuarioDAO dao = new UsuarioDAO();
        boolean ok = false;

        if ("agregar".equals(accion)) {
            int rolId = Integer.parseInt(req.getParameter("rol_id"));
            ok = dao.asignarRol(usuario, rolId);
            if (ok) {
                BitacoraDAO.registrar((Integer) session.getAttribute("id_usuario"),
                        "Gestion de Roles", "Agregar",
                        "Asignacion de rol al usuario: " + usuario);
            }
        } else if ("eliminar".equals(accion)) {
            ok = dao.quitarRol(usuario);
            if (ok) {
                BitacoraDAO.registrar((Integer) session.getAttribute("id_usuario"),
                        "Gestion de Roles", "Eliminar",
                        "Rol reiniciado a Empleado para el usuario: " + usuario);
            }
        }

        res.sendRedirect("Vistas/gestionRoles.jsp?" + (ok ? "ok=1" : "error=1"));
    }
}


