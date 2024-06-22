package com.myapp.threads;
import java.io.*;

public class ExecutePythonScript implements Runnable {

    private String pythonScriptPath;
    private String outputFilePath;
    private int count;
    private String seq;
    private String scriptName;

    public ExecutePythonScript(int count, String seq, String scriptName) {
        this.count = count;
        this.seq = seq;
        this.scriptName = scriptName;
        
        // Define o caminho do script Python com base no nome do script
        if ("NW".equals(scriptName)) {
            // pythonScriptPath = "/Bioinfo/main/java/codes/python/NW.py";
            // outputFilePath = "/Bioinfo/main/java/respostas/NW/NWresultado_python" + count + ".txt";

            // Se não estiver usando docker:
            pythonScriptPath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/python/NW.py";
            outputFilePath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW/NWresultado_pyton" + count + ".txt";
        
        } else if ("SW".equals(scriptName)) {
            // pythonScriptPath = "/Bioinfo/main/java/codes/python/SW.py";
            // outputFilePath = "/Bioinfo/main/java/respostas/SW/SWresultado_python" + count + ".txt";

            // Se não estiver usando docker lembre de atulizar seu caminho:
            pythonScriptPath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/codes/python/SW.py";
            outputFilePath = "C:/Users/Usuário/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/SW/SWresultado_python" + count + ".txt";
        }
    }

    @Override
    public void run() {
        try {
            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);

            // Cria um processo para executar o script Python
            // ProcessBuilder pb = new ProcessBuilder("python3", pythonScriptPath, seq);

            // Se não estiver usando docker :
            ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath, seq);

            // Redireciona a saída do processo para o arquivo de saída
            pb.redirectErrorStream(true);
            pb.redirectOutput(outputFile);

            if ("NW".equals(scriptName)) {
                System.out.println("Python NW executando o arquivo...");
            } else if ("SW".equals(scriptName)) {
              System.out.println("Python SW executando o arquivo...");
            }

            // Inicia o processo
            Process process = pb.start();

            // Aguarda a execução do processo e obtém o código de retorno
            int exitCode = process.waitFor();

            // Exibe o resultado da execução
            if ("NW".equals(scriptName)) {
                System.out.println("Python NW programa executado. Codigo de retorno: " + exitCode);
            } else if ("SW".equals(scriptName)) {
              System.out.println("Python SW programa executado. Codigo de retorno: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
