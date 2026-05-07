<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="Config.Conexion" %>

<%
String usuarioSesion = (String) session.getAttribute("usuario");
String rolSesion = (String) session.getAttribute("rol");

if (usuarioSesion == null) {
    response.sendRedirect("login.jsp");
    return;
}

if (rolSesion == null || !rolSesion.equals("AdminArea")) {
    response.sendRedirect("dashboard.jsp");
    return;
}

Connection con = null;
PreparedStatement ps = null;
PreparedStatement ps2 = null;
ResultSet rs = null;
ResultSet rs2 = null;

int id = Integer.parseInt(request.getParameter("id"));
int turnoActual = 0;
String fechaInicio = "";
String fechaFin = "";

try {
    con = Conexion.getConexion();

    ps = con.prepareStatement("SELECT * FROM asignacion_turnos WHERE id=?");
    ps.setInt(1, id);
    rs = ps.executeQuery();

    if (rs.next()) {
        turnoActual = rs.getInt("turno_id");
        fechaInicio = rs.getString("fecha_inicio");
        fechaFin = rs.getString("fecha_fin");
    } else {
        response.sendRedirect("turnos.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Turno</title>
    <style>
        body{
            font-family:Arial, Helvetica, sans-serif;
            background:#f4f6f9;
            margin:0;
            padding:30px;
        }
        .contenedor{
            max-width:600px;
            margin:auto;
            background:white;
            padding:25px;
            border-radius:14px;
            box-shadow:0 0 14px rgba(0,0,0,0.1);
        }
        h2{
            text-align:center;
        }
        input, select{
            width:100%;
            padding:10px;
            margin-top:6px;
            margin-bottom:15px;
            border:1px solid #ccc;
            border-radius:8px;
            box-sizing:border-box;
        }
        button{
            width:100%;
            padding:12px;
            background:#0d6efd;
            color:white;
            border:none;
            border-radius:8px;
            cursor:pointer;
        }
        .volver{
            display:inline-block;
            margin-top:20px;
            background:#6c757d;
            color:white;
            padding:10px 16px;
            border-radius:6px;
            text-decoration:none;
        }
    </style>
</head>
<body>

<div class="contenedor">
    <h2>Editar Turno</h2>

    <form action="<%= request.getContextPath() %>/ActualizarTurnoServlet" method="post">
        <input type="hidden" name="id" value="<%=id%>">

        <label>Fecha inicio</label>
        <input type="date" name="fecha_inicio" value="<%=fechaInicio%>" required>

        <label>Fecha fin</label>
        <input type="date" name="fecha_fin" value="<%=fechaFin%>" required>

        <label>Turno</label>
        <select name="turno_id" required>
        <%
            ps2 = con.prepareStatement("SELECT id, nombre FROM turnos_catalogo ORDER BY id");
            rs2 = ps2.executeQuery();

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

        <button type="submit">Actualizar</button>
    </form>

    <a href="turnos.jsp" class="volver">Regresar</a>
</div>

</body>
</html>

<%
} finally {
    try { if(rs != null) rs.close(); } catch(Exception e){}
    try { if(ps != null) ps.close(); } catch(Exception e){}
    try { if(rs2 != null) rs2.close(); } catch(Exception e){}
    try { if(ps2 != null) ps2.close(); } catch(Exception e){}
    try { if(con != null) con.close(); } catch(Exception e){}
}
%>
