<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="Config.Conexion" %>

<%
    int id = Integer.parseInt(request.getParameter("id"));

    Connection con = Conexion.getConexion();

    String sql = "SELECT * FROM asignacion_turnos WHERE id=?";
    PreparedStatement ps = con.prepareStatement(sql);
    ps.setInt(1, id);

    ResultSet rs = ps.executeQuery();
    rs.next();

    int turnoActual = rs.getInt("turno_id");
%>

<h2>Editar Turno</h2>

<form action="<%= request.getContextPath() %>/ActualizarTurnoServlet" method="post">

    <input type="hidden" name="id" value="<%=id%>">

    Fecha inicio:
    <input type="date" name="fecha_inicio" value="<%=rs.getString("fecha_inicio")%>">

    <br><br>

    Fecha fin:
    <input type="date" name="fecha_fin" value="<%=rs.getString("fecha_fin")%>">

    <br><br>

    Turno:
    <select name="turno_id">

<%
    String sqlTurnos = "SELECT id, nombre FROM turnos_catalogo";
    PreparedStatement ps2 = con.prepareStatement(sqlTurnos);
    ResultSet rs2 = ps2.executeQuery();

    while(rs2.next()){
        int idTurno = rs2.getInt("id");
        String nombreTurno = rs2.getString("nombre");
%>

        <option value="<%=idTurno%>" <%= (idTurno == turnoActual ? "selected" : "") %>>
            <%=nombreTurno%>
        </option>

<%
    }
%>

    </select>

    <br><br>

    <button type="submit">Actualizar</button>

</form>

<%
    rs.close();
    ps.close();
    rs2.close();
    ps2.close();
    con.close();
%>

