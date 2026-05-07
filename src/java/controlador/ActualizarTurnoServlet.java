package controlador;

import Config.Conexion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ActualizarTurnoServlet")
public class ActualizarTurnoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String inicio = req.getParameter("fecha_inicio");
            String fin = req.getParameter("fecha_fin");
            int turno_id = Integer.parseInt(req.getParameter("turno_id"));

            con = Conexion.getConexion();

            String sql = "UPDATE asignacion_turnos "
                    + "SET fecha_inicio=?, fecha_fin=?, turno_id=? "
                    + "WHERE id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, inicio);
            ps.setString(2, fin);
            ps.setInt(3, turno_id);
            ps.setInt(4, id);

            ps.executeUpdate();

            res.sendRedirect(req.getContextPath() + "/Vistas/turnos.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect(req.getContextPath() + "/Vistas/turnos.jsp");
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}