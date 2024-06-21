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
    private static final String PHP_EXECUTABLE = "C:/php/php.exe"; // Ajuste conforme o seu ambiente

    public ExecutePhpProgram(int count, String seq, String phpFileName) {
        this.count = count;
        this.seq = seq;
        this.phpFileName = phpFileName;

        // Define o caminho do arquivo PHP com base no nome do arquivo
        if ("NW".equals(phpFileName)) {
            phpFilePath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/php/NW.php";
            outputFilePath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW/NWresultado_php" + count + ".txt";
        } else if ("SW".equals(phpFileName)) {
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
            System.out.println("Executando o programa PHP...");
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
                System.out.println("Erro na execução. Código de retorno: " + exitCode);
                System.out.println("Erro de execução: " + error);
            } else {
                System.out.println("Programa PHP executado com sucesso.");
            }

            // Exibe o resultado da execução
            System.out.println("Código de retorno da execução: " + exitCode);
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
