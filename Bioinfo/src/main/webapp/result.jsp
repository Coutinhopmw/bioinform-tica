<%@ page language="java" contentType="text/html; charset=UTF-8"pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Result Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="result.css" type="text/css"/>
</head>
<body>
    <div class="container">
        <h2>Resultados NW</h2>
        <table class="nw_table">
            <thead>
                <tr>
                    <th>Linguagem</th>
                    <th>Tempo</th>
                    <th>Score</th>
                    <th>Gap</th>
                    <th>e-value</th>
                </tr>
            </thead>
            <tbody>
                <c: forEach var="item" items="${NW_results}">
                <tr>
                    <td><c:out value="${item.linguagem}" /></td>
                    <td><c:out value="${item.tempo}" /></td>
                    <td><c:out value="${item.score}" /></td>
                    <td><c: out value="${item.gaps}"/></td>
                </tr>
                </c:forEach>
                <c:if test="${empty NW_results}">
                    <tr>
                        <td colspan="5">Nenhum resultado encontrado</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
        <h2>Resultados SW</h2>
        <table class="sw_table">
            <thead>
                <tr>
                    <th>Linguagem</th>
                    <th>Tempo</th>
                    <th>Score</th>
                    <th>Gap</th>
                    <th>e-value</th>
                </tr>
            </thead>
            <tbody>
                <c: forEach var="item" items="${SW_results}">
                <tr>
                    <td><c:out value="${item.linguagem}" /></td>
                    <td><c:out value="${item.tempo}" /></td>
                    <td><c:out value="${item.score}" /></td>
                    <td><c: out value="${item.gaps}"/></td>
                </tr>
                </c:forEach>
                <c:if test="${empty SW_results}">
                    <tr>
                        <td colspan="5">Nenhum resultado encontrado</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</body>
</html>