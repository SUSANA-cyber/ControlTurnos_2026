<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Usuario" %>

<%
String usuarioSesion = (String) session.getAttribute("usuario");
String rolSesion = (String) session.getAttribute("rol");

if (usuarioSesion == null) {
    response.sendRedirect("login.jsp");
    return;
}

if (rolSesion == null || !rolSesion.equals("AdminRRHH")) {
    response.sendRedirect("dashboard.jsp");
    return;
}
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consultar Usuarios</title>
    <style>
        body{
            font-family:Arial,Helvetica,sans-serif;
            background:#f4f6f9;
            padding:30px;
            margin:0;
        }
        .contenedor{
            max-width:1100px;
            margin:auto;
            background:white;
            padding:25px;
            border-radius:14px;
            box-shadow:0 0 14px rgba(0,0,0,.1);
        }
        h2{
            text-align:center;
        }
        form{
            text-align:center;
            margin-bottom:20px;
        }
        input[type="text"]{
            padding:10px;
            width:300px;
        }
        button{
            padding:10px 16px;
            background:#0d6efd;
            color:white;
            border:none;
            border-radius:6px;
            cursor:pointer;
        }
        table{
            width:100%;
            border-collapse:collapse;
        }
        th,td{
            border:1px solid #ccc;
            padding:10px;
            text-align:left;
        }
        th{
            background:#0d6efd;
            color:white;
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
<h2>Consultar Usuario</h2>

<form action="<%= request.getContextPath() %>/ConsultarUsuarioServlet" method="get">
    <input type="text" name="buscar" placeholder="Buscar por nombre, usuario o DPI">
    <button type="submit">Buscar</button>
</form>

<table>
    <tr>
        <th>Usuario</th>
        <th>Area</th>
        <th>Estado</th>
        <th>Turno</th>
        <th>Rol</th>
        <th>Administrador</th>
    </tr>

<%
List<Usuario> lista = (List<Usuario>) request.getAttribute("lista");

if (lista != null && !lista.isEmpty()) {
    for (Usuario u : lista) {
%>
<tr>
    <td><%= u.getUsuario() %></td>
    <td><%= u.getArea() != null ? u.getArea() : "" %></td>
    <td><%= u.getEstado() != null ? u.getEstado() : "" %></td>
    <td><%= u.getTurno() != null ? u.getTurno() : "" %></td>
    <td><%= u.getRol() != null ? u.getRol() : "" %></td>
    <td><%= u.getAdminResponsableNombre() != null ? u.getAdminResponsableNombre() : "" %></td>
</tr>
<%
    }
} else {
%>
<tr>
    <td colspan="6">No hay resultados</td>
</tr>
<%
}
%>

</table>

<a href="dashboard.jsp" class="volver">Regresar</a>
</div>

</body>
</html>
