<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Usuario" %>
<<<<<<< HEAD

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

=======
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)
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
<<<<<<< HEAD

<div class="contenedor">
<h2>Consultar Usuario</h2>

=======
<h2>Consultar Usuarios</h2>
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)
<form action="<%= request.getContextPath() %>/ConsultarUsuarioServlet" method="get">
    <input type="text" name="buscar" placeholder="Buscar por nombre, usuario o DPI">
    <button type="submit">Buscar</button>
</form>
<<<<<<< HEAD

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
=======
<br>


<% if (request.getAttribute("mensaje") != null) { %>
    <div style="background-color: #d4edda; color: #155724; padding: 15px; border: 1px solid #c3e6cb; border-radius: 5px; margin-bottom: 20px;">
        <strong>¡Hecho!</strong> <%= request.getAttribute("mensaje") %>
    </div>
<% } %>


<% if (request.getAttribute("error") != null) { %>
    <div style="background-color: #f8d7da; color: #721c24; padding: 15px; border: 1px solid #f5c6cb; border-radius: 5px; margin-bottom: 20px;">
        <strong>Error:</strong> <%= request.getAttribute("error") %>
    </div>
<% } %>
<table border="1" style="width:100%; text-align:center;">
    <thead>
        <tr style="background-color: #f2f2f2;">
            <th>Usuario</th>
            <th>Área</th>
            <th>Puesto</th>
            <th>Turno</th>
            <th>Rol</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
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
        <td>
            <span style="color: <%= u.getEstado().equals("Activo") ? "green" : "red" %>; font-weight: bold;">
                <%= u.getEstado() %>
            </span>
        </td>
        <td>
            <% if ("Activo".equals(u.getEstado())) { %>
                <button type="button" onclick="prepararInactivar('<%= u.getId_usuario() %>', '<%= u.getDpi() %>')">
                    Inactivar
                </button>
            <% } else { %>
                <form action="ConsultarUsuarioServlet" method="post" style="display:inline;">
                    <input type="hidden" name="id_usuario" value="<%= u.getId_usuario() %>">
                    <input type="hidden" name="dpi" value="<%= u.getDpi() %>">
                    <input type="hidden" name="accion" value="activar">
                    <button type="submit" onclick="return confirm('¿Desea reactivar al empleado <%= u.getUsuario() %>?')">
                        Activar
                    </button>
                </form>
            <% } %>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <tr><td colspan="7">No se encontraron empleados</td></tr>
    <% } %>
    </tbody>
</table>
<br>
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)

<div id="modalMotivo" style="display:none; position:fixed; top:20%; left:30%; background:#eee; padding:20px; border:2px solid #000;">
    <h3>Inactivar Empleado</h3>
    <form action="ConsultarUsuarioServlet" method="post">
        <input type="hidden" name="id_usuario" id="modal_id">
        <input type="hidden" name="dpi" id="modal_dpi">
        
        <label>Seleccione el motivo:</label><br>
        <select name="motivo" required>
              <option value="1">vacaciones</option>
              <option value="2">permiso personal</option>
              <option value="3">cita al Igss</option>
              <option value="3">Licencia de Cumpleaños</option>
              <option value="3">Suspension Laboral</option>
              <option value="3">otros</option>

    
         </select><br><br>
        
        <button type="submit" name="accion" value="inactivar">Aceptar</button>
        <button type="button" onclick="cerrarModal()">Cancelar</button>
    </form>
</div>

<script>
function prepararInactivar(id, dpi) {
    document.getElementById('modal_id').value = id;
    document.getElementById('modal_dpi').value = dpi;
    document.getElementById('modalMotivo').style.display = 'block';
}
function cerrarModal() {
    document.getElementById('modalMotivo').style.display = 'none';
}
</script>
<a href="<%= request.getContextPath() %>/Vistas/dashboard.jsp" class="btn-regresar">
    Regresar
</a>
</body>
</html>
<<<<<<< HEAD
=======
 
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)
