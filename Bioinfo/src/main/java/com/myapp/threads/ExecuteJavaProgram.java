package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExecuteJavaProgram implements Runnable {

    private String javaFilePath;
    private String outputFilePath;
    private String seq1;
    private String seq2;
    private String javaFileName;

    public ExecuteJavaProgram(ArrayList<String> seqList, String javaFileName) {
        this.seq1 = seqList.get(0);
        this.seq2 = seqList.get(1);
        this.javaFileName = javaFileName;
        
        // Define o caminho do arquivo Java com base no nome do arquivo
        if ("NW".equals(javaFileName)) {
            javaFilePath = "/Bioinfo/src/main/java/codes/java/NW.java";
            outputFilePath = "/Bioinfo/src/main/java/respostas/NW/resultado_java.txt";
        } else if ("SW".equals(javaFileName)) {
            javaFilePath = "/Bioinfo/src/main/java/codes/java/SW.java";
            outputFilePath = "/Bioinfo/src/main/java/respostas/SW/resultado_java_SW.txt";
        }
    }

    @Override
    public void run() {
        try {
            String outputDir = "/Bioinfo/src/main/java/";

            if ("NW".equals(javaFileName)) {
                System.out.println("Java   NW executando o arquivo...");
            } else if ("SW".equals(javaFileName)) {
              System.out.println("Java   SW executando o arquivo...");
            }
            
            // Compila o arquivo Java
            System.out.println(javaFileName);
            ProcessBuilder compileProcessBuilder = new ProcessBuilder("javac", "-d", outputDir, javaFilePath);
            Process compileProcess = compileProcessBuilder.start();
            int compileExitCode = compileProcess.waitFor();
            if (compileExitCode != 0) {
                 if ("NW".equals(javaFileName)) {
                     System.out.println("Java   NW Erro na compilacao. Codigo de retorno: " + compileExitCode);
                } else if ("SW".equals(javaFileName)) {
                   System.out.println("Java   SW Erro na compilacao. Codigo de retorno: " + compileExitCode);
                }
                return;
            }

            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);
            FileWriter fileWriter = new FileWriter(outputFile, true);

            String className = "codes.java." + javaFileName;
            ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-cp", outputDir,className, seq1, seq2);
            runProcessBuilder.redirectErrorStream(true);
            Process runProcess = runProcessBuilder.start();
            int runExitCode = runProcess.waitFor();

            // Lê a saída do programa Java
            Scanner runOutputStream = new Scanner(runProcess.getInputStream()).useDelimiter("\\A");
            String runOutput = runOutputStream.hasNext() ? runOutputStream.next() : "";
            fileWriter.write(runOutput);

            if (runExitCode != 0) {
                // System.out.println("Erro na execução. Código de retorno: " + runExitCode);
            } else {
                // System.out.println("Programa Java executado com sucesso.");
            }
            runOutputStream.close(); 
            fileWriter.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}