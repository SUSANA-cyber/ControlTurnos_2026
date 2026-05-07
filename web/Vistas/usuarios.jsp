<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.*" %>
<%@ page import="Config.Conexion" %>
<%@ page import="ModeloDAO.AreaDAO" %>

<%
String usuarioSesion = (String) session.getAttribute("usuario");
String rolSesion = (String) session.getAttribute("rol");

if (usuarioSesion == null) {
    response.sendRedirect("login.jsp");
    return;
}

if (!"AdminRRHH".equals(rolSesion)) {
    response.sendRedirect("dashboard.jsp");
    return;
}

String ok = request.getParameter("ok");
String error = request.getParameter("error");
String areaok = request.getParameter("areaok");
String areaerror = request.getParameter("areaerror");

AreaDAO areaDAO = new AreaDAO();
List<String[]> areas = areaDAO.listarAreas();

Connection conAdmins = null;
PreparedStatement psAdmins = null;
ResultSet rsAdmins = null;
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mantenimiento de Usuarios</title>
    <style>
        body{
            font-family:Arial, Helvetica, sans-serif;
            background:#f4f6f9;
            margin:0;
            padding:30px;
        }
        .contenedor{
            max-width:900px;
            margin:auto;
            background:white;
            padding:30px;
            border-radius:14px;
            box-shadow:0 0 14px rgba(0,0,0,0.10);
        }
        h1,h2{
            text-align:center;
        }
        label{
            font-weight:bold;
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
            background:#0d6efd;
            color:white;
            border:none;
            padding:12px 18px;
            border-radius:8px;
            cursor:pointer;
            width:100%;
        }
        button:hover{
            background:#0b5ed7;
        }
        .mensaje-ok{
            color:green;
            text-align:center;
            font-weight:bold;
        }
        .mensaje-error{
            color:red;
            text-align:center;
            font-weight:bold;
        }
        .regresar{
            display:inline-block;
            margin-top:20px;
            padding:10px 18px;
            background:#6c757d;
            color:white;
            text-decoration:none;
            border-radius:8px;
        }
        .seccion{
            margin-top:30px;
            padding-top:20px;
            border-top:1px solid #ddd;
        }
    </style>
</head>
<body>

<div class="contenedor">
    <h1>Mantenimiento de Usuarios</h1>

    <% if (ok != null) { %>
        <p class="mensaje-ok">Se creó correctamente</p>
    <% } %>

    <% if ("duplicado".equals(error)) { %>
        <p class="mensaje-error">Error: Ya existe un empleado con ese usuario o DPI</p>
    <% } else if ("dpi".equals(error)) { %>
        <p class="mensaje-error">Error: El DPI solo debe contener números</p>
    <% } else if (error != null) { %>
        <p class="mensaje-error">Error: Ha ocurrido un error al registrar el empleado</p>
    <% } %>

    <% if (areaok != null) { %>
        <p class="mensaje-ok">Área creada correctamente</p>
    <% } %>

    <% if (areaerror != null) { %>
        <p class="mensaje-error">Error al crear el área</p>
    <% } %>

    <div class="seccion">
        <h2>Agregar Nueva Área</h2>
        <form action="<%= request.getContextPath() %>/AreaServlet" method="post">
            <label>Nombre del área</label>
            <input type="text" name="nombre_area" required>
            <button type="submit">Guardar Área</button>
        </form>
    </div>

    <div class="seccion">
        <h2>Agregar Empleado</h2>

        <form action="<%= request.getContextPath() %>/UsuarioServlet" method="post">
            <label>No. DPI empleado</label>
            <input type="text"
                   name="dpi"
                   id="dpi"
                   required
                   inputmode="numeric"
                   pattern="[0-9]+"
                   title="El DPI solo debe contener números"
                   oninput="this.value = this.value.replace(/[^0-9]/g, '')">

            <label>Nombre Completo</label>
            <input type="text" name="nombre" required>

            <label>Usuario</label>
            <input type="text" name="usuario" required>

            <label>Área</label>
            <select name="area_id" id="area_id" required onchange="filtrarAdministradores()">
                <option value="">Seleccione un área</option>
                <%
                for (String[] area : areas) {
                %>
                    <option value="<%= area[0] %>"><%= area[1] %></option>
                <%
                }
                %>
            </select>

            <input type="hidden" name="area_texto" id="area_texto">

            <label>Puesto</label>
            <input type="text" name="puesto">

            <label>Administrador responsable</label>
            <select name="admin_responsable_id" id="admin_responsable_id">
                <option value="">Seleccione un administrador</option>
                <%
                try {
                    conAdmins = Conexion.getConexion();
                    psAdmins = conAdmins.prepareStatement(
                        "SELECT u.id, u.nombre, u.area_id, COALESCE(a.nombre, u.area) AS area_nombre " +
                        "FROM usuarios u " +
                        "LEFT JOIN areas a ON u.area_id = a.id " +
                        "WHERE u.rol_id = 2 AND u.estado = 'Activo' " +
                        "ORDER BY a.nombre, u.nombre"
                    );
                    rsAdmins = psAdmins.executeQuery();

                    while (rsAdmins.next()) {
                %>
                    <option value="<%= rsAdmins.getInt("id") %>"
                            data-area-id="<%= rsAdmins.getInt("area_id") %>">
                        <%= rsAdmins.getString("nombre") %> - <%= rsAdmins.getString("area_nombre") %>
                    </option>
                <%
                    }
                } catch(Exception e) {
                %>
                    <option value="">No se pudieron cargar administradores</option>
                <%
                } finally {
                    try { if(rsAdmins != null) rsAdmins.close(); } catch(Exception e){}
                    try { if(psAdmins != null) psAdmins.close(); } catch(Exception e){}
                    try { if(conAdmins != null) conAdmins.close(); } catch(Exception e){}
                }
                %>
            </select>

            <label>Correo</label>
            <input type="email" name="correo" required>

            <label>Contraseña</label>
            <input type="password" name="password" required>

            <label>Turno</label>
            <select name="turno_actual_id" required>
                <option value="1">Matutino</option>
                <option value="2">Vespertino</option>
                <option value="4">Diurno</option>
            </select>

            <label>Estado</label>
            <select name="estado" id="estado" onchange="toggleMotivo()" required>
                <option value="Activo">Activo</option>
                <option value="Inactivo">Inactivo</option>
            </select>

            <div id="bloqueMotivo" style="display:none;">
                <label>Motivo Inactividad</label>
                <select name="motivo_inactivo_id" id="motivo_inactivo_id">
                    <option value="">Seleccione un motivo</option>
                    <option value="2">Permiso Personal</option>
                    <option value="1">Vacaciones</option>
                    <option value="3">Citas al IGSS</option>
                    <option value="4">Licencia de Cumpleaños</option>
                    <option value="5">Suspensión Laboral</option>
                    <option value="6">Otros</option>
                </select>
            </div>

            <label>Rol</label>
            <select name="rol_id" required>
                <option value="3">Empleado</option>
                <option value="2">AdminArea</option>
                <option value="1">AdminRRHH</option>
            </select>

            <button type="submit">Registrar</button>
        </form>
    </div>

    <a href="dashboard.jsp" class="regresar">Regresar</a>
</div>

<script>
function toggleMotivo() {
    var estado = document.getElementById("estado").value;
    var bloque = document.getElementById("bloqueMotivo");
    var motivo = document.getElementById("motivo_inactivo_id");

    if (estado === "Inactivo") {
        bloque.style.display = "block";
        motivo.setAttribute("required", "required");
    } else {
        bloque.style.display = "none";
        motivo.removeAttribute("required");
        motivo.value = "";
    }
}

function filtrarAdministradores() {
    var areaSelect = document.getElementById("area_id");
    var adminSelect = document.getElementById("admin_responsable_id");
    var areaTexto = document.getElementById("area_texto");
    var areaId = areaSelect.value;

    if (areaSelect.selectedIndex >= 0) {
        areaTexto.value = areaSelect.options[areaSelect.selectedIndex].text;
    }

    adminSelect.value = "";

    for (var i = 0; i < adminSelect.options.length; i++) {
        var option = adminSelect.options[i];

        if (option.value === "") {
            option.hidden = false;
            option.disabled = false;
        } else if (option.getAttribute("data-area-id") === areaId) {
            option.hidden = false;
            option.disabled = false;
        } else {
            option.hidden = true;
            option.disabled = true;
        }
    }
}

toggleMotivo();
filtrarAdministradores();
</script>

</body>
</html>