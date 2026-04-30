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
    max-width:900px;
    margin:auto;
    margin-top:40px;
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
th{
    background:#00bfff;
    color:black;
}
.msg-ok{color:#00ff99;text-align:center;}
.msg-error{color:red;text-align:center;}
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
ps = con.prepareStatement("SELECT id,nombre,area,puesto FROM usuarios WHERE estado='Activo'");
rs = ps.executeQuery();

while(rs.next()){
%>

<option value="<%=rs.getInt("id")%>"
data-area="<%=rs.getString("area")%>"
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
psTurnos = con.prepareStatement("SELECT id,nombre FROM turnos_catalogo");
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

<%
if(request.getParameter("ok") != null){
%>
<p class="msg-ok">Asignacion creada con exito</p>
<%
}

if("1".equals(request.getParameter("error"))){
%>
<p class="msg-error">Error en las fechas</p>
<%
}

if("duplicado".equals(request.getParameter("error"))){
%>
<p class="msg-error">El turno ya existe para ese empleado</p>
<%
}
%>

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
<th>Acciones</th>
</tr>

<%
String sql2 =
"SELECT a.id,u.nombre,u.area,u.puesto,a.fecha_inicio,a.fecha_fin,t.nombre AS turno "
+ "FROM asignacion_turnos a "
+ "INNER JOIN usuarios u ON a.usuario_id=u.id "
+ "INNER JOIN turnos_catalogo t ON a.turno_id=t.id";

ps2 = con.prepareStatement(sql2);
rs2 = ps2.executeQuery();

while(rs2.next()){
%>

<tr>
<td><%=rs2.getString("nombre")%></td>
<td><%=rs2.getString("area")%></td>
<td><%=rs2.getString("puesto")%></td>
<td><%=rs2.getString("fecha_inicio")%></td>
<td><%=rs2.getString("fecha_fin")%></td>
<td><%=rs2.getString("turno")%></td>
<td>
<a href="editarTurno.jsp?id=<%=rs2.getInt("id")%>">Editar</a> |
<a href="<%= request.getContextPath() %>/EliminarTurnoServlet?id=<%=rs2.getInt("id")%>"
onclick="return confirm('Â¿Eliminar turno?')">Eliminar</a>
</td>
</tr>

<% } %>

</table>

</div>

</div>

<script>
function mostrarDatos(select){
let option = select.options[select.selectedIndex];
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


