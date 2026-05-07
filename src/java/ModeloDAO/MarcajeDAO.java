package ModeloDAO;

import Config.Conexion;
import Modelo.Bitacora;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;

public class MarcajeDAO {

    public String procesarMarcaje(int idUsuario, String accion) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion();

            String sql = "SELECT * FROM marcajes WHERE usuario_id=? AND fecha=CURDATE()";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            boolean existe = rs.next();
            Time ahora = new Time(System.currentTimeMillis());
            String mensaje = "";
            boolean guardarBitacora = false;

            if ("entrada".equals(accion)) {
                if (existe) {
                    mensaje = "Ya marco entrada hoy";
                } else {
                    String insert = "INSERT INTO marcajes "
                            + "(usuario_id, fecha, hora_entrada, entrada_tarde) "
                            + "VALUES (?, CURDATE(), ?, ?)";
                    PreparedStatement ps2 = con.prepareStatement(insert);
                    ps2.setInt(1, idUsuario);
                    ps2.setTime(2, ahora);
                    ps2.setBoolean(3, ahora.after(Time.valueOf("08:00:00")));
                    ps2.executeUpdate();
                    ps2.close();

                    if (ahora.after(Time.valueOf("08:00:00"))) {
                        mensaje = "Entrada registrada tarde";
                    } else {
                        mensaje = "Marcaje realizado con exito";
                    }

                    guardarBitacora = true;
                }

            } else if ("descanso1".equals(accion)) {
                if (!existe) {
                    mensaje = "Debe marcar la entrada antes de registrar el descanso.";
                } else if (rs.getTime("hora_descanso1") != null) {
                    mensaje = "Ya marco descanso 1";
                } else {
                    actualizarHora(con, "hora_descanso1", idUsuario, ahora);
                    mensaje = "Descanso 1 registrado";
                    guardarBitacora = true;
                }

            } else if ("descanso2".equals(accion)) {
                if (!existe) {
                    mensaje = "Debe marcar la entrada antes de registrar el descanso.";
                } else if (rs.getTime("hora_descanso1") == null) {
                    mensaje = "Debe marcar el primer descanso antes de registrar el segundo descanso.";
                } else if (rs.getTime("hora_descanso2") != null) {
                    mensaje = "Ya marco descanso 2";
                } else {
                    actualizarHora(con, "hora_descanso2", idUsuario, ahora);
                    mensaje = "Descanso 2 registrado";
                    guardarBitacora = true;
                }

            } else if ("salida".equals(accion)) {
                if (!existe) {
                    mensaje = "Debe marcar entrada primero";
                } else if (rs.getTime("hora_descanso1") == null) {
                    mensaje = "Debe marcar el primer descanso antes de registrar la salida.";
                } else if (rs.getTime("hora_descanso2") == null) {
                    mensaje = "Debe marcar el segundo descanso antes de registrar la salida.";
                } else if (rs.getTime("hora_salida") != null) {
                    mensaje = "Ya marco salida";
                } else {
                    actualizarHora(con, "hora_salida", idUsuario, ahora);
                    mensaje = "Salida registrada";
                    guardarBitacora = true;
                }
            }

            if (guardarBitacora) {
                Bitacora.marcajes(idUsuario, mensaje);
            }

            return mensaje;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error en el sistema";

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    private void actualizarHora(Connection con, String columna, int idUsuario, Time hora) throws Exception {
        String update = "UPDATE marcajes SET " + columna + "=? WHERE usuario_id=? AND fecha=CURDATE()";
        PreparedStatement ps = con.prepareStatement(update);
        ps.setTime(1, hora);
        ps.setInt(2, idUsuario);
        ps.executeUpdate();
        ps.close();
    }
}