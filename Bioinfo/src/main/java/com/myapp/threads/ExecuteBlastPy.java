package com.myapp.threads;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ExecuteBlastPy implements Runnable {
    private String pythonScriptPath;
    private String seq;
    private static final String PYTHON_EXECUTABLE = "python"; // ou "python3" dependendo do sistema
    private static final String OUTPUT_FILE_PATH = "C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/com/myapp/blast/blast_align.txt";

    public ExecuteBlastPy(String seq) {
        // Certifique-se de que o caminho está correto
        pythonScriptPath = "C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/com/myapp/blast/blast_and_process.py";
        this.seq = seq;
    }

    @Override
    public void run() {
        System.out.println("Iniciando a execução do script Python...");
        System.out.println("Script Python: " + pythonScriptPath);
        System.out.println("Sequência: " + seq);

        try {
            ProcessBuilder runProcessBuilder = new ProcessBuilder(PYTHON_EXECUTABLE, pythonScriptPath, seq);
            runProcessBuilder.redirectErrorStream(true);
            Process runProcess = runProcessBuilder.start();
            System.err.println("Processo iniciado");

            try (// Capturar a saída do processo
            Scanner runOutputStream = new Scanner(runProcess.getInputStream()).useDelimiter("\\A")) {
                String runOutput = runOutputStream.hasNext() ? runOutputStream.next() : "";

                // Escrever a saída em um arquivo
                try (FileWriter fileWriter = new FileWriter(OUTPUT_FILE_PATH, false)) {
                    fileWriter.write(runOutput);
                    System.out.println("Saída do script Python foi escrita no arquivo: " + OUTPUT_FILE_PATH);
                } catch (IOException e) {
                    System.err.println("Erro ao escrever a saída no arquivo: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            // Esperar o processo Python terminar
            int exitCode = runProcess.waitFor();

            if (exitCode != 0) {
                System.err.println("Erro na execução do script Python. Código de saída: " + exitCode);
            } else {
                System.out.println("Script Python executado com sucesso.");
            }
        } catch (IOException e) {
            System.err.println("Erro de IO ao executar o script Python: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("A execução do script Python foi interrompida: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Execução do script Python concluída.");
    }

    public static void main(String[] args) {
        // Exemplo de uso
        String sequence = "ACGATGCGAGCTAGCTAGTAGCTAGCTGTAGTGCTGTAGTGC";
        ExecuteBlastPy task = new ExecuteBlastPy(sequence);
        Thread thread = new Thread(task);
        thread.start();
    }
}
