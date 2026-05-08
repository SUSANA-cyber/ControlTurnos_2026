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
        if (buscar == null) { buscar = ""; }
        
        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> lista = dao.listarUsuarios(buscar);
        
        req.setAttribute("lista", lista);
        req.getRequestDispatcher("Vistas/consultarUsuarios.jsp").forward(req, res);
    }

    
  @Override
protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String accion = req.getParameter("accion");
    int id = Integer.parseInt(req.getParameter("id_usuario"));
    String dpi = req.getParameter("dpi");
    UsuarioDAO dao = new UsuarioDAO();
    boolean exito = false;

    if ("inactivar".equals(accion)) {
        int motivoId = Integer.parseInt(req.getParameter("motivo"));
        exito = dao.inactivarUsuario(id, motivoId);
    } else if ("activar".equals(accion)) {
        exito = dao.activarUsuario(id);
    }

    if (exito) {
        req.setAttribute("mensaje", "Estado del empleado " + dpi + " actualizado correctamente.");
    } else {
        req.setAttribute("error", "No se pudo cambiar el estado del empleado.");
    }
    
    doGet(req, res);
}
    
}

