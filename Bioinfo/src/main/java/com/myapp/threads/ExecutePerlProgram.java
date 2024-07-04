package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExecutePerlProgram implements Runnable {

    private String perlFilePath;
    private String outputFilePath;
    private String seq1;
    private String seq2;

    // Caminho para o executável Perl no seu sistema
    private static final String PERL_EXECUTABLE = "perl"; // Ajuste conforme o seu ambiente

    public ExecutePerlProgram(ArrayList<String> seqList, String perlFileName) {
        this.seq1 = seqList.get(0);
        this.seq2 = seqList.get(1);
        
        // Define o caminho do script Perl com base no nome do script
        if ("NW".equals(perlFileName)) {
            perlFilePath = "/Bioinfo/src/main/java/codes/perl/NW.pl";
            outputFilePath = "/Bioinfo/src/main/java/respostas/NW/resultado_perl.txt";
        } else if ("SW".equals(perlFileName)) {
            perlFilePath = "/Bioinfo/src/main/java/codes/perl/SW.pl";
            outputFilePath = "/Bioinfo/src/main/java/respostas/SW/resultado_perl_SW.txt";
        }     
    }

    @Override
    public void run() {
        try {
            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);
            FileWriter fileWriter = new FileWriter(outputFile, true);

            ProcessBuilder runProcessBuilder = new ProcessBuilder(PERL_EXECUTABLE, perlFilePath, seq1, seq2);
            runProcessBuilder.redirectErrorStream(true);
            Process runProcess = runProcessBuilder.start();
            int runExitCode = runProcess.waitFor();

            // Lê a saída do script Perl
            Scanner runOutputStream = new Scanner(runProcess.getInputStream()).useDelimiter("\\A");
            String runOutput = runOutputStream.hasNext() ? runOutputStream.next() : "";
            fileWriter.write(runOutput);

            if (runExitCode != 0) {
                // System.out.println("Erro na execução. Código de retorno: " + runExitCode);
            } else {
                // System.out.println("Script Perl executado com sucesso.");
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
