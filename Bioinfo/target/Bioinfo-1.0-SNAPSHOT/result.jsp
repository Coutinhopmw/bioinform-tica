<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="style.css" type="text/css"/>
    <title>Result Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
    <div class="container">
        <h2>Resultado</h2>
        <div>
            <%
                ArrayList<String> seqList = (ArrayList<String>) session.getAttribute("seqList");
                if (seqList != null && !seqList.isEmpty()) {
                    out.println("Resultado: " + String.join(", ", seqList));
                    seqList.clear();
                } else {
                    out.println("Nenhum resultado encontrado.");
                }
            %>
        </div>
    </div>
</body>
</html>