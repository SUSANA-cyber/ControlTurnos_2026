package controlador;

import Modelo.Usuario;
import Config.ServicioCorreo;
import ModeloDAO.BitacoraDAO;
import ModeloDAO.UsuarioDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        String usuarioSesion = (String) session.getAttribute("usuario");
        String rolSesion = (String) session.getAttribute("rol");

        if (usuarioSesion == null) {
            res.sendRedirect("Vistas/login.jsp");
            return;
        }

        if (rolSesion == null || !rolSesion.equals("AdminRRHH")) {
            res.sendRedirect("Vistas/dashboard.jsp");
            return;
        }

        String estado = req.getParameter("estado");
        Integer motivoInactivoId = null;
        String motivoParam = req.getParameter("motivo_inactivo_id");

        if (motivoParam != null && !motivoParam.isEmpty()) {
            motivoInactivoId = Integer.parseInt(motivoParam);
        }

        if ("Activo".equals(estado)) {
            motivoInactivoId = null;
        }

        Usuario u = new Usuario();
        u.setDpi(req.getParameter("dpi"));
        u.setNombre(req.getParameter("nombre"));
        u.setUsuario(req.getParameter("usuario"));
        u.setArea(req.getParameter("area"));
        u.setPuesto(req.getParameter("puesto"));
        u.setCorreo(req.getParameter("correo"));
        u.setPassword(req.getParameter("password"));
        u.setEstado(estado);
        u.setMotivo_inactivo_id(motivoInactivoId);
        u.setTurno_actual_id(parseEntero(req.getParameter("turno_actual_id"), 1));
        u.setRol_id(parseEntero(req.getParameter("rol_id"), 3));

        UsuarioDAO dao = new UsuarioDAO();
        boolean resultado = dao.registrarUsuario(
                u.getDpi(),
                u.getNombre(),
                u.getUsuario(),
                u.getArea(),
                u.getPuesto(),
                u.getTurno_actual_id(),
                u.getCorreo(),
                u.getPassword(),
                u.getEstado(),
                u.getMotivo_inactivo_id(),
                u.getRol_id()
        );

        if (resultado) {
            BitacoraDAO.registrar((Integer) session.getAttribute("id_usuario"),
                    "Mantenimiento de Usuarios", "Crear",
                    "Registro del empleado: " + u.getUsuario());

            if ("Inactivo".equals(u.getEstado())) {
                String motivoTexto = obtenerMotivoInactividad(u.getMotivo_inactivo_id());

                BitacoraDAO.registrar((Integer) session.getAttribute("id_usuario"),
                        "Mantenimiento de Usuarios", "Inactivar",
                        "Empleado creado en estado inactivo: " + u.getUsuario()
                        + ". Motivo: " + motivoTexto);

                if (u.getCorreo() != null && !u.getCorreo().trim().isEmpty()) {
                    ServicioCorreo.enviarEmail(
                            u.getCorreo(),
                            "Notificacion de inactivacion de usuario",
                            "Hola " + u.getNombre() + ",\n\n"
                            + "Te informamos que tu usuario fue registrado en estado INACTIVO.\n"
                            + "Motivo de inactivacion: " + motivoTexto + ".\n\n"
                            + "Saludos,\nSistema ControlTurnos2026"
                    );
                }
            }

            res.sendRedirect("Vistas/usuarios.jsp?ok=1");
        } else {
            res.sendRedirect("Vistas/usuarios.jsp?error=1");
        }
    }

    private int parseEntero(String valor, int defecto) {
        try {
            if (valor != null && !valor.isEmpty()) {
                return Integer.parseInt(valor);
            }
        } catch (NumberFormatException e) {
        }
        return defecto;
    }

    private String obtenerMotivoInactividad(Integer motivoId) {
        if (motivoId == null) {
            return "No especificado";
        }

        switch (motivoId) {
            case 1:
                return "Vacaciones";
            case 2:
                return "Permiso Personal";
            case 3:
                return "Citas al IGSS";
            case 4:
                return "Licencia de cumpleanos";
            case 5:
                return "Suspension Laboral";
            case 6:
                return "Otros";
            default:
                return "No especificado";
        }
    }
}


