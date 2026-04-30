<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
String error = request.getParameter("error");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body{
            height:100vh;
            margin:0;
            background-color:white;
            background-image:url('<%= request.getContextPath() %>/123.png');
            background-repeat:no-repeat;
            background-position:center;
            background-size:contain;
        }

        .contenedor{
            height:100vh;
            display:flex;
            justify-content:flex-end;
            align-items:center;
            padding-right:80px;
        }

        .card-login{
            width:500px;
            border-radius:20px;
            background:rgba(0,0,0,0.7);
            backdrop-filter:blur(10px);
            border:2px solid #00bfff;
            box-shadow:0 0 20px rgba(0,191,255,0.6);
            padding:30px;
        }

        .titulo{
            color:#00bfff;
            text-align:right;
            font-weight:bold;
            font-size:36px;
            margin-bottom:20px;
            background:rgba(0,0,0,0.6);
            padding:10px;
            border-radius:10px;
        }

        label{
            color:#ddd;
        }

        .form-control{
            background:rgba(255,255,255,0.1);
            color:white;
            border:1px solid #00bfff;
        }

        .form-control:focus{
            box-shadow:0 0 10px #00bfff;
        }

        .btn-login{
            background:transparent;
            border:2px solid #00bfff;
            color:#00bfff;
            font-weight:bold;
            width:100%;
        }

        .btn-login:hover{
            background:#00bfff;
            color:black;
        }
    </style>
</head>

<body>

<div class="contenedor">

    <div>

        <h2 class="titulo">Sistema Control de Turnos</h2>

        <div class="card-login">

            <h4 class="text-center text-light mb-4">
                Inicio de Sesion
            </h4>

            <form action="<%= request.getContextPath() %>/LoginServlet" method="post">

                <div class="mb-3">
                    <label>Usuario</label>
                    <input type="text" name="usuario" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label>Contraseña</label>
                    <input type="password" name="password" class="form-control" required>
                </div>

                <div class="mt-4">
                    <input type="submit" value="Ingresar" class="btn btn-login">
                </div>

            </form>

            <% if (error != null) { %>

                <div class="alert alert-danger mt-3 text-center">
                    Credenciales incorrectas o usuario inactivo
                </div>

            <% } %>

        </div>

    </div>

</div>

</body>
</html>

