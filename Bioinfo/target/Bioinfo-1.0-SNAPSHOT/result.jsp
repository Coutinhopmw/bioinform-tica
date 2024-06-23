<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
    <title>Result Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="result.css" type="text/css"/>
    <script>
        function showInfo(action) {
            var infoDiv = document.getElementById("info");
            
            if (action === "time") {
                infoDiv.innerHTML = "<p>Informações de tempo...</p>";
            } else if (action === "score") {
                infoDiv.innerHTML = "<p>Informações de score...</p>";
            } else if (action === "gap") {
                infoDiv.innerHTML = "<p>Informações de gap...</p>";
            }
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Resultado</h2>
        <div>
            <%
                ArrayList<String> seqList = (ArrayList<String>) session.getAttribute("seqList");
                if (seqList != null && !seqList.isEmpty()) {
                    out.println("Resultado: ");
                    seqList.clear();
                } else {
                    out.println("Nenhum resultado encontrado.");
                }
            %>
        </div>
        
        <div class="button-container">
            <button type="button" onclick="showInfo('time')">Time</button>
            <button type="button" onclick="showInfo('score')">Score</button>
            <button type="button" onclick="showInfo('gap')">Gap</button>
        </div>
        
        <div id="info">
            <!-- Aqui serão inseridas as informações dinamicamente -->
        </div>
    </div>
</body>
</html>
