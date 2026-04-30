<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="Config.Conexion" %>

<%
String usuario = (String) session.getAttribute("usuario");
String rol = (String) session.getAttribute("rol");

if (usuario == null) {
    response.sendRedirect("login.jsp");
    return;
}

if (rol == null || (!rol.equals("AdminRRHH") && !rol.equals("AdminArea"))) {
    response.sendRedirect("dashboard.jsp");
    return;
}
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion de Solicitudes</title>

    <style>
        body{
            font-family:Arial, Helvetica, sans-serif;
            background:#f5f7fa;
            margin:0;
            padding:30px;
        }

        .contenedor{
            max-width:1100px;
            margin:auto;
            background:white;
            padding:25px;
            border-radius:15px;
            box-shadow:0 0 15px rgba(0,0,0,0.12);
        }

        h1,h2{
            text-align:center;
        }

        table{
            width:100%;
            border-collapse:collapse;
            margin-top:20px;
        }

        th,td{
            border:1px solid #ddd;
            padding:10px;
            text-align:left;
        }

        th{
            background:#0d6efd;
            color:white;
        }

        button{
            padding:8px 12px;
            border:none;
            border-radius:6px;
            cursor:pointer;
            color:white;
            margin-right:5px;
        }

        .aprobar{
            background:#198754;
        }

        .rechazar{
            background:#dc3545;
        }

        .volver{
            display:inline-block;
            margin-top:20px;
            padding:10px 15px;
            background:#6c757d;
            color:white;
            text-decoration:none;
            border-radius:8px;
        }
    </style>
</head>

<body>

<div class="contenedor">

<h1>Panel de Administracion</h1>
<h2>Gestion de Solicitudes Pendientes</h2>

<table>
<thead>
<tr>
    <th>Empleado</th>
    <th>Tipo</th>
    <th>Detalles / Motivo</th>
    <th>Accion</th>
</tr>
</thead>

<tbody>

<%
Connection con = null;

try {

    con = Conexion.getConexion();

    String sqlTurnos =
        "SELECT t.id, u.usuario, u.correo, t.Motivo, " +
        "t.TurnoInicial, t.NuevoTurno " +
        "FROM turnos t " +
        "JOIN usuarios u ON t.id_usuario = u.id " +
        "WHERE t.estado LIKE 'pendiente%'";

    PreparedStatement ps1 = con.prepareStatement(sqlTurnos);
    ResultSet rs1 = ps1.executeQuery();

    while (rs1.next()) {
%>

<tr>
<td><%= rs1.getString("usuario") %> (<%= rs1.getString("correo") %>)</td>
<td>Cambio de Turno</td>
<td>
De <%= rs1.getString("TurnoInicial") %>
a <%= rs1.getString("NuevoTurno") %><br>
Motivo: <%= rs1.getString("Motivo") %>
</td>
<td>

<form action="<%= request.getContextPath() %>/GestionAdminServlet" method="POST">

<input type="hidden" name="id_solicitud"
value="<%= rs1.getInt("id") %>">

<input type="hidden" name="correo_empleado"
value="<%= rs1.getString("correo") %>">

<input type="hidden" name="tipo_tabla" value="turnos">

<input type="hidden" name="tipo_solicitud"
value="Cambio de Turno">

<button class="aprobar" type="submit"
name="decision" value="Aprobado">
Aprobar
</button>

<button class="rechazar" type="submit"
name="decision" value="Rechazado">
Rechazar
</button>

</form>

</td>
</tr>

<%
    }

    rs1.close();
    ps1.close();

    String sqlSol =
        "SELECT s.id, u.usuario, u.correo, s.tipo, s.motivo " +
        "FROM solicitudes s " +
        "JOIN usuarios u ON s.usuario_id = u.id " +
        "WHERE s.estado LIKE 'pendiente%'";

    PreparedStatement ps2 = con.prepareStatement(sqlSol);
    ResultSet rs2 = ps2.executeQuery();

    while (rs2.next()) {
%>

<tr>
<td><%= rs2.getString("usuario") %> (<%= rs2.getString("correo") %>)</td>
<td><%= rs2.getString("tipo") %></td>
<td><%= rs2.getString("motivo") %></td>

<td>

<form action="<%= request.getContextPath() %>/GestionAdminServlet" method="POST">

<input type="hidden" name="id_solicitud"
value="<%= rs2.getInt("id") %>">

<input type="hidden" name="correo_empleado"
value="<%= rs2.getString("correo") %>">

<input type="hidden" name="tipo_tabla"
value="solicitudes">

<input type="hidden" name="tipo_solicitud"
value="<%= rs2.getString("tipo") %>">

<button class="aprobar" type="submit"
name="decision" value="Aprobado">
Aprobar
</button>

<button class="rechazar" type="submit"
name="decision" value="Rechazado">
Rechazar
</button>

</form>

</td>
</tr>

<%
    }

    rs2.close();
    ps2.close();

} catch (Exception e) {
%>

<tr>
<td colspan="4">Error al cargar datos</td>
</tr>

<%
} finally {

    try {
        if (con != null) con.close();
    } catch (Exception e) {
    }
}
%>

</tbody>
</table>

<a href="dashboard.jsp" class="volver">Regresar</a>

</div>

</body>
</html>

