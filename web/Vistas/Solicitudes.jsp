<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
String usuario = (String) session.getAttribute("usuario");
String rol = (String) session.getAttribute("rol");

if (usuario == null) {
    response.sendRedirect("login.jsp");
    return;
}

if (rol == null || !rol.equals("Empleado")) {
    response.sendRedirect("dashboard.jsp");
    return;
}

String mensaje = request.getParameter("msj");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Solicitudes</title>

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <style>
        body{
            font-family:Arial, Helvetica, sans-serif;
            background:#f5f7fa;
            margin:0;
            padding:40px;
        }

        .contenedor{
            max-width:650px;
            margin:auto;
            background:white;
            padding:30px;
            border-radius:15px;
            box-shadow:0 0 15px rgba(0,0,0,0.12);
        }

        h1{
            text-align:center;
            margin-bottom:25px;
        }

        .campo{
            margin-bottom:15px;
        }

        label{
            display:block;
            font-weight:bold;
            margin-bottom:5px;
        }

        input, select, textarea{
            width:100%;
            padding:10px;
            border:1px solid #ccc;
            border-radius:8px;
            box-sizing:border-box;
        }

        .botones{
            margin-top:20px;
            display:flex;
            gap:10px;
        }

        .botones a,
        .botones button{
            flex:1;
            padding:10px;
            border:none;
            border-radius:8px;
            text-align:center;
            text-decoration:none;
            cursor:pointer;
            color:white;
            background:#0d6efd;
        }

        .botones a{
            background:#198754;
        }
    </style>
</head>

<body>

<div class="contenedor">

<h1>Solicitudes</h1>

<form action="<%= request.getContextPath() %>/Solicitudes" method="POST">

    <div class="campo">
        <label>Tipo de Solicitud</label>
        <select name="tipo_solicitud" required>
            <option value="Vacaciones">Vacaciones</option>
<<<<<<< HEAD
            <option value="PermisoPersonal">Permiso Personal</option>
            <option value="CitaAlIgss">Cita al IGSS</option>
            <option value="LicenciaDeCumpleanos">Licencia de Cumpleaños</option>
            <option value="SuspensionLaboral">Suspension Laboral</option>
=======
            <option value="Permiso Personal">Permiso Personal</option>
            <option value="Cita Al Igss">Cita al IGSS</option>
            <option value="Licencia De CumpleaÃ±os">Licencia de Cumple años</option>
            <option value="Suspension Laboral">Suspension Laboral</option>
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)
            <option value="Otros">Otros</option>
        </select>
    </div>

    <div class="campo">
        <label>Fecha de Inicio</label>
        <input type="date" name="FechaInicio" required>
    </div>

    <div class="campo">
        <label>Fecha Fin</label>
        <input type="date" name="FechaFin" required>
    </div>

    <div class="campo">
        <label>Motivo de la Solicitud</label>
        <textarea name="motivo" rows="4" required placeholder="Explique brevemente el motivo"></textarea>
    </div>

    <div class="botones">
        <a href="dashboard.jsp">Regresar</a>
        <button type="submit" name="accion" value="GuardarSolicitud">Guardar</button>
    </div>

</form>

</div>

<% if ("exito".equals(mensaje)) { %>
<script>
Swal.fire({
<<<<<<< HEAD
    title:'Operacion Exitosa',
=======
    title:'operacion exitosa',
>>>>>>> 1f1b84a (Jerarquia de solicitudes, informacion de las fechas en las solicitudes, conexiones y botones de regresar)
    text:'Solicitud enviada correctamente.',
    icon:'success',
    confirmButtonText:'Entendido'
});
</script>
<% } %>

<% if ("error".equals(mensaje)) { %>
<script>
Swal.fire({
    title:'Error',
    text:'No se pudo completar la solicitud.',
    icon:'error',
    confirmButtonText:'Aceptar'
});
</script>
<% } %>

</body>
</html>
