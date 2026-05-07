<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="Config.Conexion" %>

<%
Connection con = null;
PreparedStatement ps = null;
PreparedStatement psTurnos = null;
PreparedStatement ps2 = null;
ResultSet rs = null;
ResultSet rsTurnos = null;
ResultSet rs2 = null;

try{
    String usuarioSesion = (String) session.getAttribute("usuario");
    String rolSesion = (String) session.getAttribute("rol");
    Integer adminId = (Integer) session.getAttribute("id_usuario");

    if (usuarioSesion == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    if (rolSesion == null || !rolSesion.equals("AdminArea")) {
        response.sendRedirect("dashboard.jsp");
        return;
    }

    con = Conexion.getConexion();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Asignacion de Turnos</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body{
    background:linear-gradient(135deg,#0f2027,#203a43,#2c5364);
    min-height:100vh;
    color:white;
}
.container-box{
    max-width:950px;
    margin:auto;
    margin-top:40px;
    padding-bottom:40px;
}
.card-box{
    background:rgba(0,0,0,0.75);
    border-radius:15px;
    padding:25px;
    box-shadow:0 0 20px rgba(0,191,255,0.4);
    margin-bottom:25px;
}
h2,h3{
    text-align:center;
    color:#00bfff;
}
label{
    margin-top:10px;
    font-weight:bold;
    color:#00e6ff;
}
.form-control,select{
    background:rgba(255,255,255,0.15);
    color:white;
    border:1px solid #00bfff;
}
button{
    margin-top:15px;
    width:100%;
    border:2px solid #00bfff;
    background:transparent;
    color:#00bfff;
    padding:10px;
    border-radius:10px;
    font-weight:bold;
}
button:hover{
    background:#00bfff;
    color:black;
}
th{
    background:#00bfff !important;
    color:black !important;
}
.msg-ok{color:#00ff99;text-align:center;font-weight:bold;}
.msg-error{color:#ff8080;text-align:center;font-weight:bold;}
.acciones a{
    color:#7dd3fc;
    text-decoration:none;
    font-weight:bold;
}
.acciones a:hover{
    text-decoration:underline;
}
.btn-regresar{
    display:inline-block;
    margin-top:15px;
    padding:10px 18px;
    border-radius:10px;
    background:#6c757d;
    color:white;
    text-decoration:none;
}
.btn-regresar:hover{
    background:#5a6268;
    color:white;
}
</style>
</head>

<body>

<div class="container-box">

<div class="card-box">

<h2>Asignacion de Turnos</h2>

<form action="<%= request.getContextPath() %>/TurnoServlet" method="post">

<label>Empleado:</label>
<select name="usuario_id" class="form-control" required onchange="mostrarDatos(this)">
<option value="">Seleccione un empleado</option>

<%
ps = con.prepareStatement(
    "SELECT u.id, u.nombre, u.puesto, COALESCE(a.nombre, u.area) AS area_nombre " +
    "FROM usuarios u " +
    "LEFT JOIN areas a ON u.area_id = a.id " +
    "WHERE u.estado='Activo' " +
    "AND u.rol_id = 3 " +
    "AND u.admin_responsable_id = ? " +
    "ORDER BY u.nombre"
);
ps.setInt(1, adminId.intValue());
rs = ps.executeQuery();

while(rs.next()){
%>
<option value="<%=rs.getInt("id")%>"
data-area="<%=rs.getString("area_nombre")%>"
data-puesto="<%=rs.getString("puesto")%>">
<%=rs.getString("nombre")%>
</option>
<% } %>

</select>

<label>Area:</label>
<input type="text" id="area" class="form-control" readonly>

<label>Puesto:</label>
<input type="text" id="puesto" class="form-control" readonly>

<label>Fecha inicio:</label>
<input type="date" name="fecha_inicio" class="form-control" required>

<label>Fecha fin:</label>
<input type="date" name="fecha_fin" class="form-control" required>

<label>Turno:</label>
<select name="turno_id" class="form-control" required>
<option value="">Seleccione</option>

<%
psTurnos = con.prepareStatement("SELECT id,nombre FROM turnos_catalogo ORDER BY id");
rsTurnos = psTurnos.executeQuery();

while(rsTurnos.next()){
%>
<option value="<%=rsTurnos.getInt("id")%>">
<%=rsTurnos.getString("nombre")%>
</option>
<% } %>
</select>

<button type="submit">Guardar</button>

</form>

<% if(request.getParameter("ok") != null){ %>
<p class="msg-ok">Asignacion creada con exito</p>
<% } %>

<% if("1".equals(request.getParameter("error"))){ %>
<p class="msg-error">La fecha de inicio no puede ser mayor a la fecha fin</p>
<% } %>

<% if("asignado".equals(request.getParameter("error"))){ %>
<p class="msg-error">El empleado ya tiene un turno asignado en ese rango de fechas</p>
<% } %>

<% if("horas".equals(request.getParameter("error"))){ %>
<p class="msg-error">Solo se pueden asignar turnos de 8 horas</p>
<% } %>

<% if("noautorizado".equals(request.getParameter("error"))){ %>
<p class="msg-error">No puedes gestionar ese empleado</p>
<% } %>

<% if("2".equals(request.getParameter("error"))){ %>
<p class="msg-error">Ocurrio un error al guardar la asignacion</p>
<% } %>

<a href="dashboard.jsp" class="btn-regresar">Regresar</a>

</div>

<div class="card-box">

<h3>Turnos asignados</h3>

<table class="table table-dark table-bordered table-hover">
<tr>
<th>Empleado</th>
<th>Area</th>
<th>Puesto</th>
<th>Inicio</th>
<th>Fin</th>
<th>Turno</th>
<th>Estado</th>
<th>Acciones</th>
</tr>

<%
String sql2 =
"SELECT at.id, u.nombre, COALESCE(ar.nombre, u.area) AS area_nombre, u.puesto, " +
"at.fecha_inicio, at.fecha_fin, at.estado, tc.nombre AS turno " +
"FROM asignacion_turnos at " +
"INNER JOIN usuarios u ON at.usuario_id = u.id " +
"INNER JOIN turnos_catalogo tc ON at.turno_id = tc.id " +
"LEFT JOIN areas ar ON u.area_id = ar.id " +
"WHERE u.admin_responsable_id = ? " +
"ORDER BY at.fecha_inicio DESC";

ps2 = con.prepareStatement(sql2);
ps2.setInt(1, adminId.intValue());
rs2 = ps2.executeQuery();

while(rs2.next()){
%>

<tr>
<td><%=rs2.getString("nombre")%></td>
<td><%=rs2.getString("area_nombre")%></td>
<td><%=rs2.getString("puesto")%></td>
<td><%=rs2.getString("fecha_inicio")%></td>
<td><%=rs2.getString("fecha_fin")%></td>
<td><%=rs2.getString("turno")%></td>
<td><%=rs2.getString("estado")%></td>
<td class="acciones">
<a href="editarTurno.jsp?id=<%=rs2.getInt("id")%>">Editar</a> |
<a href="<%= request.getContextPath() %>/EliminarTurnoServlet?id=<%=rs2.getInt("id")%>"
onclick="return confirm('¿Eliminar turno?')">Eliminar</a>
</td>
</tr>

<% } %>

</table>

</div>

</div>

<script>
function mostrarDatos(select){
    var option = select.options[select.selectedIndex];
    document.getElementById("area").value = option.getAttribute("data-area") || "";
    document.getElementById("puesto").value = option.getAttribute("data-puesto") || "";
}
</script>

</body>
</html>

<%
}catch(Exception e){
    out.print("Error: " + e.getMessage());
}finally{
    try{ if(rs!=null) rs.close(); }catch(Exception e){}
    try{ if(ps!=null) ps.close(); }catch(Exception e){}
    try{ if(rsTurnos!=null) rsTurnos.close(); }catch(Exception e){}
    try{ if(psTurnos!=null) psTurnos.close(); }catch(Exception e){}
    try{ if(rs2!=null) rs2.close(); }catch(Exception e){}
    try{ if(ps2!=null) ps2.close(); }catch(Exception e){}
    try{ if(con!=null) con.close(); }catch(Exception e){}
}
%>