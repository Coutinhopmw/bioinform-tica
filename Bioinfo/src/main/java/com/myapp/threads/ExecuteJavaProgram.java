package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ExecuteJavaProgram implements Runnable {

    private String javaFilePath;
    private String outputFilePath;
    private int count;
    private String seq;
    private String javaFileName;
    private String directoryPath;

    public ExecuteJavaProgram(int count, String seq, String javaFileName) {
        this.count = count;
        this.seq = seq;
        this.javaFileName = javaFileName;
        this.directoryPath = "/Bioinfo/database";

        
        // Define o caminho do arquivo Java com base no nome do arquivo
        if ("NW".equals(javaFileName)) {
            // javaFilePath = "//Bioinfo/main/java/codes/java/NW.java";
            // outputFilePath = "//Bioinfo/main/java/respostas/NW/NWresultado_java" + count + ".txt";

            // Se não estiver usando docker:
            javaFilePath = "/Bioinfo/src/main/java/codes/java/NW.java";
            outputFilePath = "/Bioinfo/src/main/java/respostas/NW/resultado_java.txt";
        
        } else if ("SW".equals(javaFileName)) {
            // javaFilePath = "//Bioinfo/main/java/codes/java/SW.java";
            // outputFilePath = "//Bioinfo/main/java/respostas/SW/SWresultado_java" + count + ".txt";

            // Se não estiver usando docker lembre de atulizar seu caminho:
            javaFilePath = "/Bioinfo/src/main/java/codes/java/SW.java";
            outputFilePath = "/Bioinfo/src/main/java/respostas/SW/resultado_java_SW.txt";
        }
    }

    @Override
    public void run() {
        try {
            // Diretório de saída para arquivos .class
            // String outputDir = "//Bioinfo/target/classes/";

            // Se não estiver usando docker lembre de atulizar seu caminho:
            String outputDir = "/Bioinfo/src/main/java/";

            if ("NW".equals(javaFileName)) {
                System.out.println("Java   NW executando o arquivo...");
            } else if ("SW".equals(javaFileName)) {
              System.out.println("Java   SW executando o arquivo...");
            }
            
            // Compila o arquivo Java
            System.out.println(javaFileName);
            ProcessBuilder compileProcessBuilder = new ProcessBuilder("javac", "-d", outputDir, javaFilePath);
            Process compileProcess = compileProcessBuilder.start();
            int compileExitCode = compileProcess.waitFor();
            if (compileExitCode != 0) {
                 if ("NW".equals(javaFileName)) {
                     System.out.println("Java   NW Erro na compilacao. Codigo de retorno: " + compileExitCode);
                } else if ("SW".equals(javaFileName)) {
                   System.out.println("Java   SW Erro na compilacao. Codigo de retorno: " + compileExitCode);
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

                            fileWriter.write(firstLine+"\n");

                            // Executa o programa Java compilado
                            String className = "codes.java." + javaFileName;
                            ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-cp", outputDir,className, seq, secondLine);
                            runProcessBuilder.redirectErrorStream(true);
                            Process runProcess = runProcessBuilder.start();
                            int runExitCode = runProcess.waitFor();

                            // Lê a saída do programa Java
                            Scanner runOutputStream = new Scanner(runProcess.getInputStream()).useDelimiter("\\A");
                            String runOutput = runOutputStream.hasNext() ? runOutputStream.next() : "";
                            fileWriter.write(runOutput);

                            if (runExitCode != 0) {
                               // System.out.println("Erro na execução. Código de retorno: " + runExitCode);
                            } else {
                               // System.out.println("Programa Java executado com sucesso.");
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