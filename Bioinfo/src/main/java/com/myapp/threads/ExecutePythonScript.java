package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExecutePythonScript implements Runnable {

    // Caminho para o executável Python no seu sistema
    private static final String PYTHON_EXECUTABLE = "python3"; // Ajuste conforme o seu ambiente

    private String pyFilePath;
    private String outputFilePath;
    private String seq1;
    private String seq2;

    public ExecutePythonScript(ArrayList<String> seqList, String pyFileName) {
        this.seq1 = seqList.get(0);
        this.seq2 = seqList.get(1);
        
        // Define o caminho do script Python com base no nome do script
        if ("NW".equals(pyFileName)) {
            pyFilePath= "/Bioinfo/src/main/java/codes/python/NW.py";
            outputFilePath = "/Bioinfo/src/main/java/respostas/NW/resultado_python.txt";
        } else if ("SW".equals(pyFileName)) {
            pyFilePath= "/Bioinfo/src/main/java/codes/python/SW.py";
            outputFilePath = "/Bioinfo/src/main/java/respostas/SW/resultado_python_SW.txt";
        } else{
            pyFilePath= "/Bioinfo/src/main/java/com/myapp/aling.py";
            outputFilePath = "/Bioinfo/src/main/java/com/myapp/aling.txt";
        } 
    }

    @Override
    public void run() {
        try {
            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);
            FileWriter fileWriter = new FileWriter(outputFile, true);

            ProcessBuilder runProcessBuilder = new ProcessBuilder(PYTHON_EXECUTABLE, pyFilePath, seq1, seq2);
            runProcessBuilder.redirectErrorStream(true);
            Process runProcess = runProcessBuilder.start();
            int runExitCode = runProcess.waitFor();

            // Lê a saída do script Python
            Scanner runOutputStream = new Scanner(runProcess.getInputStream()).useDelimiter("\\A");
            String runOutput = runOutputStream.hasNext() ? runOutputStream.next() : "";
            fileWriter.write(runOutput);

            if (runExitCode != 0) {
                // System.out.println("Erro na execução. Código de retorno: " + runExitCode);
            } else {
                // System.out.println("Script Python executado com sucesso.");
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
