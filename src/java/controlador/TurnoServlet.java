package controlador;

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
        if (!"AdminArea".equals(rol)) {
            response.sendRedirect("Vistas/dashboard.jsp");
            return;
        }

        int usuarioId = Integer.parseInt(request.getParameter("usuario_id"));
        String fechaInicio = request.getParameter("fecha_inicio");
        String fechaFin = request.getParameter("fecha_fin");
        int turnoId = Integer.parseInt(request.getParameter("turno_id"));

        Connection con = null;
        PreparedStatement psVal = null;
        PreparedStatement psHoras = null;
        PreparedStatement psIns = null;
        ResultSet rsVal = null;
        ResultSet rsHoras = null;

        try {
            if (fechaInicio.compareTo(fechaFin) > 0) {
                response.sendRedirect("Vistas/turnos.jsp?error=1");
                return;
            }

            con = Conexion.getConexion();

            psHoras = con.prepareStatement("SELECT horas FROM turnos_catalogo WHERE id=?");
            psHoras.setInt(1, turnoId);
            rsHoras = psHoras.executeQuery();

            if (rsHoras.next() && rsHoras.getInt("horas") != 8) {
                response.sendRedirect("Vistas/turnos.jsp?error=horas");
                return;
            }

            String validar = "SELECT id FROM asignacion_turnos "
                    + "WHERE usuario_id=? AND estado='Activo' "
                    + "AND NOT (? < fecha_inicio OR ? > fecha_fin)";

            psVal = con.prepareStatement(validar);
            psVal.setInt(1, usuarioId);
            psVal.setString(2, fechaFin);
            psVal.setString(3, fechaInicio);
            rsVal = psVal.executeQuery();

            if (rsVal.next()) {
                response.sendRedirect("Vistas/turnos.jsp?error=asignado");
                return;
            }

            psIns = con.prepareStatement(
                "INSERT INTO asignacion_turnos "
                + "(usuario_id, fecha_inicio, fecha_fin, turno_id, estado) "
                + "VALUES (?, ?, ?, ?, 'Activo')"
            );
            psIns.setInt(1, usuarioId);
            psIns.setString(2, fechaInicio);
            psIns.setString(3, fechaFin);
            psIns.setInt(4, turnoId);
            psIns.executeUpdate();

            response.sendRedirect("Vistas/turnos.jsp?ok=1");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Vistas/turnos.jsp?error=2");
        } finally {
            try { if (rsVal != null) rsVal.close(); } catch (Exception e) {}
            try { if (rsHoras != null) rsHoras.close(); } catch (Exception e) {}
            try { if (psVal != null) psVal.close(); } catch (Exception e) {}
            try { if (psHoras != null) psHoras.close(); } catch (Exception e) {}
            try { if (psIns != null) psIns.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}