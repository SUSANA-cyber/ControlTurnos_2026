<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
String usuario = (String) session.getAttribute("usuario");
String rol = (String) session.getAttribute("rol");
Integer idUsuario = (Integer) session.getAttribute("id_usuario");

if (usuario == null || idUsuario == null) {
    response.sendRedirect("login.jsp");
    return;
}

if (rol == null) {
    rol = "";
}
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Menu Principal</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body{
            height:100vh;
            margin:0;
            background-color:white;
            background-image:url('<%= request.getContextPath() %>/456.png');
            background-repeat:no-repeat;
            background-position:center;
            background-size:contain;
            display:flex;
            justify-content:flex-end;
            align-items:center;
            padding-right:100px;
        }

        .menu-box{
            width:500px;
            padding:40px;
            border-radius:20px;
            background:rgba(0,0,0,0.75);
            border:2px solid #00bfff;
            box-shadow:0 0 20px #00bfff;
            text-align:center;
        }

        h1{
            color:#00bfff;
            margin-bottom:20px;
        }

        p{
            color:white;
            margin-bottom:25px;
        }

        .menu-btn{
            display:block;
            width:100%;
            margin:10px 0;
            padding:12px;
            text-decoration:none;
            font-weight:bold;
            color:#00bfff;
            border:2px solid #00bfff;
            border-radius:10px;
            transition:0.3s;
        }

        .menu-btn:hover{
            background:#00bfff;
            color:black;
        }

        .logout{
            color:red;
            border-color:red;
        }

        .logout:hover{
            background:red;
            color:white;
        }
    </style>
</head>

<body>

<div class="menu-box">

    <h1>Menu Principal</h1>

    <p>
        Bienvenido: <%= usuario %> | Rol: <%= rol %>
    </p>

    <% if ("AdminRRHH".equals(rol)) { %>

        <a href="<%= request.getContextPath() %>/Vistas/usuarios.jsp" class="menu-btn">Agregar Empleado</a>
        <a href="<%= request.getContextPath() %>/ConsultarUsuarioServlet" class="menu-btn">Consultar Usuarios</a>
        <a href="<%= request.getContextPath() %>/Vistas/gestionRoles.jsp" class="menu-btn">Gestion de Roles</a>
        <a href="<%= request.getContextPath() %>/Vistas/GstionSolicitudes.jsp" class="menu-btn">Solicitudes</a>

    <% } %>

    <% if ("AdminArea".equals(rol)) { %>

        <a href="<%= request.getContextPath() %>/Vistas/turnos.jsp" class="menu-btn">Asignacion de Turnos</a>
        <a href="<%= request.getContextPath() %>/Vistas/GstionSolicitudes.jsp" class="menu-btn">Solicitudes</a>

    <% } %>

    <% if ("Empleado".equals(rol)) { %>

        <a href="<%= request.getContextPath() %>/Vistas/marcaje.jsp" class="menu-btn">Marcaje</a>
        <a href="<%= request.getContextPath() %>/Vistas/Solicitudes.jsp" class="menu-btn">Solicitudes</a>
        <a href="<%= request.getContextPath() %>/Vistas/ActualizarTurno.jsp" class="menu-btn">Cambio De Turno</a>

    <% } %>

    <a href="<%= request.getContextPath() %>/LogoutServlet" class="menu-btn logout">
        Cerrar sesion
    </a>

</div>

</body>
</html>


