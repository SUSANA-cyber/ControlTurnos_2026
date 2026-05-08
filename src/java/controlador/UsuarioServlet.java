package controlador;

<<<<<<< HEAD
=======
import Config.Conexion;
import Modelo.Usuario;
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)
import Config.ServicioCorreo;
import Modelo.Usuario;
import Modelo.Bitacora;
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

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            res.sendRedirect("Vistas/login.jsp");
            return;
        }

        String rolSesion = (String) session.getAttribute("rol");
        if (!"AdminRRHH".equals(rolSesion)) {
            res.sendRedirect("Vistas/dashboard.jsp");
            return;
        }

        String dpi = req.getParameter("dpi");
        String usuario = req.getParameter("usuario");

        if (dpi == null || !dpi.matches("[0-9]+")) {
            res.sendRedirect("Vistas/usuarios.jsp?error=dpi");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();

        if (dao.existeUsuarioODpi(usuario, dpi)) {
            res.sendRedirect("Vistas/usuarios.jsp?error=duplicado");
            return;
        }

        String estado = req.getParameter("estado");
        Integer motivoInactivoId = parseEnteroObjeto(req.getParameter("motivo_inactivo_id"));

        if ("Activo".equals(estado)) {
            motivoInactivoId = null;
        }

        Usuario u = new Usuario();
        u.setDpi(dpi);
        u.setNombre(req.getParameter("nombre"));
        u.setUsuario(usuario);
        u.setArea_id(parseEnteroObjeto(req.getParameter("area_id")));
        u.setArea(req.getParameter("area_texto"));
        u.setPuesto(req.getParameter("puesto"));
        u.setCorreo(req.getParameter("correo"));
        u.setPassword(req.getParameter("password"));
        u.setEstado(estado);
        u.setMotivo_inactivo_id(motivoInactivoId);
        u.setTurno_actual_id(parseEntero(req.getParameter("turno_actual_id"), 1));
        u.setRol_id(parseEntero(req.getParameter("rol_id"), 3));
        u.setAdmin_responsable_id(parseEnteroObjeto(req.getParameter("admin_responsable_id")));

<<<<<<< HEAD
        boolean resultado = dao.registrarUsuario(u);
=======
        UsuarioDAO dao = new UsuarioDAO();
        boolean resultado = dao.registrarUsuario(
                u.getDpi(), u.getNombre(), u.getUsuario(), u.getArea(),
                u.getPuesto(), u.getTurno_actual_id(), u.getCorreo(),
                u.getPassword(), u.getEstado(), u.getMotivo_inactivo_id(),
                u.getRol_id()
        );
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)

        if (resultado) {
            Bitacora.creacionUsuario((Integer) session.getAttribute("id_usuario"),
                    "Registro del empleado: " + u.getUsuario());

            boolean correoEnviado = true;

            if ("Inactivo".equals(u.getEstado())) {
                String motivoTexto = obtenerMotivoInactividad(u.getMotivo_inactivo_id());
<<<<<<< HEAD

                if (u.getCorreo() != null && !u.getCorreo().trim().isEmpty()) {
                    correoEnviado = ServicioCorreo.enviarEmail(
                            u.getCorreo(),
                            "Notificacion de inactivacion de usuario",
                            "Hola " + u.getNombre() + ",\n\n"
                            + "Tu usuario fue registrado en estado INACTIVO.\n"
                            + "Motivo: " + motivoTexto + ".\n\n"
                            + "Saludos,\nSistema ControlTurnos2026"
                    );
                }
            }

            if (correoEnviado) {
                res.sendRedirect("Vistas/usuarios.jsp?ok=1");
            } else {
                res.sendRedirect("Vistas/usuarios.jsp?ok=1&correo=0");
            }
=======
                // Lógica de Bitacora y Correo...
                if (u.getCorreo() != null && !u.getCorreo().trim().isEmpty()) {
                    ServicioCorreo.enviarEmail(u.getCorreo(), "Notificación", "Hola " + u.getNombre() + "...");
                }
            }
            res.sendRedirect("Vistas/usuarios.jsp?ok=1");
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)
        } else {
            res.sendRedirect("Vistas/usuarios.jsp?error=1");
        }
    }

    private int parseEntero(String valor, int defecto) {
        try {
<<<<<<< HEAD
            if (valor != null && !valor.trim().isEmpty()) {
                return Integer.parseInt(valor);
            }
        } catch (Exception e) {
        }
        return defecto;
    }

    private Integer parseEnteroObjeto(String valor) {
        try {
            if (valor != null && !valor.trim().isEmpty()) {
                return Integer.parseInt(valor);
            }
        } catch (Exception e) {
        }
        return null;
    }

    private String obtenerMotivoInactividad(Integer motivoId) {
        if (motivoId == null) return "No especificado";

        switch (motivoId.intValue()) {
=======
            if (valor != null && !valor.isEmpty()) return Integer.parseInt(valor);
        } catch (NumberFormatException e) {}
        return defecto;
    }

    private String obtenerMotivoInactividad(Integer motivoId) {
        if (motivoId == null) return "No especificado";
        switch (motivoId) {
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)
            case 1: return "Vacaciones";
            case 2: return "Permiso Personal";
            case 3: return "Citas al IGSS";
            case 4: return "Licencia de cumpleanos";
            case 5: return "Suspension Laboral";
            case 6: return "Otros";
            default: return "No especificado";
        }
    }
}