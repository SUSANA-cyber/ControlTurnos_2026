<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="Config.Conexion" %>

<%
Integer idUsuario = (Integer) session.getAttribute("id_usuario");
String rol = (String) session.getAttribute("rol");

if (idUsuario == null) {
    response.sendRedirect("login.jsp");
    return;
}

if (rol == null || !rol.equals("Empleado")) {
    response.sendRedirect("dashboard.jsp");
    return;
}

Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Marcaje</title>

<style>
body {
    font-family: Arial;
    text-align: center;
    background:#f4f7fb;
    margin:0;
    padding:30px;
}
.reloj {
    font-size: 40px;
    font-weight: bold;
    margin: 20px;
}
button {
    padding: 12px 22px;
    margin: 5px;
    background: #2d89ef;
    color: white;
    border: none;
    border-radius: 5px;
    cursor:pointer;
}
button:hover{
    background:#1c6fd1;
}
.panel{
    max-width:900px;
    margin:auto;
    background:white;
    padding:25px;
    border-radius:14px;
    box-shadow:0 0 14px rgba(0,0,0,0.1);
}
.tabla{
    width:100%;
    border-collapse:collapse;
    margin-top:20px;
}
.tabla th, .tabla td{
    border:1px solid #ccc;
    padding:10px;
}
.tabla th{
    background:#2d89ef;
    color:white;
}
.btn-regresar{
    display:inline-block;
    margin-top:20px;
    background:#6c757d;
    color:white;
    padding:10px 18px;
    text-decoration:none;
    border-radius:6px;
}
</style>
</head>

<body>

<div class="panel">
    <h2>MARCAJE</h2>

    <div class="reloj" id="reloj"></div>

    <%
    String msg = request.getParameter("msg");
    if(msg != null){
    %>
    <p style="color:red;"><b><%= msg %></b></p>
    <%
    }
    %>

    <form action="<%= request.getContextPath() %>/MarcajeServlet" method="post">
        <button type="submit" name="accion" value="entrada">Marcar Entrada</button>
        <button type="submit" name="accion" value="descanso1">Marcar Descanso 1</button>
        <button type="submit" name="accion" value="descanso2">Marcar Descanso 2</button>
        <button type="submit" name="accion" value="salida">Marcar Salida</button>
    </form>

    <h3>Informacion del Marcaje</h3>

    <table class="tabla">
        <tr>
            <th>Fecha</th>
            <th>Hora Entrada</th>
            <th>Entrada Tarde</th>
            <th>Descanso 1</th>
            <th>Descanso 2</th>
            <th>Salida</th>
        </tr>

        <%
        try{
            con = Conexion.getConexion();
            ps = con.prepareStatement("SELECT * FROM marcajes WHERE usuario_id=? AND fecha=CURDATE()");
            ps.setInt(1, idUsuario.intValue());
            rs = ps.executeQuery();

            if(rs.next()){
        %>
        <tr>
            <td><%= rs.getDate("fecha") %></td>
            <td><%= rs.getTime("hora_entrada") != null ? rs.getTime("hora_entrada") : "--" %></td>
            <td><%= rs.getBoolean("entrada_tarde") ? "Si" : "No" %></td>
            <td><%= rs.getTime("hora_descanso1") != null ? rs.getTime("hora_descanso1") : "--" %></td>
            <td><%= rs.getTime("hora_descanso2") != null ? rs.getTime("hora_descanso2") : "--" %></td>
            <td><%= rs.getTime("hora_salida") != null ? rs.getTime("hora_salida") : "--" %></td>
        </tr>
        <%
            } else {
        %>
        <tr>
            <td colspan="6">Aun no hay marcajes hoy</td>
        </tr>
        <%
            }
        } catch(Exception e){
        %>
        <tr>
            <td colspan="6">Error al cargar informacion del marcaje</td>
        </tr>
        <%
        } finally {
            try { if(rs != null) rs.close(); } catch(Exception e){}
            try { if(ps != null) ps.close(); } catch(Exception e){}
            try { if(con != null) con.close(); } catch(Exception e){}
        }
        %>
    </table>

    <a href="dashboard.jsp" class="btn-regresar">Regresar</a>
</div>

<script>
function actualizarReloj() {
    const ahora = new Date();
    let h = String(ahora.getHours()).padStart(2, '0');
    let m = String(ahora.getMinutes()).padStart(2, '0');
    let s = String(ahora.getSeconds()).padStart(2, '0');
    document.getElementById("reloj").innerHTML = h + ":" + m + ":" + s;
}
setInterval(actualizarReloj, 1000);
actualizarReloj();
</script>

</body>
</html>
