<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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

String ok = request.getParameter("ok");
String error = request.getParameter("error");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Usuarios</title>
</head>
<body>

    <h1>Registrar Empleado</h1>

    <% if (ok != null) { %>
        <p style="color:green;">Usuario registrado correctamente</p>
    <% } %>

    <% if (error != null) { %>
        <p style="color:red;">Error al registrar usuario</p>
    <% } %>

    <form action="<%= request.getContextPath() %>/UsuarioServlet" method="post">

        <label>DPI</label><br>
        <input type="text" name="dpi"><br><br>

        <label>Nombre</label><br>
        <input type="text" name="nombre"><br><br>

        <label>Usuario</label><br>
        <input type="text" name="usuario"><br><br>

        <label>Area</label><br>
        <input type="text" name="area"><br><br>

        <label>Puesto</label><br>
        <input type="text" name="puesto"><br><br>

        <label>Correo</label><br>
        <input type="text" name="correo"><br><br>

        <label>Contraseña</label><br>
        <input type="password" name="password"><br><br>

        <label>Turno</label><br>
        <select name="turno_actual_id">
            <option value="1">Matutino</option>
            <option value="2">Vespertino</option>
            <option value="3">Nocturno</option>
        </select><br><br>

        <label>Estado</label><br>
        <select name="estado">
            <option value="Activo">Activo</option>
            <option value="Inactivo">Inactivo</option>
        </select><br><br>

        <label>Motivo Inactividad</label><br>
        <select name="motivo_inactivo_id">
            <option value="">-- Ninguno --</option>
            <option value="1">Vacaciones</option>
            <option value="2">Permiso Personal</option>
            <option value="3">IGSS</option>
            <option value="4">Licencia de Cumple años</option>
            <option value="5">Suspension Laboral</option>
            <option value="6">Otros</option>
        </select><br><br>

        <label>Rol</label><br>
        <select name="rol_id">
            <option value="1">AdminRRHH</option>
            <option value="2">AdminArea</option>
            <option value="3">Empleado</option>
        </select><br><br>

        <button type="submit">Registrar</button>

    </form>

    <br>
    <a href="dashboard.jsp">Regresar</a>

</body>
</html>


