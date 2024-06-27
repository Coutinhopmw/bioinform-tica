package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ExecuteCsProgram implements Runnable {

    private String csFilePath;
    private String outputFilePath;
    private int count;
    private String seq;
    private String csFileName;
    private String directoryPath;

    public ExecuteCsProgram(int count, String seq, String csFileName) {
        this.count = count;
        this.seq = seq;
        this.csFileName = csFileName;
        this.directoryPath = "/Bioinfo/database";

        // Define o caminho do arquivo C# com base no nome do arquivo
        if ("NW".equals(csFileName)) {
            csFilePath = "/Bioinfo/src/main/java/codes/cs/NW/";
            outputFilePath = "/Bioinfo/src/main/java/respostas/NW/resultado_c_sharp.txt";
        } else if ("SW".equals(csFileName)) {
            csFilePath = "/Bioinfo/src/main/java/codes/cs/SW/";
            outputFilePath = "/Bioinfo/src/main/java/respostas/SW/resultado_c_sharp_SW.txt";
        }
    }

    @Override
    public void run() {
        try {
            // Compila o arquivo C#
            if ("NW".equals(csFileName)) {
                System.out.println("C#     NW compilando o arquivo...");
            } else if ("SW".equals(csFileName)) {
                System.out.println("C#     SW compilando o arquivo...");
            }

            ProcessBuilder compileProcessBuilder = new ProcessBuilder("dotnet", "build", "-o", "build", csFilePath);
            compileProcessBuilder.directory(new File(csFilePath));
            Process compileProcess = compileProcessBuilder.start();

            // Captura a saída de erro da compilação
            Scanner compileErrorStream = new Scanner(compileProcess.getErrorStream()).useDelimiter("\\A");

            int compileExitCode = compileProcess.waitFor();
            if (compileExitCode != 0) {
                if ("NW".equals(csFileName)) {
                    System.out.println("C#     NW Erro na compilação. Código de retorno: " + compileExitCode);
                } else if ("SW".equals(csFileName)) {
                    System.out.println("C#     SW Erro na compilação. Código de retorno: " + compileExitCode);
                }
                return;
            }
            if ("NW".equals(csFileName)) {
                System.out.println("C#     NW compilação bem-sucedida.");
            } else if ("SW".equals(csFileName)) {
                System.out.println("C#     SW compilação bem-sucedida.");
            }

            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);
            FileWriter fileWriter = new FileWriter(outputFile, true);

            File dir = new File(directoryPath);
            if (!dir.exists()) {
                System.out.println("Diretório não existe: " + directoryPath);
            }

            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    try (Scanner fileScanner = new Scanner(file)) {
                        String firstLine = fileScanner.nextLine();
                        String secondLine = fileScanner.hasNextLine() ? fileScanner.nextLine() : "";

                        fileWriter.write(firstLine + "\n");

                        ProcessBuilder runProcessBuilder = new ProcessBuilder("dotnet", "run", "--project", csFilePath + csFileName + ".csproj", "--", seq, secondLine);
                        runProcessBuilder.directory(new File(csFilePath));
                        runProcessBuilder.redirectErrorStream(true);

                        Process runProcess = runProcessBuilder.start();
                        int runExitCode = runProcess.waitFor();

                        Scanner runOutputStream = new Scanner(runProcess.getInputStream()).useDelimiter("\\A");
                        String runOutput = runOutputStream.hasNext() ? runOutputStream.next() : "";
                        fileWriter.write(runOutput);

                        if (runExitCode != 0) {
                            // System.out.println("Erro na execução. Código de retorno: " + runExitCode);
                        } else {
                            // System.out.println("Programa C# executado com sucesso.");
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            fileWriter.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Exemplo de uso para NW.cs
        Thread threadNW = new Thread(new ExecuteCsProgram(1, "sequenciaNW", "NW"));
        threadNW.start();

        // Exemplo de uso para SW.cs
        Thread threadSW = new Thread(new ExecuteCsProgram(2, "sequenciaSW", "SW"));
        threadSW.start();
    }
}
