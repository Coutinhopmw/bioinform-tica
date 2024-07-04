<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="indexF.css" type="text/css"/>
    <title>Start Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
        function handleSubmit() {
            document.getElementById('form').style.display = 'none';
            document.getElementById('wait').style.display = 'block';
            document.getElementById('loading').style.display = 'block';
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Formulário de Entrada</h2>
        <form id="form" action="linkstart" method="post" onsubmit="handleSubmit()">
            <input type="text" id="seq1" placeholder="Digite a primeira sequência..." name="seq1">
            <br>
            <input type="text" id="seq2" placeholder="Digite a segunda sequência..." name="seq2">
            <input type="submit" id="resultado" value="Resultado" name="resultado"/>
        </form>
        <img id="loading" src="loading.gif" style="display:none;" alt="Loading...">
        <p id="wait" style="display: none;">Aguarde enguanto a sequência é processada...</p>
    </div>
</body>
</html>
