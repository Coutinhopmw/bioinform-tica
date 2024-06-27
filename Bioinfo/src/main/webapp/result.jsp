<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Result Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/resultF.css" type="text/css"/>
    <script>
        function showSection(sectionId) {
            document.getElementById('NWSection').style.display = 'none';
            document.getElementById('SWSection').style.display = 'none';
            document.getElementById(sectionId).style.display = 'block';
        }
    </script>
    <%! 
    // Recuperando os atributos do request
    String veryfy(String file) {
        if (file.startsWith("resultado_c_plus")) {
            return "C++";
        } else if (file.startsWith("resultado_c_sharp")) {
            return "C#";
        } else if (file.startsWith("resultado_c.")) {
            return "C";
        } else if (file.startsWith("resultado_java")) {
            return "Java";
        } else if (file.startsWith("resultado_perl")) {
            return "Perl";
        } else if (file.startsWith("resultado_php")) {
            return "PHP";
        } else {
            return "Python";
        }
    }
    %>
    <%
        String NWbestTimeLenguage = (String) request.getAttribute("NWbestTimeLenguage");
        NWbestTimeLenguage = veryfy(NWbestTimeLenguage);
        String NWbestTime = (String) request.getAttribute("NWbestTime");

        String NWbestScoreLenguage = (String) request.getAttribute("NWbestScoreLenguage");
        NWbestScoreLenguage = veryfy(NWbestScoreLenguage);
        String NWbestScore = (String) request.getAttribute("NWbestScore");

        String NWbestGapLenguage = (String) request.getAttribute("NWbestGapLenguage");
        NWbestGapLenguage = veryfy(NWbestGapLenguage);
        String NWbestGap = (String) request.getAttribute("NWbestGap");

        String NWbestLineLenguage = (String) request.getAttribute("NWbestLineLenguage");
        NWbestLineLenguage = veryfy(NWbestLineLenguage);
        String NWbestLine = (String) request.getAttribute("NWbestLine");

        String SWbestTimeLenguage = (String) request.getAttribute("SWbestTimeLenguage");
        SWbestTimeLenguage = veryfy(SWbestTimeLenguage);
        String SWbestTime = (String) request.getAttribute("SWbestTime");

        String SWbestScoreLenguage = (String) request.getAttribute("SWbestScoreLenguage");
        SWbestScoreLenguage = veryfy(SWbestScoreLenguage);
        String SWbestScore = (String) request.getAttribute("SWbestScore");

        String SWbestGapLenguage = (String) request.getAttribute("SWbestGapLenguage");
        SWbestGapLenguage = veryfy(SWbestGapLenguage);
        String SWbestGap = (String) request.getAttribute("SWbestGap");

        String SWbestLineLenguage = (String) request.getAttribute("SWbestLineLenguage");
        SWbestLineLenguage = veryfy(SWbestLineLenguage);
        String SWbestLine = (String) request.getAttribute("SWbestLine");
    %>
</head>
<body>
    <div class="container">
        <h2>Resultado</h2>
        <div class="button-container">
            <button onclick="showSection('NWSection')">Comparações NW</button>
            <button onclick="showSection('SWSection')">Comparações SW</button>
        </div>
        
        <div id="NWSection" class="result-section">
            <h3>Comparações NW:</h3>
            <div class="style-p">
                <table>
                    <tr>
                        <td><strong>Menor tempo:</strong></td>
                        <td><%= NWbestTimeLenguage != null ? NWbestTimeLenguage : "N/A" %> :</td>
                        <td><%= NWbestTime != null ? NWbestTime : "N/A" %> seg</td>
                    </tr>
                    <tr>
                        <td><strong>Melhor score:</strong></td>
                        <td><%= NWbestScoreLenguage != null ? NWbestScoreLenguage : "N/A" %> :</td>
                        <td><%= NWbestScore != null ? NWbestScore : "N/A" %> score</td>
                    </tr>
                    <tr>
                        <td><strong>Melhor gap:</strong></td>
                        <td><%= NWbestGapLenguage != null ? NWbestGapLenguage : "N/A" %> :</td>
                        <td><%= NWbestGap != null ? NWbestGap : "N/A" %> gaps</td>
                    </tr>
                    <tr>
                        <td><strong>Melhor linha:</strong></td>
                        <td><%= NWbestLineLenguage != null ? NWbestLineLenguage : "N/A" %> :</td>
                        <td><%= NWbestLine != null ? NWbestLine : "N/A" %> linhas</td>
                    </tr>
                </table>
            </div>
        </div>

        <div id="SWSection" class="result-section">
            <h3>Comparações SW:</h3>
            <div class="style-p">
                <table>
                    <tr>
                        <td><strong>Menor tempo:</strong></td>
                        <td><%= SWbestTimeLenguage != null ? SWbestTimeLenguage : "N/A" %> :</td>
                        <td><%= SWbestTime != null ? SWbestTime : "N/A" %> seg</td>
                    </tr>
                    <tr>
                        <td><strong>Melhor score:</strong></td>
                        <td><%= SWbestScoreLenguage != null ? SWbestScoreLenguage : "N/A" %> :</td>
                        <td><%= SWbestScore != null ? SWbestScore : "N/A" %> score</td>
                    </tr>
                    <tr>
                        <td><strong>Melhor gap:</strong></td>
                        <td><%= SWbestGapLenguage != null ? SWbestGapLenguage : "N/A" %> :</td>
                        <td><%= SWbestGap != null ? SWbestGap : "N/A" %> gaps</td>
                    </tr>
                    <tr>
                        <td><strong>Melhor linha:</strong></td>
                        <td><%= SWbestLineLenguage != null ? SWbestLineLenguage : "N/A" %> :</td>
                        <td><%= SWbestLine != null ? SWbestLine : "N/A" %> linhas</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
