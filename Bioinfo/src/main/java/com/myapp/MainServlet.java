package com.myapp;

import com.myapp.threads.ExecuteCProgram;
import com.myapp.threads.ExecuteCppProgram;
import com.myapp.threads.ExecuteCsProgram;
import com.myapp.threads.ExecuteJavaProgram;
import com.myapp.threads.ExecuteMainPython;
import com.myapp.threads.ExecutePerlProgram;
import com.myapp.threads.ExecutePhpProgram;
import com.myapp.threads.ExecutePythonScript;
import com.myapp.threads.ExecuteBlastPy;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainServlet extends HttpServlet {
    private ArrayList<String> seqList = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        String seq1 = request.getParameter("seq1");
        String seq2 = request.getParameter("seq2");
        String action = request.getParameter("resultado");
        System.err.println(seq1 + seq2 + action);
        
        if ("Resultado".equals(action)) {
            
            seqList.add(seq1);
            seqList.add(seq2);
            session.setAttribute("seqList", seqList); // Salva a lista na sessão
            
            // Deleta todos os arquivos de uma pasta específica
            deleteFilesInFolder();
            
            // Lista para armazenar as threads
            List<Thread> threads = new ArrayList<>();

            Thread perlNW = new Thread(new ExecutePerlProgram(seqList, "NW"));
            Thread perlSW = new Thread(new ExecutePerlProgram(seqList, "SW"));
            threads.add(perlNW);
            threads.add(perlSW);

            Thread pythonNW = new Thread(new ExecutePythonScript(seqList, "NW"));
            Thread pythonSW = new Thread(new ExecutePythonScript(seqList, "SW"));
            threads.add(pythonNW);
            threads.add(pythonSW);
            
            Thread javaNW = new Thread(new ExecuteJavaProgram(seqList, "NW"));
            Thread javaSW = new Thread(new ExecuteJavaProgram(seqList, "SW"));
            threads.add(javaNW);
            threads.add(javaSW);
            
            Thread cppNW = new Thread(new ExecuteCppProgram(seqList, "NW"));
            Thread cppSW = new Thread(new ExecuteCppProgram(seqList, "SW"));
            threads.add(cppNW);
            threads.add(cppSW);
            
            Thread csNW = new Thread(new ExecuteCsProgram(seqList, "NW"));
            Thread csSW = new Thread(new ExecuteCsProgram(seqList, "SW"));
            threads.add(csNW);
            threads.add(csSW);

            Thread cNW = new Thread(new ExecuteCProgram(seqList, "NW"));
            Thread cSW = new Thread(new ExecuteCProgram(seqList, "SW"));
            threads.add(cNW);
            threads.add(cSW);
            
            Thread phpNW = new Thread(new ExecutePhpProgram(seqList, "NW"));
            Thread phpSW = new Thread(new ExecutePhpProgram(seqList, "SW"));
            threads.add(phpNW);
            threads.add(phpSW);

            // Inicia todas as threads
            for (Thread thread : threads) {
                thread.start();
                System.out.println("theads iniciadas");
            }
            isrun(threads);
            threads.clear();

            Thread main = new Thread(new ExecuteMainPython());
            threads.add(main);

            String seq = "";
            try {
                // Cria um arquivo para armazenar a saída
                File outputFile = new File("/Bioinfo/src/main/java/com/myapp/aling.txt");
                FileWriter fileWriter = new FileWriter(outputFile, true);

                ProcessBuilder runProcessBuilder = new ProcessBuilder("python", "/Bioinfo/src/main/java/com/myapp/aling.py", seq1, seq2);
                runProcessBuilder.redirectErrorStream(true);
                Process runProcess = runProcessBuilder.start();
                int runExitCode = runProcess.waitFor();

                // Lê a saída do script Python
                Scanner runOutputStream = new Scanner(runProcess.getInputStream()).useDelimiter("\\A");
                String runOutput = runOutputStream.hasNext() ? runOutputStream.next() : "";
                fileWriter.write(runOutput);
                seq = runOutput;
            } catch (Exception e) {
                
            }
            System.err.println(seq);
            Thread blast = new Thread(new ExecuteBlastPy(seq));
            threads.add(blast);

            for (Thread thread : threads) {
                thread.start();
            }
            isrun(threads);
            threads.clear();
            
            try {
               Thread.sleep(5000); // Espera por 5 segundos (5000 milissegundos)
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
            doGet(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File result = new File("/Bioinfo/src/main/java/com/myapp/resultado.txt");
        Scanner fileScanner = new Scanner(result);

        String NWbestTimeLenguage = fileScanner.nextLine();
        String NWbestTime = fileScanner.nextLine();
        String NWbestScoreLenguage = fileScanner.nextLine();
        String NWbestScore = fileScanner.nextLine();
        String NWbestGapLenguage = fileScanner.nextLine();
        String NWbestGap = fileScanner.nextLine();
        String NWbestLineLenguage = fileScanner.nextLine();
        String NWbestLine = fileScanner.nextLine();

        String SWbestTimeLenguage = fileScanner.nextLine();
        String SWbestTime = fileScanner.nextLine();
        String SWbestScoreLenguage = fileScanner.nextLine();
        String SWbestScore = fileScanner.nextLine();
        String SWbestGapLenguage = fileScanner.nextLine();
        String SWbestGap = fileScanner.nextLine();
        String SWbestLineLenguage = fileScanner.nextLine();
        String SWbestLine = fileScanner.nextLine();

        request.setAttribute("NWbestTimeLenguage", NWbestTimeLenguage);
        request.setAttribute("NWbestTime", NWbestTime);
        request.setAttribute("NWbestScoreLenguage", NWbestScoreLenguage);
        request.setAttribute("NWbestScore", NWbestScore);
        request.setAttribute("NWbestGapLenguage", NWbestGapLenguage);
        request.setAttribute("NWbestGap", NWbestGap);
        request.setAttribute("NWbestLineLenguage", NWbestLineLenguage);
        request.setAttribute("NWbestLine", NWbestLine);

        request.setAttribute("SWbestTimeLenguage", SWbestTimeLenguage);
        request.setAttribute("SWbestTime", SWbestTime);
        request.setAttribute("SWbestScoreLenguage", SWbestScoreLenguage);
        request.setAttribute("SWbestScore", SWbestScore);
        request.setAttribute("SWbestGapLenguage", SWbestGapLenguage);
        request.setAttribute("SWbestGap", SWbestGap);
        request.setAttribute("SWbestLineLenguage", SWbestLineLenguage);
        request.setAttribute("SWbestLine", SWbestLine);

        File result2 = new File("/Bioinfo/src/main/java/com/myapp/blast/blast_align.txt");
        Scanner fileScanner2 = new Scanner(result);
        List<String> linesList = new ArrayList<>();
        

        // Lê cada linha do arquivo e armazena na lista
        while (fileScanner2.hasNextLine()) {
            String line = fileScanner2.nextLine();
            linesList.add(line);
        }
        request.setAttribute("linesList", linesList);

        // Use RequestDispatcher para encaminhar a requisição para o JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
        dispatcher.forward(request, response);
        fileScanner.close();
        response.sendRedirect(request.getContextPath() + "/result.jsp");
    }
             

    // Verifica se as threads ainda estão em execução
    private void isrun(List<Thread> threads){
        boolean anyThreadRunning;
        do {
            anyThreadRunning = false;
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    anyThreadRunning = true;
                    break;
                }
            }
            if (anyThreadRunning) {
                try {
                    // Aguarda um pouco antes de verificar novamente
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (anyThreadRunning);
    }

    // Método para deletar todos os arquivos de uma pasta
    private void deleteFilesInFolder() {
        File NW = new File("/Bioinfo/src/main/java/respostas/NW");
        File[] files = NW.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
        File SW = new File("/Bioinfo/src/main/java/respostas/SW");
        files = SW.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
        File javaClass = new File("/Bioinfo/src/main/java/codes/java");
        files = javaClass.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".class")) {
                    file.delete();
                }
            }
        }
        File cppClass = new File("/Bioinfo/src/main/java/codes/cpp");
        files = cppClass.listFiles();
        
        if (files != null ) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".exe")) {
                    file.delete();
                }
            }
        }
        File cClass = new File("/Bioinfo/src/main/java/codes/c");
        files = cClass.listFiles();
        
        if (files != null ) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".exe")) {
                    file.delete();
                }
            }
        }

        File csnClass = new File("/Bioinfo/src/main/java/codes/cs/NW");
        files = csnClass.listFiles();
        
        if (files != null ) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".exe")) {
                    file.delete();
                }
            }
        }

        File cssClass = new File("/Bioinfo/src/main/java/codes/cs/SW");
        files = cssClass.listFiles();
        
        if (files != null ) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".exe")) {
                    file.delete();
                }
            }
        }

        File result = new File("/Bioinfo/src/main/java/com/myapp");
        files = result.listFiles();
        
        if (files != null ) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    file.delete();
                }
            }
        }

        File sresult = new File("/Bioinfo");
        files = sresult.listFiles();
        
        if (files != null ) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    file.delete();
                }
            }
        }
    }
}
