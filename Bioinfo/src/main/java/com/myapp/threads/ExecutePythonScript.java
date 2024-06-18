package com.myapp.threads;
import java.io.*;

public class ExecutePythonScript implements Runnable {

    private String pythonScriptPath;
    private String outputFilePath;
    private int cout;
    private String seq;

    public ExecutePythonScript(int cout,String seq) {
        this.cout =cout;
        this.seq =seq;
        pythonScriptPath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/python/NW.py";
        outputFilePath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NWresultado_python"+cout+".txt";
    }

    @Override
    public void run() {
        try {
            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);

            // Cria um processo para executar o script Python
            ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath,seq);

            // Redireciona a saída do processo para o arquivo de saída
            pb.redirectErrorStream(true);
            pb.redirectOutput(outputFile);

            // Inicia o processo
            Process process = pb.start();

            // Aguarda a execução do processo e obtém o código de retorno
            int exitCode = process.waitFor();

            // Exibe o resultado da execução
            System.out.println("Script Python executado. Código de retorno: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}