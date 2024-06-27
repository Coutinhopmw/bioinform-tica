<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="css/indexF.css" type="text/css"/>
    <title>Start Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
        function handleSubmit(action) {
            if (action === 'resultado') {
                document.getElementById('seq').disabled = true;
                document.getElementById('seq').placeholder = "Aguarde enguanto a sequência é processada...";
                document.getElementById('adicionar').style.display = 'none';
                document.getElementById('resultado').style.display = 'none';
                document.getElementById('loading').style.display = 'block';
            }
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Formulário de Entrada</h2>
        <form action="linkstart" method="post" onsubmit="handleSubmit(document.activeElement.name)">
            <input type="text" id="seq" placeholder="Digite a sequência..." name="seq">
            <br>
            <input type="submit" id="adicionar" value="Adicionar" name="adicionar"/>
            <input type="submit" id="resultado" value="Resultado" name="resultado"/>
            <img id="loading" src="loading.gif" style="display:none;" alt="Loading...">
        </form>
    </div>
</body>
</html>