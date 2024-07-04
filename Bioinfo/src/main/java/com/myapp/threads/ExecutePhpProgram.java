package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExecutePhpProgram implements Runnable {

    // Caminho para o executável Python no seu sistema
    private static final String PHP_EXECUTABLE = "php"; // Ajuste conforme o seu ambiente

    private String phpFilePath;
    private String outputFilePath;
    private String seq1;
    private String seq2;

    public ExecutePhpProgram(ArrayList<String> seqList, String phpFileName) {
        this.seq1 = seqList.get(0);
        this.seq2 = seqList.get(1);
        
        // Define o caminho do arquivo PHP com base no nome do arquivo
        if ("NW".equals(phpFileName)) {
            phpFilePath = "/Bioinfo/src/main/java/codes/php/NW.php";
            outputFilePath = "/Bioinfo/src/main/java/respostas/NW/resultado_php.txt";
        } else if ("SW".equals(phpFileName)) {
            phpFilePath = "/Bioinfo/src/main/java/codes/php/SW.php";
            outputFilePath = "/Bioinfo/src/main/java/respostas/SW/resultado_php_SW.txt";
        }    
    }

    @Override
    public void run() {
        try {
            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);
            FileWriter fileWriter = new FileWriter(outputFile, true);

            ProcessBuilder runProcessBuilder = new ProcessBuilder(PHP_EXECUTABLE, phpFilePath, seq1, seq2);
            runProcessBuilder.redirectErrorStream(true);
            Process runProcess = runProcessBuilder.start();
            int runExitCode = runProcess.waitFor();

            // Lê a saída do programa PHP
            Scanner runOutputStream = new Scanner(runProcess.getInputStream()).useDelimiter("\\A");
            String runOutput = runOutputStream.hasNext() ? runOutputStream.next() : "";
            fileWriter.write(runOutput);

            if (runExitCode != 0) {
                // System.out.println("Erro na execução. Código de retorno: " + runExitCode);
            } else {
                // System.out.println("Programa PHP executado com sucesso.");
            }
            runOutputStream.close(); 
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
