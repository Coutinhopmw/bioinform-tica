package com.myapp.threads;

import java.io.File;
import java.io.IOException;

public class ExecuteJavaProgram implements Runnable {

    private String javaFilePath;
    private String outputFilePath;
    private int count;
    private String seq;

    public ExecuteJavaProgram(int count, String seq) {
        this.count = count;
        this.seq = seq;
        javaFilePath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/java/NW.java";
        outputFilePath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NWresultado_java" + count + ".txt";
    }

    @Override
    public void run() {
        try {
            // Diretório de saída para arquivos .class
            String outputDir = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java";

            // Compila o arquivo Java
            ProcessBuilder compileProcessBuilder = new ProcessBuilder("javac", "-d", outputDir, javaFilePath);
            Process compileProcess = compileProcessBuilder.start();
            int compileExitCode = compileProcess.waitFor();
            if (compileExitCode != 0) {
                System.out.println("Erro na compilação. Código de retorno: " + compileExitCode);
                return;
            }

            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);

            // Nome completo da classe com o pacote
            String className = "codes.java.NW";

            // Cria um processo para executar o programa Java
            ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-cp", outputDir, className, seq);

            // Redireciona a saída do processo para o arquivo de saída
            runProcessBuilder.redirectErrorStream(true);
            runProcessBuilder.redirectOutput(outputFile);

            // Inicia o processo
            Process runProcess = runProcessBuilder.start();

            // Aguarda a execução do processo e obtém o código de retorno
            int runExitCode = runProcess.waitFor();

            // Exibe o resultado da execução
            System.out.println("Programa Java executado. Código de retorno: " + runExitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
