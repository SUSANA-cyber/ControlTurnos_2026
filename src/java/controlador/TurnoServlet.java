package controlador;

import ModeloDAO.BitacoraDAO;
import Config.Conexion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "TurnoServlet", urlPatterns = {"/TurnoServlet"})
public class TurnoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("id_usuario") == null) {
            response.sendRedirect("Vistas/login.jsp");
            return;
        }

        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("AdminArea")) {
            response.sendRedirect("Vistas/dashboard.jsp");
            return;
        }

        int usuarioId = Integer.parseInt(request.getParameter("usuario_id"));
        String fechaInicio = request.getParameter("fecha_inicio");
        String fechaFin = request.getParameter("fecha_fin");
        int turnoId = Integer.parseInt(request.getParameter("turno_id"));

        Connection con = null;
        PreparedStatement psVal = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (fechaInicio.compareTo(fechaFin) > 0) {
                response.sendRedirect("Vistas/turnos.jsp?error=1");
                return;
            }

            con = Conexion.getConexion();

            String validar = "SELECT id FROM asignacion_turnos "
                    + "WHERE usuario_id=? AND fecha_inicio=? AND fecha_fin=?";
            psVal = con.prepareStatement(validar);
            psVal.setInt(1, usuarioId);
            psVal.setString(2, fechaInicio);
            psVal.setString(3, fechaFin);
            rs = psVal.executeQuery();

            if (rs.next()) {
                response.sendRedirect("Vistas/turnos.jsp?error=duplicado");
                return;
            }

            String sql = "INSERT INTO asignacion_turnos "
                    + "(usuario_id, fecha_inicio, fecha_fin, turno_id) VALUES (?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, usuarioId);
            ps.setString(2, fechaInicio);
            ps.setString(3, fechaFin);
            ps.setInt(4, turnoId);
            ps.executeUpdate();

            BitacoraDAO.registrar((Integer) session.getAttribute("id_usuario"),
                    "Asignacion de Turnos", "Crear",
                    "Asignacion de turno al empleado ID " + usuarioId);

            response.sendRedirect("Vistas/turnos.jsp?ok=1");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Vistas/turnos.jsp?error=2");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (psVal != null) psVal.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}


