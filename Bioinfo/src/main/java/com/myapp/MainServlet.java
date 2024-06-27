package com.myapp;

import com.myapp.threads.ExecuteCProgram;
import com.myapp.threads.ExecuteCppProgram;
import com.myapp.threads.ExecuteCsProgram;
import com.myapp.threads.ExecuteJavaProgram;
import com.myapp.threads.ExecuteMainPython;
import com.myapp.threads.ExecutePerlProgram;
import com.myapp.threads.ExecutePhpProgram;
import com.myapp.threads.ExecutePythonScript;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainServlet extends HttpServlet {
    private ArrayList<String> seqList = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        String seq = request.getParameter("seq");
        String action = request.getParameter("adicionar") != null ? "adicionar" : "resultado";

        if ("adicionar".equals(action)) {
            seqList.add(seq);
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else if ("resultado".equals(action)) {
            session.setAttribute("seqList", seqList); // Salva a lista na sessão
            
            // Deleta todos os arquivos de uma pasta específica
            deleteFilesInFolder();
            
            // Lista para armazenar as threads
            List<Thread> threads = new ArrayList<>();
            
            // Inicia as threads conforme o seu código original
            for (int i = 0; i < seqList.size(); i++) {

                Thread perlNW = new Thread(new ExecutePerlProgram(i, seqList.get(i), "NW"));
                Thread perlSW = new Thread(new ExecutePerlProgram(i, seqList.get(i), "SW"));
                threads.add(perlNW);
                threads.add(perlSW);

                Thread pythonNW = new Thread(new ExecutePythonScript(i, seqList.get(i), "NW"));
                Thread pythonSW = new Thread(new ExecutePythonScript(i, seqList.get(i), "SW"));
                threads.add(pythonNW);
                threads.add(pythonSW);
                
                Thread javaNW = new Thread(new ExecuteJavaProgram(i, seqList.get(i), "NW"));
                Thread javaSW = new Thread(new ExecuteJavaProgram(i, seqList.get(i), "SW"));
                threads.add(javaNW);
                threads.add(javaSW);
                
                Thread cppNW = new Thread(new ExecuteCppProgram(i, seqList.get(i), "NW"));
                Thread cppSW = new Thread(new ExecuteCppProgram(i, seqList.get(i), "SW"));
                threads.add(cppNW);
                threads.add(cppSW);
                
                Thread csNW = new Thread(new ExecuteCsProgram(i, seqList.get(i), "NW"));
                Thread csSW = new Thread(new ExecuteCsProgram(i, seqList.get(i), "SW"));
                threads.add(csNW);
                threads.add(csSW);

                Thread cNW = new Thread(new ExecuteCProgram(i, seqList.get(i), "NW"));
                Thread cSW = new Thread(new ExecuteCProgram(i, seqList.get(i), "SW"));
                threads.add(cNW);
                threads.add(cSW);
                
                Thread phpNW = new Thread(new ExecutePhpProgram(i, seqList.get(i), "NW"));
                Thread phpSW = new Thread(new ExecutePhpProgram(i, seqList.get(i), "SW"));
                threads.add(phpNW);
                threads.add(phpSW);

                break;
            }

            // Inicia todas as threads
            for (Thread thread : threads) {
                thread.start();
            }
            isrun(threads);
            threads.clear();

            Thread main = new Thread(new ExecuteMainPython());
            threads.add(main);

            for (Thread thread : threads) {
                thread.start();
            }
            isrun(threads);
            threads.clear();

            doGet(request, response);
        }
    }
    
    @Override
     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         File result = new File("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/com/myapp/resultado.txt");
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

            // Use RequestDispatcher para encaminhar a requisição para o JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
            dispatcher.forward(request, response);
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
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (anyThreadRunning);
    }

    // Método para deletar todos os arquivos de uma pasta
    private void deleteFilesInFolder() {
        File NW = new File("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW");
        File[] files = NW.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
        File SW = new File("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/SW");
        files = SW.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
        File javaClass = new File("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/java");
        files = javaClass.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".class")) {
                    file.delete();
                }
            }
        }
        File cppClass = new File("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/cpp");
        files = cppClass.listFiles();
        
        if (files != null ) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".exe")) {
                    file.delete();
                }
            }
        }
        File cClass = new File("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/c");
        files = cClass.listFiles();
        
        if (files != null ) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".exe")) {
                    file.delete();
                }
            }
        }
    }
}
