<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="ModeloDAO.UsuarioDAO" %>

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

UsuarioDAO daoRoles = new UsuarioDAO();
List<String[]> roles = daoRoles.listarRoles();
String ok = request.getParameter("ok");
String error = request.getParameter("error");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion de Roles</title>
    <style>
        body{font-family:Arial,Helvetica,sans-serif;background:#f5f7fa;margin:0;padding:40px;}
        .contenedor{max-width:760px;margin:auto;background:white;padding:28px;border-radius:12px;box-shadow:0 0 14px rgba(0,0,0,.12);}
        h1{text-align:center;margin-top:0;}
        form{border:1px solid #ddd;padding:18px;border-radius:10px;margin-top:18px;}
        label{display:block;font-weight:bold;margin-top:12px;}
        input,select{width:100%;padding:10px;border:1px solid #bbb;border-radius:8px;box-sizing:border-box;}
        button,.volver{display:inline-block;margin-top:18px;padding:10px 16px;border:0;border-radius:8px;background:#0d6efd;color:white;text-decoration:none;cursor:pointer;}
        .eliminar{background:#dc3545;}
        .volver{background:#6c757d;}
        .ok{color:#198754;text-align:center;font-weight:bold;}
        .error{color:#dc3545;text-align:center;font-weight:bold;}
    </style>
</head>
<body>
<div class="contenedor">
    <h1>Gestion de Roles</h1>

    <% if (ok != null) { %>
        <p class="ok">La operacion de rol fue exitosa</p>
    <% } %>

    <% if (error != null) { %>
        <p class="error">No se pudo completar la operacion</p>
    <% } %>

    <form action="<%= request.getContextPath() %>/RolServlet" method="post">
        <h2>Agregar Rol</h2>
        <label>Usuario</label>
        <input type="text" name="usuario" required>

        <label>Rol</label>
        <select name="rol_id" required>
            <% for (String[] rol : roles) { %>
                <option value="<%= rol[0] %>"><%= rol[1] %></option>
            <% } %>
        </select>

        <button type="submit" name="accion" value="agregar">Agregar</button>
    </form>

    <form action="<%= request.getContextPath() %>/RolServlet" method="post">
        <h2>Eliminar Rol</h2>
        <label>Usuario</label>
        <input type="text" name="usuario" required>

        <p></p>
        <button class="eliminar" type="submit" name="accion" value="eliminar">Eliminar</button>
    </form>

    <a class="volver" href="dashboard.jsp">Regresar</a>
</div>
</body>
</html>


