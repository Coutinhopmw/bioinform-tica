import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStreamReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

public class Result_array extends HttpServlet{
    public String[] read_lines(String filename, BufferedReader buffered_reader) throws IOException {
        FileReader file_reader = new FileReader(filename);
        List<String> NW_results = new ArrayList<>();
        List<String> SW_results = new ArrayList<>();
        List<String> current_list = null;

        String line;
        while ((line = buffered_reader.readLine()) != null) {
            linha = line.trim();
            current_list = detectSection(line, current_list);
            if (current_list != null && linha.startsWith("('")){
                // Extrai os dados da linha e adiciona à categoria atual
                String[] dados = linha.replace("(", "").replace(")", "").replace("'", "").split(", ");
                current_list.add(dados);
            }
        }

        buffered_reader.close();
        return new result(NW_results, SW_results);
    }

    private static List<String> detect_section(String line, List<String> current_list) {
        if (line.equals("Comparações NW:")) {
            return NW_results;
        } else if (line.equals("Comparações SW:")) {
            return SW_results;
        }
        return current_list;
    }

    protected static void main(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String[] name_file = "/arquivo/resultados.txt";
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(filePart.getInputStream()));

            Result result = read_lines(name_file, bufferedReader);
            
            request.setAttribute("NW_results", result.getNW_results());
            request.setAttribute("SW_results", result.getSW_results());
            request.getRequestDispatcher("/result.jsp").forward(request, response);
        } (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}

class Result {
    private List<String> NW_results;
    private List<String> SW_results;

    public result(List<String> NW_results, List<String> SW_results) {
        this.NW_results = NW_results;
        this.SW_results = SW_results;
    }

    public List<String> getNW_results() {
        return NW_results;
    }

    public List<String> getSW_results() {
        return SW_results;
    }
}


/*
ArrayList<String> seqList = (ArrayList<String>) session.getAttribute("seqList");
                if (seqList != null && !seqList.isEmpty()) {
                    out.println("Resultado: ");
                    seqList.clear();
                } else {
                    out.println("Nenhum resultado encontrado.");
                }
*/