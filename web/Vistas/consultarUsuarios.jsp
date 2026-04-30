<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Usuario" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consultar Usuarios</title>
</head>
<body>

<h2>Consultar Usuarios</h2>

<form action="<%= request.getContextPath() %>/ConsultarUsuarioServlet" method="get">
    <input type="text" name="buscar" placeholder="Buscar usuario...">
    <button type="submit">Buscar</button>
</form>

<br>

<table border="1">
    <tr>
        <th>Usuario</th>
        <th>Area</th>
        <th>Puesto</th>
        <th>Turno</th>
        <th>Rol</th>
        <th>Estado</th>
    </tr>

<%
List<Usuario> lista = (List<Usuario>) request.getAttribute("lista");

if (lista != null && !lista.isEmpty()) {
    for (Usuario u : lista) {
%>

<tr>
    <td><%= u.getUsuario() %></td>
    <td><%= u.getArea() %></td>
    <td><%= u.getPuesto() %></td>
    <td><%= u.getTurno() %></td>
    <td><%= u.getRol() %></td>
    <td><%= u.getEstado() %></td>
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

<br>
<a href="dashboard.jsp">Regresar</a>

</body>
</html>

