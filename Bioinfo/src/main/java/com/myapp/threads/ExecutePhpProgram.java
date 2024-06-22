package com.myapp.threads;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ExecutePhpProgram implements Runnable {

    private String phpFilePath;
    private String outputFilePath;
    private int count;
    private String seq;
    private String phpFileName;

    // Caminho para o executável PHP no seu sistema
    private static final String PHP_EXECUTABLE = "php"; // Ajuste conforme o seu ambiente

    public ExecutePhpProgram(int count, String seq, String phpFileName) {
        this.count = count;
        this.seq = seq;
        this.phpFileName = phpFileName;

        // Define o caminho do arquivo PHP com base no nome do arquivo
        if ("NW".equals(phpFileName)) {
            // phpFilePath = "/Bioinfo/main/java/codes/php/NW.php";
            // outputFilePath = "/Bioinfo/main/java/respostas/NW/NWresultado_php" + count + ".txt";

            // Se não estiver usando docker:
            phpFilePath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/php/NW.php";
            outputFilePath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW/NWresultado_php" + count + ".txt";
        
        } else if ("SW".equals(phpFileName)) {
            // phpFilePath = "/Bioinfo/main/java/codes/php/SW.php";
            // outputFilePath = "/Bioinfo/main/java/respostas/SW/SWresultado_php" + count + ".txt";

            // Se não estiver usando docker lembre de atulizar seu caminho:
            phpFilePath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/php/SW.php";
            outputFilePath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/SW/SWresultado_php" + count + ".txt";
        }
    }

    @Override
    public void run() {
        try {
            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);

            // Cria um processo para executar o programa PHP
            if ("NW".equals(phpFileName)) {
                System.out.println("Php    NW executando o arquivo...");
            } else if ("SW".equals(phpFileName)) {
              System.out.println("Php    SW executando o arquivo...");
            }
            ProcessBuilder processBuilder = new ProcessBuilder(PHP_EXECUTABLE, phpFilePath, seq);

            // Redireciona a saída do processo para o arquivo de saída
            processBuilder.redirectErrorStream(true);
            processBuilder.redirectOutput(outputFile);

            // Inicia o processo
            Process process = processBuilder.start();

            // Captura a saída de erro da execução
            Scanner errorStream = new Scanner(process.getErrorStream()).useDelimiter("\\A");
            String error = errorStream.hasNext() ? errorStream.next() : "";

            // Aguarda a execução do processo e obtém o código de retorno
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                if ("NW".equals(phpFileName)) {
                    System.out.println("Php    NW Erro na execucao. Codigo de retorno: " + exitCode);
                } else if ("SW".equals(phpFileName)) {
                    System.out.println("Php    SW na execucao. Codigo de retorno: " + exitCode);
                }
                
            } else {
                if ("NW".equals(phpFileName)) {
                    System.out.println("Php    NW programa executado");
                } else if ("SW".equals(phpFileName)) {
                  System.out.println("Php    SW programa executado");
                }
            }
        } catch (IOException | InterruptedException e) {
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
}
