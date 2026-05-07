package controlador;

import Config.Conexion;
import ModeloDAO.TurnoDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/EliminarTurnoServlet")
public class EliminarTurnoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("id_usuario") == null) {
            res.sendRedirect(req.getContextPath() + "/Vistas/login.jsp");
            return;
        }

        String rol = (String) session.getAttribute("rol");
        Integer adminId = (Integer) session.getAttribute("id_usuario");

        if (!"AdminArea".equals(rol)) {
            res.sendRedirect(req.getContextPath() + "/Vistas/dashboard.jsp");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            int id = Integer.parseInt(req.getParameter("id"));

            con = Conexion.getConexion();
            ps = con.prepareStatement(
                "SELECT at.id FROM asignacion_turnos at " +
                "JOIN usuarios u ON at.usuario_id = u.id " +
                "WHERE at.id=? AND u.admin_responsable_id=?"
            );
            ps.setInt(1, id);
            ps.setInt(2, adminId.intValue());
            rs = ps.executeQuery();

            if (!rs.next()) {
                res.sendRedirect(req.getContextPath() + "/Vistas/turnos.jsp?error=noautorizado");
                return;
            }

            TurnoDAO dao = new TurnoDAO();
            dao.eliminarTurno(id);

            res.sendRedirect(req.getContextPath() + "/Vistas/turnos.jsp?ok=1");

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect(req.getContextPath() + "/Vistas/turnos.jsp?error=2");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}