package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ExecutePerlProgram implements Runnable {

    private String perlScriptPath;
    private String outputFilePath;
    private int count;
    private String seq;
    private String scriptName;
    private String directoryPath;

    // Caminho para o executável Perl no seu sistema
    private static final String PERL_EXECUTABLE = "perl"; // Ajuste conforme o seu ambiente

    public ExecutePerlProgram(int count, String seq, String scriptName) {
        this.count = count;
        this.seq = seq;
        this.scriptName = scriptName;
        this.directoryPath = "C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/Bancos de dados";

        // Define o caminho do script Perl com base no nome do script
        if ("NW".equals(scriptName)) {
            perlScriptPath = "C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/perl/NW.pl";
            outputFilePath = "C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW/resultado_perl.txt";
        } else if ("SW".equals(scriptName)) {
            perlScriptPath = "C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/perl/SW.pl";
            outputFilePath = "C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/SW/resultado_perl_SW.txt";
        }     
    }

    @Override
    public void run() {
        try {
            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);
            FileWriter fileWriter = new FileWriter(outputFile, true);

            // Lê os arquivos de entrada do diretório especificado
            File dir = new File(directoryPath);
            if (!dir.exists()) {
                System.out.println("Diretório não existe: " + directoryPath);
                return;
            }

            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".fasta")) {
                        try (Scanner fileScanner = new Scanner(file)) {
                            String firstLine = fileScanner.nextLine();
                            String secondLine = fileScanner.hasNextLine() ? fileScanner.nextLine() : "";

                            fileWriter.write(firstLine + "\n");

                            // Executa o script Perl
                            ProcessBuilder runProcessBuilder = new ProcessBuilder(PERL_EXECUTABLE, perlScriptPath, seq, secondLine);
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
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Exemplo de uso para script1.pl
        Thread threadScript1 = new Thread(new ExecutePerlProgram(1, "sequencia1", "script1"));
        threadScript1.start();

        // Exemplo de uso para script2.pl
        Thread threadScript2 = new Thread(new ExecutePerlProgram(2, "sequencia2", "script2"));
        threadScript2.start();
    }
}
