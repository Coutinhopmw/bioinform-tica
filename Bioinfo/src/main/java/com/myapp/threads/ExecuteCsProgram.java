package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExecuteCsProgram implements Runnable {

    private String csFilePath;
    private String outputFilePath;
    private String seq1;
    private String seq2;
    private String csFileName;

    public ExecuteCsProgram(ArrayList<String> seqList, String csFileName) {
        this.seq1 = seqList.get(0);
        this.seq2 = seqList.get(1);
        this.csFileName = csFileName;

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
            // Compilar o arquivo C# usando mcs do Mono
            ProcessBuilder compileProcessBuilder = new ProcessBuilder("mcs", csFileName + ".cs");
            compileProcessBuilder.directory(new File(csFilePath));
            Process compileProcess = compileProcessBuilder.start();

            // Capturar a saída de erro da compilação
            Scanner compileErrorStream = new Scanner(compileProcess.getErrorStream()).useDelimiter("\\A");

            int compileExitCode = compileProcess.waitFor();
            if (compileExitCode != 0) {
                System.out.println("Erro na compilação. Código de retorno: " + compileExitCode);
                compileErrorStream.close();;
                return;
            }

            System.out.println("Compilação bem-sucedida.");

            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);
            FileWriter fileWriter = new FileWriter(outputFile, true);

            ProcessBuilder runProcessBuilder = new ProcessBuilder("mono", csFileName + ".exe", seq1, seq2);
            runProcessBuilder.directory(new File(csFilePath));
            runProcessBuilder.redirectErrorStream(true);

            Process runProcess = runProcessBuilder.start();
            int runExitCode = runProcess.waitFor();

            Scanner runOutputStream = new Scanner(runProcess.getInputStream()).useDelimiter("\\A");
            String runOutput = runOutputStream.hasNext() ? runOutputStream.next() : "";
            fileWriter.write(runOutput);

            if (runExitCode != 0) {
                System.out.println("Erro na execução. Código de retorno: " + runExitCode);
            } else {
                System.out.println("Programa executado com sucesso.");
            }
            compileErrorStream.close();
            runOutputStream.close(); 
            fileWriter.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
