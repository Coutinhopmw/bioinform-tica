package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ExecutePhpProgram implements Runnable {

    private String phpFilePath;
    private String outputFilePath;
    private int count;
    private String seq;
    private String phpFileName;
    private String directoryPath;

    public ExecutePhpProgram(int count, String seq, String phpFileName) {
        this.count = count;
        this.seq = seq;
        this.phpFileName = phpFileName;
        this.directoryPath = "/Bioinfo/database";

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

                            // Executa o programa PHP
                            ProcessBuilder runProcessBuilder = new ProcessBuilder(PHP_EXECUTABLE, phpFilePath, seq, secondLine);
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
        // Exemplo de uso para NW.php
        Thread threadNW = new Thread(new ExecutePhpProgram(1, "sequenciaNW", "NW"));
        threadNW.start();

        // Exemplo de uso para SW.php
        Thread threadSW = new Thread(new ExecutePhpProgram(2, "sequenciaSW", "SW"));
        threadSW.start();
    }

    // Caminho para o executável PHP no seu sistema
    private static final String PHP_EXECUTABLE = "php"; // Ajuste conforme o seu ambiente
}
