package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ExecutePythonScript implements Runnable {

    private String pythonScriptPath;
    private String outputFilePath;
    private int count;
    private String seq;
    private String scriptName;
    private String directoryPath;

    // Caminho para o executável Python no seu sistema
    private static final String PYTHON_EXECUTABLE = "python3"; // Ajuste conforme o seu ambiente

    public ExecutePythonScript(int count, String seq, String scriptName) {
        this.count = count;
        this.seq = seq;
        this.scriptName = scriptName;
        this.directoryPath = "/Bioinfo/database";

        // Define o caminho do script Python com base no nome do script
        if ("NW".equals(scriptName)) {
            pythonScriptPath = "/Bioinfo/src/main/java/codes/python/NW.py";
            outputFilePath = "/Bioinfo/src/main/java/respostas/NW/resultado_python.txt";
        } else if ("SW".equals(scriptName)) {
            pythonScriptPath = "/Bioinfo/src/main/java/codes/python/SW.py";
            outputFilePath = "/Bioinfo/src/main/java/respostas/SW/resultado_python_SW.txt";
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

                            // Executa o script Python
                            ProcessBuilder runProcessBuilder = new ProcessBuilder(PYTHON_EXECUTABLE, pythonScriptPath, seq, secondLine);
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
        // Exemplo de uso para NW.py
        Thread threadNW = new Thread(new ExecutePythonScript(1, "sequenciaNW", "NW"));
        threadNW.start();

        // Exemplo de uso para SW.py
        Thread threadSW = new Thread(new ExecutePythonScript(2, "sequenciaSW", "SW"));
        threadSW.start();
    }
}
