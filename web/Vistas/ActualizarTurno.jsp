<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
String usuario = (String) session.getAttribute("usuario");

if (usuario == null) {
    response.sendRedirect("login.jsp");
    return;
}

String mensaje = request.getParameter("msj");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cambio de Turno</title>

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <style>
        body{
            font-family:Arial, Helvetica, sans-serif;
            background:#f4f6f9;
            margin:0;
            padding:40px;
        }

        .contenedor{
            max-width:600px;
            margin:auto;
            background:white;
            padding:30px;
            border-radius:15px;
            box-shadow:0 0 15px rgba(0,0,0,0.15);
        }

        h2{
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

        .botones button,
        .botones a{
            flex:1;
            padding:10px;
            text-align:center;
            text-decoration:none;
            border:none;
            border-radius:8px;
            cursor:pointer;
            background:#0d6efd;
            color:white;
        }

        .botones button[type="reset"]{
            background:#6c757d;
        }

        .botones a{
            background:#198754;
        }
    </style>
</head>

<body>

<div class="contenedor">

    <h2>Solicitud de Cambio de Turno</h2>

    <form action="<%= request.getContextPath() %>/CambioDeTurno" method="POST">

        <div class="campo">
            <label>Fecha de Inicio</label>
            <input type="date" name="FechaInicio" required>
        </div>

        <div class="campo">
            <label>Turno Inicial</label>
            <select name="InicialTurno" required>
                <option value="Matutino">Matutino</option>
                <option value="Vespertino">Vespertino</option>
                <option value="Diurno">Diurno</option>
            </select>
        </div>

        <div class="campo">
            <label>Nueva Fecha</label>
            <input type="date" name="Nuevafecha" required>
        </div>

        <div class="campo">
            <label>Nuevo Turno</label>
            <select name="NuevoTurno" required>
                <option value="Matutino">Matutino</option>
                <option value="Vespertino">Vespertino</option>
                <option value="Diurno">Diurno</option>
            </select>
        </div>

        <div class="campo">
            <label>Motivo de la Solicitud</label>
            <textarea name="motivo" rows="4" required
            placeholder="Explique brevemente el motivo"></textarea>
        </div>

        <div class="botones">
            <a href="dashboard.jsp">Regresar</a>
            <button type="submit">Guardar</button>
            <button type="reset">Limpiar</button>
        </div>

    </form>

</div>

<% if ("exito".equals(mensaje)) { %>

<script>
Swal.fire({
    title: Operacion Exitosa!',
    text: 'La solicitud fue enviada correctamente.',
    icon: 'success',
    confirmButtonText: 'Entendido'
});
</script>

<% } %>

<% if ("error".equals(mensaje)) { %>

<script>
Swal.fire({
    title: 'Error',
    text: 'No se pudo completar la solicitud.',
    icon: 'error',
    confirmButtonText: 'Aceptar'
});
</script>

<% } %>

</body>
</html>

