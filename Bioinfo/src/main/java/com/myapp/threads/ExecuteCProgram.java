package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ExecuteCProgram implements Runnable {

    private String cFilePath;
    private String outputFilePath;
    private int count;
    private String seq;
    private String cFileName;
    private String directoryPath;

    public ExecuteCProgram(int count, String seq, String cFileName) {
        this.count = count;
        this.seq = seq;
        this.cFileName = cFileName;
        this.directoryPath = "/Bioinfo/database";

        if ("NW".equals(cFileName)) {
            cFilePath = "/Bioinfo/src/main/java/codes/c/NW.c";
            outputFilePath = "/Bioinfo/src/main/java/respostas/NW/resultado_c.txt";
        
        } else if ("SW".equals(cFileName)) {
            cFilePath = "/Bioinfo/src/main/java/codes/c/SW.c";
            outputFilePath = "/Bioinfo/src/main/java/respostas/SW/resultado_c_SW.txt";
        }
    }

    @Override
    public void run() {
        try {
            String outputDir = "/Bioinfo/src/main/java/codes/c/";

            if ("NW".equals(cFileName)) {
                System.out.println("C      NW executando o arquivo...");
            } else if ("SW".equals(cFileName)) {
                System.out.println("C      SW executando o arquivo...");
            }
            
            // Compila o arquivo C
            ProcessBuilder compileProcessBuilder = new ProcessBuilder("gcc", "-o", outputDir + cFileName, cFilePath);
            Process compileProcess = compileProcessBuilder.start();
            int compileExitCode = compileProcess.waitFor();
            if (compileExitCode != 0) {
                if ("NW".equals(cFileName)) {
                    System.out.println("C      NW Erro na compilacao. Codigo de retorno: " + compileExitCode);
                } else if ("SW".equals(cFileName)) {
                    System.out.println("C      SW Erro na compilacao. Codigo de retorno: " + compileExitCode);
                }
                return;
            }

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

                            // Executa o programa C compilado
                            ProcessBuilder runProcessBuilder = new ProcessBuilder(outputDir + cFileName, seq, secondLine);
                            runProcessBuilder.redirectErrorStream(true);
                            Process runProcess = runProcessBuilder.start();
                            int runExitCode = runProcess.waitFor();

                            // Lê a saída do programa C
                            Scanner runOutputStream = new Scanner(runProcess.getInputStream()).useDelimiter("\\A");
                            String runOutput = runOutputStream.hasNext() ? runOutputStream.next() : "";
                            fileWriter.write(runOutput);

                            if (runExitCode != 0) {
                                // System.out.println("Erro na execução. Código de retorno: " + runExitCode);
                            } else {
                                // System.out.println("Programa C executado com sucesso.");
                            }
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            fileWriter.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
