package com.myapp.threads;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ExecuteCppProgram implements Runnable {

    private String cppFilePath;
    private String outputFilePath;
    private int count;
    private String seq;

    public ExecuteCppProgram(int count, String seq) {
        this.count = count;
        this.seq = seq;
        cppFilePath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/cpp/NW.cpp";
        outputFilePath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NWresultado_cpp" + count + ".txt";
    }

    @Override
    public void run() {
        try {
            // Diretório de saída para o arquivo executável
            String outputDir = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/cpp";
            String exeFilePath = outputDir + "/NW_executable" + count + ".exe";

            // Compila o arquivo C++
            System.out.println("Compilando o arquivo C++...");
            ProcessBuilder compileProcessBuilder = new ProcessBuilder("g++", "-o", exeFilePath, cppFilePath);
            Process compileProcess = compileProcessBuilder.start();
            
            // Captura a saída de erro da compilação
            Scanner compileErrorStream = new Scanner(compileProcess.getErrorStream()).useDelimiter("\\A");
            String compileError = compileErrorStream.hasNext() ? compileErrorStream.next() : "";
            
            int compileExitCode = compileProcess.waitFor();
            if (compileExitCode != 0) {
                System.out.println("Erro na compilação. Código de retorno: " + compileExitCode);
                System.out.println("Erro de compilação: " + compileError);
                return;
            }
            System.out.println("Compilação bem-sucedida.");

            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);

            // Cria um processo para executar o programa C++
            System.out.println("Executando o programa C++...");
            ProcessBuilder runProcessBuilder = new ProcessBuilder(exeFilePath, seq);

            // Redireciona a saída do processo para o arquivo de saída
            runProcessBuilder.redirectErrorStream(true);
            runProcessBuilder.redirectOutput(outputFile);

            // Inicia o processo
            Process runProcess = runProcessBuilder.start();

            // Captura a saída de erro da execução
            Scanner runErrorStream = new Scanner(runProcess.getErrorStream()).useDelimiter("\\A");
            String runError = runErrorStream.hasNext() ? runErrorStream.next() : "";

            // Aguarda a execução do processo e obtém o código de retorno
            int runExitCode = runProcess.waitFor();
            if (runExitCode != 0) {
                System.out.println("Erro na execução. Código de retorno: " + runExitCode);
                System.out.println("Erro de execução: " + runError);
            } else {
                System.out.println("Programa C++ executado com sucesso.");
            }

            // Exibe o resultado da execução
            System.out.println("Código de retorno da execução: " + runExitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}