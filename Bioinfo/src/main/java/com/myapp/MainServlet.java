package com.myapp;

import com.myapp.threads.ExecuteCppProgram;
import com.myapp.threads.ExecuteJavaProgram;
import com.myapp.threads.ExecutePhpProgram;
import com.myapp.threads.ExecutePythonScript;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                
                Thread phpNW = new Thread(new ExecutePhpProgram(i, seqList.get(i), "NW"));
                Thread phpSW = new Thread(new ExecutePhpProgram(i, seqList.get(i), "SW"));
                threads.add(phpNW);
                threads.add(phpSW);
            }

            // Inicia todas as threads
            for (Thread thread : threads) {
                thread.start();
            }

            // Verifica se as threads ainda estão em execução
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
            
            response.sendRedirect(request.getContextPath() + "/result.jsp");
        }
    }

    // Método para deletar todos os arquivos de uma pasta
    private void deleteFilesInFolder() {
        // File NW = new File("/Bioinfo/main/java/respostas/NW/");
        File NW = new File("C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW");
        File[] files = NW.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }

        // File SW = new File("/Bioinfo/main/java/respostas/SW/");
        File SW = new File("C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/SW");
        files = SW.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }

        // File javaClass = new File("/Bioinfo/target/classes/codes/java");
        File javaClass = new File("C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/java");
        files = javaClass.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".class")) {
                    file.delete();
                }
            }
        }

        // File cppClass = new File("/Bioinfo/main/java/codes/cpp");
        File cppClass = new File("C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/cpp");
        files = cppClass.listFiles();
        
        if (files != null ) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".exe")) {
                    file.delete();
                }
            }
        }
    }
}
