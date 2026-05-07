package controlador;

import Config.Conexion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ActualizarTurnoServlet")
public class ActualizarTurnoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/Vistas/turnos.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
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
        PreparedStatement psVal = null;
        PreparedStatement ps = null;
        ResultSet rsVal = null;

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String inicio = req.getParameter("fecha_inicio");
            String fin = req.getParameter("fecha_fin");
            int turnoId = Integer.parseInt(req.getParameter("turno_id"));

            if (inicio.compareTo(fin) > 0) {
                res.sendRedirect(req.getContextPath() + "/Vistas/turnos.jsp?error=1");
                return;
            }

            con = Conexion.getConexion();

            psVal = con.prepareStatement(
                "SELECT at.id FROM asignacion_turnos at " +
                "JOIN usuarios u ON at.usuario_id = u.id " +
                "WHERE at.id=? AND u.admin_responsable_id=?"
            );
            psVal.setInt(1, id);
            psVal.setInt(2, adminId.intValue());
            rsVal = psVal.executeQuery();

            if (!rsVal.next()) {
                res.sendRedirect(req.getContextPath() + "/Vistas/turnos.jsp?error=noautorizado");
                return;
            }

            String sql = "UPDATE asignacion_turnos "
                    + "SET fecha_inicio=?, fecha_fin=?, turno_id=? "
                    + "WHERE id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, inicio);
            ps.setString(2, fin);
            ps.setInt(3, turnoId);
            ps.setInt(4, id);

            ps.executeUpdate();

            res.sendRedirect(req.getContextPath() + "/Vistas/turnos.jsp?ok=1");

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect(req.getContextPath() + "/Vistas/turnos.jsp?error=2");
        } finally {
            try { if (rsVal != null) rsVal.close(); } catch (Exception e) {}
            try { if (psVal != null) psVal.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}