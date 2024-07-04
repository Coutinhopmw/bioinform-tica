package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExecuteCProgram implements Runnable {

    private String cFilePath;
    private String outputFilePath;
    private String seq1;
    private String seq2;
    private String cFileName;

    public ExecuteCProgram(ArrayList<String> seqList, String cFileName) {
        this.seq1 = seqList.get(0);
        this.seq2 = seqList.get(1);
        this.cFileName = cFileName;


        if ("NW".equals(cFileName)) {
            cFilePath = "/Bioinfo/src/main/java/codes/c/NW.c";
            outputFilePath = "/Bioinfo/src/main/java/respostas/NW/resultado_c.txt";
        
        } else if ("SW".equals(cFileName)) {
            cFilePath = "/Bioinfo/src/main/java/codes/c/SW.c";
            outputFilePath = "/Bioinfo/src/main/java/respostas/SW/resultado_c_SW.txt";
        }
    }

    @Override
    public void run() {
        try {
            String outputDir = "/Bioinfo/src/main/java/codes/c/";

            if ("NW".equals(cFileName)) {
                System.out.println("C      NW executando o arquivo...");
            } else if ("SW".equals(cFileName)) {
                System.out.println("C      SW executando o arquivo...");
            }
            
            // Compila o arquivo C
            ProcessBuilder compileProcessBuilder = new ProcessBuilder("gcc", "-o", outputDir + cFileName, cFilePath);
            Process compileProcess = compileProcessBuilder.start();
            int compileExitCode = compileProcess.waitFor();
            if (compileExitCode != 0) {
                if ("NW".equals(cFileName)) {
                    System.out.println("C      NW Erro na compilacao. Codigo de retorno: " + compileExitCode);
                } else if ("SW".equals(cFileName)) {
                    System.out.println("C      SW Erro na compilacao. Codigo de retorno: " + compileExitCode);
                }
                return;
            }

            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);
            FileWriter fileWriter = new FileWriter(outputFile, true);

            ProcessBuilder runProcessBuilder = new ProcessBuilder(outputDir + cFileName, seq1, seq2);
            runProcessBuilder.redirectErrorStream(true);
            Process runProcess = runProcessBuilder.start();
            int runExitCode = runProcess.waitFor();

            // Lê a saída do programa C
            Scanner runOutputStream = new Scanner(runProcess.getInputStream()).useDelimiter("\\A");
            String runOutput = runOutputStream.hasNext() ? runOutputStream.next() : "";
            fileWriter.write(runOutput);

            if (runExitCode != 0) {
                // System.out.println("Erro na execução. Código de retorno: " + runExitCode);
            } else {
                // System.out.println("Programa C executado com sucesso.");
            } 
            runOutputStream.close();        
            fileWriter.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
